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
import com.csm.dashboard.misc.ResponseMap;
import com.csm.dashboard.model.CSMUser;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserRepository userRepository;
	
	public HashMap<String, Object> validateUser(String userid, String password) {
		List<CSMUser> getAllUserData = userRepository.findAll();
		Optional<CSMUser> userData = userRepository.findByUserId(userid);
		HashMap<String, Object> responseMap = new HashMap<String, Object>(); 
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
				responseMap.put("status", 200);
				responseMap.put("message", "success");
				responseMap.put("key", MD5String);
				responseMap.put("admin", userData.get().getAdmin());
			}
			else {
				throw new UnathorizedException(userid+" not authorized");
			}
		}		
		return responseMap;
	}
	
	/**
	 * Create new user
	 * @param user
	 * @return
	 */
	public Object createUser(CSMUser user) {
		HashMap<String, String> repsonse=ResponseMap.getResponseMap();

		if(userRepository.save(user)!=null) {
			repsonse.put("status", "true");
		}else {
			repsonse.put("status", "false");
		}
		
		return repsonse;
	}
	
	
	/**
	 * UpdateAttributes of User
	 * @param user
	 * @return
	 */
	public Object updateUser(CSMUser user) {
		HashMap<String, String> repsonse=ResponseMap.getResponseMap();

		CSMUser csmUser= userRepository.findByUserId(user.getUserId()).get();
		
		csmUser.setAdmin(user.getAdmin());
		csmUser.setfName(user.getfName());
		csmUser.setlName(user.getlName());
		csmUser.setPassword(user.getPassword());
		csmUser.setUserId(user.getUserId());
		if(userRepository.save(csmUser)!=null) {
			repsonse.put("status", "true");
		}else {
			repsonse.put("status", "false");
		}
		
		return repsonse;
	
	}

	/**
	 * Delete and User By Id
	 * @param id
	 * @return
	 */
	public HashMap<String,String> removeUser(String id) {
		HashMap<String, String> repsonse=ResponseMap.getResponseMap();
		try{
			userRepository.delete( userRepository.findByUserId(id).get());
			
			repsonse.put("status", "true");
		}catch (Exception e) {
			repsonse.put("status", "false");
		}
		
		return repsonse;
	}

	public List<CSMUser>  getAllUser() {
		List<CSMUser> list=userRepository.findAll();
		return list;
	}
	
}
