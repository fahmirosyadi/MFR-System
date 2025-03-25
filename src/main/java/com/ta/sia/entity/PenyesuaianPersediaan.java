package com.ta.sia.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class PenyesuaianPersediaan extends Transaksi {

	@Valid
	@Size(min = 1,message = "Penyesuaian Persediaan Details tidak boleh kosong!")
	@OneToMany(mappedBy = "penyesuaianPersediaan", cascade = CascadeType.ALL)
	@JsonIgnoreProperties(value = {"penyesuaianPersediaan"})
	private List<PenyesuaianPersediaanDetails> penyesuaianPersediaanDetails = new ArrayList<PenyesuaianPersediaanDetails>();
	
	@Transient
	public void detailsFilter(){
		for(int index = 0; index < this.penyesuaianPersediaanDetails.size(); index++){
			PenyesuaianPersediaanDetails ppd = this.penyesuaianPersediaanDetails.get(index);
			if(ppd.getJumlah() == null || ppd.getJumlah() == 0){
				this.penyesuaianPersediaanDetails.remove(index);
				index--;
			}else{
				ppd.setPenyesuaianPersediaan(this);
				ppd.setTransaksi(this);
				ppd.setId(null);
			}
		}
	}
	
}
