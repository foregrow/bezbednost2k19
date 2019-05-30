package signing;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.Init;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Signature {

	private static final String target = "./data/photos.xml";
	private static final String signed = "./data/photosSigned.xml";
	private static final String KEY_STORE_FILE = "./data/aa.jks";


    static {
        Security.addProvider(new BouncyCastleProvider());
        Init.init();
    }

    public void signingXml() {
        Document doc = load(target);
        Certificate cert = certificate();
        if (doc == null || cert == null){
        	return;
        }  
        else{
        	PrivateKey pk = privateKey();
            
            doc = sign(doc, pk, cert);
            save(doc, signed);
            
            System.out.println("Potpisivanje zavrseno");
        }
    }

    private Document load(String file) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(new File(file));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private Certificate certificate() {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");

            BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
            ks.load(in, "aa".toCharArray());

            if(ks.isKeyEntry("aa")) {
                return ks.getCertificate("aa");
            }
            else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private PrivateKey privateKey() {
        try {
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
            ks.load(in, "aa".toCharArray());
            if(ks.isKeyEntry("aa")) {
                return (PrivateKey) ks.getKey("aa", "aa".toCharArray());
            }
            else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private Document sign(Document doc, PrivateKey privateKey, Certificate cert) {

        try {
            Element rootEl = doc.getDocumentElement();
            XMLSignature sig = new XMLSignature(doc, null, XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);
            Transforms transforms = new Transforms(doc);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
            sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
            sig.addKeyInfo(cert.getPublicKey());
            sig.addKeyInfo((X509Certificate) cert);
            rootEl.appendChild(sig.getElement());
            sig.sign(privateKey);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void save(Document doc, String singedFile) {
        try {
            File file = new File(singedFile);
            FileOutputStream f = new FileOutputStream(file);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(f);

            transformer.transform(source, result);

            f.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
}
