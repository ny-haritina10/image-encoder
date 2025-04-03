package mg.itu.decoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

public class PositionBasedDecoder {

    public String decode(String imagePath, List<int[]> pixelPositions) throws Exception {
        BufferedImage image = ImageIO.read(new File(imagePath));
        
        if (pixelPositions.size() < 32) 
        { throw new Exception("Position list too short to contain message length"); }
        
        // Extract bits from specified positions
        StringBuilder binary = new StringBuilder();
        for (int[] position : pixelPositions) {
            int x = position[0];
            int y = position[1];
            
            // Check bounds
            if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) 
            { throw new Exception("Invalid pixel position: (" + x + ", " + y + ")"); }
            
            int pixel = image.getRGB(x, y);
            int bit = pixel & 1;  // Get LSB
            
            binary.append(bit);
        }
        
        // Get message length and validate
        long messageLength = getMessageLength(binary.toString());
        long requiredBits = 32 + messageLength * 8;
        if (binary.length() < requiredBits) {
            throw new Exception("Not enough positions: " + binary.length() + " bits found, " + requiredBits + " needed");
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