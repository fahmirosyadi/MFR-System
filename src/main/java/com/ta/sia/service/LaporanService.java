package com.ta.sia.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.sia.entity.AccountLink;
import com.ta.sia.entity.Rekening;
import com.ta.sia.repository.AccountLinkRepository;
import com.ta.sia.repository.JurnalRepository;
import com.ta.sia.repository.PembelianRepository;
import com.ta.sia.repository.PenjualanRepository;
import com.ta.sia.repository.RekeningRepository;
import com.ta.sia.repository.UserRepository;

@Service
public class LaporanService {

	@PersistenceContext
	private EntityManager em;
	@Autowired
	RekeningRepository rr;
	@Autowired
	UserRepository ur;
	@Autowired
	JurnalRepository jr;
	@Autowired
	PembelianRepository pr;
	@Autowired
	PenjualanRepository pnr;
	@Autowired
	AccountLinkRepository alr;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public List<Rekening> getAllData(List<Rekening> lrek){
		List<Rekening> data = new ArrayList<Rekening>();
		return data;
	}

	
	public Rekening neraca(Date periode) {
		System.out.println("NERACA!!");
		Optional<AccountLink> al = alr.findById(Long.valueOf(1));
		List<Rekening> labarugiList = rr.findByJenis("labarugi");
		Rekening labarugi = null;
		if(labarugiList != null && labarugiList.size() > 0) {
			labarugi = labarugiList.get(0);
			Rekening tam = new Rekening();
			tam.setNomor("00-0000");
			tam.setNama("LB");
			tam.setPeriode(periode);
			labarugi.setParent(tam);
			tam.getChild().add(labarugi);
		}
		System.out.println(periode + " : Saldo Laba Rugi : " + labarugi.getSaldo());
		if(al.isPresent()) {
			al.get().getRekLabaDitahan().setSaldoAwal(labarugi.getSaldo() - labarugi.getSaldoPeriodik());
			al.get().getRekLabaTahunBerjalan().setSaldoAwal(labarugi.getSaldoPeriodik());
		}
		List<Rekening> neracaList = rr.findByJenis("neraca");
		Rekening neraca = neracaList != null && neracaList.size() > 0 ? neracaList.get(0) : new Rekening();
		neraca.setPeriode(periode);
		return neraca;
	}
	
	public Rekening labarugi(Date periode) {
		List<Rekening> labarugiList = rr.findByJenis("labarugi");
		if(labarugiList != null && labarugiList.size() > 0) {
			Rekening labarugi = labarugiList.get(0);
			Rekening tam = new Rekening();
			tam.setNomor("00-0000");
			tam.setNama("LB");
			tam.setPeriode(periode);
			labarugi.setParent(tam);
			tam.getChild().add(labarugi);
//			em.detach(labarugi);
			return tam;
		}
		return null;
	}
	
}
