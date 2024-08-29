package it.interno.gestioneutentiservice.advice;

import it.interno.gestioneutentiservice.dto.ResponseDto;
import it.interno.gestioneutentiservice.exception.CancellazioneComandanteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author mirco.cennamo on 29/08/2024
 * @project gestione-utente-service
 */
@ControllerAdvice
public class CancellazioneComandanteExceptionAdvice {

    @ExceptionHandler(CancellazioneComandanteException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ResponseDto<String>> cancellazioneComandanteExceptionHandler(RuntimeException e) {
        ResponseDto<String> responseDto = ResponseDto.<String>builder().code(HttpStatus.UNPROCESSABLE_ENTITY.value()).error(e.getMessage()).build();
        return new ResponseEntity(responseDto, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
