package com.ead.course.services.impl;

import com.ead.course.dtos.NotificationCommandDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;
import com.ead.course.publishers.NotificationCommandPublisher;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.CourseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final NotificationCommandPublisher notificationCommandPublisher;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, ModuleRepository moduleRepository, LessonRepository lessonRepository, UserRepository userRepository, NotificationCommandPublisher notificationCommandPublisher) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.notificationCommandPublisher = notificationCommandPublisher;
    }

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleModelList = this.moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if (!moduleModelList.isEmpty()) {
            for (ModuleModel moduleModel : moduleModelList) {
                List<LessonModel> lessonModelList = this.lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());
                if (!lessonModelList.isEmpty()) {
                    this.lessonRepository.deleteAll(lessonModelList);
                }
            }
            this.moduleRepository.deleteAll(moduleModelList);
        }
        // deleta o id de um determinado curso na tabela.
        this.courseRepository.deleteCourseUserByCourse(courseModel.getCourseId());
        this.courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return this.courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return this.courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return this.courseRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
        return this.courseRepository.existsByCourseAndUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourse(UUID courseId, UUID userId) {
        this.courseRepository.saveCourseUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourseAndSendNotification(CourseModel courseModel, UserModel userModel) {
        this.courseRepository.saveCourseUser(courseModel.getCourseId(), userModel.getUserId());
        try {
            NotificationCommandDto notificationCommandDto = new NotificationCommandDto();
            notificationCommandDto.setTitle("Bem-Vindo(a) ao Curso: " + courseModel.getName());
            notificationCommandDto.setMessage(userModel.getFullName() + " a sua inscrição foi realizada com sucesso!");
            notificationCommandDto.setUserId(userModel.getUserId());
            notificationCommandPublisher.publishNotificationCommand(notificationCommandDto);
        } catch (Exception e) {
            log.warn("Error sending notification");
        }

    }

}
