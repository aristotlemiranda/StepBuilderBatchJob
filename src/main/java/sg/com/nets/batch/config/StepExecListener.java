package sg.com.nets.batch.config;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class StepExecListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("listener.... afterStep");
		System.out.println("exceptions.... ----> " + stepExecution.getFailureExceptions());
		
		Optional<Exception> ex = stepExecution.getFailureExceptions().stream().flatMap(select(Exception.class)).findAny();
		
		
		if(ex.isPresent()) {
			System.out.println("Something might have happened.");
			return ExitStatus.FAILED;
		}
		
		
		System.out.println("isPresent ===" + ex.isPresent());
		
		return ExitStatus.COMPLETED;
	}
	
	private static <T, R> Function<T, Stream<R>> select(Class<R> clazz) {
	    return e -> clazz.isInstance(e) ? Stream.of(clazz.cast(e)) : null;
	}

}
