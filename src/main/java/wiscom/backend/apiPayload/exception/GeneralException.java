package wiscom.backend.apiPayload.exception;

import lombok.Getter;
import wiscom.backend.apiPayload.code.BaseErrorCode;
import wiscom.backend.apiPayload.code.ErrorReasonDTO;

@Getter
public class GeneralException extends RuntimeException {
    private final BaseErrorCode code;

    public GeneralException(BaseErrorCode code) {
        super(code.getReason().getMessage());
        this.code = code;
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return code.getReasonHttpStatus();
    }
}
