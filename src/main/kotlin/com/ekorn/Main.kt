package com.ekorn

import com.ekorn.configuration.ApplicationConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackageClasses = [ApplicationConfiguration::class]
)
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}
