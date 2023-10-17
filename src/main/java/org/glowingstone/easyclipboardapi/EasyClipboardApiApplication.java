package org.glowingstone.easyclipboardapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EasyClipboardApiApplication {

	public static void main(String[] args) {
		Funcs.initdb();
		SpringApplication.run(EasyClipboardApiApplication.class, args);
	}

}
