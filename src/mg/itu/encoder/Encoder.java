package mg.itu.encoder;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Encoder {

    public void encode(String imagePath, String message, String outputPath) 
        throws Exception 
    {
        BufferedImage image = ImageIO.read(new File(imagePath));

        long requiredBits = 32 + (long) message.length() * 8;  // 32 for length + 8 per char
        long availableBits = (long) image.getWidth() * image.getHeight();
        
        if (requiredBits > availableBits) {
            throw new Exception("Image too small: " + availableBits + " bits available, " + requiredBits + " needed");
        }
        
        String binaryMessage = messageToBinary(message);
        if (binaryMessage.length() > image.getWidth() * image.getHeight()) {
            throw new Exception("Message too large for image capacity");
        }
        
        // Hide message in image
        int messageIndex = 0;
        outerloop:      // used to deal with nested loops
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (messageIndex >= binaryMessage.length()) { 
                    break outerloop;        // breaks both loops
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
        String lengthBinary = String.format("%32s", Long.toBinaryString(message.length())).replace(' ', '0');
        binary.append(lengthBinary);
        
        for (char c : message.toCharArray()) {
            String charBinary = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
            binary.append(charBinary);
        }
        return binary.toString();
    }
    
    private int hideBit(int pixel, char bit) {
        int newPixel = (pixel & 0xFFFFFFFE) | (bit - '0');
        return newPixel;
    }
}