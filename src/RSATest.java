import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class RSATest {

	public static void main(String[] args) throws Exception {
		BigInteger message;
		Random rand = new Random();  // to generate a message randomly
		int numBits;
		BigInteger mForSignature = new BigInteger("111111111111111111111111111111111111111111111111111111111111111111111111");  // former m

		// Message input by user
		Scanner input = new Scanner(System.in);
		System.out.println("Enter 1 to enter message manually, or any other number to generate randomly:");
		if (input.nextInt() == 1) {
			System.out.println("Enter message:");
			message = input.nextBigInteger();
		} else {
			System.out.println("Enter digit to generate message randomly:");
			//message have to be greater than one and less than n = pq
			numBits = input.nextInt();
			do {
				message = new BigInteger(numBits, rand);;
	        } while (message.compareTo(BigInteger.ONE) <= 0);
		}

		RSA rsa = new RSA();

		//  key generation
		rsa.generateKeys(1024);  // Change here key length. 1024, 2048, 4096
	
		//  Encryption
	BigInteger cipherText = rsa.encryptMessage(message);
	
		System.out.println("Message: " + message);
		System.out.println("cipherText: " + cipherText);
	
			//Decryption without CRT
		BigInteger decrptedMessage =	rsa.decryptMessage(cipherText);
	
			//Decryption with CRT
		BigInteger decrptedMessageCRT = rsa.decryptMessageUseCrt(cipherText);
		System.out.println("Decrypted Message without CRT: " + decrptedMessage);
		System.out.println("Decrypted Message using CRT: " + decrptedMessageCRT);

		// The task of digital signature
		BigInteger signature = rsa.decryptMessageUseCrt(mForSignature);  // Former m
		System.out.println("signature using CRT: " + signature);
		System.out.println(" verify signature: " + rsa.encryptMessage(signature));

	}
}