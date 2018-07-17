package com.example.demo.repository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.HashSet;

import org.omg.PortableInterceptor.AdapterManagerIdHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean dataLoaded =false;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {

		if(dataLoaded) {
			return;
		}
		createRoleIfNotFound("USER_ROLE");
		createRoleIfNotFound("ADMIN_ROLE");
		
		System.out.println("rolerepo: "+userRepository.findAll().size());
		if(userRepository.findAll().size()==0) {
			User adminUser = new User();
			adminUser.setUsername("admin");
			adminUser.setPassword("tester@testing.com");
			adminUser.setPassword(bCryptPasswordEncoder.encode("Administrator"));
			adminUser.setRoles(new HashSet<>(roleRepository.findAll()));
			userRepository.save(adminUser);
		}
		
		dataLoaded=true;
		
	}
	@Transactional
	private void createRoleIfNotFound(String name) {
		System.out.println("Creating Role");
		Role oldrole = roleRepository.findByName(name);
		Role newrole=null;
		if(oldrole==null) {
			newrole=new Role();
			newrole.setName(name);
			roleRepository.save(newrole);
		}
		
		
		
	}
	
	
}
