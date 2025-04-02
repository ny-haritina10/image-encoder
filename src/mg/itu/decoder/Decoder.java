package mg.itu.decoder;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Decoder {

    public String decode(String imagePath) 
        throws Exception 
    {
        BufferedImage image = ImageIO.read(new File(imagePath));
        StringBuilder binary = new StringBuilder();
        
        // Collect all bits from the image
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int bit = pixel & 1;  // Get LSB
                binary.append(bit);
            }
        }
        
        // Check if we have enough bits for length prefix
        if (binary.length() < 32) {
            throw new Exception("Image too small to contain message length");
        }
        
        long messageLength = getMessageLength(binary.toString());
        long requiredBits = 32 + messageLength * 8;
        
        if (binary.length() < requiredBits) {
            throw new Exception("Not enough bits extracted: " + binary.length() + " found, " + requiredBits + " needed");
        }
        
        return binaryToMessage(binary.toString());
    }
    
    private long getMessageLength(String binary) {
        String lengthBits = binary.substring(0, 32);
        return Long.parseLong(lengthBits, 2);  
    }
    
    private String binaryToMessage(String binary) {
        long messageLength = getMessageLength(binary);  
        StringBuilder message = new StringBuilder();
        
        for (int i = 32; i < 32 + (messageLength * 8); i += 8) {
            String charBits = binary.substring(i, i + 8);
            char c = (char) Integer.parseInt(charBits, 2);
            message.append(c);
        }
        return message.toString();
    }
}