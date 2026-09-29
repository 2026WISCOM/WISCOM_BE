package wiscom.backend.web.dto.guestbook;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wiscom.backend.domain.enums.TeamId;

public class GuestbookRequestDTO {
    @Getter
    @NoArgsConstructor
    public static class CreateRequestDTO {
        @NotNull(message = "teamId는 필수입니다.")
        @Schema(example = "가디언즈")
        private TeamId teamId;

        @Schema(example = "김은서")
        private String writer;
        @Schema(example = "가디언즈가 통대안받으면 누가받음?;;")
        private String content;
    }
}
