package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.spacifications.SpecificationTemplate;
import com.ead.course.utils.ConstantsMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 2600)
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private CourseService courseService;

    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId, @RequestBody ModuleDTO moduleDTO) {
        log.debug(ConstantsMessage.DEBUG_SAVEMODULE_RECEIVED, moduleDTO.toString());
        Optional<CourseModel> optionalCourseModel = courseService.findById(courseId);
        if (!optionalCourseModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.COURSE_NOT_FOUND);
        }
        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDTO, moduleModel);
        setData(optionalCourseModel.get(), moduleModel);
        log.debug(ConstantsMessage.DEBUG_SAVEMODULE_SAVED, moduleModel.getModuloId());
        log.info(ConstantsMessage.INFO_SAVEMODULE_SAVED, moduleModel.getModuloId());
        return ResponseEntity.status(HttpStatus.CREATED).body(this.moduleService.save(moduleModel));
    }


    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(
            @PathVariable(value = "courseId") UUID courseId,
            @PathVariable(value = "moduleId") UUID moduleId,
            @RequestBody ModuleDTO moduleDTO) {
        log.debug(ConstantsMessage.DEBUG_UPDATEMODULE_RECEIVED, moduleDTO.toString());
        Optional<ModuleModel> optionalModuleModel = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (!optionalModuleModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.MODULE_NOT_FOUND_FOR_THIS_COURSE);
        }
        var moduleModel = optionalModuleModel.get();
        setTitleAndDescription(moduleModel, moduleDTO);
        moduleService.save(moduleModel);
        log.debug(ConstantsMessage.DEBUG_UPDATEMODULE_UPDATED, moduleModel.getModuloId());
        log.info(ConstantsMessage.INFO_UPDATEMODULE_UPDATED, moduleModel.getModuloId());
        return ResponseEntity.status(HttpStatus.OK).body(moduleModel);
    }


    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId, @PathVariable(value = "moduleId") UUID moduleId) {
        Optional<ModuleModel> optionalModuleModel = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (!optionalModuleModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.MODULE_NOT_FOUND_FOR_THIS_COURSE);
        }
        moduleService.delete(optionalModuleModel.get());
        log.debug(ConstantsMessage.DEBUG_DELETEMODULE_DELETED, moduleId);
        log.info(ConstantsMessage.INFO_DELETEMODULE_DELETED, moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(ConstantsMessage.MODULE_DELETED);
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModule(@PathVariable(value = "courseId") UUID courseId,
                                                          SpecificationTemplate.ModuleSpec spec,
                                                          @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Sort.Direction.ASC)
                                                              Pageable pageable
    ) {
        Page<ModuleModel> modules = moduleService.findAllByModule(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable);
        if (modules.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(modules);
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId, @PathVariable(value = "moduleId") UUID moduleId) {
        Optional<ModuleModel> optionalModuleModel = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (!optionalModuleModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.MODULE_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalModuleModel.get());
    }

    private void setData(CourseModel courseModel, ModuleModel moduleModel) {
        moduleModel.setCourse(courseModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    }
    private void setTitleAndDescription(ModuleModel moduleModel, ModuleDTO moduleDTO) {
        moduleModel.setTitle(checkStringData(moduleModel.getTitle(), moduleDTO.getTitle()));
        moduleModel.setDescription(checkStringData(moduleModel.getDescription(), moduleDTO.getDescription()));
    }

    private String checkStringData(String oldData, String newData) {
        return newData.isBlank() ? oldData : newData;
    }

}
