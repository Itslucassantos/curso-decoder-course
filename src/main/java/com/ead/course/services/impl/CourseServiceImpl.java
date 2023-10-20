package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final CourseUserRepository courseUserRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, ModuleRepository moduleRepository, LessonRepository lessonRepository, CourseUserRepository courseUserRepository) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.courseUserRepository = courseUserRepository;
    }

    // para fazer tudo dentro de uma transação e se algo der errado os dados não é perdido.
    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        // deleta em cascata -> vincula o curso e todos os modules e as lessons e deleta em cascata, ou seja, todos juntos.
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
        List<CourseUserModel> courseUserModelList = this.courseUserRepository.findAllCourseUserIntoCourse(courseModel.getCourseId());
        if(!courseUserModelList.isEmpty()) {
            this.courseUserRepository.deleteAll(courseUserModelList);
        }
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

}
