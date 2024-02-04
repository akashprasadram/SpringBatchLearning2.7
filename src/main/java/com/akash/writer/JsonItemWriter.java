package com.akash.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.akash.model.StudentJson;

@Component
public class JsonItemWriter implements ItemWriter<StudentJson>{

	@Override
	public void write(List<? extends StudentJson> items) throws Exception {
		System.out.println("Inside Json Item Writter");
		items.stream().forEach(System.out::println);
	}

}