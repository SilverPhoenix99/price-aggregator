package com.ekorn.configuration

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@ComponentScan("com.ekorn.business", "com.ekorn.adapter")
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@EnableFeignClients("com.ekorn.adapter.downstream")
@EnableRetry
@EnableTransactionManagement
class ApplicationConfiguration
