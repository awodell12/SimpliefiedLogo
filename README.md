parser
====

This project implements a development environment that helps users write SLogo programs.

Names: Austin Odell 


### Timeline

Start Date: 2/19

Finish Date: 3/6/20

Hours Spent: 
Austin: 15 basic. 15 complete
James: 12 basic, 12 complete

### Primary Roles
Austin and Cary frontEnd and controller. Austin focused mostly on UI and preferences. 

Sam and James worked on the backend commands and parser. James focused primarily on implementation of commands, implementation of multiple turtles, and outputting changes to the model as command results.


### Resources Used

Java Reflection Tutorial/Resources From 2/27 Lab on Reflection (for reflection in CommandFactory class)

### Running the Program

Main class: src/slogo/controller/Controller.java

Data files needed: All of src/slogo/frontEnd/Resources, src/resources.languages, src/slogo/backend/resources/commands.properties

Features implemented:
- All of Basic
- Front End Complete: All of the basic extensions except for saving and loading preferences, which we 
started and are able to use for user defined commands and variables. Challenging extensions of changing
the language of commands in all displays (buttons and menus for some languages have limited translations).
Undo/Redo is working. Animation is working. 
- Back End Complete: All commands involving multiple turtles


### Notes/Assumptions

Assumptions or Simplifications: The size of a lot of UI components are fixed and thus the current application
window only fits on some screens. The window size was created assuming a 16:9 aspect ratio that is common
for computers. 

Interesting data files: 

Known Bugs:

Extra credit:


### Impressions

I liked the assignment and the amount of freedom we had to do things our own way and decide on some 
difficult decisions. That said it definitely wasn't simple and there was some pretty complex logic /
algorithms that had to implemented for this to be successful. 