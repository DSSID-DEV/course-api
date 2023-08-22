package com.ead.course.controllers;


import com.ead.course.clients.AuthClient;
import com.ead.course.dtos.SubscritionDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import com.ead.course.utils.ConstantsMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private CourseUserService courseUserService;

    @Autowired
    private CourseService courseService;


    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(
            @PageableDefault(page = 0, size = 10, sort = "userId",
                    direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "courseId") UUID courseId) {

        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if (!optionalCourseModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ConstantsMessage.COURSE_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(authClient.getAllUsersByCourse(pageable, courseId));
    }

    @PostMapping("/courses/{courseId}/users/subscrition")
    public ResponseEntity<Object> saveSubscrtionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                             @RequestBody @Valid SubscritionDTO subscritionDTO){

        ResponseEntity<UserDTO> responseUser;

        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if (!optionalCourseModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.COURSE_NOT_FOUND);
        }

        if(courseUserService.existsByCourseAndUserId(optionalCourseModel.get(), subscritionDTO.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ConstantsMessage.SUBSCRITON_ALREADY_EXISTS);
        }

        try {
            responseUser = authClient.getOneUserById(subscritionDTO.getUserId());
            if (responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ConstantsMessage.USER_IS_BLOCKED);
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.USER_NOT_FOUND);
            }
        }

        CourseUserModel courseUserModel = courseUserService.saveAndSendSubscriptionUserInCourse(optionalCourseModel.get().toConvetToCourseUserModel(subscritionDTO.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }

    @DeleteMapping("/courses/users/{userId}")
    public ResponseEntity<Object> deleteCourseUserByUser(@PathVariable(value = "userId") UUID userId) {
        if (!courseUserService.existsByUserId(userId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.COURSE_USER_NOT_FOUND);
        }
        courseUserService.deleteCourseUserByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ConstantsMessage.COURSE_USER_DELETED_SUCESSFULY);
    }

}
