package es.upm.miw.devops.rest.exceptionshandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseBody
    public ErrorMessage noResourceFoundRequest(NoResourceFoundException exception) {
        return new ErrorMessage(new RuntimeException(
                "Ruta no encontrada. Prueba con: **/actuator/info o **/swagger-ui.html o **/v3/api-docs o **/v3/api-docs.yaml"),
                HttpStatus.NOT_FOUND.value());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ErrorMessage responseStatusException(ResponseStatusException exception) {
        String message = exception.getReason();
        if (message == null) {
            message = exception.getStatusCode().toString();
        }
        return new ErrorMessage(new RuntimeException(message), exception.getStatusCode().value());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            Exception.class
    })
    @ResponseBody
    public ErrorMessage exception(Exception exception) {
        return new ErrorMessage(new RuntimeException("ERROR"), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
