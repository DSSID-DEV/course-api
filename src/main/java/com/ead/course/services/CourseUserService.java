package com.ead.course.services;

import com.ead.course.dtos.UserDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CourseUserService {
    Page<UserDTO> getAllUsersByCourse(Pageable pageable, UUID courseId);

    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

    CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel convetToCourseUserModel);

    boolean existsByUserId(UUID userId);

    void deleteCourseUserByUser(UUID userId);
}
