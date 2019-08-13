package com.revature.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import org.springframework.web.multipart.MultipartFile;

/**
 * File utilities.
 * 
 * @author Java, JUN 19 - USF
 *
 */
public class FileHelper {
	
	/**
	 * Get content from a text file.
	 * @param fileName File name in the URL.
	 * @return File content
	 */
	public static String getTextFileContent(String fileName) {
		Scanner sc = null;
		String fileContent = "";

		try {
			File file = new File(FileHelper.class.getClassLoader().getResource(fileName).getFile());
			sc = new Scanner(file);

			while (sc.hasNextLine()) {
				fileContent += sc.nextLine() + "\n";
			}

		} catch (Exception e) {
			// TODO: handle exception, Log the exception
		} finally {
			sc.close();
		}
		
		return fileContent;
	}
	
	/**
	 * Got this from Project service.
	 * @param multipartFile Sql script
	 * @return Java File object
	 * @throws IOException
	 */
	public static File convert(MultipartFile multipartFile) throws IOException {
		File convFile = new File(multipartFile.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(multipartFile.getBytes());
		fos.close();
		return convFile;
	}

}
