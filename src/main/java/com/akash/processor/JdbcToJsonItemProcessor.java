package com.akash.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.akash.model.StudentJdbc;
import com.akash.model.StudentJson;

@Component
public class JdbcToJsonItemProcessor implements ItemProcessor<StudentJdbc, StudentJson> {

	@Override
	public StudentJson process(StudentJdbc item) throws Exception {

		StudentJson studentJson = new StudentJson();
		studentJson.setId(item.getId());
		studentJson.setFirstName(item.getFirstName());
		studentJson.setLastName(item.getLastName());
		studentJson.setEmail(item.getEmail());

		return studentJson;

	}

}
