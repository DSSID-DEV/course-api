package com.ead.course.controllers;

import com.ead.course.dtos.LessonDTO;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private ModuleService moduleService;

    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                       @RequestBody LessonDTO lessonDTO) {
        log.debug(ConstantsMessage.DEBUG_SAVELESSON_RECEIVED, lessonDTO.toString());
        Optional<ModuleModel> optionalModuleModel = moduleService.findById(moduleId);
        if (moduleNotPresent(optionalModuleModel)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.MODULE_NOT_FOUND);
        }
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDTO, lessonModel);
        setData(optionalModuleModel.get(), lessonModel);
        this.lessonService.save(lessonModel);
        log.debug(ConstantsMessage.DEBUG_SAVELESSON_SAVED, lessonModel.getLessonId());
        log.info(ConstantsMessage.INFO_SAVELESSON_SAVED, lessonModel.getLessonId());
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonModel);
    }



    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(
            @PathVariable(value = "moduleId") UUID moduleId,
            @PathVariable(value = "lessonId") UUID lessonId,
            @RequestBody LessonDTO lessonDTO) {
        log.debug(ConstantsMessage.DEBUG_SAVELESSON_RECEIVED, lessonDTO);
        Optional<LessonModel> optionalLessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonNotPresent(optionalLessonModel)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.LESSON_NOT_FOUND_THIS_MODULE);
        }
        var lessonModel = optionalLessonModel.get();
        setNewData(lessonModel, lessonDTO);
        lessonService.save(lessonModel);
        log.debug(ConstantsMessage.DEBUG_UPDATELESSON_SAVED, lessonModel.getLessonId());
        log.info(ConstantsMessage.INFO_UPDATELESSON_SAVED, lessonModel.getLessonId());
        return ResponseEntity.status(HttpStatus.OK).body(lessonModel);
    }



    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value = "moduleId") UUID moduleId,
                                         @PathVariable(value = "lessonId") UUID lessonId) {
        Optional<LessonModel> optionalLessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonNotPresent(optionalLessonModel)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.LESSON_NOT_FOUND_THIS_MODULE);
        }
        lessonService.delete(optionalLessonModel.get());
        log.debug(ConstantsMessage.DEBUG_DELETETELESSON_DELETED, lessonId);
        log.info(ConstantsMessage.INFO_DELETELESSON_DELETED, lessonId);
        return ResponseEntity.status(HttpStatus.OK).body(ConstantsMessage.LESSON_DELETED);
    }


    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId,
                                                           SpecificationTemplate.LessonSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "lessonId",
                                                                   direction = Sort.Direction.ASC)
                                                               Pageable pageable) {
        Page<LessonModel> list = lessonService.findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable);
        if (list.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "moduleId") UUID moduleId,
                                               @PathVariable(value = "lessonId") UUID lessonId) {
        Optional<LessonModel> optionalLessonModel = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonNotPresent(optionalLessonModel)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ConstantsMessage.LESSON_NOT_FOUND_THIS_MODULE);
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalLessonModel.get());
    }

    private boolean moduleNotPresent(Optional<ModuleModel> optionalModuleModel) {
        return !optionalModuleModel.isPresent();
    }

    private boolean lessonNotPresent(Optional<LessonModel> optionalLessonModel) {
        return !optionalLessonModel.isPresent();
    }

    private void setNewData(LessonModel lessonModel, LessonDTO lessonDTO) {
        lessonModel.setTitle(checkStringData(lessonModel.getTitle(), lessonDTO.getTitle()));
        lessonModel.setDescription(checkStringData(lessonModel.getDescription(), lessonDTO.getDescription()));
        lessonModel.setVideoUrl(checkStringData(lessonModel.getVideoUrl(), lessonDTO.getVideoUrl()));
    }
    private void setData(ModuleModel moduleModel, LessonModel lessonModel) {
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModel);
    }
    private String checkStringData(String oldData, String newData) {
        return newData.isBlank() ? oldData : newData;
    }

}
