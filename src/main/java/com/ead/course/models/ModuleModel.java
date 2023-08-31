package com.ead.course.models;

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
@Table(name = "tb_modules")
public class ModuleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID moduleId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 250)
    private String description;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;

    // A configuração access da anotação @JsonProperty controla a visibilidade da propriedade durante a serialização
    // e desserialização. Quando você define access = JsonProperty.Access.WRITE_ONLY, você está indicando que a
    // propriedade só deve ser considerada durante a serialização (conversão de um objeto Java em JSON), mas deve ser
    // ignorada durante a desserialização (conversão de JSON para um objeto Java). Isso é útil quando você deseja que
    // uma determinada propriedade seja fornecida ao cliente apenas na forma de JSON, mas não deseja permitir que o
    // cliente envie essa propriedade de volta para o servidor durante a desserialização. Isso é bom para proteger
    // campos com informações sensíveis.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // optional = false nn é opcional tem q sempre associar um módulo a um curso
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CourseModel course;

    // para acesso de escrita somente
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    // em um módulo, temos diversos cursos, por isso usar o Set.
    private Set<LessonModel> lessons;

}
