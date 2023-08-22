package com.ead.course.validation;

import com.ead.course.clients.AuthClient;
import com.ead.course.dtos.CourseDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.enums.UserType;
import com.ead.course.utils.ConstantsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    private AuthClient  authClient;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseDTO courseDTO = (CourseDTO) o;
        validator.validate(courseDTO, errors);
        if (!errors.hasErrors()) {
            validatorUserInstructor(courseDTO.getUserInstructor(), errors);
        }
    }

    private void validatorUserInstructor(UUID userInstructor, Errors errors) {
        ResponseEntity<UserDTO> responseUserInsturctor;
        try {
            responseUserInsturctor = authClient.getOneUserById(userInstructor);
            if (responseUserInsturctor.getBody().getUserType().equals(UserType.STUDENT)) {
                errors.rejectValue(ConstantsMessage.FIELD_INSTRUCTOR, ConstantsMessage.ERRO_CODE, ConstantsMessage.DEFAULT_MESSAGE);
            }
        } catch (HttpStatusCodeException e) {
            if(e.getStatusCode().equals(HttpStatus.FOUND)) {
                errors.rejectValue(ConstantsMessage.FIELD_INSTRUCTOR, ConstantsMessage.ERRO_CODE, ConstantsMessage.INSTRUCTOR_NOT_FOUND);
            }
        }
    }
}
