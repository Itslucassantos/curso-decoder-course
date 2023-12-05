package com.ead.course.services.impl;

import com.ead.course.models.UserModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> specification, Pageable pageable) {
        return this.userRepository.findAll(specification, pageable);
    }

    @Override
    public UserModel save(UserModel userModel) {
        return this.userRepository.save(userModel);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        // deleta o id de um determinado user na tabela.
        this.courseRepository.deleteCourseUserByUser(userId);
        this.userRepository.deleteById(userId);
    }

    @Override
    public Optional<UserModel> findById(UUID userInstructor) {
        return this.userRepository.findById(userInstructor);
    }

}
