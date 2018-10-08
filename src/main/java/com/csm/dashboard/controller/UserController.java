package com.csm.dashboard.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csm.dashboard.model.CSMUser;
import com.csm.dashboard.service.UserServiceImpl;

@RestController
@CrossOrigin
@RequestMapping(path="/user")
public class UserController {

	@Autowired
	private UserServiceImpl userService;
	
	@GetMapping(path="/loginUser")
	public HashMap<String, Object> loginUser(@RequestParam("userid") String userid,
			@RequestParam("password") String password) {
		return userService.validateUser(userid, password);
		
	}
	
	@GetMapping(path="/mgmt")
	public List<CSMUser>  getUser() {
		return userService.getAllUser();
	}
	
	@PostMapping(path="/mgmt")
	public Object createUser(@RequestBody CSMUser user) {
			return userService.createUser(user);
	}
	
	@PutMapping(path="/mgmt")
	public Object updateUser(@RequestBody CSMUser user) {
			return userService.updateUser(user);
	}
	
	@DeleteMapping(path="/mgmt/{id}")
	public Object deleteUser(@PathVariable("id") String id) {
		return userService.removeUser(id);
	}
}
