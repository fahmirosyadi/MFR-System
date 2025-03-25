package com.mfr.system.controller;
//package com.ta.sia.controller;
//
//import java.security.Principal;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.WebAttributes;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.SessionAttributes;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.ta.sia.entity.MyRole;
//import com.ta.sia.entity.RoleUser;
//import com.ta.sia.entity.User;
//import com.ta.sia.repository.RoleRepository;
//import com.ta.sia.repository.RoleUserRepository;
//import com.ta.sia.repository.UserRepository;
//import com.ta.sia.service.RekeningService;
//
//@Controller
//@SessionAttributes({"user"})
//public class LoginController {
//
//	@Autowired
//	UserRepository ur;
//	
//	@Autowired
//	RoleUserRepository rur;
//	
//	@Autowired
//	RoleRepository rr;
//	
//	@Autowired
//	RekeningService rs;
//	
//	@Autowired
//	BCryptPasswordEncoder encoder;
//	
//	@RequestMapping(value = "/login",method = RequestMethod.GET)
//	public String login() {
//		List<User> lUser = ur.findAll();
//		if(lUser.size() == 0) {
//			return "redirect:/register";
//		}
//		return "login";
//	}
//	
//	@RequestMapping(value = "/register",method = RequestMethod.GET)
//	public String registrasi() {
//		List<User> lUser = ur.findAll();
//		if(lUser.size() > 0) {
//			return "redirect:/login";
//		}
//		return "register";
//	}
//	
//	@RequestMapping(value = "/register",method = RequestMethod.POST)
//	public String save(Model model, User user) {	
//		user.setEnabled(true);
//		user.setPassword(encoder.encode(user.getPassword()));
//		ur.save(user);
//		RoleUser roleUser = new RoleUser();
//		roleUser.setUser(user);
//		roleUser.setRole("ADMIN");
//		rur.save(roleUser);
//		this.initialSetting();
//		return "redirect:/";
//	}
//	
//	@RequestMapping(value = "/login-error",method = RequestMethod.GET)
//	public String loginError(RedirectAttributes ra, HttpServletRequest request, Model model) {
//		HttpSession session = request.getSession(false);
//		String message = "";
//		if(session != null) {
//			AuthenticationException ae = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
//			if(ae != null) {
//				message = ae.getMessage();
//			}
//		}
//		ra.addFlashAttribute("warning", message);
//		System.out.println("Login Failed : " + message);
//		return "redirect:" + request.getHeader("Referer");
//	}
//	
//	public void initialSetting() {
//		rs.initialRekening();
//	}
//	
//}
