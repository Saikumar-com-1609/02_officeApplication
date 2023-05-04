package com.ait.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ait.bindingclasses.DashboardForm;
import com.ait.bindingclasses.EnquiryForm;
import com.ait.bindingclasses.EnquirySearchCriteria;
import com.ait.entity.CoursesEntity;
import com.ait.entity.EnquiryStatusEntity;
import com.ait.entity.StudentEntity;
import com.ait.entity.UserDetailsEntity;
import com.ait.repository.CoursesRepository;
import com.ait.repository.EnquiryStatusRepository;
import com.ait.repository.StudentRepository;
import com.ait.repository.UserDetailsRepository;
import com.ait.service.EnquiryService;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private UserDetailsRepository userRepo;

	@Autowired
	private CoursesRepository courseRepo;

	@Autowired
	private EnquiryStatusRepository enqStatusRepo;

	@Autowired
	private HttpSession session;

	@Override
	public DashboardForm getDashboardData(Integer staffId) {

		DashboardForm dashboard = new DashboardForm();

		Optional<UserDetailsEntity> findById = userRepo.findById(staffId);

		if (findById.isPresent()) {

			UserDetailsEntity userDetailsEntity = findById.get();

			List<StudentEntity> studentEnquiries = userDetailsEntity.getStudentEnquiries();

			int totalCount = studentEnquiries.size();

			int enrolledCount = studentEnquiries.stream().filter(e -> e.getStatus().equals("Enrolled"))
					.collect(Collectors.toList()).size();

			int lostCount = studentEnquiries.stream().filter(e -> e.getStatus().equals("Lost"))
					.collect(Collectors.toList()).size();

			dashboard.setTotalEnquiries(totalCount);
			dashboard.setEnrolled(enrolledCount);
			dashboard.setLost(lostCount);

		}

		return dashboard;
	}

	@Override
	public List<String> getCourseNames() {

		List<CoursesEntity> findAll = courseRepo.findAll();

		List<String> courseNames = new ArrayList();

		for (CoursesEntity entity : findAll) {
			courseNames.add(entity.getCourseName());
		}

		return courseNames;
	}

	@Override
	public List<String> getEnqStatus() {

		List<EnquiryStatusEntity> findAll = enqStatusRepo.findAll();

		List<String> statusNames = new ArrayList();
		for (EnquiryStatusEntity entity : findAll) {
			statusNames.add(entity.getStatus());
		}

		return statusNames;
	}

	@Override
	public String saveEnquiry(EnquiryForm form) {

		Integer staffId = (Integer) session.getAttribute("staffId");

		Optional<UserDetailsEntity> user = userRepo.findById(staffId);
		UserDetailsEntity userDetails = user.get();

		if (form.getEnquiryId() != null) {

			Optional<StudentEntity> findById2 = studentRepo.findById(form.getEnquiryId());

			StudentEntity entity = findById2.get();

			entity.setClassMode(form.getClassMode());
			entity.setCourse(form.getCourse());
			entity.setStatus(form.getStatus());
			entity.setContactNumber(form.getContactNumber());
			entity.setStudentName(form.getStudentName());

			entity.setUserDetails(userDetails);

			studentRepo.save(entity);
			return "Record is Updated successfully";
		}
		
        StudentEntity entity = new StudentEntity();
        
        BeanUtils.copyProperties(form, entity);
        
        entity.setUserDetails(userDetails);
        
        studentRepo.save(entity);
        
        return "Record saved successfully";
        
        
	}
	
	
	
	@Override
	public List<StudentEntity> getStudentDetails() {

		Integer staffId = (Integer) session.getAttribute("staffId");

		Optional<UserDetailsEntity> findById = userRepo.findById(staffId);

		if (findById.isPresent()) {

			UserDetailsEntity detailsEntity = findById.get();

			List<StudentEntity> enquiries = detailsEntity.getStudentEnquiries();

			return enquiries;

		}
		return null;
	}

	@Override
	public List<StudentEntity> getFilteredEnquiries(EnquirySearchCriteria criteria, Integer staffId) {

		Optional<UserDetailsEntity> findById = userRepo.findById(staffId);

		if (findById.isPresent()) {

			UserDetailsEntity detailsEntity = findById.get();

			List<StudentEntity> enquiries = detailsEntity.getStudentEnquiries();

			// here is ajax filter logic

			if (null != criteria.getCourse() & !"".equals(criteria.getCourse())) {

				enquiries = enquiries.stream().filter(e -> e.getCourse().equals(criteria.getCourse()))
						.collect(Collectors.toList());

			}

			if (null != criteria.getStatus() & !"".equals(criteria.getStatus())) {

				enquiries = enquiries.stream().filter(e -> e.getStatus().equals(criteria.getStatus()))
						.collect(Collectors.toList());

			}

			if (null != criteria.getClassMode() & !"".equals(criteria.getClassMode())) {

				enquiries = enquiries.stream().filter(e -> e.getClassMode().equals(criteria.getClassMode()))
						.collect(Collectors.toList());

			}

			return enquiries;
		}

		return null;
	}

}
