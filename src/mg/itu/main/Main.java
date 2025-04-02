package mg.itu.main;

import mg.itu.decoder.Decoder;
import mg.itu.encoder.Encoder;

public class Main {
    
    public static void main(String[] args) {
        try {
            String originalImagePath = "D:\\Studies\\ITU\\S6\\INF-310_Codage\\image-encoder\\img\\red.png";
            String message = "Secret message!";
            String outputImagePath = "D:\\Studies\\ITU\\S6\\INF-310_Codage\\image-encoder\\img\\output.png";
            
            Encoder encoder = new Encoder();
            encoder.encode(originalImagePath, message, outputImagePath);
            
            System.out.println("Message encoded successfully!");
            
            Decoder decoder = new Decoder();
            String extractedMessage = decoder.decode(outputImagePath);
            System.out.println("Extracted message: " + extractedMessage);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}