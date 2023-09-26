package com.ead.course.specifications;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            // path = "courseLevel" = vai filtrar por courseLevel, spec = Equal.class = traz a informação exata.
            // Por ser enum é um Equal.class.
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            // spec = Like.class = traz todos os parâmetros com aquele nome, ex: se buscar por Lucas e tiver Lucas 123,
            // vai trazer todos.
            @Spec(path = "name", spec = Like.class)
    })
    public interface CourseSpec extends Specification<CourseModel> {}

    @Spec(path = "title", spec = Like.class)
    public interface ModuleSpec extends Specification<ModuleModel> {}

    @Spec(path = "title", spec = Like.class)
    public interface LessonSpec extends Specification<LessonModel> {}

    // No geral, essa especificação está construindo uma consulta JPA que busca todos os módulos que estão associados
    // a um determinado curso, identificado pelo courseId.
    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, query, criteriaBuilder) -> {
            // Essa linha configura a consulta para retornar apenas resultados distintos. Ou seja, elimina duplicatas dos resultados.
            query.distinct(true);
            Root<ModuleModel> module = root; // definindo a entidade 1
            Root<CourseModel> course = query.from(CourseModel.class); // definindo a entidade 2
            // Isso cria uma expressão que representa a coleção de módulos dentro da entidade CourseModel. Pega os módulos
            // do curso específico.
            Expression<Collection<ModuleModel>> coursesModules = course.get("modules");
            // Isso verifica se o courseId da entidade CourseModel é igual ao courseId fornecido como parâmetro.
            return criteriaBuilder.and(criteriaBuilder.equal(course.get("courseId"), courseId),
            // Isso verifica se o module é membro da coleção de módulos modules na entidade CourseModel. Isso é útil
                    // quando você deseja encontrar módulos que pertençam a um curso específico.
                    criteriaBuilder.isMember(module, coursesModules));
        };
    }

    public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Root<LessonModel> lesson = root;
            Root<ModuleModel> module = query.from(ModuleModel.class);
            // Dentro de module, a gente tem uma coleção de lessons, module.get("lessons") é para pegar a coleção.
            Expression<Collection<LessonModel>> moduleLessons = module.get("lessons");
            return criteriaBuilder.and(criteriaBuilder.equal(module.get("moduleId"), moduleId),
                    // para ver qual os lesson que fazer parte do moduleLessons
                    criteriaBuilder.isMember(lesson, moduleLessons));
        };
    }

    public static Specification<CourseModel> courseUserId(final UUID userId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            // "coursesUsers" é a Lista de set definido na CourseModel que tem relação com o CourseUserModel
            Join<CourseModel, CourseUserModel> courseProd = root.join("coursesUsers");
            return criteriaBuilder.equal(courseProd.get("userId"), userId);
        };
    }

}
