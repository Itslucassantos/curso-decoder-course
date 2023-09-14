package com.ead.course.services.impl;

import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserRepository courseUserRepository;

    @Autowired
    public CourseUserServiceImpl(CourseUserRepository courseUserRepository) {
        this.courseUserRepository = courseUserRepository;
    }

}
