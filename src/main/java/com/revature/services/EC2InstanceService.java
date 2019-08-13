package com.revature.services;

import java.io.UnsupportedEncodingException;

/**
 * Service to spin up, and other operations in EC2 instances in which
 * the projects are going to be deployed.
 * 
 * @author Java, JUN 19 - USF
 *
 */
public interface EC2InstanceService {

	/**
	 * Create a new instance of an EC2 and running the bashScript that will
	 * deploy the projects using Docker.
	 *  
	 * @param bashScript Bash script that is going to be run.
	 * @return Instance-id of the newly EC2 instance (Need to be stored by the Project service). 
	 * @throws UnsupportedEncodingException 
	 */
	public String spinUpEC2Instance(String bashScript) throws UnsupportedEncodingException;
	
	/**
	 * Get EC2 instance DNS (url) in which the project will be running.
	 * Because creating the instance takes a long time, we need to called this in order
	 * to be able to obtain the EC2 public DNS.
	 * @param instaceId Instance id of the EC2
	 * @return Public DNS of the EC2 (Should be stored by the Project service). 
	 */
	public String getEC2InstancePublicDNS(String instaceId);
}
