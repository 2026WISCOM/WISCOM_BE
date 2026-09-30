package wiscom.backend.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import wiscom.backend.apiPayload.code.BaseErrorCode;
import wiscom.backend.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    INVALID_TEAM_ID(HttpStatus.BAD_REQUEST, "GUESTBOOK4001", "teamId는 등록된 12개 팀명 중 하나와 정확히 일치해야 합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder().isSuccess(false).code(code).message(message).build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder().httpStatus(httpStatus).isSuccess(false).code(code).message(message).build();
    }
}
