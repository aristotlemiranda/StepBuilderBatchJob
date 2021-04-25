package sg.com.nets.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.com.nets.batch.config.StepExecListener;

@RestController
@RequestMapping("/api")
public class RestAPI {
	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	ApplicationContext context;
	
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	StepExecListener stepExecutionListener;
	
	@Autowired
	StepBuilderFactory stepBuilderFactory;
	
	@GetMapping("batch")
	public String testJob() {
		
		JobParameters jobParameters = new JobParametersBuilder()
					.addLong("time", System.currentTimeMillis())
					.toJobParameters();
		
		Step step1 = (Step) context.getBean("step1");
		
		
		List<String> steps = new ArrayList<>();
		steps.add("step2");
		steps.add("step3");
		
		JobBuilder jb = new JobBuilder("job1");
		SimpleJobBuilder sjb = jb.start(step1);
		
		for(String s : steps) {
			Step step = (Step) context.getBean(s);
			sjb.next(step);
		}
		sjb.next(this.createStep("customStep2"));
		
		Job job = sjb.repository(jobRepository).build();
		
		try {
			jobLauncher.run(job, jobParameters);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return "BATCH COMPLETED";
	}
	
	
	@StepScope
	public Step createStep(String stepName) {
		/*
		 * StepBuilder stepBuilder = new StepBuilder(stepName);
		 * stepBuilder.tasklet(createTaskLet()).build().setJobRepository(jobRepository);
		 */
		
		return stepBuilderFactory.get(stepName).listener(new StepExecListener()).tasklet((Tasklet) context.getBean(stepName)).build();
		
	}
	
	@StepScope
	@Bean("customStep1")
	public Tasklet createTaskLet1() {
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					System.out.println("Completed....." + chunkContext.getStepContext().getStepName());
//					if(true) {
//						throw new Exception("I demand some error...");
//					}
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	@StepScope
	@Bean("customStep2")
	public Tasklet createTaskLet2() {
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
					System.out.println("Completed....." + chunkContext.getStepContext().getStepName());
					
//					if(true) {
//						throw new Exception("I demand some error...");
//					}
					
				return RepeatStatus.FINISHED;
			}
		};
	}

}
