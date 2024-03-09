# RSA-Implementation-In-Java
This project implements the RSA (Rivest-Shamir-Adleman) encryption algorithm in Java, providing secure key generation, encryption, and decryption functionalities. The implementation adheres to specific requirements outlined for the project, ensuring security, performance, and correctness.



## Requirements

### 1. Long Integer Arithmetics Library
- The project utilizes an appropriate long integer arithmetics library to handle large integer operations required for RSA encryption and decryption.

### 2. RSA Implementation
- Implements the RSA encryption and decryption   algorithm with configurable key lengths, i.e., 1024, 2048, and 4096 bits.

- Implements RSA key generation functionality with configurable key lengths, i.e, 1024, 2048, and 4096 bits.
offers options for RSA implementation with and without CRT (Chinese Remainder Theorem) for improved efficiency.

### 3. Security Measures
- Ensures security against side channel attacks and fault injection by implementing countermeasures such as:
  - Constant-time algorithms to prevent timing attacks.
  - Error detection and correction techniques to mitigate fault injection attacks.

### 4. Performance Measures
- Conducts performance measurements for RSA encryption, decryption, and key generation across all three key lengths and with and without CRT.
- Analyzes performance dependencies on message sizes and starting values.
## Usage

To use the RSA implementation, follow these steps:
1. Clone or download the project repository to your local machine.
2. Compile the Java source files using a Java compiler.
3. Run the compiled Java program to execute RSA encryption, decryption, key generation, and performance measurement functionalities.

