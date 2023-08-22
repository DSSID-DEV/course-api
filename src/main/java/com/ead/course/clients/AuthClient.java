package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDTO;
import com.ead.course.dtos.ResponsePageDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.services.UtilService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class AuthClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilService utilService;

    @Value("${ead.api.url.authuser}")
    private String HOST_AUTHUSER;

    public Page<UserDTO> getAllUsersByCourse(Pageable pageable, UUID courseId) {
        List<UserDTO>  usersDTO = null;
        String url = HOST_AUTHUSER + utilService.createUrlGetAllUsersByCourse(pageable, courseId);
        log.debug("DEBUG Request /users {} ", url);
        log.info("DEBUG Request /users {} ", url);
        try {
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType =
                    new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {};
             ResponseEntity<ResponsePageDTO<UserDTO>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            usersDTO =result.getBody().getContent();
            log.debug("DEBUG: Response Number of Elements {} ",  usersDTO.size());
        }catch (HttpClientErrorException e) {
            log.error("Error request /users {} ", e);
        }
        return new PageImpl<>(usersDTO);
    }


    public ResponseEntity<UserDTO> getOneUserById(UUID userId) {
        String url = HOST_AUTHUSER + "/users/" + userId;
        return restTemplate.exchange(url, HttpMethod.GET, null, UserDTO.class);
    }


    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = HOST_AUTHUSER + utilService.createUrlPostSubscriptionUserInCourse(userId);
        var courseUserDTO = new CourseUserDTO(courseId, userId);
        restTemplate.postForEntity(url, courseUserDTO, String.class);
    }

    public void deleteCourseInAuthUser(UUID courseId) {
        String url = HOST_AUTHUSER + "/users/courses/" + courseId;
        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}
