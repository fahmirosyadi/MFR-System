package com.mfr.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.mfr.system.entity.Role;
import com.mfr.system.repository.RoleRepository;
import com.mfr.system.repository.UserRepository;

@Service
public class RoleService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public RoleRepository rr;

}
