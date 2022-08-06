package persistence;

import model.Wardrobe;
import org.json.JSONObject;


import java.io.*;

// Represents a writer that can write a JSON representation of wardrobe to files
public class JsonWriter {
    private static final int TAB = 5;

    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer, throws FileNotFoundException if destination file cannot be opened to write in
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writers a JSON representation of the wardrobe to file
    public void write(Wardrobe wardrobe) {
        JSONObject json = wardrobe.toJson();
        makeNewString(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes the string to the file
    private void makeNewString(String json) {
        writer.print(json);
    }
}
