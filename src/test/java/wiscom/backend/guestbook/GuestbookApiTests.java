package wiscom.backend.guestbook;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import wiscom.backend.domain.Guestbook;
import wiscom.backend.domain.enums.TeamId;
import wiscom.backend.repository.GuestbookRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GuestbookApiTests {
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired GuestbookRepository repository;
    @Autowired JdbcTemplate jdbc;
    @Autowired EntityManager entityManager;

    @ParameterizedTest
    @ValueSource(strings = {"데드락", "Quadcore", "2233", "아자쓰!", "공일공일", "PolyStack",
            "exit(0)", "MOOD:E", "404", "BE1", "Axis", "가디언즈"})
    void acceptsExactTeamNamesInJsonAndDatabase(String teamName) throws Exception {
        mockMvc.perform(post("/api/guestbooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "teamId", teamName, "writer", "김철수", "content", "멋져요!"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.success").doesNotExist())
                .andExpect(jsonPath("$.result.teamId").value(teamName))
                .andExpect(jsonPath("$.result.createdAt").isNotEmpty());

        entityManager.flush();
        entityManager.clear();
        assertThat(jdbc.queryForObject("select team_id from guestbook", String.class)).isEqualTo(teamName);
        assertThat(repository.findAll().getFirst().getTeamId()).isEqualTo(TeamId.fromValue(teamName));

        mockMvc.perform(get("/api/guestbooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", hasSize(1)))
                .andExpect(jsonPath("$.result[0].teamId").value(teamName))
                .andExpect(jsonPath("$.result[0].writer").value("김철수"))
                .andExpect(jsonPath("$.result[0].content").value("멋져요!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"없는팀", "quadcore", "MOOD_E", "exit0", "아자쓰", "GUARDIANS",
            "DEADLOCK", "TEAM_2233", "TEAM_404", "QUADCORE", "POLYSTACK", "AXIS", "EXIT_0",
            "AJASS", "ZERO_ONE_ZERO_ONE", "", " 가디언즈", "가디언즈 ", "Mood:E", "2233 "})
    void rejectsInvalidNamesWithoutSaving(String teamName) throws Exception {
        long count = repository.count();
        mockMvc.perform(post("/api/guestbooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "teamId", teamName, "writer", "김철수", "content", "안녕하세요"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false));
        assertThat(repository.count()).isEqualTo(count);
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "2233", "404", "0", "true", "{}", "[]", "[\"가디언즈\"]"})
    void rejectsNonStringTeamIds(String teamJson) throws Exception {
        mockMvc.perform(post("/api/guestbooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"teamId\":" + teamJson + ",\"writer\":\"김철수\",\"content\":\"안녕하세요\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false));
        assertThat(repository.count()).isZero();
    }

    @Test
    void requiresTeamIdWhenCreating() throws Exception {
        mockMvc.perform(post("/api/guestbooks").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"writer\":\"김철수\",\"content\":\"안녕하세요\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess").value(false));
        assertThat(repository.count()).isZero();
    }

    @Test
    void returnsAllTeamsInDescendingCreatedAtOrder() throws Exception {
        Guestbook older = repository.saveAndFlush(new Guestbook(TeamId.GUARDIANS, "먼저", "오래된 글"));
        Guestbook newer = repository.saveAndFlush(new Guestbook(TeamId.GUARDIANS, "나중", "최근 글"));
        Guestbook otherTeam = repository.saveAndFlush(new Guestbook(TeamId.MOOD_E, "다른 팀", "다른 팀 글"));
        jdbc.update("update guestbook set created_at = ? where id = ?",
                Timestamp.valueOf(LocalDateTime.of(2026, 1, 1, 0, 0)), older.getId());
        jdbc.update("update guestbook set created_at = ? where id = ?",
                Timestamp.valueOf(LocalDateTime.of(2026, 1, 2, 0, 0)), newer.getId());
        jdbc.update("update guestbook set created_at = ? where id = ?",
                Timestamp.valueOf(LocalDateTime.of(2026, 1, 3, 0, 0)), otherTeam.getId());
        entityManager.clear();

        mockMvc.perform(get("/api/guestbooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", hasSize(3)))
                .andExpect(jsonPath("$.result[0].id").value(otherTeam.getId()))
                .andExpect(jsonPath("$.result[0].teamId").value("MOOD:E"))
                .andExpect(jsonPath("$.result[1].id").value(newer.getId()))
                .andExpect(jsonPath("$.result[2].id").value(older.getId()));
    }

    @Test
    void returnsEmptyListWithoutEntries() throws Exception {
        mockMvc.perform(get("/api/guestbooks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", hasSize(0)));
    }

    @Test
    void databaseRejectsNullTeamId() {
        assertThatThrownBy(() -> jdbc.update(
                "insert into guestbook (team_id, writer, content, created_at, updated_at) "
                        + "values (null, 'writer', 'content', current_timestamp, current_timestamp)"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
