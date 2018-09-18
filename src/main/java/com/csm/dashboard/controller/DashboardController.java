package com.csm.dashboard.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csm.dashboard.model.UsageBean;
import com.csm.dashboard.service.DashboardServiceImpl;
import com.csm.dashboard.service.UserServiceImpl;

@RestController
@CrossOrigin
public class DashboardController {
	
	@Autowired
	private DashboardServiceImpl dashboardService;

	@Autowired
	private UserServiceImpl userService;

	@GetMapping(path="/usage/overall")
	public HashMap<String, Object> overAll(){
		return dashboardService.getOverallUsageData();
	}	
	
	@GetMapping(path="/usage/kpi")
	public HashMap<String, Object> lobAndProducts(){
		return dashboardService.getLOBAndProducts();
	}
	@GetMapping(path="/usage/periodic")
	public HashMap<String, Object> periodicUsage(){
		return dashboardService.getPeriodicUsage();
	}
	
	@GetMapping(path="/user/loginUser")
	public HashMap<String, Object> loginUser(@RequestParam("userid") String userid,
			@RequestParam("password") String password) {
		return userService.validateUser(userid, password);
		
	}
}