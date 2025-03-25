package com.ta.sia.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.sia.repository.RekeningRepository;

@RestController
@RequestMapping("/report")
public class ReportController {

	@Autowired
	RekeningRepository rr;
	
	
	
//	@GetMapping("/neraca/{format}")
//	public String neraca(Model model,@PathVariable String format) throws FileNotFoundException, JRException {
//		return rs.neraca(format);
//	}
	
}
