package com.mfr.system.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mfr.system.dto.ResponseDto;
import com.mfr.system.entity.PasswordResetToken;
import com.mfr.system.entity.User;
import com.mfr.system.repository.CommonRepository;
import com.mfr.system.repository.PasswordResetTokenRepository;
import com.mfr.system.repository.RoleRepository;
import com.mfr.system.repository.UserRepository;
import com.mfr.system.service.EmailServiceImpl;
import com.mfr.system.service.FileUploadService;
import com.mfr.system.util.JwtUtil;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.Principal;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserRestController extends AbstractRestController<User>{

	@Autowired
	EmailServiceImpl esi;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	UserRepository ur;
	
	@Autowired
	PasswordResetTokenRepository ptr;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	RoleRepository rr;
	
	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/create")
	public ResponseEntity<Object> create(){
		return new ResponseEntity<>(new ResponseDto(true, "Data berhasil diambil", new User()),HttpStatus.OK);
	} 
	
	@GetMapping("/count")
	public ResponseEntity<Object> count(){
		return new ResponseEntity<>(new ResponseDto(true, "Data berhasil diambil", ur.count()),HttpStatus.OK);
	} 	
	
	@GetMapping("/login")
	public ResponseEntity<Object> userLogin(Principal p){
		return new ResponseEntity<>(new ResponseDto(true, "Data berhasil diambil", ur.findByUsername(p.getName()).get()),HttpStatus.OK);
	}
	
	@Override
	@GetMapping("/{data}")
	public ResponseEntity<Object> details(@PathVariable User data){
		System.out.println("Role : " + data.getRole().size());
		return this.getResponse(data);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> userLogin(@RequestBody User user) throws Exception{
		System.out.println("Login");
		Map<String,Object> obj = new HashMap<String, Object>();
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (Exception e) {
			obj.put("status", false);
			obj.put("messages", e.getMessage());
			return new ResponseEntity<>(obj,HttpStatus.OK);
		}
		Map<String,Object> obj2 = new HashMap<String, Object>();
		obj2.put("user",ur.findByUsername(user.getUsername()));
		obj2.put("token", jwtUtil.generateToken(user));
		
		obj.put("status", true);
		obj.put("messages", "Login berhasil");
		obj.put("data", obj2);
		System.out.println("Success");
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}

	@PostMapping("")
	@Override
	public ResponseEntity<Object> save(@RequestBody User user, BindingResult bindingResult){
		return this.save(user, bindingResult, (Map<String, Object> obj) -> {
			if(user.getId() != null) {
				Optional<User> u = ur.findById(user.getId());
				if(u.isPresent()) {
					if(!user.isChangePassword()) {
						user.setPassword(u.get().getPassword());
					}else {
						System.out.println("PW : " + user.getPassword());
						System.out.println("PWC : " + user.getPasswordConfirm());
						if(!user.getPassword().equals(user.getPasswordConfirm())) {
							obj.put("status", false);
							obj.put("messages", "Konfirmasi Password tidak sesuai!");
						}
						user.setPassword(encoder.encode(user.getPassword()));
					}
				}
			}else {
				user.setPassword(encoder.encode(user.getPassword()));
			}
			System.out.println("S : " + obj.get("status"));
			if(obj.get("status") == null || (boolean) obj.get("status")) {
				ur.saveAndFlush(user);	
				this.log.info("Save User");
			}
		});
	}
	
	@PostMapping("/register")
	public ResponseEntity<Object> register(@RequestBody User user, BindingResult bindingResult){
		return this.save(user, bindingResult, (Map<String, Object> obj) -> {
			if(user.getId() != null) {
				Optional<User> u = ur.findById(user.getId());
				if(u.isPresent() == false) {
					user.setPassword(encoder.encode(user.getPassword()));
				}
			}else {
				user.setPassword(encoder.encode(user.getPassword()));
			}
			
			ur.save(user);
		});
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<Object> resetPassword(HttpServletRequest request, 
			@RequestBody User user2) {
		Map<String,Object> obj = new HashMap<String, Object>();
		
		Optional<User> user = this.ur.findByEmail(user2.getEmail());
	    if (user == null || !user.isPresent()) {
	    	obj.put("status", false);
			obj.put("messages", "Email is not found!");
		    return new ResponseEntity<>(obj,HttpStatus.OK);
	    }
	    
	    String token = UUID.randomUUID().toString();
	    
	    this.log.info("Before save token");
	    
	    if(user.get().getPasswordResetToken() != null) {
	    	this.ptr.delete(user.get().getPasswordResetToken());	
	    }
	    this.ptr.save(new PasswordResetToken(null, token, user.get(), new Date(System.currentTimeMillis())));

	    this.log.info("After save token"); 
	    
	    String url = "localhost:4200/changePassword/" + token;
	    esi.sendSimpleMessage(user.get().getEmail(), "Change Password", "Please Click this Link : " + url);
	    
	    obj.put("status", true);
		obj.put("messages", "Data berhasil diambil");
	    return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	public String validatePasswordResetToken(String token) {
	    this.log.info("Start validate password reset token");
		PasswordResetToken passToken = null;
	    if(ptr.findByToken(token).isPresent()) {
	    	passToken = ptr.findByToken(token).get();
	    }
	    
	    if(!this.isTokenFound(passToken)) {
	    	this.log.warning("Invalid Token");
	    	return "invalidToken";
	    }else if(!this.isTokenExpired(passToken)) {
	    	this.log.warning("Expired Token");
	    	return "expired";
	    }else {
	    	this.log.warning("Token is Valid");
	    	return null;
	    }
	}

	private boolean isTokenFound(PasswordResetToken passToken) {
	    return passToken != null;
	}

	private boolean isTokenExpired(PasswordResetToken passToken) {
	    final Calendar cal = Calendar.getInstance();
	    return passToken.getExpiryDate().before(cal.getTime());
	}
	
	@PostMapping("/changePassword")
	public ResponseEntity<Object> savePassword(@RequestBody User user2) {
		this.log.info("Start change password");
		String token = user2.getPasswordResetToken().getToken();
	    String result = this.validatePasswordResetToken(token);

	    Map<String,Object> obj = new HashMap<String, Object>();
		
	    if(result != null) {
	    	obj.put("status", false);
			obj.put("messages", "Token Invalid");
			return new ResponseEntity<>(obj,HttpStatus.OK);
	    }
	    
	    if(this.ptr.findByToken(token).isPresent()) {
	    	if(user2.getPassword().equals(user2.getPasswordConfirm())) {
	    		PasswordResetToken prt = this.ptr.findByToken(token).get();
		    	User u = prt.getUser();
		    	u.setPassword(encoder.encode(user2.getPassword()));
		    	this.log.info("Start save user");
		    	this.ur.save(u);
		    	this.log.info("Success save user");
		        obj.put("status", true);
				obj.put("messages", "Ubah Password Berhasil");
	    	}else {
	    		this.log.warning("Konfirmasi Password tidak sesuai!");
		    	obj.put("status", false);
				obj.put("messages", "Konfirmasi Password tidak sesuai!");
	    	}
	    } else {
	    	this.log.warning("User not found!");
	    	obj.put("status", false);
			obj.put("messages", "User Tidak ditemukan");
	    }
	    return new ResponseEntity<>(obj,HttpStatus.OK);
	
	}

	@Override
	public CommonRepository<User, Long> getRepository() {
		return this.ur;
	}

	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(User.class.getDeclaredFields());
	}
}