package com.ait.service;

import java.util.List;

import com.ait.bindingclasses.DashboardForm;
import com.ait.bindingclasses.EnquiryForm;
import com.ait.bindingclasses.EnquirySearchCriteria;
import com.ait.entity.StudentEntity;

public interface EnquiryService {
	
	public DashboardForm getDashboardData(Integer staffId);
	
	public String saveEnquiry(EnquiryForm form);

	public List<String> getCourseNames();
	
	public List<String> getEnqStatus();
	
	public List<StudentEntity> getStudentDetails();
	
	public List<StudentEntity> getFilteredEnquiries(EnquirySearchCriteria criteria, Integer StaffId);
}
