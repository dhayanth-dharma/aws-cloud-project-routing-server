package com.cc.client.worker.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.amazonaws.util.json.Jackson;
import com.cc.client.worker.model.LogginModel;
import com.cc.client.worker.websocket.config.Message_Handler_Singleton;
import com.cc.client.worker.websocket.config.ResponseMessageModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LogginService {
	
@Autowired
AmazonS3Client amazonS3Client;
String bucketName="custom-loggin";
String bucketNameBinary="custom-loggin-bin";
 public void addLoggin(LogginModel logModel) throws IOException {
	 ObjectMapper objectMapper = new ObjectMapper(); 
	 byte[] bytesToWrite = objectMapper.writeValueAsBytes(logModel);
	 ObjectMetadata omd = new ObjectMetadata();
	 omd.setContentLength(bytesToWrite.length);
	 Date date= new Date();
	 long time = date.getTime();
	 Timestamp ts = new Timestamp(time);
	 amazonS3Client.putObject(bucketNameBinary, "log_"+ts.toString(), new ByteArrayInputStream(bytesToWrite), omd);
 }
 
 public void getLogginList() throws IOException {
	 List<LogginModel> logList=getLoggins();
     String logText= Jackson.toJsonString(logList);
     ResponseMessageModel msg=new ResponseMessageModel();
	 msg.message=logText;
	 msg.page_id=2;
	 msg.func_id=204;
	 msg.message_type="logging";
	 Message_Handler_Singleton messagePip=Message_Handler_Singleton.getInstance();
	 messagePip.sendMsh(msg);
 }
 
 public void addLogginString(String content) throws JsonProcessingException {

	 Date date= new Date();
	 long time = date.getTime();
	 Timestamp ts = new Timestamp(time);
	 amazonS3Client.putObject(bucketName, "log_"+ts.toString()+".txt", content);
 }
 
 public List<String> getObjectslistFromFolder(String bucketName) {   
	  ListObjectsRequest listObjectsRequest = 
	                                new ListObjectsRequest()
	                                      .withBucketName(bucketName);
	  List<String> keys = new ArrayList<>();
	 
	  ObjectListing objects = amazonS3Client.listObjects(listObjectsRequest);
	  for (;;) {
	    List<S3ObjectSummary> summaries = objects.getObjectSummaries();
	    if (summaries.size() < 1) {
	      break;
	    }
	    summaries.forEach(s -> keys.add(s.getKey()));
	    objects = amazonS3Client.listNextBatchOfObjects(objects);
	  }
	  return keys;
	}
   
 public List<LogginModel> getLoggins() throws IOException {
	 List<LogginModel> loginModelList=new ArrayList<>();
	 List<String> names=getObjectslistFromFolder(bucketNameBinary);
	 
	 for (String key: names) {
    	 S3Object obj = amazonS3Client.getObject(bucketNameBinary, key);
    	 S3ObjectInputStream stream = obj.getObjectContent();
    	 byte[] content = IOUtils.toByteArray(stream);
         obj.close();
         ObjectMapper objectMapper = new ObjectMapper(); 
         LogginModel loginmodel = objectMapper.readValue(content, LogginModel.class);
         loginModelList.add(loginmodel);
 	}
	 return loginModelList;
 }
 
 public List<LogginModel> getLoggins_NotWorking() throws IOException {
	 List<LogginModel> loginModelList=new ArrayList<>();
	 ObjectListing objectListing = amazonS3Client.listObjects(new ListObjectsRequest().withBucketName(bucketName));
     for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
    	 S3Object obj = amazonS3Client.getObject(bucketNameBinary, objectSummary.getKey());
    	 S3ObjectInputStream stream = obj.getObjectContent();
    	 byte[] content = IOUtils.toByteArray(stream);
         obj.close();
         ObjectMapper objectMapper = new ObjectMapper(); 
         LogginModel loginmodel = objectMapper.readValue(content, LogginModel.class);
         loginModelList.add(loginmodel);
 	}
	 return loginModelList;
 }
}
