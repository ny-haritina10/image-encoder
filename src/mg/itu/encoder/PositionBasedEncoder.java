package mg.itu.encoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

public class PositionBasedEncoder {
    public void encode(String imagePath, String message, String outputPath, List<int[]> pixelPositions) 
        throws Exception 
    {
        BufferedImage image = ImageIO.read(new File(imagePath));        
        String binaryMessage = messageToBinary(message);
        
        // Validate input
        if (binaryMessage.length() > pixelPositions.size()) {
            throw new Exception("Not enough pixel positions: " + pixelPositions.size() + " available, " + binaryMessage.length() + " needed");
        }
        
        // Hide message in specified pixel positions
        for (int i = 0; i < binaryMessage.length(); i++) {
            int[] position = pixelPositions.get(i);
            int x = position[0];
            int y = position[1];
            
            // Check bounds
            if (x < 0 || x >= image.getWidth() || y < 0 || y >= image.getHeight()) {
                throw new Exception("Invalid pixel position: (" + x + ", " + y + ")");
            }
            
            int pixel = image.getRGB(x, y);
            pixel = hideBit(pixel, binaryMessage.charAt(i));
            image.setRGB(x, y, pixel);
        }
        
        // Save the modified image
        ImageIO.write(image, "png", new File(outputPath));
    }
    
    private String messageToBinary(String message) {
        StringBuilder binary = new StringBuilder();
        // Use long for message length, 32-bit prefix
        String lengthBinary = String.format("%32s", Long.toBinaryString(message.length())).replace(' ', '0');
        binary.append(lengthBinary);
        
        // Convert message to binary
        for (char c : message.toCharArray()) {
            String charBinary = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            binary.append(charBinary);
        }
        return binary.toString();
    }
    
    private int hideBit(int pixel, char bit) {
        // Clear LSB and set it to message bit
        return (pixel & 0xFFFFFFFE) | (bit - '0');
    }
}