package com.mfr.system.service;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CellBuilder {

	private Cell cell;
	private Workbook wb;
	private CreationHelper createHelper;
	
	public CellBuilder(Workbook wb) {
		this.wb = wb;
		this.createHelper = wb.getCreationHelper();
	}
	
	public Cell getCell() {
		return this.cell;
	}
	
	public CellBuilder create(Row row, int cellNumber) {
		this.cell = row.createCell(cellNumber);
		CellStyle cellStyle = wb.createCellStyle();
		this.cell.setCellStyle(cellStyle);
		return this;
	}
	
	public CellBuilder addBorder() {
		this.cell.getCellStyle().setBorderBottom(BorderStyle.THIN);
		this.cell.getCellStyle().setBottomBorderColor(IndexedColors.BLACK.getIndex());
		this.cell.getCellStyle().setBorderLeft(BorderStyle.THIN);
		this.cell.getCellStyle().setLeftBorderColor(IndexedColors.BLACK.getIndex());
		this.cell.getCellStyle().setBorderRight(BorderStyle.THIN);
		this.cell.getCellStyle().setRightBorderColor(IndexedColors.BLACK.getIndex());
		this.cell.getCellStyle().setBorderTop(BorderStyle.THIN);
		this.cell.getCellStyle().setTopBorderColor(IndexedColors.BLACK.getIndex());
		return this;
	}
	
	public CellBuilder addDataFormat() {
		return this.addDataFormat("#,##0.00");
	}
	
	public CellBuilder addDataFormat(String format) {
		cell.getCellStyle().setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
		return this;
	}
	
	public CellBuilder addDateFormat() {
		return this.addDateFormat("m/d/yy");
	}
	
	public CellBuilder addDateFormat(String format) {
		cell.getCellStyle().setDataFormat(createHelper.createDataFormat().getFormat(format));
		return this;
	}
	
	public CellBuilder setAsHeader() {
		cell.getCellStyle().setFillForegroundColor(IndexedColors.AQUA.getIndex());
		cell.getCellStyle().setFillPattern(FillPatternType.SOLID_FOREGROUND);
		this.setFont("Arial", (short) 12, true);
		return this;
	}
	
	public CellBuilder setFont(String fontName, short height, boolean isBold) {
		XSSFFont font = ((XSSFWorkbook) wb).createFont();
		font.setFontName(fontName);
		font.setFontHeightInPoints(height);
		font.setBold(isBold);
		cell.getCellStyle().setFont(font);
		return this;
	}
	
	public CellBuilder setAlign(HorizontalAlignment align) {
		this.cell.getCellStyle().setAlignment(align);
		return this;
	}
	
	public CellBuilder setAlign(VerticalAlignment align) {
		this.cell.getCellStyle().setVerticalAlignment(align);
		return this;
	}
	
	public CellBuilder setValue(Double value, boolean nullable) {
		if(!nullable || value.equals(Double.valueOf(0)) == false) {
			this.cell.setCellValue(value);
		}
		return this;
	}
	
}
