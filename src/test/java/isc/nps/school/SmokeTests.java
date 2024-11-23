package isc.nps.school;

import isc.nps.school.dto.ClassDto;
import isc.nps.school.dto.StudentDto;
import isc.nps.school.security.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SmokeTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private HttpHeaders createHeadersWithJwt() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + jwtTokenUtil.generateToken("admin"));
		return headers;
	}

	private ResponseEntity<Void> createClass(ClassDto classDto, HttpHeaders headers) {
		HttpEntity<ClassDto> request = new HttpEntity<>(classDto, headers);
		return restTemplate.postForEntity(getApplicationPath() + "/classes", request, Void.class);
	}

	private ResponseEntity<ClassDto> getClass(long classNumber, HttpHeaders headers) {
		return restTemplate.exchange(
				getApplicationPath() + "/classes/" + classNumber,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				ClassDto.class
		);
	}

	private ResponseEntity<Void> updateClass(ClassDto classDto, HttpHeaders headers) {
		HttpEntity<ClassDto> request = new HttpEntity<>(classDto, headers);
		return restTemplate.exchange(
				getApplicationPath() + "/classes",
				HttpMethod.PUT,
				request,
				Void.class
		);
	}

	private ResponseEntity<Void> deleteClass(long classNumber, HttpHeaders headers) {
		return restTemplate.exchange(
				getApplicationPath() + "/classes/" + classNumber,
				HttpMethod.DELETE,
				new HttpEntity<>(headers),
				Void.class
		);
	}

	private String getApplicationPath() {
		return "http://localhost:" + port;
	}

	@Test
	void removeClass_withValidCourseNumber_shouldUpdateRemoveCourseAndStudents() {
		HttpHeaders headers = createHeadersWithJwt();

		// ایجاد داده‌های تست
		List<StudentDto> studentDtos = List.of(
				new StudentDto(456521L, "Milad", "Asadi", "Italian"),
				new StudentDto(14012010L, "Ahmad", "Ramezan", "Iraqi")
		);

		ClassDto classDto = new ClassDto(722L, (short) 1403, (short) 5, "JPA class", studentDtos);

		//create
		ResponseEntity<Void> postResponse = createClass(classDto, headers);
		assertEquals(HttpStatus.CREATED, postResponse.getStatusCode(), "Class creation failed!");

		//get
		ResponseEntity<ClassDto> getResponse = getClass(classDto.getNumber(), headers);
		assertEquals(HttpStatus.OK, getResponse.getStatusCode(), "Class retrieval failed!");
		assertNotNull(getResponse.getBody(), "Class data is null!");
		assertEquals(classDto.getName(), getResponse.getBody().getName(), "Class names do not match!");
		assertEquals(studentDtos.size(), getResponse.getBody().getStudents().size(), "Student count mismatch!");

		// update
		classDto.setCourseYear((short) 1405);
		ResponseEntity<Void> putResponse = updateClass(classDto, headers);
		assertEquals(HttpStatus.OK, putResponse.getStatusCode(), "Class update failed!");

		ResponseEntity<ClassDto> updatedGetResponse = getClass(classDto.getNumber(), headers);
		assertEquals(HttpStatus.OK, updatedGetResponse.getStatusCode(), "Class retrieval after update failed!");
		assertNotNull(updatedGetResponse.getBody(), "Updated class data is null!");
		assertEquals(classDto.getCourseYear(), updatedGetResponse.getBody().getCourseYear(), "Course year did not update!");

		// delete
		ResponseEntity<Void> deleteResponse = deleteClass(classDto.getNumber(), headers);
		assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode(), "Class deletion failed!");

		ResponseEntity<ClassDto> getAfterDeleteResponse = getClass(classDto.getNumber(), headers);
		assertNotEquals(HttpStatus.OK, getAfterDeleteResponse.getStatusCode(), "Deleted class is still retrievable!");
	}
}
