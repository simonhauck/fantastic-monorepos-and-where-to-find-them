package com.github.monorepo.server

import com.github.monorepo.reverse.ReverseString
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/example")
class ExampleController {

    @GetMapping()
    fun helloWorld(): HelloWorld {
        val message = "tseb eht era soperonoM"
        return HelloWorld(message.reverse())
    }
}

data class HelloWorld(val message: String)

private fun String.reverse(): String = ReverseString.reversed(this)
