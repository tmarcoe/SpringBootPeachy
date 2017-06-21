package com.peachy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peachy.dao.EmployeeDao;
import com.peachy.entity.Employee;
import com.peachy.entity.UserProfile;
import com.peachy.interfaces.IEmployee;

@Service
public class EmployeeService implements IEmployee {

	@Autowired
	EmployeeDao employeeDao;
	
	@Override
	public void Create(Employee employee) {
		employeeDao.Create(employee);
	}

	@Override
	public Employee retrieve(int user_id) {
		return employeeDao.retrieve(user_id);
	}

	@Override
	public void update(Employee employee) {
		employeeDao.update(employee);
	}

	@Override
	public void delete(Employee employee) {
		employeeDao.delete(employee);
	}

	public List<UserProfile> employeeList() {
		return employeeDao.employeeList();
	}

}
