package com.revature.services;

import java.io.File;

/**
 * Service to store files in an S3 bucket.
 * 
 * @author Java, JUN 19 - USF
 *
 */
public interface S3FileStorageService {
	
	/**
	 * Store file in an S3 bucket and return the URL of this file. 
	 * @param file File to store
	 * @return S3 bucket's URL
	 */
	public String storeFile(File file);

}
