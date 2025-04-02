package mg.itu.main;

import java.util.ArrayList;
import java.util.List;

import mg.itu.decoder.PositionBasedDecoder;
import mg.itu.encoder.PositionBasedEncoder;

public class Main {
    
    public static void main(String[] args) {
        try {
            String originalImagePath = "D:\\Studies\\ITU\\S6\\INF-310_Codage\\image-encoder\\img\\red.png";
            String message = "hi guys!";
            String outputImagePath = "D:\\Studies\\ITU\\S6\\INF-310_Codage\\image-encoder\\img\\output.png";
            
            // Encoder encoder = new Encoder();
            // encoder.encode(originalImagePath, message, outputImagePath);
            
            // System.out.println("Message encoded successfully!");
            
            // Decoder decoder = new Decoder();
            // String extractedMessage = decoder.decode(outputImagePath);
            // System.out.println("Extracted message: " + extractedMessage);

            List<int[]> pixelPositions = new ArrayList<>();

            // For simplicity, using positions like (0,0), (1,0), (2,0), etc.
            int requiredBits = 32 + message.length() * 8; // Length prefix + message bits
            for (int i = 0; i < requiredBits; i++) {
                pixelPositions.add(new int[]{i % 10, i / 10}); // Assuming at least 10x10 image
            }
            
            // Position-Based Encoding
            PositionBasedEncoder encoder = new PositionBasedEncoder();
            encoder.encode(originalImagePath, message, outputImagePath, pixelPositions);
            System.out.println("Message encoded successfully using position-based approach!");
            
            // Position-Based Decoding
            PositionBasedDecoder decoder = new PositionBasedDecoder();
            String extractedMessage = decoder.decode(outputImagePath, pixelPositions);
            System.out.println("Extracted message: " + extractedMessage);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}