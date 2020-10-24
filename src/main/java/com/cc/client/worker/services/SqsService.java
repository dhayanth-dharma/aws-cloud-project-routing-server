package com.cc.client.worker.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cc.client.worker.model.NumberListRequest;


@Service 
public class SqsService {
//	@Autowired
//	private PowerRepository powerRepo;
	public double calculateMedian(NumberListRequest request) {
		double total=0;
		double res=0;
		for(int num:request.input) {
			total+=num;
		}
		res=total/request.input.size();
		return res;
	}
	
}
