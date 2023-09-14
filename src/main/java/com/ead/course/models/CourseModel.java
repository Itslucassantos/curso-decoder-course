package com.ead.course.models;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tb_courses")
public class CourseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID courseId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 250)
    private String description;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    //Padrão ISO 8601 UTC
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @Column(nullable = false)
    private UUID userInstructor;

    // para definir o tipo de acesso na serialização, quanto na deserialização.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // mappedBy = "course" para indicar a chave estrangeira
    // Quando faz a consulta por um curso, ele traz as subconsultas que é os modules dos curso, então o
    // fetch = FetchType.LAZY é para baixar as informações das subconsultas só quando necessário.
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    // Para fazer uma consulta para cada curso e uma consulta para todos os módulos relacionados a ele.
    @Fetch(FetchMode.SUBSELECT)
    // é uma lista, pq pode ter mais de um modulo em cada curso, a diferença do Set<> para o List<>, é q o Set não
    // aceita objeto duplicado, enquanto o List sim.
    private Set<ModuleModel> modules;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<CourseUserModel> coursesUsers;

}
