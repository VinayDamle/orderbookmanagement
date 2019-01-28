package com.cs.orderbookmanagement.swagger;

import com.fasterxml.classmate.TypeResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Slf4j
@Configuration
@EnableSwagger2
public class OrderBookSwaggerConfig {

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket fraudManagementApi() {
        return new Docket(DocumentationType.SWAGGER_2).
                genericModelSubstitutes(ResponseEntity.class)
                .genericModelSubstitutes(ResponseEntity.class)
                .alternateTypeRules(newRule(
                        typeResolver.resolve(DeferredResult.class,
                                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                        typeResolver.resolve(WildcardType.class)))
                .useDefaultResponseMessages(true).apiInfo(apiInfo()).
                        select().paths(regex("/orderbook/.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        String description = null;
        try {
            ClassPathResource resource = new ClassPathResource("instructions.html");
            InputStream stream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            description = out.toString();
        } catch (IOException ioe) {
            log.error("Could not load the resource");
        }
        return new ApiInfoBuilder().title("OrderBook Management APIs").description(description.toString())
                .termsOfServiceUrl("http://localhost:9090.com").build();
    }

}
