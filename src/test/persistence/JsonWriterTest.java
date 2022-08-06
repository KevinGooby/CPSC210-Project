package persistence;

import model.Wardrobe;
import model.Clothing;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Represents a test class to test the JsonWriter functionality. Includes a file with a bad name, empty file, and
// filled file
public class JsonWriterTest extends JsonTest {

    // Test writing to a file with a bad file name, should expect exception
    @Test
    void testWriterWithMissingFile() {
        try {
            Wardrobe wardrobe = new Wardrobe();
            JsonWriter writer = new JsonWriter("./data/my\0Bad:NameFile.json");
            writer.open();
            fail("An IOException was expected to be thrown");
        } catch (IOException e) {
            // this is the desired outcome, so this should pass if the IOException is thrown
        }
    }

    // Test writing nothing to a valid file, should not expect exception
    @Test
    void testWriterEmptyWardrobe() {
        try {
            Wardrobe wardrobe = new Wardrobe();
            JsonWriter writer = new JsonWriter("./data/testWriterWithEmptyWardrobe.json");
            writer.open();
            writer.write(wardrobe);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterWithEmptyWardrobe.json");
            wardrobe = reader.read();
            assertEquals(0, wardrobe.getAmount());
            List<Clothing> testList = new ArrayList<>();

            assertEquals(testList, wardrobe.viewClothing());
        } catch (IOException e) {
            fail("We could not write to the file, and the IOException was thrown");
        }
    }

    // Test writing multiple things to a valid file, should not expect exception
    @Test
    void testWriterWithFilledWardrobe() {
        try {
            Wardrobe wardrobe = new Wardrobe();
            Clothing testClothing1 = new Clothing("Red", 5.99, "H&M", "wool", "S",
                    "Sweater");
            Clothing testClothing2 = new Clothing("Blue", 7.99, "Hugo Boss", "polyester",
                    "M", "Hoodie");
            Clothing testClothing3 = new Clothing("Green", 10.00, "Supreme", "cotton",
                    "L", "T-shirt");

            wardrobe.addClothing(testClothing1);
            wardrobe.addClothing(testClothing2);
            wardrobe.addClothing(testClothing3);

            // Write it first
            JsonWriter writer = new JsonWriter("./data/testWriterWithFilledWardrobe.json");
            writer.open();
            writer.write(wardrobe);
            writer.close();

            // Then, read it to test if it is correct
            JsonReader reader = new JsonReader("./data/testReaderWithFilledWardrobe.json");

            wardrobe = reader.read();
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
            fail("We could not write to the file and the IOException was thrown");
        }
    }


}
