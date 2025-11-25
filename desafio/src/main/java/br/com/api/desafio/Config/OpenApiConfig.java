package br.com.api.desafio.Config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo(){
        return new OpenAPI()
                .info(new Info()
                        .title("Desafio API - Controle de Acessos")
                        .description("Api para Controle de Acessos")
                        .version("1.0.0")
                        .license(new License().name("MIT").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação Completa")
                        .url("http://localhost:8080/swagger-ui.html"));
    }
}
