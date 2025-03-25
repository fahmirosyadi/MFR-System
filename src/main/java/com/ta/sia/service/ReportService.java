//package com.ta.sia.service;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ResourceUtils;
//
//import com.ta.sia.entity.Rekening;
//import com.ta.sia.repository.RekeningRepository;
//
//import net.sf.jasperreports.engine.JREmptyDataSource;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperCompileManager;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//
//@Service
//public class ReportService {
//
//	
//	public String neraca(String format, List<Map<String, Object>> data1) throws FileNotFoundException, JRException {
//		String path = "C:\\Users\\lenovo\\Desktop\\report";
//		File file = ResourceUtils.getFile("classpath:report/Blank_A4.jrxml");
//		JasperReport jr = JasperCompileManager.compileReport(file.getAbsolutePath());
//		JRBeanCollectionDataSource data = new JRBeanCollectionDataSource(data1);
//		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
//		parameters.put("creator", "M. Fahmi Rosyadi");
//		JasperPrint jp = JasperFillManager.fillReport(jr, parameters,data);
//		if(format.equalsIgnoreCase("pdf")) {
//			JasperExportManager.exportReportToPdfFile(jp,path + "\\neraca.pdf");
//		}else if(format.equalsIgnoreCase("html")) {
//			JasperExportManager.exportReportToHtmlFile(jp,path + "\\neraca.html");
//		}
//		return "Exported to " + path;
//	}
//	
//}
