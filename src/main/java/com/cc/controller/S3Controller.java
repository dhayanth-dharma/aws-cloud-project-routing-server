package com.cc.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.Bucket;
import com.cc.services.S3BucketService;
@RestController
@RequestMapping(value = "/api/s3")

@CrossOrigin
public class S3Controller {
	
	@Autowired
	S3BucketService s3bucketService;
    @Value("${aws.s3.bucket.image_list.name}")
    String defaultBucketName;

    @Value("${aws.s3.bucket.image_list.default.folder}")
    String defaultBaseFolder;
    
    
    @CrossOrigin
	@RequestMapping(method=RequestMethod.GET, path= "/buckets" )
   public List<Bucket> listBuckets(){
        return s3bucketService.getAllBuckets();
    }

    @RequestMapping(method=RequestMethod.POST, path = "/upload",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String,String> uploadFile(@RequestPart(value = "file", required = false) MultipartFile files) throws IOException {
        s3bucketService.uploadFile(files.getOriginalFilename(),files.getBytes());
        Map<String,String> result = new HashMap<>();
        result.put("key",files.getOriginalFilename());
        return result;
    }

    @GetMapping(path = "/download")
    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "file") String file) throws IOException {
        byte[] data = s3bucketService.getFile(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                .body(resource);

    }
}
