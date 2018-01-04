import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.*;
import java.util.Scanner;

import javax.crypto.*;
  
public class TripleDes
{
   public static void main(String []args) throws Exception 
   {	   
	  Scanner input = new Scanner(System.in);
	  //getting text to be encrypted from user
	  System.out.println("Enter the text to be encrypted");
      String toEncrypt = input.nextLine();
      String genKey = "key";
      System.out.println("Encrypting...");
      //calling encryption method
      encrypt(toEncrypt, genKey);
      input.close();
   } 
  
   public static void encrypt(String toEncrypt, String key) throws Exception 
   {
      // Creating key
      SecureRandom sr = new SecureRandom(key.getBytes());
      KeyGenerator kg = KeyGenerator.getInstance("DESede");
      kg.init(sr);
      SecretKey sk = kg.generateKey();
      // creating cipher instance
      Cipher cipher = Cipher.getInstance("DESede");
  
      // initializing cipher 
      cipher.init(Cipher.ENCRYPT_MODE, sk);
  
      // enctypting
      byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
      String enc =new String(encrypted);
      PrintWriter out = null;
	 	  
	  System.out.println("Creating secrete key and sending key to server");
	  //Establishing connection with server
	  Socket clientSocket = new Socket("localhost", 5555);
	  BufferedReader inServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	  out=new PrintWriter(clientSocket.getOutputStream(), true);
	  
	  //sending key to server
	  out.println(key);
	  System.out.println("Sending the cipher : "+enc);	
	  //sending the encrypted cipher to server
	  out.println(enc);
	  //receiving new cipher from server
	  String newCipher=inServer.readLine();
	  System.out.println("cipher from server "+newCipher);
	  //receiving new key from server
	  String newKey=inServer.readLine();
	  System.out.println("New encrypted Key from server "+newKey);
	  //decrypting new key using the key available in client
	  String decryptedKey= decrypt(key,newKey);	   	   
	  //using the decrypted  key decrypting the cipher from server
	  String decryptedCipher= decrypt(decryptedKey,newCipher);
	  System.out.println("decrypted cipher text using the new Key from server: " + decryptedCipher);
	  clientSocket.close();
   }

   private static String decrypt(String key, String newKey) throws NoSuchAlgorithmException, NoSuchPaddingException, 
   InvalidKeyException, IllegalBlockSizeException, BadPaddingException
   {
		//converting key to bytes 
	   SecureRandom sr = new SecureRandom(key.getBytes());
		//generating key
	   KeyGenerator kg = KeyGenerator.getInstance("DESede");
	   kg.init(sr);
	   SecretKey sk = kg.generateKey();
	   byte[] enc=newKey.getBytes();
		//creating instance
	   Cipher cipher = Cipher.getInstance("DESede");
	   cipher.init(Cipher.DECRYPT_MODE, sk);
	   //decrypting
	   byte[] decrypted = cipher.doFinal(enc);
	   String dec=new String(decrypted);   
	   return dec;
   }  
}