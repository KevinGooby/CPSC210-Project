package model;
// Tests for the Wardrobe class

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WardrobeTest {
    private Wardrobe testWardrobe;
    private Clothing testClothing1;
    private Clothing testClothing2;
    private Clothing testClothing3;

    @BeforeEach
    void runBefore() {
        testWardrobe = new Wardrobe();
        testClothing1 = new Clothing("Red", 5.00, "UBC", "Wool", "S",
                "Sweatshirt");
        testClothing2 = new Clothing("Blue", 10.00, "H&M", "Cotton", "M",
                "Hoodie");
        testClothing3 = new Clothing("Red", 10.00, "H&M", "Wool", "L",
                "Jacket");
    }

    @Test
    void testConstructor() {
        ArrayList<Clothing> result = testWardrobe.viewClothing();
        assertTrue(result.isEmpty());
        assertEquals(0.00, testWardrobe.getAmount());
    }

    @Test
    void testAddClothing() {
        testWardrobe.addClothing(testClothing1);
        assertEquals(5.00, testWardrobe.getAmount());
        ArrayList<Clothing> result = testWardrobe.viewClothing();
        assertEquals(1, result.size());

        testWardrobe.addClothing(testClothing2);
        assertEquals(15.00, testWardrobe.getAmount());
        assertEquals(2, result.size());
    }

    @Test
    void testViewClothing() {
        testWardrobe.addClothing(testClothing1);
        ArrayList<Clothing> testList = new ArrayList<>();
        testList.add(testClothing1);
        assertEquals(testList, testWardrobe.viewClothing());

        testWardrobe.addClothing(testClothing2);
        testList.add(testClothing2);
        assertEquals(testList, testWardrobe.viewClothing());
    }

    @Test
    void testViewFilteredClothing() {
        testWardrobe.addClothing(testClothing1);
        testWardrobe.addClothing(testClothing2);
        testWardrobe.addClothing(testClothing3);

        // First test case: COLOUR
        ArrayList<Clothing> testList = new ArrayList<>();
        testList.add(testClothing1);
        testList.add(testClothing3);
        assertEquals(testList, testWardrobe.viewFilteredClothing("Colour", "Red"));

        // Second test case: BRAND
        testList.clear();
        testList.add(testClothing2);
        testList.add(testClothing3);
        assertEquals(testList, testWardrobe.viewFilteredClothing("Brand", "H&M"));

        // Third test case: MATERIAL
        testList.clear();
        testList.add(testClothing1);
        testList.add(testClothing3);
        assertEquals(testList, testWardrobe.viewFilteredClothing("Material", "Wool"));

        // Fourth test case: SIZE
        testList.remove(testClothing1);
        assertEquals(testList, testWardrobe.viewFilteredClothing("Size", "L"));

        // Fifth test case: DESCRIPTION
        assertEquals(testList, testWardrobe.viewFilteredClothing("Description", "Jacket"));
    }

    @Test
    void testViewClothingByPrice() {
        testWardrobe.addClothing(testClothing1);

        // Test case one: Clothing is in between the range
        ArrayList<Clothing> testList = new ArrayList<>();
        testList.add(testClothing1);
        assertEquals(testList, testWardrobe.viewClothingByPrice(3.00, 7.00));

        // Test case two: Clothing is = min
        assertEquals(testList, testWardrobe.viewClothingByPrice(5.00, 10.00));

        // Test case three: Clothing is = max
        assertEquals(testList, testWardrobe.viewClothingByPrice(3.00, 5.00));

        // Test case four: Clothing is = min and max. This is for viewing clothing items of only one price!
        assertEquals(testList, testWardrobe.viewClothingByPrice(5.00, 5.00));

        testList.remove(testClothing1); // To represent an empty list.

        // Test case five: Clothing price is < min
        assertEquals(testList, testWardrobe.viewClothingByPrice(7.00, 10.00));

        // Test case six: Clothing price is > max
        assertEquals(testList, testWardrobe.viewClothingByPrice(2.00, 4.00));

        testWardrobe.addClothing(testClothing2);
        testWardrobe.addClothing(testClothing3);

        // Test case seven: Multiple clothing items, some satisfy criteria while others do not
        testList.add(testClothing2);
        testList.add(testClothing3);

        assertEquals(testList, testWardrobe.viewClothingByPrice(6.00, 10.00));
    }

    @Test
    void testRemoveClothing() {
        testWardrobe.addClothing(testClothing1);
        testWardrobe.addClothing(testClothing2);

        testWardrobe.removeClothing(testClothing1);
        ArrayList<Clothing> testList = new ArrayList<>();
        testList.add(testClothing2);
        assertEquals(testList, testWardrobe.viewClothing());
    }
}
