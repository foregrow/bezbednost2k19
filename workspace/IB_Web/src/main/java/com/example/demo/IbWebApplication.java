package com.example.demo;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import controller.FileUploadController;

@SpringBootApplication
@ComponentScan({"com.example.demo","controller"})
public class IbWebApplication {

	public static void main(String[] args) {
		new File(FileUploadController.uploadDirectory).mkdir();
		SpringApplication.run(IbWebApplication.class, args);
	}

}
