package com.mfr.system.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mfr.system.entity.Genre;
import com.mfr.system.repository.CommonRepository;
import com.mfr.system.repository.GenreRepository;

@RestController
@CrossOrigin
@RequestMapping("/api/genre")
public class GenreRestController extends AbstractRestController<Genre> {

	@Autowired
	private GenreRepository repo;
	
	@Override
	public CommonRepository<Genre, Long> getRepository() {
		return this.repo;
	}

	@Override
	public List<String> getDatatablesFields() {
		return this.getDatatablesFields(Genre.class.getDeclaredFields());
	}

}
