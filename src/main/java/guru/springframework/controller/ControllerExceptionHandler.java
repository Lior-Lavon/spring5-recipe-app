package guru.springframework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

//-----------------------------------
//     Global Exception Handler    //
//-----------------------------------
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {


    // Global Exception Handler using the NotFoundException exception class
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView HandleNumberFormat(Exception exception){

        log.error("Handling Number Format exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        // 400  error match the view name html
        modelAndView.setViewName("400error");
        modelAndView.addObject("exception", exception);

        return modelAndView;
    }

}
