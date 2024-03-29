package com.akash.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.akash.request.JobParamsRequest;

@Service
public class JobService {

	@Autowired
	private JobLauncher jobLauncher;

	@Qualifier("firstJob")
	@Autowired
	private Job firstJob;

	@Qualifier("secondJob")
	@Autowired
	private Job secondJob;

	@Async
	public void startJob(String jobName, List<JobParamsRequest> jobParamsRequestList) {
		Map<String, JobParameter> params = new HashMap<>();
		params.put("currentTime", new JobParameter(System.currentTimeMillis()));
		jobParamsRequestList.stream().forEach(jobParamReq -> {
			params.put(jobParamReq.getParamKey(), 
					new JobParameter(jobParamReq.getParamValue()));
		});

		JobParameters jobParameter = new JobParameters(params);
		try {
			JobExecution jobExecution = null;
			if (jobName.equals("First Job")) {
				jobExecution = jobLauncher.run(firstJob, jobParameter);
			} else if (jobName.equals("Second Job")) {
				jobExecution = jobLauncher.run(secondJob, jobParameter);
				System.out.println("Job Execution ID = " + jobExecution.getId());
			}
		} catch (Exception ex) {
			System.out.println("Exception while starting Job " + ex.getMessage());
		}
	}
}
