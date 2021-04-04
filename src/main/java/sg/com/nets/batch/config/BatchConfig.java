package sg.com.nets.batch.config;


import javax.batch.runtime.context.JobContext;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {

	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	JobBuilderFactory jobFactory;
	
	
	@Autowired
	StepBuilderFactory stepBuilderFactory;
	
	
	@Bean("step1")
	public Step step1() {
		return stepBuilderFactory.get("step1").tasklet(new Tasklet() {
		
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("This is step 1 running....");
				return RepeatStatus.FINISHED;
			}
		}).build();
	};
	
	@Bean("step2")
	public Step step2() {
		return stepBuilderFactory.get("step1").tasklet(new Tasklet() {
		
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("This is step 2 running....");
				return RepeatStatus.FINISHED;
			}
		}).build();
	};
	
	@Bean("step3")
	public Step step3() {
		return stepBuilderFactory.get("step1").tasklet(new Tasklet() {
		
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("This is step 3 running....");
				return RepeatStatus.FINISHED;
			}
		}).build();
	};
}
