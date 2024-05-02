package com.github.monorepo.server

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleControllerTest {

    private val exampleController = ExampleController()

    @Test
    fun helloWorld() {
        val actual = exampleController.helloWorld()

        assertThat(actual).isEqualTo(HelloWorld("Monorepos are the best"))
    }
}
