package com.akash.services;

import org.springframework.stereotype.Service;

@Service
public class StudentService {

	/*
	 * private List<StudentResponse> list = null;
	 * 
	 * private List<StudentResponse> restcallToGetStudents() {
	 * 
	 * RestTemplate restTemplate = new RestTemplate(); StudentResponse[]
	 * studentResponseArray =
	 * restTemplate.getForObject("http://localhost:9091/api/v1/students",
	 * StudentResponse[].class); List<StudentResponse> list = new ArrayList<>(); for
	 * (StudentResponse sr : studentResponseArray) { list.add(sr); }
	 * 
	 * return list; }
	 * 
	 * public StudentResponse getStudent(long id, String name) {
	 * System.out.println("id=" + id + "and name=" + name); if (list == null) { list
	 * = restcallToGetStudents(); System.out.println(list); } if (list != null &&
	 * !list.isEmpty()) { return list.remove(0); } return null; }
	 * 
	 * public StudentCsv restCallToCreateStudent(StudentCsv student) { RestTemplate
	 * restTemplate = new RestTemplate(); return
	 * restTemplate.postForObject("http://localhost:9091/api/v1/createStudent",
	 * student, StudentCsv.class); }
	 */
}
