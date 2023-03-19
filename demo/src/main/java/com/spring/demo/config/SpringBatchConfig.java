package com.spring.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.spring.demo.entity.Employee;
import com.spring.demo.repo.EmployeeRepo;

import lombok.AllArgsConstructor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private EmployeeRepo employeeRepo;

	
	@Bean
	public FlatFileItemReader<Employee> reader(){
		FlatFileItemReader<Employee> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new FileSystemResource("src/main/resources/employees.csv"));
		flatFileItemReader.setName("csvReader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		
		return flatFileItemReader;
	}
	
	private LineMapper<Employee> lineMapper(){
		DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setDelimiter(",");
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames("EMPLOYEE_ID","FIRST_NAME","LAST_NAME","EMAIL");
		
		BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Employee.class);
		
		lineMapper.setLineTokenizer(delimitedLineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}
	
	@Bean
	public EmployeeProcessor processor() {
		return new EmployeeProcessor();
	}
	
	@Bean
	public RepositoryItemWriter<Employee> writer(){
		RepositoryItemWriter<Employee> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(employeeRepo);
		itemWriter.setMethodName("save");
		return itemWriter;		
	}
	
	@Bean
	public Step step1() {
		return (Step) stepBuilderFactory.get("csv-step").<Employee,Employee>chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	@Bean
	public Job runJob() {
		return jobBuilderFactory.get("importEmployee")
				.flow(step1())
				.end()
				.build();
	}
	
}
