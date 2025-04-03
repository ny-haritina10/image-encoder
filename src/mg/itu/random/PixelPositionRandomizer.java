package mg.itu.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PixelPositionRandomizer {
    private final Random random;

    public PixelPositionRandomizer() {
        this.random = new Random();
    }

    public List<int[]> generateRandomPositions(int width, int height, int requiredPositions) throws Exception {
        int totalPixels = width * height;
        if (requiredPositions > totalPixels) {
            throw new Exception("Required positions (" + requiredPositions + ") exceed total pixels (" + totalPixels + ")");
        }

        List<int[]> positions = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) 
            { positions.add(new int[]{x, y}); }
        }

        shuffle(positions);

        return positions.subList(0, requiredPositions);
    }

    private void shuffle(List<int[]> list) {
        int n = list.size();
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = list.get(i);
            
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }
}