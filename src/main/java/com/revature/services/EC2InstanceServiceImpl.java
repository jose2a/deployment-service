package com.revature.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import software.amazon.awssdk.services.ec2.model.CreateKeyPairRequest;
import software.amazon.awssdk.services.ec2.model.CreateKeyPairResponse;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupRequest;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceType;
import software.amazon.awssdk.services.ec2.model.IpPermission;
import software.amazon.awssdk.services.ec2.model.IpRange;
import software.amazon.awssdk.services.ec2.model.Reservation;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Tag;

@Component
public class EC2InstanceServiceImpl implements EC2InstanceService {
	
	private String ec2TagName = "Revature project 2"; // This need to change (property file maybe)
	private String amiId = "ami-02f706d959cedf892"; // This need to change (property file maybe)
	private String securityGroupName = "revature"; // This need to change (property file maybe)
	private String keyName = "revatureRPM"; // This need to change (property file maybe)
	private String vpcId = "vpc-0913f361"; // This need to change (property file maybe)
	
	private Ec2Client ec2Client; // EC2 client

	@Autowired
	public void setEc2Client(Ec2Client ec2Client) {
		this.ec2Client = ec2Client;
	}
	
	public void setEc2TagName(String ec2TagName) {
		this.ec2TagName = ec2TagName;
	}

	public void setAmiId(String amiId) {
		this.amiId = amiId;
	}

	public void setSecurityGroupName(String securityGroupName) {
		this.securityGroupName = securityGroupName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}


	@Override
	public String spinUpEC2Instance(String bashScript) throws UnsupportedEncodingException {
		
		// Create security group for this EC2
		createSecurityGroupRequest();
		
		// Create access rules for accessing the EC2
		createEC2AccessRules();
		        
		// Create key pair request and store .PEM file in case we need to access the EC2 using ssh
		createKeyPairRequest();
				
		// Create instance in ec2
		RunInstancesRequest runInstanceRequest = RunInstancesRequest.builder()
				.imageId(amiId)
				.instanceType(InstanceType.T2_MICRO)
				.maxCount(1)
				.minCount(1)
				.keyName(keyName)
				.securityGroups(securityGroupName)
				.userData(Base64.getEncoder().encodeToString(bashScript.getBytes("UTF-8"))).build();

		// Run instance
		RunInstancesResponse runInstanceResponse = ec2Client.runInstances(runInstanceRequest);

		// Getting the newly instance id
		String instanceId = runInstanceResponse.instances().get(0).instanceId();

		// Add tag to the instance
		addTagToInstance(instanceId);

		return instanceId;
	}

	private void addTagToInstance(String instanceId) {
		Tag tag = Tag.builder().key("Name").value(ec2TagName).build();

		CreateTagsRequest tag_request = CreateTagsRequest.builder()
				.resources(instanceId)
				.tags(tag)
				.build();

		try {
			ec2Client.createTags(tag_request);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void createKeyPairRequest() {
		CreateKeyPairRequest createKeyPairRequest =  CreateKeyPairRequest.builder()
				.keyName(keyName).build();
		
		try {
			
			CreateKeyPairResponse createKeyPairResponse = ec2Client .createKeyPair(createKeyPairRequest);

			try (PrintStream out = new PrintStream(new FileOutputStream(keyName + ".pem"))) {
				// Saving the .pem file in case we need to login the system using ssh 
				out.print(createKeyPairResponse.keyMaterial());
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				System.out.println("Exception when saving the file.");
			}
		} catch (Exception e) {
			System.out.println("Pair response already exist.");
		}
	}

	private void createEC2AccessRules() {
		// Creating rules to access the ec2 instance from outside
		IpRange ip_range = IpRange.builder()
		    .cidrIp("0.0.0.0/0").build();

		IpPermission ip_perm = IpPermission.builder()
		    .ipProtocol("tcp")
		    .toPort(80)
		    .fromPort(80)
		    .ipRanges(ip_range)
		    .build();

		IpPermission ip_perm2 = IpPermission.builder()
		    .ipProtocol("tcp")
		    .toPort(22)
		    .fromPort(22)
		    .ipRanges(ip_range)
		    .build();
		
		IpPermission ip_perm3 = IpPermission.builder()
		        .ipProtocol("tcp")
		        .toPort(5432)
		        .fromPort(5432)
		        .ipRanges(ip_range)
		        .build();

		AuthorizeSecurityGroupIngressRequest authSecGroupInRequest = AuthorizeSecurityGroupIngressRequest.builder()
		        .groupName(securityGroupName)
		        .ipPermissions(ip_perm, ip_perm2, ip_perm3)
		        .build();

		try {
			// Creating rule for the ec2 instance
			ec2Client.authorizeSecurityGroupIngress(authSecGroupInRequest);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Rule already exist.");
		}
	}

	private void createSecurityGroupRequest() {
		CreateSecurityGroupRequest createSecurityGroupRequest = CreateSecurityGroupRequest.builder()
				.groupName(securityGroupName)
				.description("Revature tests")
				.vpcId(vpcId)
				.build();
		
		try {
			// Creating Security Group
			ec2Client.createSecurityGroup(createSecurityGroupRequest);
		} catch (Exception e) {
			System.out.println("Security group already exist.");
		}
	}

	@Override
	public String getEC2InstancePublicDNS(String instaceId) {
		
		String ec2PublicDns = "";
		
		String nextToken = null;
		
        do {
            DescribeInstancesRequest desRequest = DescribeInstancesRequest.builder()
            		.nextToken(nextToken).build();
            
            DescribeInstancesResponse desResponse = ec2Client.describeInstances(desRequest);

            for (Reservation reservation : desResponse.reservations()) {
                for (Instance instance : reservation.instances()) {
                	if (instance.instanceId().equals(instaceId)) {
						
                		ec2PublicDns = instance.publicDnsName();
					}
                }
            }
            
            nextToken = desResponse.nextToken();


        } while (nextToken != null);
        
        return ec2PublicDns;
	}

}
