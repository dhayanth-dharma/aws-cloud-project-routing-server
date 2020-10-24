package com.cc.client.worker.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
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
	    //LOGGING
//	    @Value("${aws.s3.bucket.log.name}")
//	    String logginBucket;
//	    
//	    @Value("${ aws.s3.bucket.log.folder.name}")
//	    String logginBucketFolder;
	    
	    public List<Bucket> getAllBuckets() {
	        return amazonS3Client.listBuckets();
	    }


	    public void uploadFile(File uploadFile) {
	        amazonS3Client.putObject(defaultBucketName, uploadFile.getName(), uploadFile);
	    }

	    public void uploadFile(String name,byte[] content)  {
	        File file = new File("src/main/resources/img/original/"+name);
	        file.canWrite();
	        file.canRead();
	        FileOutputStream iofs = null;
	        try {
	            iofs = new FileOutputStream(file);
	            iofs.write(content);
	            amazonS3Client.putObject(defaultBucketName, originalImgFolder+"/"+file.getName(), file);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public byte[] getFile(String key) {
	        S3Object obj = amazonS3Client.getObject(defaultBucketName, "edited/"+key);
	        S3ObjectInputStream stream = obj.getObjectContent();
	        File file = new File("src/main/resources/img/edited/"+key);
	        file.canWrite();
	        file.canRead();
	        FileOutputStream iofs = null;
	        try {
	            byte[] content = IOUtils.toByteArray(stream);
	            obj.close();
	            iofs = new FileOutputStream(file);
	            iofs.write(content);
	            return content;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    public byte[] test(String key) {
	        S3Object obj = amazonS3Client.getObject(defaultBucketName, "original/q.jpg");
	        S3ObjectInputStream stream = obj.getObjectContent();
	        File file = new File("src/main/resources/img/edited/"+key);
	        file.canWrite();
	        file.canRead();
	        FileOutputStream iofs = null;
	        try {
	            byte[] content = IOUtils.toByteArray(stream);
	            obj.close();
	            iofs = new FileOutputStream(file);
	            iofs.write(content);
	            return content;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
}
