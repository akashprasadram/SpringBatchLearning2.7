package com.akash.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.akash.model.StudentXml;

@Component
public class XmlItemWriter implements ItemWriter<StudentXml>{

	@Override
	public void write(List<? extends StudentXml> items) throws Exception {
		System.out.println("Inside Xml Item Writter");
		items.stream().forEach(System.out::println);
	}

}