import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class RSA {

	// to use p and q on the outside of the generateKeys, keep p and q generated
	private BigInteger p;
	private BigInteger q;

	// this is the 'e' parameter (encryption)
	private BigInteger publicKey;
	// this is the 'd' parameter (decryption)
	private BigInteger privateKey;
	// this is n=p*q
	private BigInteger n;
	// need a random number generator
	private SecureRandom random;

	public RSA() {
		this.random = new SecureRandom();
	}

	public void generateKeys(int keyDigits) {
		// giving a choice for the user to enter p and q or generate them randomly
		Scanner input = new Scanner(System.in);
		System.out.println("Enter 1 to enter p and q or any other number to generate them randomly:");
		if (input.nextInt() == 1) {
			System.out.println("p and q have to be big prime and are not equal. There length has to be at least 512.");
			System.out.println("Enter p:");
			p = input.nextBigInteger();
			System.out.println("Enter q:");
			q = input.nextBigInteger();
		} else {

			// p is a large prime number
			p = BigInteger.probablePrime(keyDigits/2, random);
			do {
				// q is a large prime number different from p
				q = BigInteger.probablePrime(keyDigits/2, random);
			} while (q.equals(p)); // Ensure q is different from p
		}
		// n=q*p
		n = p.multiply(q);

		// Euler's totient phi function (p-1)*(q-1)
		BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

		BigInteger e = new BigInteger("65537");// generatePublicKey(phi);

		// modular inverse of e mod phi (extended Euclidean algorithm )
		BigInteger d = e.modInverse(phi);

		this.privateKey = d;
		this.publicKey = e;
		
		System.out.println("p:" + p);
		System.out.println("q:" + q);
		System.out.println("n:" + n);
		System.out.println("phi_n:" + phi);
		System.out.println("publicKey_e:" + e);
		System.out.println("privateKey_d:" + d);
	}

	// The public key e= 2^16+1 is used now. No usage of this method now.
	private BigInteger generatePublicKey(BigInteger phi) {
		BigInteger e;
		do {
			e = new BigInteger(phi.bitLength(), random);

			// Ensure e is within the range 1 < e < phi and gcd(e, phi) = 1
		} while (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(phi) >= 0 || !e.gcd(phi).equals(BigInteger.ONE));
		return e;
	}

	/*
	 * encryptMessage and decryptMessage method uses Square-and-Multiply-Algorithm
	 * to encrypt and decrypt the message.
	 */
	public BigInteger encryptMessage(BigInteger message) throws Exception {
		boolean messageIsInRange = (message.compareTo(BigInteger.ONE) > 0 && message.compareTo(n) < 0);
		if (messageIsInRange) {
			return squareAndMultiply(message, publicKey, n);
		} else {
			throw new Exception("The message: " + message + " is not in the range.It have to be between 1 and n.");
		}
	}

	public BigInteger decryptMessage(BigInteger cipherText) {
		return squareAndMultiply(cipherText, privateKey, n);
	}

	/*
	 * Square-and-Multiply-Algorithm to calculate x^k mod n for encryption and
	 * decryption
	 */
	private BigInteger squareAndMultiply(BigInteger base, BigInteger exponent, BigInteger modulus) {
		// start from result 1
		BigInteger result = BigInteger.ONE;
		String binaryExponent = exponent.toString(2); // Convert the exponent to binary
		for (int index = 0; index < binaryExponent.length(); index++) {
			result = result.multiply(result).mod(modulus); // Square step // result^2 mod n
			// only when binary_index = 1, add the Multiply step
			if (binaryExponent.charAt(index) == '1') {
				result = result.multiply(base).mod(modulus); // Multiply step
			} else {
				BigInteger temp = result.multiply(base).mod(modulus); // Square-and-always-Multiply
			}
		}
		return result;
	}

	public BigInteger decryptMessageUseCrt(BigInteger cipherText) throws Exception {
		BigInteger dp = privateKey.mod(this.p.subtract(BigInteger.ONE));
		BigInteger dq = privateKey.mod(this.q.subtract(BigInteger.ONE));
		BigInteger m1 = squareAndMultiply(cipherText, dp, this.p);
		BigInteger m2 = squareAndMultiply(cipherText, dq, this.q);
		BigInteger qInv = this.q.modInverse(this.p);
		BigInteger h = qInv.multiply(m1.subtract(m2)).mod(p); // h replaces m1
		BigInteger m = m2.add(h.multiply(q)); // m is decrypted message

		// To secure against fault injections, verifying before transmission
		boolean verify = encryptMessage(m).compareTo(cipherText) == 0;
		if (verify) {
			return m;
		} else {
			throw new Exception("Decryption/Verification Failed");
		}
	}

	// For testing
	public void setP(BigInteger p) {
		this.p = p;
	}

	public void setQ(BigInteger q) {
		this.q = q;
	}

	public void setPublicKey(BigInteger publicKey) {
		this.publicKey = publicKey;
	}

	public void setPrivateKey(BigInteger privateKey) {
		this.privateKey = privateKey;
	}

	public void setN(BigInteger n) {
		this.n = n;
	}
}