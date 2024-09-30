//package Ibmec.edu.br.AP1.exceptions;
//
//import Ibmec.edu.br.AP1.model.Cliente;
//import org.springframework.http.HttpStatus;
//import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ControllerAdvice
//public class Interceptor {
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String handleException(MethodArgumentNotValidException e) {
//        ValidationMessage message = new ValidationMessage();
//        for (FieldError error : e.getBindingResult().getFieldErrors()) {
//            ValidaErro erro = new ValidaErro();
//            erro.setField(error.getField());
//            erro.setMessage(error.getDefaultMessage());
//            response.getErrors().add(erro);
//
//        }
//        return response;
//    }
//}
