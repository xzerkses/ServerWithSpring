package com.example.demo.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptEncoder;

	@Override
	public void save(User user) {
		System.out.println("saving user");
		user.setPassword(bCryptEncoder.encode(user.getPassword()));
		user.setPasswordConfirm("");
		HashSet<Role> roles = new HashSet<>();
		
		if(user.getRoles().isEmpty()) {		
			System.out.println("adding USER");
			roles.add(roleRepository.findByName("ROLE_USER"));
			user.setRoles(roles);
		}	
			
		System.out.println("saving user end");
		userRepository.save(user);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
