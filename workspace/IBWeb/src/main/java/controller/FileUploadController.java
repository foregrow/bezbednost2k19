package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	
	@GetMapping("/download")
	public static String download() throws IOException {
		String path = "";
		String path1 = "";
		if(ApiToken.user != null) {
			path = "data/" + ApiToken.user.getEmail();	
			path1 = "E:/" + ApiToken.user.getEmail();
		}
	
		File srcDir = new File(path);
		File destDir = new File(path1);
		
		copyDir(srcDir, destDir);
		
		return "uploadview";
	}
	
	public static void copyDir(File src, File dest) throws IOException{
		
		if(src.isDirectory()) {
			if(!dest.exists()) {
				dest.mkdir();
			}

			String files[] = src.list();
			
			for (String fileName : files) {
				File srcFile = new File(src, fileName);
				File destFile = new File(dest, fileName);
				copyDir(srcFile, destFile);
			}
		}
		else {
			fileCopy(src, dest);
		}
		
		
	}
	
	public static void fileCopy(File src, File dest) throws FileNotFoundException, IOException {
		
		InputStream in = null;
		OutputStream out = null;
		
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(dest);
			
			byte[] buffer = new byte[1024];
			
			int length;
			while((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
		}
		finally {
			if(in != null) {
				in.close();
			}
			if(out != null) {
				out.close();
			}
		}
		
	}
}












