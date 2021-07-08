package it.blog.demomicrometerpushgatewayprometheusgrafana;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;


@Configuration
@EnableBatchProcessing
@EnableScheduling
@Slf4j
public class ScheduledJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Tasklet helloWorldTasklet;
    
    @Autowired
    PrometheusConfiguration prometheusConfig;
    
    @Scheduled(cron = "${job.cron}")
    public void performHelloWorld() throws Exception
    {
    	JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();

		JobExecution execution = jobLauncher.run(readJob(), param);

		log.info("Job finished with status {}", execution.getStatus());
		
    }
    
    @Bean
	public Job readJob() {
		return jobBuilderFactory.get("job").incrementer(new RunIdIncrementer()).flow(step1()).end().build();
	}
    
    @Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").tasklet(helloWorldTasklet).build();
	}
    

    @Scheduled(fixedRateString = "${prometheus.push.rate}")
    public void schedule() {
    	log.info("Send Alive signal");
    	prometheusConfig.setAlive();
    	prometheusConfig.pushMetrics();
    }
}
