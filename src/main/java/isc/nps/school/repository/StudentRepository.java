package isc.nps.school.repository;

import isc.nps.school.model.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student,Integer> {
    List<Student> findAllByCourseId(long courseId);

    void deleteAllByCourseId(long courseId);
}
