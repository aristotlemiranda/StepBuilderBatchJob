package sg.com.nets.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestAPI {
	
	@Autowired
	JobRepository jobRepository;
	
	@Autowired
	ApplicationContext context;
	
	
	@Autowired
	JobLauncher jobLauncher;
	
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
		
		
		Job job = sjb.repository(jobRepository).build();
		
		try {
			jobLauncher.run(job, jobParameters);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return "BATCH COMPELTED";
	}
	
	
	

}
