package isc.nps.school.mapper;

import isc.nps.school.dto.StudentDto;
import isc.nps.school.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);
    Student dtoToEntity(StudentDto dto);
    StudentDto entityToDto(Student entity);

    List<Student> dtoToEntityList(List<StudentDto> studentDtos);
    List<StudentDto> entityToDtoList(List<Student> students);

}
