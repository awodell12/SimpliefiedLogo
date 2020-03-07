parser
====

This project implements a development environment that helps users write SLogo programs.

Names: Austin Odell (awo6), James Rumsey (jpr31), Cary Shindell (css57), Sam Thompson (stt13)

### Timeline

Start Date: 2/19/20

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
- Back End Complete: All additional commands, updating commands and model to allow for multiple turtles and
multiple active/inactive turtles, saving mementos of current model state


### Notes/Assumptions

Assumptions or Simplifications: The size of a lot of UI components are fixed and thus the current application
window only fits on some screens. The window size was created assuming a 16:9 aspect ratio that is common
for computers. When using the "tell" command, adding an index creates only one new turtle, regardless of the number (i.e. tell 
[ 100 ] does not create 99 additional turtles in addition to turtle 0 and turtle 100). Any contents within brackets must be separated by 
a space from the bracket.

Interesting data files: 

Known Bugs:

Extra credit:


### Impressions

I liked the assignment and the amount of freedom we had to do things our own way and decide on some 
difficult decisions. That said it definitely wasn't simple and there was some pretty complex logic /
algorithms that had to implemented for this to be successful. 

I liked thinking in terms of internal/external API design during this project because it allowed us to reduce the project into distinct components.
The front end is distinctly different from the back end and were developed independent of one another, but for the most part the two communicate well with each other.