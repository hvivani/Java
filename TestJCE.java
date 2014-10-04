import javax.crypto.Cipher;
import java.security.*;
import javax.crypto.*;

class TestJCE {
 public static void main(String[] args) {
 boolean JCESupported = false;
 try {
    KeyGenerator kgen = KeyGenerator.getInstance("AES", "SunJCE");
    kgen.init(256);
    JCESupported = true;
 } catch (NoSuchAlgorithmException e) {
    JCESupported = false;
 } catch (NoSuchProviderException e) {
    JCESupported = false;
 }
    System.out.println("JCE Supported=" + JCESupported);
 }
} 
