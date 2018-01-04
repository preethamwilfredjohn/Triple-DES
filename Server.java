import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

class Server 
{
	public static void main(String argv[]) throws Exception 
	{
		String key;
		String ciph;
  
		//Creating TCP connection
		ServerSocket welcomeSocket = new ServerSocket(5555);
		InputStream in = null;
		OutputStream output=null;
		PrintWriter out = null;
		Scanner input = new Scanner(System.in);
		System.out.println("Server started");
		Socket connectionSocket = welcomeSocket.accept();
		while (true) 
		{	  
			in = connectionSocket.getInputStream();
			output=connectionSocket.getOutputStream();
			out = new PrintWriter(output,true);
			BufferedReader br =new BufferedReader(new InputStreamReader(in));
			
			//Reading key and cipher text from client
			key = br.readLine();
			ciph=br.readLine();
			System.out.println("Cipher text from Client : "+ciph);
			
			//decrypting the cipher using the key
			String dec = decrypt(key,ciph);
			System.out.println("Decrypted message : "+dec);
			
			//Creating a new key
			String genKey = "key1";
			System.out.println("Encrypting using new key : "+genKey);
			
			//encrypting the decrypted text using new key
			String encryptedText = encrypt(dec, genKey);
			System.out.println("Sending the encrypted text : "+encryptedText);
			//sending the encrypted text to client
			out.println(encryptedText);
			
			//encrypting the new key using the key received from client
			String encryptedKey = encrypt(genKey, key);
			System.out.println("Encrypting the new key using key received from client : "+encryptedKey);
			//Sending the encrypted key to client
			out.println(encryptedKey);   
			break;
		}
		welcomeSocket.close();
		input.close();
	}

	private static String encrypt(String toEncrypt, String genKey) throws NoSuchAlgorithmException, NoSuchPaddingException, 
	InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
	{
		SecureRandom sr = new SecureRandom(genKey.getBytes());
		KeyGenerator kg = KeyGenerator.getInstance("DESede");
		kg.init(sr);
		SecretKey sk = kg.generateKey();
		//creating the cipher instance
		Cipher cipher = Cipher.getInstance("DESede");
		//initializing the cipher 
		cipher.init(Cipher.ENCRYPT_MODE, sk);
		//encrypting
		byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
		String enc =new String(encrypted);
		return enc;
	}

	private static String decrypt(String key, String ciph) throws NoSuchAlgorithmException, NoSuchPaddingException, 
	InvalidKeyException, IllegalBlockSizeException, BadPaddingException 
	{
		//converting key to bytes 
		SecureRandom sr = new SecureRandom(key.getBytes());
		//generating key
		KeyGenerator kg = KeyGenerator.getInstance("DESede");
		kg.init(sr);
		SecretKey sk = kg.generateKey();
		byte[] enc=ciph.getBytes();
		//creating instance
		Cipher cipher = Cipher.getInstance("DESede");
		cipher.init(Cipher.DECRYPT_MODE, sk);
		//decrypting
		byte[] decrypted = cipher.doFinal(enc);
		String dec=new String(decrypted);   
		return dec;
	}
}