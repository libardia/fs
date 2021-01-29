package io.tonyl.fs.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.tonyl.fs.responses.SimpleResponse;

@RestController
public class TestController {
	@GetMapping("hello")
	public SimpleResponse hello() {
		return new SimpleResponse("Hello, client.");
	}
}
