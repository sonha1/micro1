package vn.gtel.user.aop;


import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.gtel.user.models.response.BadRequestResponse;
import vn.gtel.user.utils.ApplicationException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    protected final HttpServletRequest httpServletRequest;

    private final Tracer tracer;




    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("ERROR: ", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<BadRequestResponse> handleApplicationException(ApplicationException ex) {
        log.info("handleApplicationException {} with message {} , title {} , data {} ", ex.getCode(), ex.getMessage(), ex.getTitle(), ex.getData());

        BadRequestResponse responseData = new BadRequestResponse(ex, httpServletRequest);

        responseData.setTitle(ex.getMessage());

        String traceId = tracer.currentTraceContext().context() == null ? Thread.currentThread().getName() : tracer.currentTraceContext().context().traceId();

        responseData.setRequestId(traceId);

        HttpStatus httpStatus = ex.getHttpStatus() == null ? HttpStatus.BAD_REQUEST : ex.getHttpStatus();

        return new ResponseEntity<>(responseData, httpStatus);
    }
}
