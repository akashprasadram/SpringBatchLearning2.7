package com.akash.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.akash.model.StudentJdbc;
import com.akash.model.StudentJson;
import com.akash.model.StudentXml;

@Component
public class JdbcToXmlItemProcessor implements ItemProcessor<StudentJdbc, StudentXml> {

	@Override
	public StudentXml process(StudentJdbc item) throws Exception {

		StudentXml studentXml = new StudentXml();
		studentXml.setId(item.getId());
		studentXml.setFirstName(item.getFirstName());
		studentXml.setLastName(item.getLastName());
		studentXml.setEmail(item.getEmail());

		return studentXml;

	}

}
