package com.revature.utils;

import java.io.File;
import java.util.Scanner;

public class FileHelper {
	
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

}
