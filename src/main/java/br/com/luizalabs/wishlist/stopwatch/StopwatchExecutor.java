package br.com.luizalabs.wishlist.stopwatch;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.perf4j.LoggingStopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

public abstract class StopwatchExecutor {

	protected Object startExecution(ProceedingJoinPoint joinPoint, Stopwatch stopwatch) throws Throwable {

		LoggingStopWatch timer = new Slf4JStopWatch();

		if (!timer.isLogging()) {
			return joinPoint.proceed();
		}

		timer.start();

		Throwable exceptionThrown = null;
		try {
			return joinPoint.proceed();
		} catch (Throwable t) {
			exceptionThrown = t;
			throw t;
		} finally {
			String tag = getStopWatchTag(joinPoint, stopwatch);
			tag = isSuccessRequest(exceptionThrown) ? tag + ".success" : tag + ".failure";

			timer.stop(tag);
		}

	}

	protected Object startExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		return startExecution(joinPoint, null);
	}

	private String getStopWatchTag(ProceedingJoinPoint joinPoint, Stopwatch stopWatch) {

		Stopwatch stopwatch = Optional.ofNullable(stopWatch)
				.orElse(joinPoint.getTarget().getClass().getAnnotation(Stopwatch.class));

		String prefix = Optional.ofNullable(stopwatch)
				.map(wtc -> wtc.prefix().isBlank() ? "" : wtc.prefix().concat(".")).orElse("");

		String methodName = joinPoint.getSignature().getName();
		String className = joinPoint.getTarget().getClass().getSimpleName();

		return prefix + className + "." + methodName;
	}

	private boolean isSuccessRequest(Throwable exceptionThrown) {
		return exceptionThrown == null;
	}

}
