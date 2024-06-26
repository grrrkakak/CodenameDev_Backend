package codeNameDev.codeNameDev.common.exception;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.validation.*;
import org.springframework.web.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;

import java.util.stream.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        logger.error("MissingServletRequestParameterException: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("요청 파라미터 누락 오류", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e) {
        logger.error("MethodArgumentTypeMismatchException: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("요청 파라미터 타입 오류", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        logger.error("HttpMessageNotReadableException: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("메시지 파싱 오류", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        logger.error("HttpRequestMethodNotSupportedException: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("HTTP 메서드 오류", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        logger.error("MethodArgumentNotValidException : {}", e.getMessage());
        String errorMessage = createValidationErrorMessage(e.getBindingResult());
        ErrorResponse errorResponse = new ErrorResponse("입력 유효성 오류", errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        logger.error("CustomException: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getDetail());
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        logger.error("Exception: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("예상치 못한 오류 발생");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    private String createValidationErrorMessage(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}