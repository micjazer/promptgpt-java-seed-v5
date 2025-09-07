
package com.codeiguanas.promptgpt.web.advice;
import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import org.springframework.web.context.request.*;
import java.time.Instant; import java.util.Map;
@RestControllerAdvice public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<ApiError> bad(IllegalArgumentException ex, WebRequest req){ return build(HttpStatus.BAD_REQUEST,"Bad Request",ex.getMessage(),req,Map.of()); }
    @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<ApiError> val(MethodArgumentNotValidException ex, WebRequest req){ return build(HttpStatus.BAD_REQUEST,"Validation Failed",ex.getMessage(),req,Map.of("errors",ex.getBindingResult().getAllErrors())); }
    @ExceptionHandler({TooManyRequestsException.class}) public ResponseEntity<ApiError> r429(RuntimeException ex, WebRequest req){ return build(HttpStatus.TOO_MANY_REQUESTS,"Too Many Requests",ex.getMessage(),req,Map.of()); }
    @ExceptionHandler(Exception.class) public ResponseEntity<ApiError> gen(Exception ex, WebRequest req){ HttpStatus st = (ex.getMessage()!=null && ex.getMessage().toLowerCase().contains("openai"))?HttpStatus.BAD_GATEWAY:HttpStatus.INTERNAL_SERVER_ERROR; return build(st, st.getReasonPhrase(), ex.getMessage(), req, Map.of()); }
    private ResponseEntity<ApiError> build(HttpStatus s,String e,String m,WebRequest req,Map<String,Object>d){ String path=(req instanceof ServletWebRequest swr)?swr.getRequest().getRequestURI():""; return ResponseEntity.status(s).body(new ApiError(Instant.now(),s.value(),e,m,path,d)); }
}
