package web.app.bezbednost;


import java.io.File;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import controller.FileUploadController;
import dao.ConnectionManager;

@SpringBootApplication
@ComponentScan({"web.app.bezbednost","controller","dto","entity","repository","service"})

public class IbWebApplication {

	public static void main(String[] args) {
		new File(FileUploadController.uploadDirectory).mkdir();
		SpringApplication.run(IbWebApplication.class, args);
		
		ConnectionManager.open();
	}

}
