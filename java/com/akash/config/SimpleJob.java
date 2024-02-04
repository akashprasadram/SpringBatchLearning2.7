package com.akash.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.akash.listener.FirstJobListener;
import com.akash.listener.FirstStepListener;
import com.akash.processor.FirstItemProcessor;
import com.akash.reader.FirstItemReader;
import com.akash.services.SecondTasklet;
import com.akash.writer.FirstItemWriter;

@Configuration
public class SimpleJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private SecondTasklet secondTasklet;
	
	@Autowired
	private FirstJobListener firstJobListener;
	
	@Autowired
	private FirstStepListener firstStepListener;
	
	@Autowired
	private FirstItemReader firstItemReader;
	
	@Autowired
	private FirstItemProcessor firstItemProcessor;
	
	@Autowired
	private FirstItemWriter firstItemWriter;

	@Bean
	public Job firstJob() {
		return jobBuilderFactory.get("First Job")
				.incrementer(new RunIdIncrementer())
				.listener(firstJobListener)
				.start(firstStep())
				.next(secondStep())
				.build();
	}

	private Step firstStep() {
		return stepBuilderFactory.get("First Step")
				.listener(firstStepListener)
				.tasklet(firstTask()).build();

	}
	
	private Step secondStep() {
		return stepBuilderFactory.get("second Step").tasklet(secondTasklet).build();

	}

	private Tasklet firstTask() {
		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("This is first Tasklet");
				System.out.println("SEC = "+chunkContext.getStepContext().getStepExecutionContext());
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	/*
	 * private Tasklet secondTask() { return new Tasklet() {
	 * 
	 * @Override public RepeatStatus execute(StepContribution contribution,
	 * ChunkContext chunkContext) throws Exception {
	 * System.out.println("This is second Tasklet"); return RepeatStatus.FINISHED; }
	 * }; }
	 */
	
	
	
	
	@Bean
	public Job secondJob() {
		return jobBuilderFactory.get("Second Job")
				.incrementer(new RunIdIncrementer())
				.start(firstChunkStep())
				.build();
	}
	
	
	private Step firstChunkStep() {
		return stepBuilderFactory.get("First chunk Step")
				.<Integer,Long>chunk(4)
				.reader(firstItemReader)
				.processor(firstItemProcessor)
				.writer(firstItemWriter)
				.build();

	}
	
	
	
	
	
	
	
}
