package com.akash.config;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.akash.listener.SkipListener;
import com.akash.listener.SkipListenerImpl;
import com.akash.model.StudentCsv;
import com.akash.model.StudentJdbc;
import com.akash.model.StudentJson;
import com.akash.model.StudentResponse;
import com.akash.model.StudentXml;
import com.akash.processor.CsvToJsonItemProcessor;
import com.akash.processor.JdbcToJsonItemProcessor;
import com.akash.processor.JdbcToXmlItemProcessor;
import com.akash.services.StudentService;
import com.akash.writer.FirstItemWriter;
import com.akash.writer.JdbcItemWriter;
import com.akash.writer.JsonItemWriter;
import com.akash.writer.RestItemWriter;
import com.akash.writer.XmlItemWriter;

@Configuration
public class SimpleJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	// @Autowired
	private FirstItemWriter firstItemWriter;

	// @Autowired
	private JsonItemWriter jsonItemWriter;

	// @Autowired
	private XmlItemWriter xmlItemWriter;



	@Autowired
	private StudentService studentService;

	// @Autowired
	private RestItemWriter restItemWriter;

	@Autowired
	private JdbcToJsonItemProcessor jdbcToJsonItemProcessor;

	@Autowired
	private JdbcToXmlItemProcessor jdbcToXmlItemProcessor;

	@Autowired
	private CsvToJsonItemProcessor csvToJsonItemProcessor;

	@Autowired
	private SkipListener skipListener;

	@Autowired
	private SkipListenerImpl skipListenerImpl;

	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	DataSource datasource() {
		return DataSourceBuilder.create().build();
	}
	

	@Bean
	@ConfigurationProperties(prefix = "spring.universitydatasource")
	DataSource universityDatasource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public Job chunkJob() {
		return jobBuilderFactory.get("Chunk Job").incrementer(new RunIdIncrementer()).flow(tenthChunkStep()).end()
				.build();
	}

	private Step firstChunkStep() {
		return stepBuilderFactory.get("First chunk Step").<StudentCsv, StudentCsv>chunk(4)
				.reader(flatFileItemReader(null)).writer(firstItemWriter).listener(skipListener).build();

	}

	private Step secondChunkStep() {
		return stepBuilderFactory.get("second chunk Step").<StudentJson, StudentJson>chunk(4)
				.reader(jsonItemReader(null)).writer(jsonItemWriter).build();

	}

	private Step thirdChunkStep() {
		return stepBuilderFactory.get("third chunk Step").<StudentXml, StudentXml>chunk(4)
				.reader(StaxEventItemReader(null)).writer(xmlItemWriter).build();

	}

	private Step fourthChunkStep() {
		return stepBuilderFactory.get("fourth chunk Step").<StudentJdbc, StudentJdbc>chunk(4)
				.reader(jdbcCursorItemReader()).writer(flatFileItemWriter(null)).build();

	}

	private Step fivethChunkStep() {
		return stepBuilderFactory.get("fiveth chunk Step").<StudentResponse, StudentResponse>chunk(4)
				.reader(itemReaderAdapter()).writer(restItemWriter).build();

	}

	private Step sixthChunkStep() {
		return stepBuilderFactory.get("sixth chunk Step").<StudentJdbc, StudentJson>chunk(4)
				.reader(jdbcCursorItemReader()).processor(jdbcToJsonItemProcessor).writer(jsonFileItemWriter(null))
				.build();

	}

	private Step seventhChunkStep() {
		return stepBuilderFactory.get("seveth chunk Step").<StudentJdbc, StudentXml>chunk(4)
				.reader(jdbcCursorItemReader()).processor(jdbcToXmlItemProcessor).writer(staxEventItemWriter(null))
				.build();

	}

	private Step eightChunkStep() {
		return stepBuilderFactory.get("eight chunk Step").<StudentCsv, StudentCsv>chunk(4)
				.reader(flatFileItemReader(null)).writer(jdbcBatchItemWriter1()).build();

	}

	private Step ninethChunkStep() {
		return stepBuilderFactory.get("ninet chunk Step").<StudentCsv, StudentCsv>chunk(4)
				.reader(flatFileItemReader(null)).writer(itemWriterAdapter()).build();

	}

	private Step tenthChunkStep() {
		return stepBuilderFactory.get("tenth chunk Step").<StudentCsv, StudentJson>chunk(4)
				.reader(flatFileItemReader1())

				.processor(csvToJsonItemProcessor).writer(jsonFileItemWriter1()).faultTolerant().skip(Throwable.class)
				// .listener(skipListener)

				// .skipLimit(Integer.MAX_VALUE)
				// .skipPolicy(new AlwaysSkipItemSkipPolicy())
				.skipLimit(100)
				// .retryLimit(3)
				// .retry(Throwable.class)
				.listener(skipListenerImpl).build();

	}

	@Bean
	FlatFileItemReader<StudentCsv> flatFileItemReader1() {
		FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new FileSystemResource(
				new File("D:\\Spring\\SpringBatch Udemy\\CourceMine\\spring-batch-demo\\inputFiles\\students.csv")));

		DefaultLineMapper<StudentCsv> defaultLineMapper = new DefaultLineMapper<StudentCsv>();
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames("ID", "First Name", "Last Name", "Email");
		// delimitedLineTokenizer.setDelimiter("|");
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

		BeanWrapperFieldSetMapper<StudentCsv> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
		beanWrapperFieldSetMapper.setTargetType(StudentCsv.class);

		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

		flatFileItemReader.setLineMapper(defaultLineMapper);
		flatFileItemReader.setLinesToSkip(1);

		return flatFileItemReader;

	}

	@Bean
	JsonFileItemWriter<StudentJson> jsonFileItemWriter1() {
		JsonFileItemWriter<StudentJson> jsonFileItemWriter = new JsonFileItemWriter<>(
				new FileSystemResource(new File(
						"D:\\Spring\\SpringBatch Udemy\\CourceMine\\spring-batch-demo\\outputFiles\\students.json")),
				new JacksonJsonObjectMarshaller<StudentJson>()) {
			@Override
			public String doWrite(List<? extends StudentJson> items) {
				items.stream().forEach(item -> {
					if (item.getId() == 5) {
						System.out.println("Inside JsonFileItemWriter");
						throw new NullPointerException();
					}
				});

				return super.doWrite(items);
			}
		};

		return jsonFileItemWriter;
	}

	@Bean
	@StepScope
	FlatFileItemReader<StudentCsv> flatFileItemReader(

			@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource) {
		FlatFileItemReader<StudentCsv> flatFileItemReader = new FlatFileItemReader<>();
		// flatFileItemReader.setResource(new FileSystemResource(new
		// File("D:\\Spring\\SpringBatchUdemy\\CourceMine\\spring-batch-demo\\inputFiles\\students.csv")));

		// Here we are featching resource from Job parameter which passed as parameter
		// during runtime
		flatFileItemReader.setResource(fileSystemResource);
		DefaultLineMapper<StudentCsv> defaultLineMapper = new DefaultLineMapper<StudentCsv>();
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames("ID", "First Name", "Last Name", "Email"); //
		delimitedLineTokenizer.setDelimiter("|");
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

		BeanWrapperFieldSetMapper<StudentCsv> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
		beanWrapperFieldSetMapper.setTargetType(StudentCsv.class);

		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

		flatFileItemReader.setLineMapper(defaultLineMapper);
		flatFileItemReader.setLinesToSkip(1);

		return flatFileItemReader;

	}

	// @StepScope
	// @Bean
	JsonItemReader<StudentJson> jsonItemReader(
			@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource) {

		JsonItemReader<StudentJson> jsonItemReader = new JsonItemReader<>();
		jsonItemReader.setResource(fileSystemResource);
		jsonItemReader.setJsonObjectReader(new JacksonJsonObjectReader<>(StudentJson.class));
		//jsonItemReader.setMaxItemCount(8);
		//jsonItemReader.setCurrentItemCount(2);
		return jsonItemReader;
	}

	// @StepScope
	// @Bean
	StaxEventItemReader<StudentXml> StaxEventItemReader(
			@Value("#{jobParameters['inputFile']}") FileSystemResource fileSystemResource) {

		StaxEventItemReader<StudentXml> staxEventItemReader = new StaxEventItemReader<>();
		staxEventItemReader.setResource(fileSystemResource);
		staxEventItemReader.setFragmentRootElementName("student");
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(StudentXml.class);
		staxEventItemReader.setUnmarshaller(jaxb2Marshaller);
		return staxEventItemReader;
	}

	JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader() {
		JdbcCursorItemReader<StudentJdbc> jdbcCursorItemReader = new JdbcCursorItemReader<StudentJdbc>();
		jdbcCursorItemReader.setDataSource(universityDatasource());
		jdbcCursorItemReader.setSql("SELECT id, first_name as firstName,last_name as lastName, email FROM student");
		BeanPropertyRowMapper<StudentJdbc> beanPropertyRowMapper = new BeanPropertyRowMapper<StudentJdbc>();
		beanPropertyRowMapper.setMappedClass(StudentJdbc.class);
		jdbcCursorItemReader.setRowMapper(beanPropertyRowMapper);
		jdbcCursorItemReader.setCurrentItemCount(2);
		jdbcCursorItemReader.setMaxItemCount(8);

		return jdbcCursorItemReader;
	}

	public ItemReaderAdapter<StudentResponse> itemReaderAdapter() {
		ItemReaderAdapter<StudentResponse> itemReaderAdapter = new ItemReaderAdapter<StudentResponse>();
		itemReaderAdapter.setTargetObject(studentService);
		itemReaderAdapter.setTargetMethod("getStudent");
		itemReaderAdapter.setArguments(new Object[] { 1L, "test" });

		return itemReaderAdapter;
	}

	// @Bean
	// @StepScope
	FlatFileItemWriter<StudentJdbc> flatFileItemWriter(
			@Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource) {

		FlatFileItemWriter<StudentJdbc> flatFileItemWriter = new FlatFileItemWriter<StudentJdbc>();

		flatFileItemWriter.setHeaderCallback(writer -> writer.write("Id,First Name,Last Name,email"));
		flatFileItemWriter.setFooterCallback(writer -> writer.write("Created @ " + new Date()));

		BeanWrapperFieldExtractor<StudentJdbc> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<StudentJdbc>();
		beanWrapperFieldExtractor.setNames(new String[] { "id", "firstName", "lastName", "email" });

		DelimitedLineAggregator<StudentJdbc> delimitedLineAggregator = new DelimitedLineAggregator<StudentJdbc>();
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

		flatFileItemWriter.setLineAggregator(delimitedLineAggregator);

		return flatFileItemWriter;

	}

	// @Bean
	// @StepScope
	JsonFileItemWriter<StudentJson> jsonFileItemWriter(
			@Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource) {
		JsonFileItemWriter<StudentJson> jsonFileItemWriter = new JsonFileItemWriter<>(fileSystemResource,
				new JacksonJsonObjectMarshaller<StudentJson>());

		return jsonFileItemWriter;
	}

	// @Bean
	// @StepScope
	StaxEventItemWriter<StudentXml> staxEventItemWriter(
			@Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource) {

		StaxEventItemWriter<StudentXml> staxEventItemWriter = new StaxEventItemWriter<>();
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(StudentXml.class);

		staxEventItemWriter.setResource(fileSystemResource);
		staxEventItemWriter.setRootTagName("students");
		staxEventItemWriter.setMarshaller(marshaller);

		return staxEventItemWriter;

	}

	@Bean
	JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter() {
		JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setDataSource(universityDatasource());
		jdbcBatchItemWriter
				.setSql("INSERT INTO student(id,first_name,last_name,email) values (:id,:firstName,:lastName,:email)");
		jdbcBatchItemWriter
				.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<StudentCsv>());

		return jdbcBatchItemWriter;

	}

	@Bean
	JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter1() {
		JdbcBatchItemWriter<StudentCsv> jdbcBatchItemWriter = new JdbcBatchItemWriter<StudentCsv>();
		jdbcBatchItemWriter.setDataSource(universityDatasource());
		jdbcBatchItemWriter.setSql("INSERT INTO student(id,first_name,last_name,email) values (?,?,?,?)");
		jdbcBatchItemWriter.setItemPreparedStatementSetter(new ItemPreparedStatementSetter<StudentCsv>() {

			@Override
			public void setValues(StudentCsv item, PreparedStatement ps) throws SQLException {
				ps.setLong(1, item.getId());
				ps.setString(2, item.getFirstName());
				ps.setString(3, item.getLastName());
				ps.setString(4, item.getEmail());

			}
		});

		return jdbcBatchItemWriter;
	}

	// @Bean
	ItemWriterAdapter<StudentCsv> itemWriterAdapter() {

		ItemWriterAdapter<StudentCsv> itemWriterAdapter = new ItemWriterAdapter<>();
		itemWriterAdapter.setTargetObject(studentService);
		itemWriterAdapter.setTargetMethod("restCallToCreateStudent");

		return itemWriterAdapter;
	}

}
