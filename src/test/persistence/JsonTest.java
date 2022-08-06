package persistence;

import model.Clothing;

import static org.junit.jupiter.api.Assertions.assertEquals;

// This is an abstract class to test/check each of the elements/fields of a piece of clothing
public class JsonTest {
    // REQUIRES: colour, brand, material, size and description cannot be the empty string ""
    //           price >= 0.00 and must contain two decimal places
    // EFFECTS: checks that the given traits such as colour, price, etc. match the fields of the given Clothing
    protected void checkClothing(String colour, double price, String brand, String material, String size,
                                 String description, Clothing clothing) {
        assertEquals(colour, clothing.getColour());
        assertEquals(price, clothing.getPrice());
        assertEquals(brand, clothing.getBrand());
        assertEquals(material, clothing.getMaterial());
        assertEquals(size, clothing.getSize());
        assertEquals(description, clothing.getDescription());
    }
}
