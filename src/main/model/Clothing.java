package model;
// Represents a piece of clothing with its colour, price, brand, material, size and description

import org.json.JSONObject;
import persistence.Writable;

public class Clothing implements Writable {
    private String colour;
    private double price;
    private String brand;
    private String material;
    private String size;
    private String description;

    // Constructor
    // REQUIRES: the colour, brand, material, size and description cannot be empty string "",
    // description can only be one word, and price >= 0 with two decimal points
    // EFFECTS: Constructs a new piece of clothing with the given colour, price, brand, material, size and description
    public Clothing(String colour, double price, String brand, String material, String size, String description) {
        this.colour = colour;
        this.price = price;
        this.brand = brand;
        this.material = material;
        this.size = size;
        this.description = description;
    }

    public String getColour() {
        return colour;
    }

    public double getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }

    public String getMaterial() {
        return material;
    }

    public String getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    // EFFECTS: Writes a piece of clothing to JSON data
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("colour", colour);
        json.put("price", price);
        json.put("brand", brand);
        json.put("material", material);
        json.put("size", size);
        json.put("description", description);

        return json;
    }
}
