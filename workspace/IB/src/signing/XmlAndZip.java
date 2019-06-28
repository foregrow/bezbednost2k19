package signing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dao.ConnectionManager;
import dao.UserDAO;
import model.User;

public class XmlAndZip {

		

	
		private static List<File> files = new ArrayList<>();
		private static List<User> users = new ArrayList<>();
		private static User user = new User();
		
		private static String photosXmlPath = "./data/photos.xml";
		private static String photosXmlSignedPath = "./data/photosSigned.xml";
		private static String path = "";
		
		public static void main(String[] args) {
			
			ConnectionManager.open();
			
			users = UserDAO.getAllUsers();
			
	
			boolean ulogovan = false;
			
			while(!ulogovan) {

				
				System.out.println("Unesite email: ");
				
				
				Scanner scanner = new Scanner(System.in);
				String email = scanner.nextLine();
				
				System.out.println("Unesite password: ");
				
				
				Scanner scanner1 = new Scanner(System.in);
				String password = scanner1.nextLine();
				
			for(User u: users) {
				if(email.equals(u.getEmail()) && password.equals(u.getPassword())) {
					ulogovan = true;
					user.setEmail(email);
					user.setPassword(password);
					break;
					}
				}
			if(!ulogovan) {
				System.out.println("Pogresan email ili lozinka");
			}
			}
			
			if(ulogovan) {
				LoginSucces();
			}
	    	
		}
		
		private static void LoginSucces() {
			
	    	System.out.println("Unesite putanju do foldera: ");

	    	Scanner scanner = new Scanner(System.in);
	    	String in = scanner.nextLine();
	    	scanner.close();
	    	File f = new File(in);
	    	path = in;
	    	try {
	    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.newDocument();
				Element photoElement = doc.createElement("photos");
				doc.appendChild(photoElement);
				
				for (File file : f.listFiles()) {
					if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".png")) {
						files.add(file);
						
					
						
					  Element photo = doc.createElement("photo");
						
	                  Attr size = doc.createAttribute("size");    
	                  String photoSize = String.valueOf(file.length()/1024);
	                  size.setValue(photoSize);
	                  photo.setAttributeNode(size);
	                  
	                  Attr hash = doc.createAttribute("hash");
	                  String photoHash = String.valueOf(file.hashCode());
	                  hash.setValue(photoHash);
	                  photo.setAttributeNode(hash);
	                  
	                  photo.appendChild(doc.createTextNode(file.getName()));
	                  
	                  photoElement.appendChild(photo);
	                  
	                  
	                  DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                  Element date = doc.createElement("date");
	                  date.appendChild(doc.createTextNode(sdf.format(new Date())));
	                  photoElement.appendChild(date);
	                  
	                  
	                  Element user = doc.createElement("username");
	                  user.appendChild(doc.createTextNode(XmlAndZip.user.getEmail()));
	                  photoElement.appendChild(user);

					}
				}
				
				
				
		    	 TransformerFactory transformerFactory = TransformerFactory.newInstance();
		         Transformer transformer = transformerFactory.newTransformer();
		         DOMSource source = new DOMSource(doc);
		         File xml = new File(photosXmlPath);
		         File xmlSigned = new File(photosXmlSignedPath);
		         
		         StreamResult result = new StreamResult(xml);
		         transformer.transform(source, result);  
		            
		          Signature signature = new Signature();
		          signature.signingXml();
		          files.add(xmlSigned);
		            
		        
		        String newPath = path+"//"+user.getEmail()+".zip";
 		  		
		  		FileOutputStream fos = new FileOutputStream(newPath);
		  		ZipOutputStream zipOut = new ZipOutputStream(fos);
		  		
		  		for (File file : files) {
		  			FileInputStream fis = new FileInputStream(file);
		  			ZipEntry zipEntry = new ZipEntry(file.getName());
		  			zipOut.putNextEntry(zipEntry);
		  				  			
		  			byte[] bytes = new byte[1024];
		  			int length;
		  			
		  			while((length = fis.read(bytes)) >= 0) {
		  				zipOut.write(bytes, 0, length);
		  			}
		  			
		  			fis.close();
		  		}
		  		
		  		zipOut.close();
		  		fos.close();		
		  		
		  		System.out.println("Zipovano! ");
		          
	    	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
