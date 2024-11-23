package isc.nps.school.mapper;

import isc.nps.school.dto.ClassDto;
import isc.nps.school.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);
    Course dtoToEntity(ClassDto dto);
    ClassDto entityToDto(Course entity);
    void updateFromDto(ClassDto dto, @MappingTarget Course entity);
}
