package com.peachy.interfaces;

import com.peachy.entity.Employee;

public interface IEmployee {
	public void Create(Employee employee);
	public Employee retrieve(int user_id);
	public void update(Employee employee);
	public void delete(Employee employee);
}
