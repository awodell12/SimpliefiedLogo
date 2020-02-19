# DESIGN PLAN
names: Austin Odell, James Rumsey, Samuel Thompson, Cary Shindell

netids: awo6, jpr31, stt13, 

## Introduction

#####
We are trying to read in and parse user commands, which can perform a variety of functions on a turtle, which we will display. 
We want to have a visualizer that allows you to modify the display based on what the user enters, regardless of the code syntax.
We want a back end to store the state of turtles and code regardless of how they are drawn, and parse commands that return outputs to the visualizer to be interpreted and displayed.
Closed: 
* individual command implementations
* parsing implementation
* individual GUI components
* buttons and their actions
Open:
* new commands
* new ways to visualize

## Overview

The four APIs we intend to create are:
* Visualizer - Internal
* Visualizer - External
* Back End - Internal
* Back End - External


#### Back End
* Knows turtle position and heading (and pen status, purely in back end)
* Paths list/colors
* Variable names/values (independent of scripts)
* User defined commands (receiving them is external, interpreting is internal. They have a distinct name so that they can be linked between the front and back end in case we need to edit/remove them)
* Takes in a string and outputs a list of CommandResults objects. These will be looped through by the controller and interpreted one by one. Thus a user defined command will be split into multiple basic commands.

#### Front End
* Only ONE channel of information being passed from viewer to controller: a queue of strings, which can be either command text or special instructions (created by buttons)
* When a script is run, its commands are added to the queue
* When a button is pressed, it is either handled internally in the viewer or turned into a "command" and added to the TOP of the queue


#### Front End Internal API
* Visualizer:
    * Owns TurtleView objects
    * Owns CommandBox object
    * Owns History object
    * Owns buttons and root, error box, variable box, user defined commands box
* TurtleView:
    * constructor
    * set turtle pos
    * set turtle heading
    * set image
    * owns Path objects
    * addPath: passes in 2 Points (start, end) and a color
    * clearPath: 
* CommandBox:
    * constructor
    * get contents
    * clear contents
* History
    * constructor
    * add entry
    * clear entries
* Path
    * constructor

#### Front End External API
* Visualizer:
    * constructor
    * pop first entry from command queue
    * inerpret result of CommandResults object, update everything that is updateable

#### Back End External API
* BackEnd:
    * Back End constructor
    * parse command

#### Back End Internal API
* BackEnd:
    * has turtle object, variable map, user defined commands list
* VariableMap
    * could be a map, or two arrays
    * add to variable list
    * get from variable list
* User defined commands list
    * can be a map of names to text, or 2 arrays
    * add to user defined commands list
    * get from user defined commands list
* Turtle (stores the state of the turtle)
    * constructor
    * get pos
    * set pos
    * move forward
    * move backward
    * turn
    * set heading
    * get heading
    * get pen status
    * set pen status
* Command (abstract)
    * constructor
    * execute (pass in Turtle object, variables, user defined commands)
        * can modify the state (i.e. those 3 objects that are passed in)

## User Interface

See diagram below:

![](https://i.imgur.com/qiKJc6A.jpg)
* Color dropdown: Selects the path color for the turtle
* Language dropdown: Select language for view and SLogo commands
* Background dropdown: Select the background color of the TurtleView. 
* Set Turtle Image Button: Change the image for the turtle sprite from an image file. 
* Help: Display a help screen with the lost of valid commands
* Run: Take the text in the CommandBox and pass it to the CommandParser to turn it into Commands
* Clear: Deletes the text in the CommandBox
* CommandBox: A textbox where the user can type in Slogo commands. This is used to define variables and user-defined commands as well. The user can execute the "script" in the box by pressing the run botton or delete the text by pressing the clear button. 


## Design Details 
This section describes each API introduced in the Overview in detail (as well as any other sub-components that may be needed but are not significant to include in a high-level description of the program). Describe how each API supports specific features given in the assignment specification, what resources it might use, how it is intended to be used, and how it could be extended to include additional requirements (from the assignment specification or discussed by your team). Finally, justify the decision to create each class introduced with respect to the design's key goals, principles, and abstractions. This section should go into as much detail as necessary to cover all your team wants to say.

Front End External API
* Visualizer:
    * Constructor
    * Pop first entry from command queue
        * React to the text and update the model
        * Choose a language in which slogo commands are understood (with a button/menu)
    * Interpret result of CommandResults object, update everything that is updateable
        * React to the text and update the model
        * See the results of the turtle executing commands displayed visually 
        * See resulting errors in user friendly way
        * see user defined commands currently available

Front End Internal API
* Visualizer:
    * Features:
        * Allows us to enter in text and see it
        * See resulting errors in user friendly way
        * Set the pen color (with a button)
        * See commands previously run
        * See user defined commands currently available
        * Choose a language in which SLogo commands are understood (with a drop-down menu)
        * Get help about available commands
* TurtleView:
    * Constructor
        * See the results of the turtle executing commands displayed visually
    * set turtle pos
    * set turtle heading
    * set image
        * set an image to use for the turtle
    * set background color
        * Set the background color of turtle viewing area
    * owns Path objects
    * addPath: passes in 2 Points (start, end) and a color
    * clearPath
        * Removes all paths in the turtle viewing area
* CommandBox:
    * constructor
    * get contents
        * React to the text and update the model
    * clear contents
* History
    * constructor
        * see commands previously run
    * add entry
    * clear entries
* Path
    * constructor


#### Back End External API
* BackEnd:
    * Back End constructor
    * parse command
        * Interpret text and update the model accordingly

#### Back End Internal API
* BackEnd:
    * has turtle object, variable map, user defined commands list
    * Features:
        * Update the model (specifically the turtle)
        * Add to and access the variables available in the environment
        *  Add to, access, and use the user-defined commands available
* VariableMap
    * could be a map, or two arrays
    * add to variable list
    * get from variable list

* User defined commands list
    * can be a map of names to text, or 2 arrays
    * add to user defined commands list
    * get from user defined commands list
* Turtle (stores the state of the turtle)
    * constructor
    * get pos
    * set pos
    * move forward
    * move backward
    * turn
    * set heading
    * get heading
    * get pen status
    * set pen status
* Command (abstract)
    * constructor
    * execute (pass in Turtle object, variables, user defined commands)

Justifying decision to create each class:
* Visualizer
    * This is a separate class rather than having the controller interact directy with the components, because we want a single external API to handle demands for information about the model, and a single internal API to handle managing the model, enclosing the multiple different views/panes of the display
    * This also allows us to have multiple models at once
    * The purpose of the controller is only to manage and communicate, not deal directly with the model (we want that to be encapsulated)
* TurtleView
    * Stays focused by only being used to show the side effects of SLogo on the turtle.
    * It encapsulates the view of the turtle from the other possible views, allowing a display to use a costumized set of views
* CommandBox
    * Handles the specific role of displaying the command box and retrieving its contents
    * It is unlike other buttons and view components
    * Other view components don't need to know about it
* History
    * It is a unique component and we want to be able to modify how it's displayed in the future without touching the Visualizer class (closed)
*

* Path 
    * Want it to be flexible to further information that needs to be added about a path
        * for example, a path could have different line types, or wrap around the view
* Back End 
    * A public facing interface for the entire back-end
    * We want to regiment what is controllable
    * Also makes it so you could switch out any part of the model
* Variable Map
    * Wrap a map data structure for user-defined variables
    * Pass its info through a commandResult to the front end
* User Commands Map
    * Wrap a map for user-defined commands
* Command
    * The unit of everything that can happen
    * Needs to be extendable
* Turtle
    * Want to keep the state of the turtle separate
    * Turtle's have behavior in the back-end
* Controller
    * Serves as a bridge between the model and view, so that no dependencies exist between them
    * Allows for multiple models/views simultaneously

## APIs as Code
### Use cases
1. The user types 'fd 50' in the command window, and sees the turtle move in the display window leaving a trail, and the command is added to the environment's history.
Note, it is not necessary to understand exactly how parsing works in order to complete this example, just what the result of parsing the command will be.
2. The user sets the pen's color using the UI so subsequent lines drawn when the turtle moves use that color.
3. The user types 'make :dummy 50 abcdef 123456 fd 90' (while abcdef is not defined as a user-made instruction) and presses RUN. The variable view shows 'dummy = 50' and the
error box shows 'Don't know how to abcdef'.
* Visualizer.popCommandQueue() is run to get the user-entered String from the Visualizer's command box.
* The String result is passed into BackEnd.parseScript, which produces a 'make' command that executes and creates a variable 'dummy' in
VariableMap that maps to the number 50 and puts that information into a CommandResult.
BackEnd recognizes that the second command is undefined and adds a CommandResult with error message "Don't know how to abcdef", then returns
both CommandResults as a List.
* Visualizer interprets each CommandResult using interpretResult(), first telling the VariableView to add the new variable, and then
* telling the error text box to display the error message in the second CommandResult.
4. The user changes the language from English to French by selecting French in the languages drop down menu. 
* The Visualizer notifies whoever is listening that the languages drop down menu has been activated, and passes the value of the drop down menu ("French").
* The value is sent to the Model either as a 'pseudocommand' starting with some unique character like "&" or by being passed into a BackEnd.setLanguage() method.
* The model transforms the String into a filename pointing to a resource properties file with French instructions enumerated, and uses this for further 
command processing.

## Design Considerations

#### Use MVC in JavaFx
Should some buttons be created/handled by the controller, or should all buttons be handled by the viewer (in this case the buttons that affect the model would be turned into commands, and the only way the controller interacts with/listens on in the viewer is the command queue).

#### CommandResult class
Do we run commands all at once or one at a time? Right now a script is processed in its entirety in the back end, and then the command results are given to the controller as a list so that the viewer can then animate the results of each individual command. But this means the model and viewer are briefly out of sync. Do we instead want viewer to update every time a single command is executed in the backend?

Alternatively, should we do away with the CommandResult class entirely and have Controller implement a ModelListener that listens for when the model is updated, and just passes those parameters to the external visualizer update method?

#### Pseudocommands
Treating everything that can effect the model as a command
Buttons produce their own pseudo-commands to communicate with the model

pros: Only one way to communicate between front and back end 

cons: Could end up with a lot of conditionals to figure out in the back-end how to handle all of these new commmands. 

## Team Responsibilities

#### Front - End


#### Back - End


