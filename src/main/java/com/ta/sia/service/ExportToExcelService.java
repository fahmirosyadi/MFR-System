package com.ta.sia.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ta.sia.entity.BukuPembantuPiutang;
import com.ta.sia.entity.BukuPembantuUtang;
import com.ta.sia.entity.Customer;
import com.ta.sia.entity.Rekening;
import com.ta.sia.entity.Supplier;
import com.ta.sia.repository.CustomerRepository;
import com.ta.sia.repository.SupplierRepository;

@Service
public class ExportToExcelService {

	private Workbook wb;
	
	private Date periode;
	private int rowNumber;
	private CellBuilder cb;
	@Autowired
	private SupplierRepository sr;
	@Autowired
	private CustomerRepository cr;
	
	@Autowired
	private LaporanService ls;
	
	public boolean export(Date periode) {
		@SuppressWarnings("deprecation")
		String fileName = "Report_" + periode.getYear() + periode.getMonth() + periode.getDay();
		return this.export(periode, fileName);
	}
	
	public boolean export(Date periode, String fileName) {
		this.wb = new XSSFWorkbook();
		this.cb = new CellBuilder(wb);
		this.periode = periode;
		this.jurnal();
		this.bukuPembantuUtang();
		this.bukuPembantuPiutang();
		this.neraca();
		this.labarugi();
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		System.out.println(path);
		String fileLocation = path.substring(0, path.length() - 1) + fileName + ".xlsx";
		try {
			FileOutputStream outputStream = new FileOutputStream(fileLocation);
			wb.write(outputStream);
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void neraca() {
		Sheet neracaSheet = this.wb.createSheet("Neraca");
		String[] header = {"Rekening","Saldo"};
		Rekening neraca = ls.neraca(periode);
		this.rowNumber = 0;
		this.generateReport(neracaSheet, neraca, -1);
		for(int i = 0; i < header.length; i ++) {
			neracaSheet.autoSizeColumn(i);
		}
	}
	
	public void labarugi() {
		Sheet lrSheet = this.wb.createSheet("Laba Rugi");
		String[] header = {"Rekening","Saldo"};
		Rekening labarugi = ls.labarugi(periode);
		this.rowNumber = 0;
		this.generateReport(lrSheet, labarugi.getChild().get(0), -1);
		for(int i = 0; i < header.length; i ++) {
			lrSheet.autoSizeColumn(i);
		}
	}
	
	public void generateReport(Sheet sheet, Rekening r, int indent) {
		String ind = "";
		for(int i = 0; i < indent; i++) {
			ind += "    ";
		}
		
		if(r.getJenis().equals("neraca") == false) {
			Row row = sheet.createRow(rowNumber++);	
			row.createCell(0).setCellValue(ind + r.getNama());
			if(!r.isHeader()) {
				this.cb.create(row, 4 - indent).addDataFormat().getCell().setCellValue(r.getSaldo());
			}
		}

		List<Rekening> child = r.getChild();
		for(Rekening rek : child) {
			this.generateReport(sheet, rek, indent + 1);
		}
		
		if(r.getJenis().equals("neraca") == false && r.isHeader()) {
			Row rowTotal = sheet.createRow(rowNumber++);
			rowTotal.createCell(0).setCellValue(ind + "Total " +  r.getNama());
			this.cb.create(rowTotal, 4 - indent).addDataFormat().getCell().setCellValue(r.getSaldo());
		}
		
	}
	
	public void jurnal() {
//		List<Transaksi> lTrans = null;
		Sheet jurnalSheet = this.wb.createSheet("Jurnal Umum");
		String[] header = {"No","Tanggal","Rekening","Debet","Kredit","Keterangan"};
		this.rowNumber = 1;
		String[] title = {"Jurnal Umum","Per " + periode};
		this.createTitle(jurnalSheet, header, title);
		this.createHeader(jurnalSheet, header);
//		int no = 1;
//		for(Transaksi t : lTrans) {
//			Jurnal j = t.getJurnal();
//			for(JurnalDetails jd : j.getJurnalDetails()) {
//				Row row = jurnalSheet.createRow(this.rowNumber);
//				Cell noCell = cb.create(row, 0).addBorder().setAlign(HorizontalAlignment.CENTER).getCell();
//				Cell tglCell = cb.create(row, 1).addBorder().addDateFormat().getCell();
//				cb.create(row, 2).addBorder().getCell().setCellValue(jd.getRekening().getNama());
//				cb.create(row, 3).addBorder().addDataFormat().setValue(jd.getDebet(), true).getCell();
//				cb.create(row, 4).addBorder().addDataFormat().setValue(jd.getKredit(), true).getCell();
//				Cell ketCell = cb.create(row, 5).addBorder().addDataFormat().getCell();
//				if(jd == j.getJurnalDetails().get(0)) {
//					noCell.setCellValue(no++);
//					tglCell.setCellValue(j.getTanggal());
//					ketCell.setCellValue(j.getKeterangan());
//				}
//				this.rowNumber ++;
//			}
//		}
		for(int i = 0; i < header.length; i ++) {
			jurnalSheet.autoSizeColumn(i);
		}
	}
	
	public void bukuPembantuUtang() {
		Sheet sheet = this.wb.createSheet("Buku Pembantu Utang");
		this.rowNumber = 1;
		String[] title = {"Buku Pembantu Utang"};
		Integer size = Integer.valueOf(Long.valueOf(sr.count()).toString());
		List<Supplier> lSup = sr.findLike("", PageRequest.of(0, size)).getContent();
		String[] header = {"No","Tanggal","Faktur","Keterangan","Debet","Kredit","Saldo"};
		this.createTitle(sheet, header, title);
		for(Supplier s : lSup) {
			Row row1 = sheet.createRow(this.rowNumber++);
			this.cb.create(row1, 0).setFont("Arial", (short) 12, true).getCell().setCellValue(s.getNama());
			this.createHeader(sheet, header);
			int no = 1;
			for(BukuPembantuUtang bpu : s.getBukuPembantuUtang()) {
				Row row = sheet.createRow(this.rowNumber);
				this.cb.create(row, 0).addBorder().setAlign(HorizontalAlignment.CENTER).getCell().setCellValue(no++);;
				this.cb.create(row, 1).addBorder().addDateFormat().getCell().setCellValue(bpu.getTanggal());
				this.cb.create(row, 2).addBorder().getCell().setCellValue(bpu.getFaktur());
				this.cb.create(row, 3).addBorder().getCell().setCellValue(bpu.getKeterangan());
				this.cb.create(row, 4).addBorder().addDataFormat().setValue(bpu.getDebet(), true);
				this.cb.create(row, 5).addBorder().addDataFormat().setValue(bpu.getKredit(), true);
				this.cb.create(row, 6).addBorder().addDataFormat().setValue(bpu.getSaldo(), true);
				this.rowNumber++;
			}
			this.rowNumber++;
		}
		for(int i = 0; i < header.length; i ++) {
			sheet.autoSizeColumn(i);
		}
	}
	
	public void bukuPembantuPiutang() {
		Sheet sheet = this.wb.createSheet("Buku Pembantu Piutang");
		this.rowNumber = 1;
		String[] title = {"Buku Pembantu Piutang"};
		Integer size = Integer.valueOf(Long.valueOf(cr.count()).toString());
		List<Customer> lCus = cr.findLike("", PageRequest.of(0, size)).getContent();
		String[] header = {"No","Tanggal","Faktur","Keterangan","Debet","Kredit","Saldo"};
		this.createTitle(sheet, header, title);
		for(Customer c : lCus) {
			Row row1 = sheet.createRow(this.rowNumber++);
			this.cb.create(row1, 0).setFont("Arial", (short) 12, true).getCell().setCellValue(c.getNama());
			this.createHeader(sheet, header);
			int no = 1;
			for(BukuPembantuPiutang bpp : c.getBukuPembantuPiutang()) {
				Row row = sheet.createRow(this.rowNumber);
				this.cb.create(row, 0).addBorder().setAlign(HorizontalAlignment.CENTER).getCell().setCellValue(no++);;
				this.cb.create(row, 1).addBorder().addDateFormat().getCell().setCellValue(bpp.getTanggal());
				this.cb.create(row, 2).addBorder().getCell().setCellValue(bpp.getFaktur());
				this.cb.create(row, 3).addBorder().getCell().setCellValue(bpp.getKeterangan());
				this.cb.create(row, 4).addBorder().addDataFormat().setValue(bpp.getDebet(), true);
				this.cb.create(row, 5).addBorder().addDataFormat().setValue(bpp.getKredit(), true);
				this.cb.create(row, 6).addBorder().addDataFormat().setValue(bpp.getSaldo(), true);
				this.rowNumber++;
			}
			this.rowNumber++;
		}
		for(int i = 0; i < header.length; i ++) {
			sheet.autoSizeColumn(i);
		}
	}
	
	public void createHeader(Sheet sheet, String[] header) {
		Row headerRow = sheet.createRow(this.rowNumber++);
		int colNumber = 0;
		for(String value : header) {
			Cell headerCell = this.cb.create(headerRow, colNumber).addBorder().setAsHeader().getCell();
			headerCell.setCellValue(value);
			colNumber++;
		}
	}
	
	public void createTitle(Sheet sheet, String[] header, String[] title) {
		for(String t : title) {
			sheet.addMergedRegion(new CellRangeAddress(this.rowNumber,this.rowNumber,0,header.length - 1));
			Row titleRow = sheet.createRow(this.rowNumber++);
			this.cb.create(titleRow, 0).setFont("Arial", (short) 14, true).setAlign(HorizontalAlignment.CENTER)
			.getCell().setCellValue(t);
		}
		this.rowNumber++;
	}
	
}
