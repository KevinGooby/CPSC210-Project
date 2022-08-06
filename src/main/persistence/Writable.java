package persistence;

import org.json.JSONObject;

// a public interface allowing a class to be writable to JSON data
public interface Writable {
    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
