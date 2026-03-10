package com.work.springai.rag;

import org.springframework.boot.SpringApplication;

public class TestSpringAiRagApplication {

    public static void main(String[] args) {
        SpringApplication.from(SpringAiRagApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
