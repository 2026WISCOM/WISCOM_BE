package wiscom.backend.guestbook;

import org.junit.jupiter.api.Test;
import wiscom.backend.apiPayload.exception.handler.GuestbookHandler;
import wiscom.backend.converter.TeamIdAttributeConverter;
import wiscom.backend.domain.Guestbook;
import wiscom.backend.domain.enums.TeamId;

import static org.assertj.core.api.Assertions.*;

class TeamIdTests {
    @Test
    void allowedValuesIncludeTwelveTeamsAndEveryone() {
        assertThat(TeamId.values()).extracting(TeamId::getValue).containsExactly(
                "데드락", "Quadcore", "2233", "아자쓰!", "공일공일", "PolyStack",
                "exit(0)", "MOOD:E", "404", "BE1", "Axis", "가디언즈", "모두에게");
    }

    @Test
    void converterRejectsUnknownDatabaseValue() {
        assertThatThrownBy(() -> new TeamIdAttributeConverter().convertToEntityAttribute("GUARDIANS"))
                .isInstanceOf(GuestbookHandler.class);
    }

    @Test
    void entityCannotBeConstructedWithNullTeamId() {
        assertThatThrownBy(() -> new Guestbook(null, "김철수", "내용"))
                .isInstanceOf(GuestbookHandler.class);
    }
}
