package com.csm.dashboard.service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csm.dashboard.dao.UserRepository;
import com.csm.dashboard.exception.UnathorizedException;
import com.csm.dashboard.exception.UserNotFoundException;
import com.csm.dashboard.model.CSMUser;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository userRepository;
	
	public HashMap<String, Object> validateUser(String userid, String password) {
		List<CSMUser> getAllUserData = userRepository.findAll();
		Optional<CSMUser> userData = userRepository.findByUserId(userid);
		HashMap<String, Object> map1 = new HashMap<String, Object>(); 
		if (!userData.isPresent()) {
			throw new UserNotFoundException(userid+" not found");
		}
		else if (userData.isPresent()) {
			if(userData.get().getPassword().equals(password)) {
				String userID = userData.get().getUserId();
				String randomString = userID + UUID.randomUUID().toString();
				String MD5String = null;
				try{
					MD5String = DatatypeConverter.printHexBinary( 
					           MessageDigest.getInstance("MD5").digest(randomString.getBytes("UTF-8")));
				}
				catch(Exception e) {
					System.out.println("Exception during MD5 random key creation");
				}
				//CSMUser userValue = new CSMUser(userData.get().getUserId(), userData.get().getPassword(), MD5String);
				userData.get().setKey(MD5String);
				userRepository.save(userData.get());
				map1.put("status", 200);
				map1.put("message", "success");
				map1.put("key", MD5String);
			}
			else {
				throw new UnathorizedException(userid+" not authorized");
			}
		}		
		return map1;
	}
}
