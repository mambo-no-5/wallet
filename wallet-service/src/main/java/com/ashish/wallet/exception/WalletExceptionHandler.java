package com.ashish.wallet.exception;

import com.ashish.accounttransactionservice.account.PlayerAccountServiceException;
import com.ashish.accounttransactionservice.transaction.TransactionServiceException;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class WalletExceptionHandler extends ExceptionHandlerExceptionResolver {

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {

        if(exception instanceof MethodArgumentNotValidException) {
            String message = Objects.requireNonNull(((MethodArgumentNotValidException) exception).getFieldError()).getDefaultMessage();
            return errorModelAndView(400, message);

        } else if ( exception instanceof WalletException){
            return errorModelAndView(400, exception.getMessage());

        }  else if ( exception instanceof PlayerAccountServiceException || exception instanceof TransactionServiceException){
            return errorModelAndView(500, exception.getMessage());

        } else {
            return errorModelAndView(500, exception.getMessage());
        }

    }

    private ModelAndView errorModelAndView(int status, String message) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("errorStatus", status);
        modelMap.addAttribute("errorMessage", message);
        return new ModelAndView("error", modelMap);
    }

}
