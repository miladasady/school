package isc.nps.school;

import isc.nps.school.constant.ErrorConstant;
import isc.nps.school.controller.ClassController;
import isc.nps.school.dto.ClassDto;
import isc.nps.school.dto.StudentDto;
import isc.nps.school.service.ClassService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServiceTests {

//	@Test
//	void contextLoads() {
//	}



    @Autowired
	private ClassService classService;
    @Autowired
    private ClassController classController;

	public ServiceTests()
	{
		super();
	}

	@Test
	public void createClass_withValidInput_shouldPass() throws Exception {
		//prepare data
		List<StudentDto> studentDtos=new ArrayList<>();
		studentDtos.add(new StudentDto(81301408L,"Milad","Asadi","Italian"));
		studentDtos.add(new StudentDto(14012010L,"Ahmad","Ramezan","Iraqi"));

		ClassDto classDto=new ClassDto(1L,(short)1403,(short) 5, "JPA class",studentDtos);;
		classDto.setNumber(1L);


		//do test
		assertDoesNotThrow(()->classService.createClass(classDto));


		ClassDto classDtoFromDb= classService.findClassByNumber(classDto.getNumber());
		assertThat(classDtoFromDb.getName().equals(classDto.getName()));

		assertEquals(studentDtos.size(), classDtoFromDb.getStudents().size());
	}

	@Test
	public void createClass_studentsExceedMaxCount_shouldThrowException() throws Exception {
		List<StudentDto> studentDtos=new ArrayList<>();
		studentDtos.add(new StudentDto(81301408L,"Milad","Asadi","Italian"));
		studentDtos.add(new StudentDto(14012010L,"Ahmad","Ramezan","Iraqi"));
		studentDtos.add(new StudentDto(14012010L,"Akbar","Ramezan","Iraqi"));

		ClassDto classDto=new ClassDto(1L,(short)1403,(short) 2, "JPA class",studentDtos);;
		classDto.setNumber(1L);


		Exception exp = assertThrows(Exception.class, ()->{
			classService.createClass(classDto);
		});
		assertThat(exp.getMessage().equals(ErrorConstant.STUDENTS_EMPTY_MESSAGE));

	}

	@Test
	void createClass_withEmptyStudentList_shouldThrowException() {
		// Arrange
		ClassDto classDto = new ClassDto();
		classDto.setNumber(2L);
		classDto.setStudents(new ArrayList<>());

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> classService.createClass(classDto));
		assertEquals(ErrorConstant.STUDENTS_EMPTY_MESSAGE, exception.getMessage());
	}
	@Test
	void findClassByNumber_withInvalidNumber_shouldThrowException() {
		// Act & Assert
		Exception exception = assertThrows(Exception.class,
				() -> classService.findClassByNumber(99L));
		assertEquals(ErrorConstant.NOT_FOUND_MESSAGE, exception.getMessage());
	}

	@Test
	void removeClass_withValidCourseNumber_shouldUpdtaeRemoveCourseAndStudents() throws Exception {
		List<StudentDto> studentDtos=new ArrayList<>();
		studentDtos.add(new StudentDto(456521,"Milad","Asadi","Italian"));
		studentDtos.add(new StudentDto(14012010L,"Ahmad","Ramezan","Iraqi"));

		ClassDto classDto=new ClassDto(412L,(short)1403,(short) 5, "JPA class",studentDtos);;

		//do test
		assertDoesNotThrow(()->classService.createClass(classDto));


		ClassDto classDtoFromDb= classService.findClassByNumber(classDto.getNumber());
		assertThat(classDtoFromDb.getName().equals(classDto.getName()));

		assertEquals(studentDtos.size(), classDtoFromDb.getStudents().size());

		// test update
		classDto.setCourseYear((short)1405);
		classService.changeClass(classDto);

		ClassDto dbClass=classService.findClassByNumber(classDto.getNumber());
		assertEquals(dbClass.getCourseYear(),classDto.getCourseYear());


		classService.removeClass(classDto.getNumber());

		Exception exception = assertThrows(Exception.class,
				() -> classService.findClassByNumber(classDto.getNumber()));
		assertEquals(ErrorConstant.NOT_FOUND_MESSAGE, exception.getMessage());
	}

}
