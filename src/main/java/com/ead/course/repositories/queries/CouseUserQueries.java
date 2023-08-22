package com.ead.course.repositories.queries;

public class CouseUserQueries {
    public static final String FIND_ALL_COURSEUSER_INTO_COURSE = "select * from tb_courses_users where course_course_id = :courseId";
}
