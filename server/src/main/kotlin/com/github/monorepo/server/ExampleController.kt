package com.github.monorepo.server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/example")
class ExampleController {

    @GetMapping()
    fun helloWorld(): HelloWorld {
        return HelloWorld("Monorepos are the best")
    }
}

data class HelloWorld(val message: String)
