package com.akash.listener;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import com.akash.model.StudentCsv;
import com.akash.model.StudentJson;

@Component
public class SkipListener {

	@OnSkipInRead
	public void skipInRead(Throwable th) {

		if (th instanceof FlatFileParseException) {
			String filePath = "D:\\Spring\\SpringBatch Udemy\\CourceMine\\spring-batch-demo\\Chunk Job\\tenthChunkStep\\reader\\SkipInRead.txt";
			createFile(filePath, ((FlatFileParseException) th).getInput());
		}

	}

	@OnSkipInProcess
	public void skipInProgress(StudentCsv studentCsv,Throwable th) {
		if (th instanceof NullPointerException) {
			String filePath = "D:\\Spring\\SpringBatch Udemy\\CourceMine\\spring-batch-demo\\Chunk Job\\tenthChunkStep\\processor\\SkipInProcess.txt";
			createFile(filePath, studentCsv.toString());
		}
	}
	
	@OnSkipInWrite
	public void skipInWriter(StudentJson studentJson,Throwable th) {
		if (th instanceof NullPointerException) {
			String filePath = "D:\\Spring\\SpringBatch Udemy\\CourceMine\\spring-batch-demo\\Chunk Job\\tenthChunkStep\\writer\\SkipInWriter.txt";
			createFile(filePath, studentJson.toString());
		}
	}
	
	public void createFile(String filePath, String data) {
		try (FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
			fileWriter.write(data+" ,"+new Date()+"\n");
		} catch (Exception e) {
			System.out.println("Exception occured while creating file :" + e);
		}
	}

}
