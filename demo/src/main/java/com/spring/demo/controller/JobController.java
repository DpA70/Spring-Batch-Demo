package com.spring.demo.controller;

import java.util.Optional;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.demo.entity.Employee;
import com.spring.demo.exception.DefaultExceptions;
import com.spring.demo.service.EmployeeService;

@RestController
@RequestMapping("/jobs")
public class JobController {
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	@Autowired
	public EmployeeService employeeService;
	
	@PostMapping("/importData")
	public void importCsvToDbJob() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("startAt", System.currentTimeMillis()).toJobParameters();
			try {
				jobLauncher.run(job, jobParameters);
			} catch (JobExecutionAlreadyRunningException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JobRestartException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JobInstanceAlreadyCompleteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JobParametersInvalidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	@GetMapping("/emp/{id}")
	public ResponseEntity<Optional<Employee>> getEmployee(@PathVariable String id) {
		ResponseEntity<Optional<Employee>> emp = new ResponseEntity<Optional<Employee>>(employeeService.FindEmployeeByID(id), HttpStatus.OK);
		return emp;
	}
	
}
