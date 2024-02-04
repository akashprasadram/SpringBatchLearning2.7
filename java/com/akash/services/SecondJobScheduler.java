package com.akash.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

//@Service
public class SecondJobScheduler {

	@Autowired
	private JobLauncher jobLauncher;

	@Qualifier("secondJob")
	@Autowired
	private Job secondJob;

	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void secondJobStarter() {
		Map<String, JobParameter> params = new HashMap<>();
		params.put("currentTime", new JobParameter(System.currentTimeMillis()));

		JobParameters jobParameter = new JobParameters(params);
		try {
			JobExecution jobExecution = jobLauncher.run(secondJob, jobParameter);
			System.out.println("Job Execution ID = " + jobExecution.getId());

		} catch (Exception ex) {
			System.out.println("Exception while starting Job " + ex.getMessage());
		}
	}

}
