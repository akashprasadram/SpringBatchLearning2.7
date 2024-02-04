package com.akash.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.akash.model.StudentJdbc;
import com.akash.model.StudentXml;

@Component
public class JdbcItemWriter implements ItemWriter<StudentJdbc>{

	@Override
	public void write(List<? extends StudentJdbc> items) throws Exception {
		System.out.println("Inside Jdbc Item Writter");
		items.stream().forEach(System.out::println);
	}

}