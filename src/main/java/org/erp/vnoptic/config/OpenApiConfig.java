package org.erp.vnoptic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VnOptic ERP API")
                        .version("1.0")
                        .description("Backend API for Eyewear Import/Export Management System")
                        .contact(new Contact()
                                .name("ERP Team")
                                .email("support@vnoptic.org")));
    }
}
