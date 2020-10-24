package com.cc.client.worker.services;

import java.io.File; 
import java.io.IOException;
import java.net.URL;
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;

import org.springframework.stereotype.Service; 
@Service
  public class ImageProcessing{ 
	public int start(String key)throws IOException {
	try {
		BufferedImage img = null; 
		File f = null; 
		f = new File("src/main/resources/img/original/"+key); 
//		
//		String fileName="img/";
//		ClassLoader classLoader = getClass().getClassLoader();
//        URL resource = classLoader.getResource(fileName+"original/"+key);
//        if (resource == null) {
//            throw new IllegalArgumentException("file not found! " + fileName);
//        }
        //File file = new File(resource.getFile());
//        File file = new File(resource.toURI());
//	
//		  InputStream is = getClass().getClassLoader().getResourceAsStream("file.txt");
//		  File f=new File(is.);
		
		img = ImageIO.read(f);
//        img = ImageIO.read(file);
		int width = img.getWidth(); 
		int height = img.getHeight(); 
		for (int y = 0; y < height; y++){ 
			for (int x = 0; x < width; x++) { 
				int p = img.getRGB(x,y); 
				int a = (p>>24)&0xff; 
				int r = (p>>16)&0xff; 
				p = (a<<24) | (r<<16) | (0<<8) | 0; 
				img.setRGB(x, y, p); 
			} 
		} 
//		URL resource2 = classLoader.getResource(fileName+"edited/"+key);
		f=new File("src/main/resources/img/edited/"+key);
		ImageIO.write(img, "jpg", f); 
		return 1;
	} catch (Exception e) {
		e.printStackTrace();
		return 0;
	}
	} 
} 
