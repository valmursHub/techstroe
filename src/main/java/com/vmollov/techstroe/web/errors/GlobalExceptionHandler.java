package com.vmollov.techstroe.web.errors;

import com.vmollov.techstroe.web.errors.exceptions.NotFoundException;
import com.vmollov.techstroe.web.errors.exceptions.ServiceGeneralException;
import com.vmollov.techstroe.web.errors.exceptions.UnauthorizedActionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(RuntimeException e){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/404");
        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(ServiceGeneralException.class)
    public ModelAndView handleServiceGeneralException(RuntimeException e){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/500");
        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ModelAndView handleUnauthorizedActionException(RuntimeException e){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/403");
        modelAndView.addObject("message", e.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/error");

        return modelAndView;
    }
}
