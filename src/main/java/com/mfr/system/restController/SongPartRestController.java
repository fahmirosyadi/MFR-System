package com.mfr.system.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mfr.system.entity.Chord;
import com.mfr.system.entity.SongPart;
import com.mfr.system.repository.CommonRepository;
import com.mfr.system.repository.SongPartRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/songPart")
public class SongPartRestController extends AbstractRestController<SongPart> {

	@Autowired
	private SongPartRepository repo;
	
	@Override
	public CommonRepository<SongPart, Long> getRepository() {
		return this.repo;
	}

	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(Chord.class.getDeclaredFields());
	}

}
