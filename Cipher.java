
// import java.io; 
// import java.util;

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
        }else{
             //Set to Block Cipher Functionality 
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
        
    }

    public static void streamCipher(){

    }
    public static void blockCipher(){
        int padding = 0x81; 
        
    }

    public static void encrypt(){}

    public static void decrypt(){}

}