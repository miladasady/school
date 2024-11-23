package isc.nps.school.service;

import isc.nps.school.constant.ErrorConstant;
import isc.nps.school.dto.ClassDto;
import isc.nps.school.mapper.CourseMapper;
import isc.nps.school.mapper.StudentMapper;
import isc.nps.school.model.Course;
import isc.nps.school.model.Student;
import isc.nps.school.repository.CourseRepository;
import isc.nps.school.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ClassService {
    private static final Logger logger = getLogger(ClassService.class);

    private CourseRepository courseRepository;
    private StudentRepository studentRepository;


    public ClassService(CourseRepository courseRepository, StudentRepository studentRepository){
        this.courseRepository=courseRepository;
        this.studentRepository=studentRepository;
    }

    @Transactional
    public void createClass(ClassDto classDto) throws Exception {
        logger.info("Creating a new class with course number: {}", classDto.getNumber());

        //validation
        validateClassDto(classDto);

        //prepare data
        Course course = CourseMapper.INSTANCE.dtoToEntity(classDto);
        List<Student> students= StudentMapper.INSTANCE.dtoToEntityList(classDto.getStudents());

        //store
        courseRepository.save(course);
        students.forEach(student -> student.setCourseId(course.getId()));
        studentRepository.saveAll(students);

        //log
        logger.info("Class with course number {} created successfully.", classDto.getNumber());
    }

    private static void validateClassDto(ClassDto classDto) {
        if (classDto.getStudents()==null || classDto.getStudents().isEmpty()) {
            logger.error("Validation failed: Students list is empty.");
            throw new IllegalArgumentException(ErrorConstant.STUDENTS_EMPTY_MESSAGE);
        }
        if (classDto.getStudents().size()> classDto.getMaxStudentCount()) {
            logger.error("Validation failed: Students count exceeds the maximum allowed.");
            throw new IllegalArgumentException(ErrorConstant.STUDENTS_OVERFLOW_MESSAGE);
        }
    }

    @Transactional
    public void changeClass(ClassDto classDto) throws Exception {
        logger.info("Updating class with course number: {}", classDto.getNumber());

        //validation
        validateClassDto(classDto);

        //prepare data
        Optional<Course> courseOptional= courseRepository.findByNumber(classDto.getNumber());
        if (!courseOptional.isPresent()) {
            logger.error("Class with course number {} not found.", classDto.getNumber());
            throw new Exception(ErrorConstant.NOT_FOUND_MESSAGE);
        }

        Course course=courseOptional.get();

        CourseMapper.INSTANCE.updateFromDto(classDto, course);

        List<Student> students= StudentMapper.INSTANCE.dtoToEntityList(classDto.getStudents());

        //store
        studentRepository.deleteAllByCourseId(course.getId());

        courseRepository.save(course);
        students.forEach(student -> student.setCourseId(course.getId()));
        studentRepository.saveAll(students);

        logger.info("Class with course number {} updated successfully.", classDto.getNumber());
    }

    public ClassDto findClassByNumber(long courseNumber) throws Exception {
        logger.info("Retrieving class with course number: {}", courseNumber);
        Course course = courseRepository.findByNumber(courseNumber)
                .orElseThrow(() -> {
                    logger.error("Class with course number {} not found.", courseNumber);
                    return new IllegalArgumentException(ErrorConstant.NOT_FOUND_MESSAGE);
                });

        ClassDto classDto= CourseMapper.INSTANCE.entityToDto(course);
        List<Student> students= studentRepository.findAllByCourseId(course.getId());
        classDto.setStudents(StudentMapper.INSTANCE.entityToDtoList(students));

        logger.info("Class with course number {} retrieved successfully.", courseNumber);

        return classDto;
    }

    @Transactional
    public void removeClass(long courseNumber) throws Exception {
        logger.info("Deleting class with course number: {}", courseNumber);
        Course course = courseRepository.findByNumber(courseNumber)
                .orElseThrow(() ->{
                        logger.error("Class with course number {} not found.", courseNumber);
                        return new IllegalArgumentException(ErrorConstant.NOT_FOUND_MESSAGE);
                });

        List<Student> students= studentRepository.findAllByCourseId(course.getId());

        //store
        courseRepository.deleteById(course.getId());
        studentRepository.deleteAllByCourseId(course.getId());

        logger.info("Class with course number {} deleted successfully.", courseNumber);
    }
}
