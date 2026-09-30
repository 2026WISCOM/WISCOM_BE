package wiscom.backend.apiPayload.exception.handler;

import wiscom.backend.apiPayload.code.BaseErrorCode;
import wiscom.backend.apiPayload.exception.GeneralException;

public class GuestbookHandler extends GeneralException {
    public GuestbookHandler(BaseErrorCode code) {
        super(code);
    }
}
