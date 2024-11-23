package isc.nps.school.repository;

import isc.nps.school.model.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course,Long> {

    Optional<Course> findByNumber(long number);

}
