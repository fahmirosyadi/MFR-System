package com.mfr.system.service;

import java.io.File;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	
	private final String path = System.getProperty("user.home") + File.separator + "tmp";

	public String save(MultipartFile file, String folder) {
		String lokasi = path + File.separator + folder;
		File folderTujuan = new File(lokasi);
		if (!folderTujuan.exists()) {
			folderTujuan.mkdirs();
		}
		String namaBaru = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
		File tujuan = new File(folderTujuan + File.separator + namaBaru);
		try {
			file.transferTo(tujuan);
			return namaBaru;		
		} catch (Exception e) {
			System.out.println("Gagal upload file : " + e.toString());
		}
		return "";
	}
	
	public boolean hapusLama(String lama, String folder) {
		File fotoLama = new File(path + File.separator + folder + File.separator + lama);
		if (fotoLama.exists()) {
			fotoLama.delete();
		}
		return true;
	}
	
	public String upload(MultipartFile file, String lama, boolean hapus, String folder) {
		if(file != null) {
			if (!file.isEmpty()) {
				String namaBaru = save(file, folder);
				if (!namaBaru.equals("")) { // Jika upload berhasil
					System.out.println("Lama : " + lama);
					if (!lama.isEmpty()) { // Jika update
						hapusLama(lama, folder);
					}
					return namaBaru;
				}else { // Jika upload gagal
					return lama;
				}
			}else { // Jika file nya tidak ada
				System.out.println("File null");
				System.out.println("Lama : " + lama + " => " + !lama.isEmpty());
				if (!lama.isEmpty()) { // Jika foto lama nya ada
					if (hapus) { // Jika ingin hapus
						System.out.println("Dihapus!!!");
						hapusLama(lama, folder);
						return "";
					}else { // Jika tidak ingin hapus
						System.out.println("Tetap " + lama);
						return lama;
					}
				}
			}
		}
		return lama;
	}
	
}
