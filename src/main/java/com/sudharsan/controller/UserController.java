package com.sudharsan.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.exception.ServiceException;
import com.blog.model.UserDetail;
import com.blog.service.UserService;
@RestController
@RequestMapping("/users")
public class UserController {
	

		private UserDetail userDetail = new UserDetail();

		private UserService userService = new UserService();

		@GetMapping
		public List<UserDetail> index(ModelMap modelMap) {
			List<UserDetail> userList = userService.serviceListAllUsers();
			return userList;
		}

		@PostMapping("/save")
		public String store(@RequestBody UserDetail userDetail) {
			try {
				userService.serviceSave(userDetail);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			return "Your account has been created";
		}
		@GetMapping("/login")
		public String store(HttpSession session,@RequestParam("emailid") String email, @RequestParam("password") String password,ModelMap modelMap){
			userDetail.setEmailId(email);
			userDetail.setPassword(password);
			try {
				UserDetail user=userService.serviceLogin(userDetail);
				session.setAttribute("LOGGED_USER", user);
				if(user.getRoleId().getId()==2){
				return "../articles/viewArticles";
				}
				else
				{
					return "../users";
				}
			} 
			catch (ServiceException e) {
				modelMap.addAttribute("LOGIN_ERROR",e.getMessage());
				return "../register.jsp";
			}
		}
		@GetMapping("/logout")
		public String logout(HttpServletRequest request) {
			HttpSession httpSession = request.getSession(false);
			Object user = httpSession.getAttribute("LOGGED_USER");
			if (user != null) {
				httpSession.invalidate();
				return "redirect:/";
			} else {
				return "redirect:/";
			}
		}
		@GetMapping("/delete")
		public String delete(@RequestParam("userId") int userId){
			UserDetail userDetail=new UserDetail();
			userDetail.setId(userId);
			UserService userService=new UserService();
			try{
			userService.serviceDelete(userDetail);
			}
			catch(ServiceException e){
				e.printStackTrace();
			}
			return "redirect:../users";
		}
	}	

