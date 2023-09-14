package com.ead.course.configs;

import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

// para o Spring ler essas configurações, por ser uma classe de configuração
@Configuration
// Quando um aplicativo utiliza WebMvcConfigurationSupport, ele pode estender essa classe e sobrescrever seus métodos
// para personalizar a configuração de várias partes do fluxo de processamento de solicitações web, como a resolução de
// visualizações, tratamento de recursos estáticos, configuração de conversores de formato, etc.
public class ResolveConfig extends WebMvcConfigurationSupport {

    // addArgumentResolvers e addReturnValueHandlers: Permitem adicionar resolvedores de argumentos e manipuladores de
    // valores de retorno personalizados para processar parâmetros de solicitação e valores de retorno de controladores.
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // adicionando a biblioteca que vamos usar, parâmetros de specification
        argumentResolvers.add(new SpecificationArgumentResolver());

        // precisa adicionar os parâmetros da paginação tbm, pq estamos utilizando
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        argumentResolvers.add(resolver);
        // acessa o super para poder adicionar as novas configurações
        super.addArgumentResolvers(argumentResolvers);
    }

}
