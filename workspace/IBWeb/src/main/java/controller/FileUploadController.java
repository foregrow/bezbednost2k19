package controller;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import util.ApiToken;

@Controller
@RequestMapping("/")
public class FileUploadController {

	public static String uploadDirectory = "";
	

	@GetMapping("/uploadview")
	public static String uploadPage(Model model) {
		if(ApiToken.tokenActive()) {
			return "uploadview";
		}
		else {
			return "Unauthorized";
		}
		
	}
	
	@GetMapping("/role")
	public static ResponseEntity<Object> role() {
		
		String role = "regular";
		if(ApiToken.user.getAuthority().getId() == 2) {
			role = "admin";
			System.out.println(role);
		}
		
		return new ResponseEntity<Object>(role,HttpStatus.OK);
	}
	
	@PostMapping("/upload")
	public static String upload(Model model,@RequestParam("files") MultipartFile[] files) {
		if(ApiToken.tokenActive()) {
			uploadDirectory = System.getProperty("user.dir")+"/data/" + ApiToken.user.getEmail() + "/zip";
			StringBuilder fileNames = new StringBuilder();
			for(MultipartFile file : files) {
				Path fp = Paths.get(uploadDirectory,file.getOriginalFilename());
				fileNames.append(file.getOriginalFilename()+" ");
				try {
					Files.write(fp, file.getBytes());
				}catch(IOException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println("uploadstatusviewsssssssssss");
			return "uploadstatusview";
		}
		else {
			System.out.println("unnnnnnnnnnn");
			return "Unauthorized";
		}
		
	}
}
