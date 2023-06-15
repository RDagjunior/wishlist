package br.com.luizalabs.wishlist.stopwatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class StopwatchAutoAdvice extends StopwatchExecutor {

	@Around("execution(* br.com.luizalabs..controller..*(..)) || execution(* br.com.luizalabs..service..*(..))")
	public Object advice(ProceedingJoinPoint joinPoint) throws Throwable {
		return startExecution(joinPoint);
	}

}
