package com.akash.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.akash.model.StudentResponse;
import com.akash.model.StudentXml;

@Component
public class RestItemWriter implements ItemWriter<StudentResponse>{

	@Override
	public void write(List<? extends StudentResponse> items) throws Exception {
		System.out.println("Inside rest Item Writter");
		items.stream().forEach(System.out::println);
	}

}