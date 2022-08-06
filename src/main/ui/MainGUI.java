package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;

import model.Clothing;
import model.Wardrobe;
import model.Event;
import model.EventLog;

import persistence.JsonWriter;
import persistence.JsonReader;

import java.io.FileNotFoundException;
import java.io.IOException;

// Main GUI class to display the WardrobeApp
public class MainGUI extends JPanel implements ListSelectionListener {
    private JList list;
    private DefaultListModel listModel;

    private static final String ADD_BUTTON = "Add";
    private static final String REMOVE_BUTTON = "Remove";
    private static final String LOAD_BUTTON = "Load";
    private static final String SAVE_BUTTON = "Save";
    private static final String JSON_STORAGE = "./data/wardrobe.json";
    private static final String SPLASH_IMAGE_URL = "./data/c210.jpg";

    private JButton removeButton;
    private JButton addButton;
    private JButton loadButton;
    private JButton saveButton;
    private JTextField colour;
    private JTextField price;
    private JTextField brand;
    private JTextField material;
    private JTextField size;
    private JTextField description;
    private List<JTextField> listOfTexts;

    private Wardrobe wardrobe;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // Constructor
    // EFFECTS: Constructs the main GUI
    public MainGUI() {
        super(new BorderLayout());
        listModel = new DefaultListModel();
        listModel.addElement("Welcome to your wardrobe!");
        listOfTexts = new ArrayList<>();
        wardrobe = new Wardrobe();
        jsonWriter = new JsonWriter(JSON_STORAGE);
        jsonReader = new JsonReader(JSON_STORAGE);

        list = new JList(listModel);
        initializeList(list);
        JScrollPane listScrollPane = new JScrollPane(list);

        initializeButtons();

        AddListener addListener = new AddListener(addButton);
        makeButton(addButton, ADD_BUTTON, addListener);
        addButton.setEnabled(false);

        makeButton(removeButton, REMOVE_BUTTON, new RemoveListener());

        makeButton(loadButton, LOAD_BUTTON, new LoadListener());

        makeButton(saveButton, SAVE_BUTTON, new SaveListener());

        makeTextFields();
        addListeners(addListener);
        JPanel buttonPane = new JPanel();
        createPanel(buttonPane, listScrollPane);
    }

    // MODIFIES: this
    // EFFECTS: initializes all the buttons
    public void initializeButtons() {
        addButton = new JButton(ADD_BUTTON);
        removeButton = new JButton(REMOVE_BUTTON);
        loadButton = new JButton(LOAD_BUTTON);
        saveButton = new JButton(SAVE_BUTTON);
    }

    // MODIFIES: this
    // EFFECTS: Gives the buttons the functionality needed to run the app
    public void makeButton(JButton button, String label, ActionListener actionListener) {
        button.setActionCommand(label);
        button.addActionListener(actionListener);
    }

    // MODIFIES: this
    // EFFECTS: Initializes the list to display the Clothing
    public void initializeList(JList jlist) {
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlist.setSelectedIndex(0);
        jlist.addListSelectionListener(this);
        jlist.setVisibleRowCount(15);
    }

    // MODIFIES: this
    // EFFECTS: Creates the panel with buttons, text fields, and the list panel
    public void createPanel(JPanel buttonPane, JScrollPane scrollPane) {
        buttonPane.setLayout(new BoxLayout(buttonPane,
                BoxLayout.LINE_AXIS));
        buttonPane.add(removeButton);
        buttonPane.add(Box.createHorizontalStrut(10));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(10));

        addTextFields(buttonPane);

        buttonPane.add(addButton);
        buttonPane.add(loadButton);
        buttonPane.add(saveButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);

    }

    // MODIFIES: this
    // EFFECTS: Creates all the text fields to enter in clothing traits
    public void makeTextFields() {
        this.colour = new JTextField(10);
        listOfTexts.add(colour);
        this.price = new JTextField(5);
        listOfTexts.add(price);
        this.brand = new JTextField(15);
        listOfTexts.add(brand);
        this.material = new JTextField(10);
        listOfTexts.add(material);
        this.size = new JTextField(3);
        listOfTexts.add(size);
        this.description = new JTextField(10);
        listOfTexts.add(description);
    }

    // MODIFIES: this
    // EFFECTS: Adds the buttons to the button pane
    public void addTextFields(JPanel buttonPane) {
        buttonPane.add(new JLabel("Colour:"));
        buttonPane.add(colour);

        buttonPane.add(new JLabel("Price:"));
        buttonPane.add(price);

        buttonPane.add(new JLabel("Brand:"));
        buttonPane.add(brand);

        buttonPane.add(new JLabel("Material:"));
        buttonPane.add(material);

        buttonPane.add(new JLabel("Size:"));
        buttonPane.add(size);

        buttonPane.add(new JLabel("Description:"));
        buttonPane.add(description);
    }

    // MODIFIES: this
    // EFFECTS: Adds Listeners/functionality to all the text boxes
    public void addListeners(AddListener addListener) {
        for (JTextField textfield : listOfTexts) {
            textfield.addActionListener(addListener);
            textfield.getDocument().addDocumentListener(addListener);
        }
    }

    // RemoveListener class, gives the Remove button its functionality and helps remove clothing from the wardrobe
    class RemoveListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: Removes the selected clothing from list. If there are no more clothing to remove, then the
        // remove button is set to false and cannot be pressed
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            listModel.remove(index);
            List<Clothing> clothingList = wardrobe.viewClothing();
            if (index != 0) {
                wardrobe.removeClothing(clothingList.get(index - 1));
            } else {
                wardrobe.removeClothing(clothingList.get(index));
            }

            int size = listModel.getSize();

            if (size == 0) {
                removeButton.setEnabled(false);
            } else {
                if (index == listModel.getSize()) {
                    index--;
                }

                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    // AddListener class, gives the addButton functionality and allows it to add clothing to the wardrobe.
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        // Constructor
        // EFFECTS: Constructor that initializes the button
        public AddListener(JButton button) {
            this.button = button;
        }

        // MODIFIES: this
        // EFFECTS: Adds the clothing to the list. Moreover, if the current index is == -1, then it will be set to 0
        // to keep it within bounds. Adding clothing also clears all the text fields used to create clothing.
        public void actionPerformed(ActionEvent e) {
            checkEmptyTexts();

            int index = list.getSelectedIndex();
            if (index == -1) {
                index = 0;
            } else {
                index++;
            }

            Clothing newClothing = createClothing();
            String displayString = displayClothing(newClothing);

            listModel.addElement(displayString);
            wardrobe.addClothing(newClothing);

            resetClothing();

            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        // MODIFIES: this
        // EFFECTS: clears each clothing description text field
        public void resetClothing() {
            for (JTextField textfield : listOfTexts) {
                textfield.requestFocusInWindow();
                textfield.setText("");
            }
        }

        // EFFECTS: Checks if any text field is empty. If any of them are, then a beep will sound and the addition
        // of clothing cannot continue.
        public void checkEmptyTexts() {
            for (JTextField textfield: listOfTexts) {
                String name = textfield.getText();

                if (name.equals("")) {
                    Toolkit.getDefaultToolkit().beep();
                    textfield.requestFocusInWindow();
                    textfield.selectAll();
                    return;
                }
            }
        }

        // EFFECTS: Returns a piece of clothing given the inputs for the text fields
        public Clothing createClothing() {
            String clothingColour = colour.getText();
            String clothingPrice = price.getText();
            String clothingBrand = brand.getText();
            String clothingMaterial = material.getText();
            String clothingSize = size.getText();
            String clothingDescription = description.getText();

            Clothing newClothing = new Clothing(clothingColour, Double.parseDouble(clothingPrice), clothingBrand,
                    clothingMaterial,
                    clothingSize, clothingDescription);

            return newClothing;
        }

        // MODIFIES: this
        // EFFECTS: Updates the button to make sure it is enabled when valid. ie. if all text fields are filled out
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        // MODIFIES: this
        // EFFECTS: Updates the button to make sure it is disabled when invalid. ie. if a text field is not filled in
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        // MODIFIES: this
        // EFFECTS: Updates the button to make sure it is enabled when going from an invalid state to a valid one.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        // MODIFIES: this
        // EFFECTS: Enables the button if it is not already button.
        public void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        // MODIFIES: this
        // EFFECTS: Disables the button if it finds empty text fields
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    // LoadListener class, helping the program load previous wardrobe states into the program
    class LoadListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: loads the wardrobe from the file given an action (like a button click)
        public void actionPerformed(ActionEvent e) {
            loadWardrobe();
        }

        // MODIFIES: this
        // EFFECTS: loads the wardrobe from the file
        private void loadWardrobe() {
            try {
                wardrobe = jsonReader.read();
                for (Clothing clothing : wardrobe.viewClothing()) {
                    listModel.addElement(displayClothing(clothing));
                }
                loadButton.setEnabled(false);
            } catch (IOException e) {
                System.out.println("Unable to read from the file named " + JSON_STORAGE);
            }
        }
    }

    // SaveListener class, helps save the current wardrobe state to file
    class SaveListener implements ActionListener {
        // MODIFIES: this
        // EFFECTS: saves the current wardrobe to the file given an action (like a button click)
        public void actionPerformed(ActionEvent e) {
            saveWardrobe();
        }

        // MODIFIES: this
        // EFFECTS: saves the current wardrobe to the file
        private void saveWardrobe() {
            try {
                jsonWriter.open();
                jsonWriter.write(wardrobe);
                jsonWriter.close();
            } catch (FileNotFoundException e) {
                System.out.println("Unable to write to the file named " + JSON_STORAGE);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: If list index is == -1, or out of bounds, the removeButton will be disabled. Else, it will enable it
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (list.getSelectedIndex() == -1) {
                removeButton.setEnabled(false);
            } else {
                removeButton.setEnabled(true);
            }
        }
    }

    // EFFECTS: Creates the visual GUI and displays log upon closing
    private static void createAndShowGUI() {
        JWindow splashFrame = new JWindow();
        ImageIcon image = new ImageIcon(SPLASH_IMAGE_URL);
        JLabel imageLabel = new JLabel(image);
        imageLabel.setIcon(image);
        splashFrame.add(imageLabel);
        splashFrame.setBounds(500, 150, 600, 600);
        splashFrame.pack();
        splashFrame.repaint();
        splashFrame.setVisible(true);

        JFrame frame = new JFrame("Wacky Wardrobe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = new MainGUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                displayLog(EventLog.getInstance());
                System.exit(0);
            }
        });
    }

    // EFFECTS: displays each of the events in the eventlog
    private static void displayLog(EventLog eventlog) {
        for (Event event : eventlog) {
            System.out.println(event.toString());
        }
    }

    // EFFECTS: Returns a piece of clothing formatted into a string
    public String displayClothing(Clothing clothing) {
        return ("A " + clothing.getDescription() + ". It's " + clothing.getColour() + " and cost $"
                + String.valueOf(clothing.getPrice()) + ". From " + clothing.getBrand() + ", made from "
                + clothing.getMaterial() + " and size " + clothing.getSize());
    }

    // EFFECTS: Runs the main GUI function
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            // EFFECTS: Runs the GUI application, then prints the event log when it has been exited
            public void run() {
                createAndShowGUI();

                EventLog log = EventLog.getInstance();
                for (Event event: log) {
                    System.out.println(event.toString());
                }
            }
        });
    }
}


