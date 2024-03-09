
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class PerformanceTest {

	public static void main(String[] args) throws Exception {
		BigInteger message;
		Random rand = new Random(); // to generate a message randomly
		int numBits;
		int n = 500; // number of performance test
		RSA rsa = new RSA();
		// To run the performance test comment out manually entering option code for p
		// and q in RSA class
		
		// Performance measurement: key generation
		ArrayList<Double> arrayKey = new ArrayList<Double>();
		for (int i = 0; i < n; i++) {
			double startTimeKey = System.currentTimeMillis();
			rsa.generateKeys(1024); // Change here. 1024, 2048, 4096
			double endTimeKey = System.currentTimeMillis();
			double durationKey = endTimeKey - startTimeKey;
			arrayKey.add(durationKey);
		}
		double meanKey = mean(arrayKey);
		double VarianceKey = variance(arrayKey);
		writeToText("keyGeneration_1024.txt", arrayKey, meanKey, VarianceKey);
		System.out.println("Average time taken to generate the keys in ms: " + meanKey);

		// Performance measurement: Encryption
		// rsa.generateKeys(1024); // Change here. 1024, 2048, 4096
		ArrayList<Double> arrayEnc = new ArrayList<Double>();
		for (int i = 0; i < n; i++) {
			numBits = rand.nextInt(128) + 2;
			do {
				message = new BigInteger(numBits, rand);
				;
			} while (message.compareTo(BigInteger.ONE) <= 0);

			double startTimeEnc = System.nanoTime();
			rsa.encryptMessage(message);
			double endTimeEnc = System.nanoTime();
			double durationEnc = (endTimeEnc - startTimeEnc) / 1000;
			arrayEnc.add(durationEnc);
			System.out.println("Message: " + message);
		}
		double meanEnc = mean(arrayEnc);
		double VarianceEnc = variance(arrayEnc);
		writeToText("encryption_1024.txt", arrayEnc, meanEnc, VarianceEnc);
		System.out.println("Average time taken to encrypt a message in µs: " + meanEnc);

		// Performance measurement:Decrption with and without CRT
		// rsa.generateKeys(1024); // Change here. 1024, 2048, 4096
		ArrayList<Double> array1 = new ArrayList<Double>();
		ArrayList<Double> array2 = new ArrayList<Double>();
		for (int i = 0; i < n; i++) {
			numBits = rand.nextInt(128) + 2;
			do {
				message = new BigInteger(numBits, rand);
				;
			} while (message.compareTo(BigInteger.ONE) <= 0);

			BigInteger cipherText = rsa.encryptMessage(message);
			// without CRT
			double startTime = System.currentTimeMillis();
			rsa.decryptMessage(cipherText);
			double endTime = System.currentTimeMillis();
			double duration = endTime - startTime;
			array1.add(duration);
			// with CRT
			double startTime2 = System.currentTimeMillis();
			rsa.decryptMessageUseCrt(cipherText);
			double endTime2 = System.currentTimeMillis();
			double duration2 = endTime2 - startTime2;
			array2.add(duration2);
		}

		double meanWithoutCRT = mean(array1);
		double VarianceWithoutCRT = variance(array1);
		writeToText("decrptionWithoutCRT_1024.txt", array1, meanWithoutCRT, VarianceWithoutCRT);
		System.out.println("Average time taken to decrypt a message without using CRT in ms: " + meanWithoutCRT);
		double meanWithCRT = mean(array2);
		double VarianceWithCRT = variance(array2);
		writeToText("decrptionWithtCRT_1024.txt", array2, meanWithCRT, VarianceWithCRT);
		System.out.println("Average time taken to decrypt a message using CRT in ms: " + meanWithCRT);
	}

	private static double mean(ArrayList<Double> array) {
		double sum = 0;
		for (double ar : array) {
			sum += ar;
		}
		return sum / array.size();
	}

	private static double variance(ArrayList<Double> array) {
		double variance = 0;
		for (double arr : array) {
			variance += Math.pow(arr - mean(array), 2);
		}
		return variance / array.size();
	}

// To write the the out put to text file
	private static void writeToText(String st, ArrayList<Double> array, double mean, double variance) {
		FileOutputStream out; // declare a file output object
		PrintStream p; // declare a print stream object
		try {
			// Create a new file output stream
			out = new FileOutputStream(st);
			// Connect print stream to the output stream
			p = new PrintStream(out);

			p.append("Values: " + array + "\nMean: " + mean + "\nVariance: " + variance);

			p.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}