package persistence;

import model.Clothing;
import model.Wardrobe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// This class represents a reader that reads the wardrobe from JSON data stored in files
public class JsonReader {
    private String source;

    // EFFECTS: constructs a reader to read from source files
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads wardrobe from file and returns it;
    // throws the IOException if an error occurs reading data from file
    public Wardrobe read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWardrobe(jsonObject);
    }

    // EFFECTS: reads source file as a string then returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses the wardrobe from JSON object and returns it
    private Wardrobe parseWardrobe(JSONObject jsonObject) {
        double amount = jsonObject.getDouble("amount");
        Wardrobe wardrobe = new Wardrobe();
        addMultipleClothingJson(wardrobe, jsonObject);
        return wardrobe;
    }

    // MODIFIES: wardrobe
    // EFFECTS: parses multiple clothing from JSON object and adds them to the wardrobe
    private void addMultipleClothingJson(Wardrobe wardrobe, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("clothing");
        for (Object json: jsonArray) {
            JSONObject nextClothing = (JSONObject) json;
            addClothingJson(wardrobe, nextClothing);
        }
    }

    // MODIFIES: wardrobe
    // EFFECTS: parses singular clothing from JSON object and adds it to the wardrobe
    private void addClothingJson(Wardrobe wardrobe, JSONObject jsonObject) {
        String colour = jsonObject.getString("colour");
        double price = jsonObject.getDouble("price");
        String brand = jsonObject.getString("brand");
        String material = jsonObject.getString("material");
        String size = jsonObject.getString("size");
        String description = jsonObject.getString("description");

        Clothing clothing = new Clothing(colour, price, brand, material, size, description);

        wardrobe.addClothing(clothing);
    }
}
