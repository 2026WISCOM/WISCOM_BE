package wiscom.backend.apiPayload.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import wiscom.backend.apiPayload.ApiResponse;
import wiscom.backend.apiPayload.code.ErrorReasonDTO;
import wiscom.backend.apiPayload.code.status.ErrorStatus;
import wiscom.backend.domain.enums.TeamId;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(ApiResponse.onFailure("COMMON400", "잘못된 요청입니다.", errors));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        for (Throwable cause = ex; cause != null; cause = cause.getCause()) {
            if (cause instanceof GeneralException generalException) {
                return failure(generalException.getErrorReasonHttpStatus());
            }
        }
        return failure(ErrorStatus._BAD_REQUEST.getReasonHttpStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return failure((ex.getRequiredType() == TeamId.class
                ? ErrorStatus.INVALID_TEAM_ID : ErrorStatus._BAD_REQUEST).getReasonHttpStatus());
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<Object> handleGeneralException(GeneralException ex) {
        return failure(ex.getErrorReasonHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return super.handleExceptionInternal(ex,
                ApiResponse.onFailure("COMMON" + status.value(),
                        status.is4xxClientError() ? "잘못된 요청입니다." : "서버 오류가 발생했습니다.", null),
                headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnhandledException(Exception ex) {
        log.error("Unhandled exception", ex);
        return failure(ErrorStatus._INTERNAL_SERVER_ERROR.getReasonHttpStatus());
    }

    private ResponseEntity<Object> failure(ErrorReasonDTO reason) {
        return ResponseEntity.status(reason.getHttpStatus())
                .body(ApiResponse.onFailure(reason.getCode(), reason.getMessage(), null));
    }
}
