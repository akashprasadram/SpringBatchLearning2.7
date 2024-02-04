package com.akash.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.akash.model.StudentCsv;

@Component
public class FirstItemWriter implements ItemWriter<StudentCsv>{

	@Override
	public void write(List<? extends StudentCsv> items) throws Exception {
		System.out.println("Inside Item Writter");
		items.stream().forEach(System.out::println);
	}

}
