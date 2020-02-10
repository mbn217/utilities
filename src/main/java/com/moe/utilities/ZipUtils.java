package com.moe.utilities;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;


public class ZipUtils {
	private  static Logger log = Logger.getLogger(ZipUtils.class);


	public void getAllFiles(File dir, List<File> fileList) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				fileList.add(file);
				if (file.isDirectory()) {
					log.info("directory:" + file.getCanonicalPath());
					getAllFiles(file, fileList);
				} else {
					log.info("     file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			log.info(e);
		}
	}

	public String writeZipFile(File directoryToZip, List<File> fileList) {
		String zipFileName =
				directoryToZip.getName() + "_" + UUID.randomUUID() + ".zip";
		try {
			FileOutputStream fos = new FileOutputStream(
					zipFileName);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file : fileList) {
				if (!file.isDirectory()) { // we only zip files, not directories
					addToZip(directoryToZip, file, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			log.info(e);
		} catch (IOException e) {
			log.info(e);
		}
		return zipFileName;
	}

	private void addToZip(File directoryToZip, File file, ZipOutputStream zos)
			throws FileNotFoundException, IOException {

		FileInputStream fis = new FileInputStream(file);
		String zipFilePath = file.getCanonicalPath().substring(
				directoryToZip.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		log.info("Adding '" + zipFilePath + "' to zip file");
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
}