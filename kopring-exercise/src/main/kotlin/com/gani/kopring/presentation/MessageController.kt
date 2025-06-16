package com.gani.kopring.presentation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController {

    @GetMapping("/")
    fun index(@RequestParam("name") name: String) = "Hello, $name!"

}