package com.ead.course.repositories.queries;

public class ModuleQueries {
    public static final String WHERE_COURSE_ID = "select * from tb_modulos where course_course_id = :courseId";
    public static final String WHERE_COURSE_ID_AND_MODULE_ID = "select * from tb_modulos where course_course_id = :courseId and module_id = :moduleId";
}
