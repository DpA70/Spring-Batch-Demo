package com.spring.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.spring.demo.entity.Employee;
import com.spring.demo.exception.DefaultExceptions;
import com.spring.demo.exception.ValNotFoundExcemption;
import com.spring.demo.repo.EmployeeRepo;

@Service
public class EmployeeService {

	@Autowired
	public EmployeeRepo employeeRepo;
	
	
	public Optional<Employee> FindEmployeeByID(String id){
		
		Optional<Employee> emp = employeeRepo.findById(id);
		
		if(employeeRepo.findById(id).isPresent()) {
			return employeeRepo.findById(id);
		}else {
			throw new ValNotFoundExcemption();
		}
		
	}
}
