package com.ta.sia.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ta.sia.entity.Menu;
import com.ta.sia.entity.Role;
import com.ta.sia.entity.User;
import com.ta.sia.repository.MenuRepository;
import com.ta.sia.repository.RoleRepository;
import com.ta.sia.repository.UserRepository;

@Service
@Transactional
public class InitialDataService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MenuService ms;
	@Autowired
	public RoleRepository rr;
	@Autowired
	public UserRepository ur;
	@Autowired
	MenuRepository mr;
	@Autowired
	BCryptPasswordEncoder encoder;

	@EventListener
	public void initialMenu (ApplicationReadyEvent event) {
		
		if(mr.findAll().isEmpty()) {
			
			List<Menu> menuList = new ArrayList<Menu>();
			
			Menu dashboard = new Menu("Dashboard","dashboard","fa-tachometer-alt",1,null);
			Menu dataMaster = new Menu("Data Master","#","fa-table",2,null);
			Menu transaksi = new Menu("Transaksi","#","fa-table",3,null);
			Menu laporan = new Menu("Laporan","#","fa-table",4,null);
			
			Menu role = new Menu("Role","role","fa-table",1,dataMaster);
			Menu user = new Menu("User","user","fa-table",2,dataMaster);
			Menu authority = new Menu("Authority","authority","fa-table",3,dataMaster);
			Menu rekening = new Menu("Rekening","rekening","fa-table",4,dataMaster);
			Menu supplier = new Menu("Supplier","supplier","fa-table",5,dataMaster);
			Menu customer = new Menu("Customer","customer","fa-table",6,dataMaster);
			Menu persediaan = new Menu("Persediaan","persediaan","fa-table",7,dataMaster);
			Menu pajak = new Menu("Pajak","pajak","fa-table",8,dataMaster);
			
			Menu jurnalUmum = new Menu("Jurnal Umum","jurnal-umum","fa-table",1,transaksi);
			Menu pembelian = new Menu("Pembelian","pembelian","fa-table",2,transaksi);
			Menu penjualan = new Menu("Penjualan","penjualan","fa-table",3,transaksi);
			Menu penyesuaianPersediaan = new Menu("Penyesuaian Persediaan","penyesuaian-persediaan","fa-table",4,transaksi);
			Menu pembayaranUtang = new Menu("Pembayaran Utang","pembayaran-utang","fa-table",5,transaksi);
			Menu pembayaranPiutang = new Menu("Pembayaran Piutang","pembayaran-piutang","fa-table",6,transaksi);
			
			Menu neraca = new Menu("Neraca","neraca","fa-table",1,laporan);
			Menu labarugi = new Menu("Laba Rugi","laba-rugi","fa-table",2,laporan);
			Menu bukuBesar = new Menu("Buku Besar","buku-besar","fa-table",3,laporan);
			
			menuList.add(dashboard);
			menuList.add(dataMaster);
			menuList.add(transaksi);
			menuList.add(laporan);
			mr.saveAll(menuList);

			logger.info("Menu Initialization");
			
			this.initialRole();
			
		}
	}
	
	public void initialRole() {
		Role r = new Role(Long.valueOf(1),"OWNER");
		List<Role> rList = new ArrayList<>();
		rList.add(r);
		this.rr.saveAll(rList);

		logger.info("Role Initialization");
		
		this.initialAuthority();
		
	}
	
	public void initialAuthority() {

		Menu authority = this.mr.findByName("Authority");
		Menu dashboard = this.mr.findByName("Dashboard");
		Menu dataMaster = this.mr.findByName("Data Master");
		
		Role r = this.rr.findAll().get(0);
		System.out.println("Role : " + r.getRole());
		List<Role> rList = new ArrayList<>();
		rList.add(r);
		if(rList.size() > 0) {
			authority.setRole(rList);
			dashboard.setRole(rList);
			dataMaster.setRole(rList);
			this.mr.save(this.ms.getMenuWithAuthority(authority));
			this.mr.save(this.ms.getMenuWithAuthority(dashboard));
			this.mr.save(this.ms.getMenuWithAuthority(dataMaster));
		}
		logger.info("Authority Initialization");
		
		this.initialUser();
		
	}
	
	public void initialUser() {
		User u = new User();
		u.setUsername("user_owner");
		u.setNama("USER OWNER");
		u.setEmail("user_o@mailinator.com");
		u.setEnabled(true);
		u.setPassword(encoder.encode(""));
		u.getRole().add(this.rr.findByRole("OWNER"));
		
		List<User> uList = new ArrayList<>();
		uList.add(u);
		
		this.ur.saveAll(uList);
		
		logger.info("User Initialization");
	}
	
	

}
