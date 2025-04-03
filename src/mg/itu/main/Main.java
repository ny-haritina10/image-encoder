package mg.itu.main;

import java.util.List;

import mg.itu.decoder.PositionBasedDecoder;
import mg.itu.encoder.PositionBasedEncoder;

public class Main {
    
    public static void main(String[] args) {
        try {
            String originalImagePath = "D:\\Studies\\ITU\\S6\\INF-310_Codage\\image-encoder\\img\\red.png";
            String message = "hi guys!";
            String outputImagePath = "D:\\Studies\\ITU\\S6\\INF-310_Codage\\image-encoder\\img\\output.png";

            PositionBasedEncoder encoder = new PositionBasedEncoder();
            List<int[]> pixelPositions = encoder.encode(originalImagePath, message, outputImagePath);
            System.out.println("Message encoded successfully using " + pixelPositions.size() + " random positions!");

            PositionBasedDecoder decoder = new PositionBasedDecoder();
            String extractedMessage = decoder.decode(outputImagePath, pixelPositions);
            System.out.println("Extracted message: " + extractedMessage);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}