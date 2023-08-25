package com.smart.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@GetMapping(value={"/", "/home"})
	public String home(Model model) {
		model.addAttribute("title", " Home : smart contact Manager");
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About : Smart contact Manager");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register : Smart contact manager");
		model.addAttribute("user", new User());
		return "signup";
	}
    // handler for custom login
    @GetMapping("/signIn")
    public String customLogin(Model model){
        model.addAttribute("title","Login - Smart Contact Manager");
        return "login";
    }
    
    @RequestMapping(value="/register/", method = RequestMethod.POST)
	public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam(value="agreement", defaultValue = "false" ) boolean agreement, Model model, HttpSession session) {
		
		try {	
			if(bindingResult.hasErrors()) {
				System.out.println("Error : "+bindingResult);
				return "signup";
			}
			
			if(!agreement) {
				System.out.println("you have not agreed terms and Condition, .");
				throw new Exception("you have not accept terms and Condition.");
			}
			System.out.println("agreement : "+agreement);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setImageUrl("profile.jpg");
			user.setEnabled(true);
			user.setRole("ROLE_USER");

			
			User saveResult = userRepository.save(user);
		 
			User emptyUser = new User();
			model.addAttribute("user", emptyUser);
			session.setAttribute("message", new Message("Registration successfully", "alert-success"));
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("ohhh..!"+e.getMessage(), "alert-danger") );
			return "signup";
		}
		return "signup";
	}


}
