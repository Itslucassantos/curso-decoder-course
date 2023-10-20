package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserRepository courseUserRepository;

    private final AuthUserClient authUserClient;

    @Autowired
    public CourseUserServiceImpl(CourseUserRepository courseUserRepository, AuthUserClient authUserClient) {
        this.courseUserRepository = courseUserRepository;
        this.authUserClient = authUserClient;
    }

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return this.courseUserRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourseUserModel save(CourseUserModel courseUserModel) {
        return this.courseUserRepository.save(courseUserModel);
    }

    @Transactional
    @Override
    public CourseUserModel saveAndSendSubscriptionUserInCourse(CourseUserModel courseUserModel) {
        courseUserModel = this.courseUserRepository.save(courseUserModel);
        this.authUserClient.postSubscriptionUserInCourse(courseUserModel.getCourse().getCourseId(), courseUserModel.getUserId());
        return courseUserModel;
    }

}
