package isc.nps.school.controller;

import isc.nps.school.dto.ClassDto;
import isc.nps.school.service.ClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("classes")
@RestController
public class ClassController {

    private ClassService classService;

    public ClassController(ClassService classService)
    {
        this.classService=classService;
    }

    @GetMapping("/{courseNumber}")
    public ResponseEntity<ClassDto> getClass(@PathVariable Long courseNumber) throws Exception {
        ClassDto classDto = classService.findClassByNumber(courseNumber);
        return ResponseEntity.ok(classDto);
    }

    @PostMapping
    public ResponseEntity createClass(@RequestBody ClassDto classDto) throws Exception {
        classService.createClass(classDto);
        return ResponseEntity.status(201).build(); // HTTP 201 Created
    }

    @PutMapping
    public ResponseEntity changeClass(@RequestBody ClassDto classDto) throws Exception {
        classService.changeClass(classDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseNumber}")
    public ResponseEntity removeClass(@PathVariable Long courseNumber) throws Exception {
        classService.removeClass(courseNumber);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content

        //return ResponseEntity.status(404).build();
    }

}
