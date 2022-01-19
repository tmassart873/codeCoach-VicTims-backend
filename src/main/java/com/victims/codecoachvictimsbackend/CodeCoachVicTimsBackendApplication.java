package com.victims.codecoachvictimsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CodeCoachVicTimsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeCoachVicTimsBackendApplication.class, args);
    }

}
