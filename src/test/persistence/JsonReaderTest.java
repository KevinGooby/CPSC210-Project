package persistence;

import model.Clothing;
import model.Wardrobe;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// This class is meant to test the JsonReader functionality with a missing file, an empty file and a filled file
public class JsonReaderTest extends JsonTest {

    // Test reading a missing file, should expect exception
    @Test
    void testReaderWithMissingFile() {
        JsonReader reader = new JsonReader("./data/aMagicalMissingFile.json");
        try {
            Wardrobe wardrobe = reader.read();
            fail("Exception was meant to be thrown");
        } catch (IOException e) {
            // this test should pass as the exception is meant to be thrown
        }
    }

    // Test reading an empty json file that exists, should not expect exception
    @Test
    void testReaderWithEmptyWardrobe() {
        JsonReader reader = new JsonReader("./data/testReaderWithEmptyWardrobe.json");
        try {
            Wardrobe wardrobe = reader.read();
            assertEquals(0, wardrobe.getAmount());

            List<Clothing> testEmptyList = new ArrayList<>();
            assertEquals(testEmptyList, wardrobe.viewClothing());
        } catch (IOException e) {
            fail("The file could not be read, and the IOException was thrown");
        }
    }

    // Test reading a non-empty json file that exists, should not expect exception
    @Test
    void testReaderWithFilledWardrobe() {
        JsonReader reader = new JsonReader("./data/testReaderWithFilledWardrobe.json");
        try {
            Wardrobe wardrobe = reader.read();
            assertEquals(23.98, wardrobe.getAmount());

            List<Clothing> clothingList = wardrobe.viewClothing();
            assertEquals(3, clothingList.size());

            checkClothing("Red",5.99, "H&M", "wool", "S", "Sweater",
                    clothingList.get(0));

            checkClothing("Blue", 7.99, "Hugo Boss", "polyester", "M",
                    "Hoodie", clothingList.get(1));

            checkClothing("Green", 10.00, "Supreme", "cotton", "L",
                    "T-shirt", clothingList.get(2));

        } catch (IOException e) {
            fail("The file couldn't be read and the IOException was thrown");
        }
    }
}
