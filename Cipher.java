


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Cipher{

    public static void main(String[] args){
        // Ensuring there are EXACTLY 5 arguments in command line
        
        if(args.length != 5){
            System.err.println("Invalid Function Type");
            System.exit(1); //Invalid Argument Size 
        }

        String cipherType = args[0]; 
        cipherTypeFunctionality(cipherType);

        //The plaintext input file for the encryption mode will be
        //in ASCII format
        String inputFilePath = args[1];
        IfilePathCheck(inputFilePath);
        

        //Specified output file should be created by your program and your
        // program should write the encrypted ciphertext or decrpted plaintext
        //to the specified output file
        String outputFilePath = args[2];
        createOutputFile(outputFilePath);

        //Contains the symmetric key in ASCII 
        //Key size for Block Cipher is 16 bytes(16 chars)
        //Key size for Stream Cipher any length
        //Read file and use it to encrypt the plaintext or decrypt ciphertext
        //WILL NOT CONTAIN A TERMINATING NEWLINE CHAR
        String keyFilePath = args[3];
        KfilePathCheck(keyFilePath);

        String modeOfOp = args[4];
        modeOfOpFuntionality(modeOfOp);


        
        if(cipherType.equals("S")){
            //Set to Stream Cipher Functionality 
            streamCipher(inputFilePath, outputFilePath, keyFilePath, modeOfOp);
        }else{
             //Set to Block Cipher Functionality 
            if (modeOfOp.equals("E")) {
                encrypt(inputFilePath, outputFilePath, keyFilePath);
            } else {
                decrypt(inputFilePath, outputFilePath, keyFilePath);
            }
        }


    }


    public static void cipherTypeFunctionality(String cipherType){
        // Terminate program if arg is anything other than B or S
        if (!cipherType.equals("B") && !cipherType.equals("S")){
            System.exit(1);
        }

    }

    public static void modeOfOpFuntionality(String modeOfOp) {
        // Terminate program if arg is anything other than E or D
        if (!modeOfOp.equals("E") && !modeOfOp.equals("D")){
            System.err.println("Invalid Mode Type");
            System.exit(1);
        }
    }

    public static void IfilePathCheck(String inputFilePath){
        // Check if file exists

        Path path = Paths.get(inputFilePath);

        if(Files.notExists(path)){
            System.err.println("Input File Does Not Exist");
            System.exit(1);
        }
    }

    public static void KfilePathCheck(String keyFilePath){
        // Check if file exists

        Path path = Paths.get(keyFilePath);

        if(Files.notExists(path)){
            System.err.println("Key File Does Not Exist");
            System.exit(1);
        }
    }

    public static void createOutputFile(String outputFilePath) {
        try {
            Path path = Paths.get(outputFilePath);
            Files.deleteIfExists(path); // Overwrite if exists
            Files.createFile(path);
        } catch (Exception e) {
            System.err.println("Failed to create output file: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void streamCipher(String inputPath, String outputPath, String keyPath, String mode){
        try {
            byte[] input = Files.readAllBytes(Paths.get(inputPath));
            byte[] key = Files.readAllBytes(Paths.get(keyPath));

            if (key.length == 0) {
                System.err.println("Key must not be empty for stream cipher.");
                System.exit(1);
            }

            byte[] result = new byte[input.length];

            for (int i = 0; i < input.length; i++) {
                result[i] = (byte) (input[i] ^ key[i % key.length]);
            }

            Files.write(Paths.get(outputPath), result);

        } catch (Exception e) {
            System.err.println("Stream Cipher Error: " + e.getMessage());
            System.exit(1);
        }
    }


    public static void encrypt(String inputPath, String outputPath, String keyPath){
        try {
            byte[] input = Files.readAllBytes(Paths.get(inputPath));
            byte[] key = Files.readAllBytes(Paths.get(keyPath));

            if (key.length != 16) {
                System.err.println("Key must be exactly 16 bytes.");
                System.exit(1);
            }

            int blockSize = 16;
            int totalBlocks = (input.length + blockSize - 1) / blockSize;
            byte[] output = new byte[totalBlocks * blockSize];

            for (int b = 0; b < totalBlocks; b++) {
                byte[] block = new byte[blockSize];

                // Copy current block from input (pad with 0x81 if needed)
                int start = b * blockSize;
                int len = Math.min(blockSize, input.length - start);
                System.arraycopy(input, start, block, 0, len);
                for (int i = len; i < blockSize; i++) {
                    block[i] = (byte) 0x81;
                }

                // XOR with key
                for (int i = 0; i < blockSize; i++) {
                    block[i] ^= key[i];
                }

                // Byte swap
                int s = 0, e = 15;
                while (s < e) {
                    if ((key[s] & 1) == 1) {
                        byte temp = block[s];
                        block[s] = block[e];
                        block[e] = temp;
                        e--;
                    }
                    s++;
                }

                // Copy to output
                System.arraycopy(block, 0, output, b * blockSize, blockSize);
            }

            Files.write(Paths.get(outputPath), output);

        } catch (Exception e) {
            System.err.println("Encryption Error: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void decrypt(String inputPath, String outputPath, String keyPath){
        try {  
            byte[] input = Files.readAllBytes(Paths.get(inputPath));
            byte[] key = Files.readAllBytes(Paths.get(keyPath));

            if (key.length != 16 || input.length % 16 != 0) {
                System.err.println("Key must be 16 bytes and input a multiple of 16 bytes.");
                System.exit(1);
            }

            int blockSize = 16;
            int totalBlocks = input.length / blockSize;
            byte[] output = new byte[input.length];

            for (int b = 0; b < totalBlocks; b++) {
                byte[] block = new byte[blockSize];
                System.arraycopy(input, b * blockSize, block, 0, blockSize);

                // Reverse byte swap
                int s = 0, e = 15;
                while (s < e) {
                    if ((key[s] & 1) == 1) {
                        byte temp = block[s];
                        block[s] = block[e];
                        block[e] = temp;
                        e--;
                    }
                    s++;
                }

                // XOR with key
                for (int i = 0; i < blockSize; i++) {
                    block[i] ^= key[i];
                }

                // Remove padding only from LAST block
                if (b == totalBlocks - 1) {
                    int length = blockSize;
                    while (length > 0 && (block[length - 1] & 0xFF) == 0x81) {
                        length--;
                    }
                    System.arraycopy(block, 0, output, b * blockSize, length);
            
                } else {
                    System.arraycopy(block, 0, output, b * blockSize, blockSize);
                }
            }

            // Trim to actual decrypted size
            int actualSize = output.length;
            while (actualSize > 0 && output[actualSize - 1] == 0) {
                actualSize--;
            }

            byte[] trimmed = new byte[actualSize];
            System.arraycopy(output, 0, trimmed, 0, actualSize);

            Files.write(Paths.get(outputPath), trimmed);
            
        } catch (Exception e) {
            System.err.println("Decryption Error: " + e.getMessage());
            System.exit(1);
        }
            
    }


}