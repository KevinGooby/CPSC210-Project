package model;
// Represents a wardrobe with a list of clothing and a counter for how much money has been spent on the clothing

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import persistence.Writable;
import java.util.Observable;
import java.util.Observer;

public class Wardrobe extends Observable implements Writable {
    private ArrayList<Clothing> myWardrobe;
    private double amount;

    // Constructor
    // EFFECTS: Constructs a new wardrobe with an empty wardrobe and an amount of 0.00
    public Wardrobe() {
        myWardrobe = new ArrayList<>();
        amount = 0.00;
        addObserver(EventLog.getInstance());
    }

    // REQUIRES: none
    // MODIFIES: this
    // EFFECTS: Adds the given piece of clothing to myWardrobe as well as adding the clothing price to amount
    public void addClothing(Clothing clothing) {
        myWardrobe.add(clothing);
        amount += clothing.getPrice();
        setChanged();
        notifyObservers(new Event(clothing.getDescription() + " added to wardrobe"));
    }

    // REQUIRES: none
    // MODIFIES: none
    // EFFECTS: Returns the list of clothing in the wardrobe
    public ArrayList<Clothing> viewClothing() {
        return myWardrobe;
    }

    // REQUIRES: given trait must be one of "Colour", "Brand", "Material", "Size" or "Description"
    //           descriptionOfTrait cannot be "" and must describe a colour, brand, etc. already in myWardrobe
    // MODIFIES: none
    // EFFECTS: Returns a list of clothing items that fit the given descriptionOfTrait
    public ArrayList<Clothing> viewFilteredClothing(String trait, String descriptionOfTrait) {
        ArrayList<Clothing> result;
        result = new ArrayList<>();
        for (Clothing clothing: myWardrobe) {
            if ((trait.equals("Colour")) & (descriptionOfTrait.equals(clothing.getColour()))) {
                result.add(clothing);
            } else if ((trait.equals("Brand")) & descriptionOfTrait.equals(clothing.getBrand())) {
                result.add(clothing);
            } else if ((trait.equals("Material")) & (descriptionOfTrait.equals(clothing.getMaterial()))) {
                result.add(clothing);
            } else if ((trait.equals("Size")) & (descriptionOfTrait.equals(clothing.getSize()))) {
                result.add(clothing);
            } else if ((trait.equals("Description")) & (descriptionOfTrait.equals(clothing.getDescription()))) {
                result.add(clothing);
            }
        }
        return result;
    }

    // REQUIRES: min and max must both be >= 0.00 and have two decimal places
    // MODIFIES: none
    // EFFECTS: Returns a list of clothing items that are both greater than or equal to the min value, and is less
    // than or equal to the max value. The interval is [min, max]
    public ArrayList<Clothing> viewClothingByPrice(double min, double max) {
        ArrayList<Clothing> result;
        result = new ArrayList<>();
        for (Clothing clothing: myWardrobe) {
            if ((min <= clothing.getPrice()) & (clothing.getPrice() <= max)) {
                result.add(clothing);
            }
        }
        return result;
    }

    // REQUIRES: none
    // MODIFIES: this
    // EFFECTS: Removes given piece of clothing from myWardrobe
    public void removeClothing(Clothing clothing) {
        myWardrobe.remove(clothing);
        setChanged();
        notifyObservers(new Event(clothing.getDescription() + " removed"));
    }

    public double getAmount() {
        return amount;
    }

    // EFFECTS: Writes a Wardrobe to JSON data
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("amount", amount);
        json.put("clothing", clothingToJson());
        return json;
    }

    // EFFECTS: Writes the list of Clothing to JSON data
    private JSONArray clothingToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Clothing clothing : myWardrobe) {
            jsonArray.put(clothing.toJson());
        }

        return jsonArray;
    }
}
