package ui; 
// The Wardrobe app class that creates a new Wardrobe and includes a Scanner to read user input

import java.util.ArrayList;
import java.util.Scanner;
import model.Clothing;
import model.Wardrobe;
import persistence.JsonWriter;
import persistence.JsonReader;

import java.io.FileNotFoundException;
import java.io.IOException;

public class WardrobeApp {
    private static final String JSON_STORAGE = "./data/wardrobe.json";
    private Wardrobe myWardrobe;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // Constructor
    // REQUIRES: none
    // MODIFIES: none
    // EFFECTS: Runs the wardrobe app
    public WardrobeApp() {
        runWardrobeApp();
    }

    // REQUIRES: none
    // MODIFIES: this
    // EFFECTS: Receives user input and process how they wish to use the application
    public void runWardrobeApp() {
        boolean keepRunning = true;
        String command = null;

        initialize();

        while (keepRunning) {
            displayMainScreen();
            command = input.next();
            command = command.toUpperCase();

            if (command.equals("Q")) {
                keepRunning = false;
            } else {
                interpretCommands(command);
            }
        }

        System.out.println("Thanks! Bye for now.");
    }

    // REQUIRES: none
    // MODIFIES: this
    // EFFECTS: Initializes the wardrobe, as well as the Scanner to read the user input
    public void initialize() {
        myWardrobe = new Wardrobe();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORAGE);
        jsonReader = new JsonReader(JSON_STORAGE);
    }

    // REQUIRES: None
    // MODIFIES: None
    // EFFECTS: Displays/prints out a list of commands that the user can choose from
    public void displayMainScreen() {
        System.out.println("\nHello! Welcome to your wacky wardrobe");
        System.out.println("\tPress A to add a new clothing item");
        System.out.println("\tPress V to view your wardrobe");
        System.out.println("\tPress F to filter and then view your wardrobe");
        System.out.println("\tPress R to remove a clothing item to your wardrobe");
        System.out.println("\tPress M to view how much money you have spent on the clothes in your wardrobe");
        System.out.println("\tPress S to save your wardrobe to file");
        System.out.println("\tPress L to load your wardrobe from the file");
        System.out.println("\tPress Q to quit the app");
    }

    // REQUIRES: none
    // MODIFIES: this
    // EFFECTS: processes the user's answer, and prompts different actions/functions based on their input
    public void interpretCommands(String command) {
        if (command.equals("A")) {
            doAddToWardrobe();
        } else if (command.equals("V")) {
            doViewWardrobe();
        } else if (command.equals("F")) {
            doViewFilteredWardrobe();
        } else if (command.equals("R")) {
            doRemoveFromWardrobe();
        } else if (command.equals("M")) {
            doViewMoneySpent();
        } else if (command.equals("S")) {
            saveWardrobe();
        } else if (command.equals("L")) {
            loadWardrobe();
        } else {
            System.out.println("Sorry, seems like that's invalid. Please try again :)");
        }
    }

    // REQUIRES: Colour, brand, material, size, and description cannot be empty string "". Description cannot be more
    //           than one word. Price must be >= 0.00 and have two decimal places.
    // MODIFIES: this
    // EFFECTS: Prompts the user for the clothing's colour, price, brand, material, size, and description. This
    //          clothing item is then added to myWardrobe
    public void doAddToWardrobe() {
        System.out.println("What colour is this clothing item?");
        String colour = input.next();
        System.out.println("How much did it cost? Price to two decimal places 0.00");
        double cost = input.nextDouble();
        System.out.println("What brand is it?");
        String brand = input.next();
        System.out.println("What material is it made from (in general?)");
        String material = input.next();
        System.out.println("What size is it? Something like S, M, L, etc.");
        String size = input.next();
        System.out.println("Can you give a one-word description?");
        String description = input.next();
        Clothing newClothing = new Clothing(colour, cost, brand, material, size, description);
        myWardrobe.addClothing(newClothing);
        System.out.println("What a snazzy new look!");
    }

    // REQUIRES: none
    // MODIFIES: none
    // EFFECTS: Displays each item in the wardrobe
    public void doViewWardrobe() {
        ArrayList<Clothing> listOfClothing = myWardrobe.viewClothing();
        displayListOfClothing(listOfClothing);
    }

    // REQUIRES: Given Arraylist<Clothing> cannot be empty
    // MODIFIES: none
    // EFFECTS: Displays each item in the listOfClothing with the format:
    //          "A (description). It's (colour) and cost ($0.00 price). From (brand), made from (material) and
    //          size (size)"
    public void displayListOfClothing(ArrayList<Clothing> listOfClothing) {
        for (Clothing clothing : listOfClothing) {
            displayClothing(clothing);
        }
    }

    // REQUIRES: None
    // MODIFIES: None
    // EFFECTS: Displays a clothing item with the format:
    //          "A (description). It's (colour) and cost ($ price). From (brand), made from (material) and
    //           size (size)"
    public void displayClothing(Clothing clothing) {
        System.out.println("A " + clothing.getDescription() + ". It's " + clothing.getColour() + " and cost $"
                + String.valueOf(clothing.getPrice()) + ". From " + clothing.getBrand() + ", made from "
                + clothing.getMaterial() + " and size " + clothing.getSize());
    }

    // REQUIRES: none
    // MODIFIES: none
    // EFFECTS: Prompts the user to choose to filter by traits or to filter by price
    public void doViewFilteredWardrobe() {
        String reply = "";

        while (!(reply.equals("T") || (reply.equals("P")))) {
            System.out.println("Would you like to filter by traits or price? Please reply either T for traits or "
                    + "P for price!");
            reply = input.next();
            reply = reply.toUpperCase();
        }

        if (reply.equals("T")) {
            doFilterByTraits();
        } else {
            doFilterbyPrice();
        }
    }

    // REQUIRES: none
    // MODIFIES: none
    // EFFECTS: Returns a list of clothing items that meet the inputted trait and description
    public void doFilterByTraits() {
        ArrayList<Clothing> listOfClothing;
        System.out.println("What would you like to search by? Colour, Brand, Material, Size, or Description?");
        String trait = input.next();
        System.out.println("What specific description? For example, if you chose Colour, type Blue here");
        String description = input.next();
        listOfClothing = myWardrobe.viewFilteredClothing(trait, description);
        displayListOfClothing(listOfClothing);
    }

    // REQUIRES: min and max inputs >= 0.00
    // MODIFIES: none
    // EFFECTS: Returns a list of clothing items that fall within the range of inputted min and max values
    public void doFilterbyPrice() {
        ArrayList<Clothing> listOfClothing;
        System.out.println("Please input the minimum value. Format 0.00");
        double min = input.nextDouble();
        System.out.println("PLease input the maximum value. Format 0.00");
        double max = input.nextDouble();
        listOfClothing = myWardrobe.viewClothingByPrice(min, max);
        displayListOfClothing(listOfClothing);
    }

    // REQUIRES: input for index number > 0 and as a whole number
    // MODIFIES: this
    // EFFECTS: Presents the user with a list of all clothes items. Then, the user can remove the item at the nth place
    //          in myWardrobe (where n is what the user inputs). User inputs 3 = removes third clothing item
    public void doRemoveFromWardrobe() {
        doViewWardrobe();
        System.out.println("Please select an item from remove by choosing its place number. If you wish to remove"
                + " the first item, enter 1");
        int index = input.nextInt();
        ArrayList<Clothing> listOfClothing = myWardrobe.viewClothing();
        Clothing clothingToRemove = listOfClothing.get(index - 1);
        listOfClothing.remove(clothingToRemove);
    }

    // REQUIRES: none
    // MODIFIES: none
    // EFFECTS: Presents the total amount of money spent on clothes inside the wardrobe
    public void doViewMoneySpent() {
        double amountSpent = myWardrobe.getAmount();
        System.out.println("In this wardrobe is $" + String.valueOf(amountSpent) + " worth of clothes");
    }

    // EFFECTS: saves the current wardrobe to the file
    private void saveWardrobe() {
        try {
            jsonWriter.open();
            jsonWriter.write(myWardrobe);
            jsonWriter.close();
            System.out.println("We have saved your current wardrobe state to the file named" + JSON_STORAGE);
            System.out.println("The state which has been saved is: ");
            doViewWardrobe();
            doViewMoneySpent();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to the file named " + JSON_STORAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads the wardrobe from the file
    private void loadWardrobe() {
        try {
            myWardrobe = jsonReader.read();
            System.out.println("We have loaded the last saved state of your wardrobe. Here is the wardrobe: ");
            doViewWardrobe();
            doViewMoneySpent();
        } catch (IOException e) {
            System.out.println("Unable to read from the file named " + JSON_STORAGE);
        }
    }
}


