package com.ead.course.repositories.queries;

public class LessonQueries {

    public static final String WHERE_LESSON_ID = "select * from tb_lessons where module_module_id = :moduleId";

    public static final String WHERE_MODULE_ID_AND_LESSON_ID = "select * from tb_lessons where module_id = :moduleId and lesson_lesson_id = :lessonId";
}
