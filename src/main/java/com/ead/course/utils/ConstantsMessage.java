package com.ead.course.utils;

public class ConstantsMessage {

    //MESSAGE'S COURSE
    public static final String COURSE_NOT_FOUND = "Course not found!";
    public static final String COURSE_DELETED = "Course deleted successfuly!";

    public static final String MODULE_NOT_FOUND_FOR_THIS_COURSE = "Module not found for this course!";;
    public static final String LESSON_NOT_FOUND_THIS_MODULE = "Lesson not found for this module!";

    //MESSAGE'S MODULE
    public static final String MODULE_NOT_FOUND = "Module not found!";
    public static final String MODULE_DELETED = "Module deleted successfuly!";

    //MESSAGE'S LESSON
    public static final String LESSON_NOT_FOUND = "Lesson not found!";
    public static final String LESSON_DELETED = "Lesson deleted successfuly!";

    //OUTROS
    public static final String CONFLIT_USERNAME = "Error: Username is Already Taken!";
    public static final String CONFLIT_EMAIL = "Error: Email is Already Taken!";
    public static final String CONFLIT_PASSWORD = "Error: Mismatched old password!";
    public static final String PASSWORD_UPDATED = "Password updated successfuly!";

    //MESSAGE'S VALIDATION
    public static final String VALIDATION_USERNAME_NOT_NULL_NOR_NOT_EMPTY = "The username field cant't be null nor empty!";
    public static final String VALIDATION_USERNAME_SIZE = "The username field must contain between 6 and 50 characters!";
    public static final String VALIDATION_EMAIL_NOT_NULL_NOR_NOT_EMPTY = "The email field cant't be null nor empty!";
    public static final String VALIDATION_EMAIL_SIZE = "The email field must contain between 6 and 50 characters!";
    public static final String VALIDATION_EMAIL_IS_NOT_VALID = "The email informated not is valid!";
    public static final String VALIDATION_PASSWORD_NOT_NULL_NOR_NOT_EMPTY = "The password field cant't be null nor empty!";
    public static final String VALIDATION_OLD_PASSWORD_NOT_NULL_NOR_NOT_EMPTY = "The old password field cant't be null nor empty!";
    public static final String VALIDATION_FULL_NAME_NOT_NULL_NOR_NOT_EMPTY = "The full name field cant't be null nor empty!";
    public static final String VALIDATION_FULL_NAME_COMPLETE = "The full name field must contain at least the first and last name!";
    public static final String VALIDATION_PHONENUMBER_SIZE = "The username field must contain between 8 and 20 characters!";
    public static final String VALIDATION_CPF_SIZE = "The CPF field must contain between 11 and 14 characters!";
    public static final String VALIDATION_IMAGE_NOT_NULL_NOR_NOT_EMPTY = "The image field cant't be null nor empty!";
    public static final String VALIDATION_USERNAME_CONSTRAINTS = "The username field cannot be null, it cannot have a space or an empty string!";

    //TODO: MESSAGE'S LOGS

    //TODO: COURSE'S LOGS
    public static final String DEBUG_SAVECOURSE_RECEIVED = "DEBUG POST saveCourse received courseDTO {} ";
    public static final String DEBUG_SAVECOURSE_SAVED = "DEBUG POST course saved successfuly courseId {} ";
    public static final String INFO_SAVECOURSE_SAVED = "INFO POST course saved successfuly courseId {} ";
    public static final String DEBUG_UPDATECOURSE_RECEIVED ="DEBUG PUT updateCourse received courseDTO {} ";
    public static final String DEBUG_UPDATECOURSE_UPDATED = "DEBUG PUT course updated successfuly courseId {} ";
    public static final String INFO_UPDATECOURSE_UPDATED = "INFO PUT course updated successfuly courseId {} ";
    public static final String DEBUG_DELETECOURSE_DELETED = "DEBUG DELETE course deleted successfuly courseId {} ";
    public static final String INFO_DELETECOURSE_DELETED = "INFO DELETE course deleted successfuly courseId {} ";


    //TODO: MODULE'S LOGS
    public static final String DEBUG_SAVEMODULE_RECEIVED =  "DEBUG POST saveCourse received moduleDTO {} ";
    public static final String DEBUG_SAVEMODULE_SAVED = "DEBUG POST module saved successfuly moduleId {} ";
    public static final String INFO_SAVEMODULE_SAVED = "INFO POST module saved successfuly courseId {} ";
    public static final String DEBUG_UPDATEMODULE_RECEIVED = "DEBUG PUT updateCourse received moduleDTO {} ";
    public static final String DEBUG_UPDATEMODULE_UPDATED = "DEBUG PUT module update successfuly moduleId {} ";
    public static final String INFO_UPDATEMODULE_UPDATED = "INFO PUT module update successfuly moduleId {} ";
    public static final String DEBUG_DELETEMODULE_DELETED = "DEBUG DELETE module deleted successfuly moduleId {} ";
    public static final String INFO_DELETEMODULE_DELETED = "INFO DELETE module deleted successfuly moduleId {} ";

    //TODO: LESSON'S LOGS
    public static final String DEBUG_SAVELESSON_RECEIVED = "DEBUG POST saveLesson received lessonDTO {} ";
    public static final String DEBUG_SAVELESSON_SAVED = "DEBUG POST lesson saved successfuly lessonId {} ";
    public static final String INFO_SAVELESSON_SAVED = "INFO POST lesson saved successfuly lessonId {} ";
    public static final String DEBUG_UPDATELESSON_RECEIVED = "DEBUG PUT updateLesson received lessonDTO {} ";
    public static final String DEBUG_UPDATELESSON_SAVED = "DEBUG PUT lesson saved successfuly lessonId {} ";
    public static final String INFO_UPDATELESSON_SAVED = "INFO PUT lesson saved successfuly lessonId {} ";
    public static final String DEBUG_DELETETELESSON_DELETED = "DEBUG DELETE lesson deleted successfuly lessonId {} ";
    public static final String INFO_DELETELESSON_DELETED = "INFO DELETE lesson deleted successfuly lessonId {} ";

    public static final String SUBSCRITON_ALREADY_EXISTS = "Error: Subscrition Already exists!";
    public static final String USER_IS_BLOCKED = "User is blocked!";
    public static final String USER_NOT_FOUND = "User not found!";
    public static final String FIELD_INSTRUCTOR = "userInstructor";
    public static final String ERRO_CODE = "UserInstructorError";
    public static final String DEFAULT_MESSAGE = "User must be INSTRUCTOR or ADMIN.";
    public static final String INSTRUCTOR_NOT_FOUND = "Instructor not found.";
    public static final String COURSE_USER_NOT_FOUND = "CourseUser not found!";
    public static final String COURSE_USER_DELETED_SUCESSFULY = "CourseUser deleted sucessfuly!";
}
