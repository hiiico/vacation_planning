package app.web;

import app.exception.UsernameAlreadyExist;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UsernameAlreadyExist.class)
    public String handleUsernameAlreadyExist(HttpRequest request, RedirectAttributes redirectAttributes, UsernameAlreadyExist exception) {
        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("usernameAlreadyExistMessage", message);

        return "redirect:/register";
    }

    // private view

//    @ExceptionHandler(UsernameAlreadyExist.class)
//    public ModelAndView handelUsernameAlreadyExist() {
//
//        ModelAndView mnv = new ModelAndView();
//        mnv.setViewName("username-exist");
//
//        return mnv;
//    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            AccessDeniedException.class,
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class
    })
    public ModelAndView handleNotFoundException(Exception e) {
        return new ModelAndView("not-found");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception exception) {

        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("internal-server-error");
        mnv.addObject("errorMessage", exception.getClass().getSimpleName());
        return mnv;
    }
}
