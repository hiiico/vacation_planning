package app.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @After("execution(* app.web.IndexController.*(..))")
    public void logIndexControllerMethods() {
        System.out.println("Another method in IndexController is executed");
    }
}
