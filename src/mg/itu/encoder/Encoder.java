package mg.itu.encoder;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Encoder {
    public void encode(String imagePath, String message, String outputPath) throws Exception {
        // Read the original image
        BufferedImage image = ImageIO.read(new File(imagePath));
        
        // Convert message to binary with length prefix
        String binaryMessage = messageToBinary(message);
        if (binaryMessage.length() > image.getWidth() * image.getHeight()) {
            throw new Exception("Message too large for image capacity");
        }
        
        // Hide message in image
        int messageIndex = 0;
        outerloop:
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (messageIndex >= binaryMessage.length()) {
                    break outerloop;
                }
                
                int pixel = image.getRGB(x, y);
                pixel = hideBit(pixel, binaryMessage.charAt(messageIndex));
                image.setRGB(x, y, pixel);
                messageIndex++;
            }
        }
        
        // Save the modified image
        ImageIO.write(image, "png", new File(outputPath));
    }
    
    private String messageToBinary(String message) {
        StringBuilder binary = new StringBuilder();
        // Add message length as 32-bit prefix
        String lengthBinary = String.format("%32s", Integer.toBinaryString(message.length())).replace(' ', '0');
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