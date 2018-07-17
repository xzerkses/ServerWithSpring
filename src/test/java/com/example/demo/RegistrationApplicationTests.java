package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationApplicationTests {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void testEncoding() {
		String original_password = "Test_pass12word90";
		String encodedPassword= bCryptPasswordEncoder.encode(original_password);
		assertTrue(encodedPassword.equals(original_password)); 
	}
	@Test
	public void testSaveRole() {
		Role role = new Role();
		role.setName("Admin");
		roleRepository.save(role);
		assertEquals(role, roleRepository.findByName("Admin"));
	}
	
	@Test
	public void testSaveUser() {
		User user = new User();
		user.setUsername("Petreus");
		user.setEmail("petreus@email.com");
		String testPassword="Test_pass12word90";
		user.setPassword(bCryptPasswordEncoder.encode(testPassword));
		HashSet<Role> roles = new HashSet<>();
		Role role = new Role();
		role.setName("Admin");
		roles.add(role);
		user.setRoles(roles);
		userService.save(user);
		
		User foundUser = userRepository.findByUsername("Petreus");
		assertNotNull(foundUser);
		assertEquals(user, foundUser);
		assertEquals("petreus@email.com", foundUser.getEmail());
		assertEquals("Petreus", foundUser.getUsername());
		assertTrue(foundUser.getPassword().equals(testPassword));
	}

}
