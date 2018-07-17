package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import com.example.demo.validator.UserFormValidator;

@Controller
public class MainController {

	@Autowired 
	private UserService userService;
	
	
	@Autowired 
	private UserFormValidator userFormValidator;
	
	// Set a form validator
	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
      // Form target
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);
 
		if (target.getClass() == User.class) {
			dataBinder.setValidator(userFormValidator);
		}
      // ...
	}
	
	
	
	
	@RequestMapping(value= {"/","/welcome"}, method=RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("message","Welcome");
		
		return "home";
	}
	
	@RequestMapping(value="/registration", method=RequestMethod.GET)
	public String registrationForm(Model model) {
		model.addAttribute("userForm", new User());
		
		return "registration";
	}
	
	@RequestMapping(value="/registration", method = RequestMethod.POST)
	public String saveRegistration(@ModelAttribute("userForm") @Validated User userForm, BindingResult bindingResult, 
			Model model, final RedirectAttributes redirectAttributes) {
		
		//userFormValidator.validate(userForm, bindingResult);
		
		if(bindingResult.hasErrors()) {
			
			return "registration";
		}
		
		userService.save(userForm);
		redirectAttributes.addFlashAttribute("message","Your registration was successfull!");
		return "redirect:/home";
	}
		
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String showLogin(Model model) {
		return "login";
	}
	

	@RequestMapping(value="/userAccountData", method=RequestMethod.GET)
	public String showUserData(Model model, Principal principal) {
		System.out.println("userAccountData");
		String name = principal.getName();
		
		model.addAttribute("userName",name);
		System.out.println("name: "+name);
		return "userData";
	}
}
