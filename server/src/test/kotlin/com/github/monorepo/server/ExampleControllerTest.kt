package com.github.monorepo.server

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleControllerTest {

    @Autowired lateinit var restTestTemplate: TestRestTemplate

    @Test
    fun helloWorld() {
        val actual = restTestTemplate.getForEntity("/api/example", HelloWorld::class.java)

        val expected = HelloWorld("Monorepos are the best")
        assertThat(actual.body).isEqualTo(expected)
    }
}
