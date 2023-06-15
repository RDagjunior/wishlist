package br.com.luizalabs.wishlist.stopwatch;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class StopwatchAdvice extends StopwatchExecutor {

    @Around(value = "@within(stopwatch) || @annotation(stopwatch)", argNames = "pjp,stopwatch")
    public Object advice(ProceedingJoinPoint joinPoint, Stopwatch stopwatch) throws Throwable {
        return startExecution(joinPoint, stopwatch);
    }


}
