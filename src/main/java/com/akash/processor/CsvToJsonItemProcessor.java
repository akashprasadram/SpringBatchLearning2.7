package com.akash.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.akash.model.StudentCsv;
import com.akash.model.StudentJdbc;
import com.akash.model.StudentJson;
import com.akash.model.StudentJson;

@Component
public class CsvToJsonItemProcessor implements ItemProcessor<StudentCsv, StudentJson> {

	@Override
	public StudentJson process(StudentCsv item) throws Exception {

		if(item.getId()==1) {
			System.out.println("Inside Item Processor");
			throw new NullPointerException();
		}
		StudentJson studentJson = new StudentJson();
		studentJson.setId(item.getId());
		studentJson.setFirstName(item.getFirstName());
		studentJson.setLastName(item.getLastName());
		studentJson.setEmail(item.getEmail());

		return studentJson;

	}

}
