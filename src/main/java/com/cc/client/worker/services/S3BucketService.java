package com.cc.client.worker.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@Service
public class S3BucketService {
	    @Autowired
	    AmazonS3Client amazonS3Client;
	    @Value("${aws.s3.bucket.image_list.name}")
	    String defaultBucketName;
	    @Value("${aws.s3.bucket.image_list.original.folder}")
	    String originalImgFolder;
	    @Value("${aws.s3.bucket.image_list.edited.folder}")
	    String editedImgFolder;
	    public List<Bucket> getAllBuckets() {
	        return amazonS3Client.listBuckets();
	    }
	    public void uploadFile(File uploadFile) {
	        amazonS3Client.putObject(defaultBucketName, uploadFile.getName(), uploadFile);
	    }
	    public void uploadFile(String name,byte[] content)  {
	        	InputStream is = new ByteArrayInputStream(content);
	            ObjectMetadata metadata = new ObjectMetadata();
	            metadata.setContentLength(content.length);
	            metadata.setContentType("image/jpg");
	            metadata.setCacheControl("public, max-age=31536000");
	            amazonS3Client.putObject(defaultBucketName, originalImgFolder+"/"+name, is,metadata);
	    }
	    public byte[] getFile(String key) {
	        S3Object obj = amazonS3Client.getObject(defaultBucketName, "edited/"+key);
	        S3ObjectInputStream stream = obj.getObjectContent();
	        try {
	            byte[] content = IOUtils.toByteArray(stream);
	            obj.close();
	            return content;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
}
