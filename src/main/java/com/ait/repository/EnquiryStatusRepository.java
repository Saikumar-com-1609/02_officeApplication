package com.ait.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ait.entity.EnquiryStatusEntity;

public interface EnquiryStatusRepository  extends JpaRepository<EnquiryStatusEntity, Integer>{
	
	
}
