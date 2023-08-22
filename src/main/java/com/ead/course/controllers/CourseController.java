package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.spacifications.SpecificationTemplate;
import com.ead.course.utils.ConstantsMessage;
import com.ead.course.validation.CourseValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseValidator courseValidator;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody Errors errors, CourseDTO courseDTO) {
        log.debug(ConstantsMessage.DEBUG_SAVECOURSE_RECEIVED, courseDTO.toString());
        courseValidator.validate(courseDTO, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, courseModel);
        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        this.courseService.save(courseModel);
        log.debug(ConstantsMessage.DEBUG_SAVECOURSE_SAVED, courseModel.getCourseId());
        log.info(ConstantsMessage.INFO_SAVECOURSE_SAVED, courseModel.getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseModel);
    }

    @PutMapping("/courseId")
    public ResponseEntity<Object> updateCourse(
            @PathVariable(value = "courseId") UUID courseId,
            @RequestBody @Validated CourseDTO courseDTO) {
        log.debug(ConstantsMessage.DEBUG_UPDATECOURSE_RECEIVED, courseDTO.toString());
        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if (!optionalCourseModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.COURSE_NOT_FOUND);
        }
        var courseModel = optionalCourseModel.get();
        setNewData(courseModel,  courseDTO);
        log.debug(ConstantsMessage.DEBUG_UPDATECOURSE_UPDATED, courseModel.getCourseId());
        log.info(ConstantsMessage.INFO_UPDATECOURSE_UPDATED, courseModel.getCourseId());
        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }


    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if (!optionalCourseModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.COURSE_NOT_FOUND);
        }
        courseService.delete(optionalCourseModel.get());
        log.debug(ConstantsMessage.DEBUG_DELETECOURSE_DELETED, courseId);
        log.info(ConstantsMessage.INFO_DELETECOURSE_DELETED, courseId);
        return ResponseEntity.status(HttpStatus.OK).body(ConstantsMessage.COURSE_DELETED);
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(
            SpecificationTemplate.CourseSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        Page<CourseModel> courses = courseService.findAll(spec, pageable);
        if (courses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if (!optionalCourseModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.COURSE_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalCourseModel.get());
    }
    private void setNewData(CourseModel courseModel, CourseDTO courseDTO) {
        courseModel.setName(checkStringData(courseModel.getName(), courseDTO.getName()));
        courseModel.setDescription(checkStringData(courseModel.getDescription(), courseDTO.getDescription()));
        courseModel.setImageUrl(checkStringData(courseModel.getImageUrl(), courseDTO.getImageUrl()));
        courseModel.setCourseLevel(checkCourseLevelData(courseModel.getCourseLevel(), courseDTO.getCourseLevel()));
        courseModel.setCourseStatus(checkCourseStatusData(courseModel.getCourseStatus(), courseDTO.getCourseStatus()));
//        courseModel.setUserInstructor(checkeUserInstrutor(courseModel.getUserInstructor(), courseDTO.getUserInstructor()));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    }

//    private UUID checkeUserInstrutor(UUID oldInstructor, UUID newInstructor) {
//        return newInstructor != null ? newInstructor : oldInstructor;
//    }

    private CourseStatus checkCourseStatusData(CourseStatus oldData, CourseStatus newData) {
        return newData != null ? newData : oldData;
    }

    private CourseLevel checkCourseLevelData(CourseLevel oldData, CourseLevel newData) {
        return newData != null ? newData : oldData;
    }

    private String checkStringData(String oldData, String newData) {
        return !newData.isBlank() ? newData : oldData;
    }

}
