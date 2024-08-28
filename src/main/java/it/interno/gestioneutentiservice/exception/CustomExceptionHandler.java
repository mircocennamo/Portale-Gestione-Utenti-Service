package it.interno.gestioneutentiservice.exception;

import it.interno.gestioneutentiservice.dto.ResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String messaggio = getErrorMessage(ex.getBindingResult().getAllErrors());
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, StringUtils.isBlank(messaggio) ? ex.getMessage() : messaggio), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private String getErrorMessage(List<ObjectError> errori){

        if(errori == null || errori.isEmpty())
            return null;

        StringBuilder messaggio = new StringBuilder();
        for(ObjectError e : errori){
            messaggio.append(e.getDefaultMessage()).append(", ");
        }

        return messaggio.substring(0, messaggio.length() - 2);
    }

    private ResponseDto<Object> buildResponse(HttpStatus status, String messaggio){

        return ResponseDto.builder()
                .code(status.value())
                .error(messaggio)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex, WebRequest request){

        String messaggio = ex.getConstraintViolations().iterator().next().getMessage();
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, messaggio), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> emailAlreadyExistsException(EmailAlreadyExistsException ex, WebRequest request){
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(OrarioLavoroObbligatorioException.class)
    public ResponseEntity<Object> orarioLavoroObbligatorioException(OrarioLavoroObbligatorioException ex, WebRequest request){
        return handleExceptionInternal(ex, buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
