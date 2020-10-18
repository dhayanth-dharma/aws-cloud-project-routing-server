package com.cc.services;

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

	    
	    S3BucketService s3bucketService;
	    @Value("${aws.s3.bucket.image_list.name}")
	    String defaultBucketName;

	    @Value("${aws.s3.bucket.image_list.default.folder}")
	    String defaultBaseFolder;

	    public List<Bucket> getAllBuckets() {
	        return amazonS3Client.listBuckets();
	    }


	    public void uploadFile(File uploadFile) {
	        amazonS3Client.putObject(defaultBucketName, uploadFile.getName(), uploadFile);
	    }

	    public void uploadFile(String name,byte[] content)  {
	        File file = new File("src/main/resources/img/"+name);
	        file.canWrite();
	        file.canRead();
	        FileOutputStream iofs = null;
	        try {
	            iofs = new FileOutputStream(file);
	            iofs.write(content);
	            amazonS3Client.putObject(defaultBucketName, defaultBaseFolder+"/"+file.getName(), file);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	    }

	    public byte[] getFile(String key) {
	        S3Object obj = amazonS3Client.getObject(defaultBucketName, defaultBaseFolder+"/"+key);
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
