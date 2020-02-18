# DESIGN PLAN
names
netids

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

## Design Details 
This section describes each API introduced in the Overview in detail (as well as any other sub-components that may be needed but are not significant to include in a high-level description of the program). Describe how each API supports specific features given in the assignment specification, what resources it might use, how it is intended to be used, and how it could be extended to include additional requirements (from the assignment specification or discussed by your team). Finally, justify the decision to create each class introduced with respect to the design's key goals, principles, and abstractions. This section should go into as much detail as necessary to cover all your team wants to say.

Front End External API
* Visualizer:
    * constructor
    * pop first entry from command queue
        * React to the text and update the model
        * choose a language in which slogo commands are understood (with a button/menu)
    * inerpret result of CommandResults object, update everything that is updateable
        * React to the text and update the model
        * See the results of the turtle executing commands displayed visually 
        * See resulting errors in user friendly way
        * see user defined commands currently available

Front End Internal API
* Visualizer:
    * features:
        * Allows us to enter in text and see it
        * See resulting errors in user friendly way
        * set the pen color (with a button)
        * see commands previously run
        * see user defined commands currently available
        * choose a language in which slogo commands are understood (with a button/menu)
        * get help about available commands
* TurtleView:
    * constructor
        * See the results of the turtle executing commands displayed visually
    * set turtle pos
    * set turtle heading
    * set image
        * set an image to use for the turtle
    * set background color
        * set the background color of turtle viewing area
    * owns Path objects
    * addPath: passes in 2 Points (start, end) and a color
    * clearPath
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