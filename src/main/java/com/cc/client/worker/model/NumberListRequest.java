package com.cc.client.worker.model;

import java.util.List;

import lombok.Data;

@Data
public class NumberListRequest {
	public int id;
	public List<Integer> input;
	public String output;
}
