package com.estofados.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.StandardApiVersionDeprecationHandler;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Configuration
public class ApiVersionConfig implements WebMvcConfigurer {

    public static final String HEADER = "X-API-Version";
    public static final String QUERY_PARAM = "api-version";

    @Override
    public void configureApiVersioning(ApiVersionConfigurer configurer) {
        StandardApiVersionDeprecationHandler deprecation = new StandardApiVersionDeprecationHandler();
        deprecation.configureVersion("1")
                .setDeprecationDate(ZonedDateTime.of(2026, 10, 27, 0, 0, 0, 0, ZoneOffset.UTC))
                .setSunsetDate(ZonedDateTime.of(2027, 1, 31, 0, 0, 0, 0, ZoneOffset.UTC))
                .setRequestPredicate(request -> request.getRequestURI().startsWith("/estofados"));

        configurer
                .useRequestHeader(HEADER)
                .useQueryParam(QUERY_PARAM)
                .setDefaultVersion("1")
                .setDeprecationHandler(deprecation);
    }
}