package com.ta.sia.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.ta.sia.entity.AccountLink;
import com.ta.sia.entity.Rekening;
import com.ta.sia.repository.AccountLinkRepository;
import com.ta.sia.repository.RekeningRepository;

@Service
public class RekeningService {

	@Autowired
	RekeningRepository rr;
	
	@Autowired
	AccountLinkRepository alr;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@EventListener
	public void initialRekening(ApplicationReadyEvent event) {
		if(rr.findAll().isEmpty()) {
			logger.info("Initializing account data");
			
			alr.deleteAll();
			
			Rekening rekening = new Rekening("0-0000","Rekening","rekening",Double.valueOf(0),true,null);
			
			Rekening neraca = new Rekening("0-1000","Neraca","neraca",Double.valueOf(0),true,rekening);
			Rekening labarugi = new Rekening("0-2000","Laba Rugi", "labarugi",Double.valueOf(0),true,rekening);
			
			Rekening aktiva = new Rekening("1-0000","Aktiva","assets",Double.valueOf(0),true, neraca);
			Rekening utang = new Rekening("2-0000","Utang","utang",Double.valueOf(0),true,neraca);
			Rekening modal = new Rekening("3-0000","Modal","modal",Double.valueOf(0),true,neraca);
			Rekening pendapatan = new Rekening("4-0000","Pendapatan","pendapatan",Double.valueOf(0),true,labarugi);
			Rekening hpp = new Rekening("5-0000","HPP","hpp",Double.valueOf(0),true,labarugi);
			Rekening beban = new Rekening("6-0000","Beban","beban",Double.valueOf(0),true,labarugi);
			
			Rekening aktivaLancar = new Rekening("1-1000","Aktiva Lancar","assets",Double.valueOf(0),true,aktiva);
			Rekening aktivaTetap = new Rekening("1-2000","Aktiva Tetap","assets",Double.valueOf(0),true,aktiva);
			Rekening utangLancar = new Rekening("2-1000","Utang Lancar","utang",Double.valueOf(0),true,utang);
			Rekening utangJangkaPanjang = new Rekening("2-2000","Utang Jangka Panjang","utang",Double.valueOf(0),true,utang);
			Rekening modalUsaha = new Rekening("3-1000","Modal Usaha","modal",Double.valueOf(0),true,modal);
			Rekening prive = new Rekening("3-2000","Prive","modal",Double.valueOf(0),true,modal);
			Rekening labaTahunBerjalan = new Rekening("3-3000","Laba Tahun Berjalan","modal",Double.valueOf(0),true,modal);
			Rekening labaDitahan = new Rekening("3-4000","Laba Ditahan","modal",Double.valueOf(0),true,modal);
			Rekening penjualan = new Rekening("4-1000","Penjualan","pendapatan",Double.valueOf(0),true,pendapatan);
			Rekening potonganPenjualan = new Rekening("4-2000","Potongan Penjualan","pendapatan",Double.valueOf(0),true,pendapatan);
			Rekening hargaPP = new Rekening("5-1000","Harga Pokok Produksi","hpp",Double.valueOf(0),true,hpp);
			Rekening bebanListrik = new Rekening("6-1000","Beban Listrik","beban",Double.valueOf(0),false,beban);
			Rekening bebanGaji = new Rekening("6-2000","Beban Gaji","beban",Double.valueOf(0),false,beban);
			Rekening bebanPenyusutanGedung = new Rekening("6-3000","Beban Penyusutan Gedung","beban",Double.valueOf(0),false,beban);
			Rekening bebanPenyusutanKendaraan = new Rekening("6-4000","Beban Penyusutan Kendaraan","beban",Double.valueOf(0),false,beban);
			Rekening bebanPenyusutanPeralatan = new Rekening("6-5000","Beban Penyusutan Peralatan","beban",Double.valueOf(0),false,beban);
			
			Rekening kas = new Rekening("1-1100","Kas","assets",Double.valueOf(0),true,aktivaLancar);
			Rekening piutang = new Rekening("1-1200","Piutang","assets",Double.valueOf(0),true,aktivaLancar);
			Rekening persediaan = new Rekening("1-1300","Persediaan","assets",Double.valueOf(0),true,aktivaLancar);
			Rekening pajakMasukan = new Rekening("1-1400","Pajak Masukan","assets",Double.valueOf(0),true,aktivaLancar);
			Rekening tanah = new Rekening("1-2100","Tanah","assets",Double.valueOf(0),true,aktivaTetap);
			Rekening bangunan = new Rekening("1-2200","Bangunan","assets",Double.valueOf(0),true,aktivaTetap);
			Rekening kendaraan = new Rekening("1-2300","Kendaraan","assets",Double.valueOf(0),true,aktivaTetap);
			Rekening peralatan = new Rekening("1-2400","Peralatan","assets",Double.valueOf(0),true,aktivaTetap);
			Rekening akPenyusutanGedung = new Rekening("1-2500","Akumulasi Penyusutan Gedung","assets",Double.valueOf(0),false,aktivaTetap);
			Rekening akPenyusutanKendaraan = new Rekening("1-2600","Akumulasi Penyusutan Kendaraan","assets",Double.valueOf(0),false,aktivaTetap);
			Rekening akPenyusutanPeralatan = new Rekening("1-2700","Akumulasi Penyusutan Peralatan","assets",Double.valueOf(0),false,aktivaTetap);
			Rekening utangUsaha = new Rekening("2-1100","Utang Usaha","utang",Double.valueOf(0),true,utangLancar);
			Rekening utangGaji = new Rekening("2-1200","Utang Gaji","utang",Double.valueOf(0),false,utangLancar);
			Rekening utangBeban = new Rekening("2-1300","Utang Beban","utang",Double.valueOf(0),false,utangLancar);
			Rekening utangPajak = new Rekening("2-1400","Utang Pajak","utang",Double.valueOf(0),false,utangLancar);
			Rekening pajakKeluaran = new Rekening("2-1500","Pajak Keluaran","utang",Double.valueOf(0),true,utangLancar);
			Rekening potonganPembelianBahan = new Rekening("5-2100","Potongan Pembelian Bahan","hpp",Double.valueOf(0),true,hpp);
			
			Rekening bahanBaku = new Rekening("1-1310","Persediaan Bahan Baku dan Penolong","assets",Double.valueOf(0),true,persediaan);
			Rekening barangDalamProses = new Rekening("1-1320","Persediaan Barang Dalam Proses","assets",Double.valueOf(0),true,persediaan);
			Rekening barangJadi = new Rekening("1-1330","Persediaan Barang Jadi","assets",Double.valueOf(0),true,persediaan);
			
			AccountLink al = new AccountLink();
			al.setRekPembayaranPiutang(kas);
			al.setRekPembayaranUtang(kas);
			al.setRekPembelian(utangUsaha);
			al.setRekPenjualan(piutang);
			al.setRekLabaTahunBerjalan(labaTahunBerjalan);
			al.setRekLabaDitahan(labaDitahan);
			al.setRekPotonganPembelian(potonganPembelianBahan);
			al.setRekPotonganPenjualan(potonganPenjualan);
			
			rr.save(rekening);
			alr.save(al);
			
			logger.info("Account data initialization was successful");
		}
	}
	
}
