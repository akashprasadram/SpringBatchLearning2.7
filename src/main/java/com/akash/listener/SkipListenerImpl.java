package com.akash.listener;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import com.akash.model.StudentCsv;
import com.akash.model.StudentJson;

@Component
public class SkipListenerImpl implements SkipListener<StudentCsv, StudentJson> {

	@Override
	public void onSkipInRead(Throwable th) {
		System.out.println("On skip Lisenter on read : "+th.getMessage());
		if (th instanceof FlatFileParseException) {
			String filePath = "D:\\Spring\\SpringBatch Udemy\\CourceMine\\spring-batch-demo\\Chunk Job\\tenthChunkStep\\reader\\SkipInRead.txt";
			createFile(filePath, ((FlatFileParseException) th).getInput());
		}
	}

	@Override
	public void onSkipInWrite(StudentJson item, Throwable th) {
		if (th instanceof NullPointerException) {
			String filePath = "D:\\Spring\\SpringBatch Udemy\\CourceMine\\spring-batch-demo\\Chunk Job\\tenthChunkStep\\writer\\SkipInWriter.txt";
			createFile(filePath, item.toString());
		}
	}

	@Override
	public void onSkipInProcess(StudentCsv item, Throwable th) {
		if (th instanceof NullPointerException) {
			
			String filePath = "D:\\Spring\\SpringBatch Udemy\\CourceMine\\spring-batch-demo\\Chunk Job\\tenthChunkStep\\processor\\SkipInProcess.txt";
			createFile(filePath, item.toString());
		}
	}

	public void createFile(String filePath, String data) {
		try (FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
			fileWriter.write(data + " ," + new Date() + "\n");
		} catch (Exception e) {
			System.out.println("Exception occured while creating file :" + e);
		}
	}

}
