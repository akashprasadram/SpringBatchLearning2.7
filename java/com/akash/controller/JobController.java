package com.akash.controller;

import java.util.List;

import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akash.request.JobParamsRequest;
import com.akash.services.JobService;

@RestController
@RequestMapping("/api/job")
public class JobController {

	@Autowired
	private JobService jobService;

	@Autowired
	private JobOperator jobOperator;

	@GetMapping("/start/{jobName}")
	public String StartJob(@PathVariable("jobName") String jobName,
			@RequestBody List<JobParamsRequest> jobParamsRequestList) {

		jobService.startJob(jobName, jobParamsRequestList);

		return "Job Started...";

	}

	@GetMapping("/stop/{executionId}")
	public String StopJob(@PathVariable("executionId") long executionId) {

		try {
			jobOperator.stop(executionId);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "Job Stopped...";

	}

}
