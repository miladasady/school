package isc.nps.school.dto;

import isc.nps.school.model.Student;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassDto {

    private long number;

    private short courseYear;

    private short maxStudentCount;

    private String name;

    private List<StudentDto> students;

}
