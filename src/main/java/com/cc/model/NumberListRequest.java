package com.cc.model;

import java.util.List;

import lombok.Data;

@Data
public class NumberListRequest {

	public int id;
	public List<Integer> input;
	public double output;
}
