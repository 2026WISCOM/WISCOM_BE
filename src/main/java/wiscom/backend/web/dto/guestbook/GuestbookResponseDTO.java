package wiscom.backend.web.dto.guestbook;

import lombok.Builder;
import lombok.Getter;
import wiscom.backend.domain.enums.TeamId;

import java.time.LocalDateTime;

public class GuestbookResponseDTO {
    @Getter
    @Builder
    public static class GuestbookInfoDTO {
        private Long id;
        private TeamId teamId;
        private String writer;
        private String content;
        private LocalDateTime createdAt;
    }
}
