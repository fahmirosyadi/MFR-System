package com.ta.sia.entity;

import java.util.ArrayList;
import java.util.List;

public class CetakJurnal {
	
	public List<JurnalDetails> result = new ArrayList<JurnalDetails>();
	
	public void addTransaksi(Transaksi transaksi) {
//		Jurnal jurnal = transaksi.getJurnal();
//		for(JurnalDetails jd : jurnal.getJurnalDetails()) {
//			result.add(jd);
//		}
	}
	
	public void addTransaksi(List<Transaksi> transaksi) {
//		for(Transaksi t : transaksi) {
//			Jurnal jurnal = t.getJurnal();
//			for(JurnalDetails jd : jurnal.getJurnalDetails()) {
//				result.add(jd);
//			}
//		}
	}
	
	public List<JurnalDetails> getResult(){
		
		for(int i = 0; i < result.size(); i++) {
			 for(int j = 0; j < result.size() - 1; j++) {
				 if(result.get(j).getCreatedAt().compareTo(result.get(j + 1).getCreatedAt()) > 0) {
					 JurnalDetails tam = result.get(j);
					 result.set(j, result.get(j + 1));
					 result.set(j + 1, tam);
				 }
			 }
		}
		return result;
	}
	
}
