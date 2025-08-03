package shop.nandoShop.nandoshop_app.exceptions;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import shop.nandoShop.nandoshop_app.dtos.responses.ErrorResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage()
        ), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status != null ? status.value() : ex.getStatusCode().value());
        error.put("error", status != null ? status.getReasonPhrase() : "Error");
        error.put("message", ex.getReason());

        return new ResponseEntity<>(error, ex.getStatusCode());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();                // Nombre del parámetro que falló
        String requiredType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName()
                : "valor válido";

        String message = String.format("El parámetro '%s' debe ser un %s válido", paramName, requiredType);

        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Parámetro inválido",
                message
        ), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MPException.class)
    public ResponseEntity<ErrorResponse> handleMPException(MPException ex) {
        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Error en Mercado Pago",
                ex.getMessage()
        ), HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(MPApiException.class)
    public ResponseEntity<ErrorResponse> handleMPApiException(MPApiException ex) {
        String message = ex.getApiResponse() != null
                ? ex.getApiResponse().getContent()
                : "Error de API de Mercado Pago";

        return new ResponseEntity<>(new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatusCode(), // Usa el código devuelto por MP
                "Error de API de Mercado Pago",
                message
        ), HttpStatus.valueOf(ex.getStatusCode()));
    }
}
