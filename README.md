# My personal project!

### For CPSC 210 - y2x7s

Hello there!

For my CPSC 210 project I wanted to create a **wardrobe manager app** and to keep track of your clothing!

**What will the application do?**

The application will keep track of one's clothing that they have in their wardrobe as well as how much they
have spent on clothing in their wardrobe. However, in this case there will be no "maximum capacity" because
there may be many small items added to the wardrobe that, while taking up space in this app's wardrobe,
may not actually take up too much space in reality.

The clothing items will have various traits (fields) such as:
- Colour
- Price
- Brand
- Material
- Size 
- Description (One word)

Users will be able to add, remove, and look up clothing in their wardrobe based on these traits as well. For example,
only blue pieces of clothing or only clothes from Hugo Boss.

**Who will use this application?**

At this point, the application will be used by myself and to help me organize my wardrobe. However, it can also
be used by people who want to keep track of their own wardrobe!

**Why is this project of interest to you?**

I am interested in this project because I have always loved fashion, but I recognize that it can be hard
to keep track of all your clothes. Sometimes we buy a blue piece of clothing even though we have so many 
blue clothes already!
## User stories
- As a user, I want to add a new clothing item to my wardrobe
- As a user, I want to view all the clothing in my wardrobe
- As a user, I want to filter, then view the clothing in my wardrobe by its traits: colour, material, brand, size
and description
- As a user, I want to filter clothing by their price by setting a range with minimum and maximum endpoints
- As a user, I want to be able to remove a piece of clothing from my wardrobe (lost, donated, etc.)
- As a user, I want to view how much I have spent on clothing in total


In terms of data persistence:

- As a user, I want to save the entire state of my wardrobe so I can see all of it even after I've closed it. This 
includes the clothes in my wardrobe as well as the amount spent.
- As a user, I want to be able to reload my wardrobe and to see the clothing that I have added. I wish to perform 
operations (such as removing) on clothing that I added in a previous session.

Citation:

Parts of this project have been modelled and adapted from the "JsonSerializationDemo" Java application written by 
the wonderful CPSC 210 teaching team at the University of British Columbia.

**Phase 4: Task 2**

Example logs:

Fri Apr 01 13:48:10 PDT 2022

Hoodie added to wardrobe

Fri Apr 01 13:49:00 PDT 2022

Sweater added to wardrobe

Fri Apr 01 13:49:04 PDT 2022

Hoodie removed

Fri Apr 01 13:50:12 PDT 2022

Hat added to wardrobe

**Phase 4: Task 3**

**Important Note: In the UML Class Diagram, all of the "Listener" classes are inside the 
MainGUI class**

Looking at my UML Class diagram, I would try and refactor the WardrobeApp and MainGUI classes
so that the user can be presented with the option to use either the GUI verison or to use the console-based version.
I could also implement this with an abstract class (for example) to note the similarities between the two
UI applications.

Currently, the WardrobeApp and MainGUI classes both include associations with Wardrobe, JsonReader and JsonWriter. 
To remove these duplicities, I could use (for example) an abstract class with protected
fields to address these similarities.

Moreover, another change that I would like to make is to include more Exceptions within my code.
Looking at many of the REQUIRES/MODIFIES/EFFECTS clauses, some methods rely on the REQUIRES clause. However,
to make the code more **robust**, I could handle and execute code based on these Exceptions instead of 
relying on the REQUIRES clauses.



 


