package mg.itu.encoder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import mg.itu.random.PixelPositionRandomizer;

public class PositionBasedEncoder {
    public List<int[]> encode(String imagePath, String message, String outputPath) throws Exception {
        BufferedImage image = ImageIO.read(new File(imagePath));
        String binaryMessage = messageToBinary(message);
        int requiredBits = binaryMessage.length();

        PixelPositionRandomizer randomizer = new PixelPositionRandomizer();
        List<int[]> pixelPositions = randomizer.generateRandomPositions(image.getWidth(), image.getHeight(), requiredBits);

        for (int i = 0; i < binaryMessage.length(); i++) {
            int[] position = pixelPositions.get(i);
            int x = position[0];
            int y = position[1];

            int pixel = image.getRGB(x, y);
            pixel = hideBit(pixel, binaryMessage.charAt(i));
            image.setRGB(x, y, pixel);
        }

        ImageIO.write(image, "png", new File(outputPath));
        return pixelPositions;
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
        return (pixel & 0xFFFFFFFE) | (bit - '0');
    }
}