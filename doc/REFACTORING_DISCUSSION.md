### Refactoring Discussion 3/5/2020

#### Static Analysis Tool Changes and Other Refactorings By Class

SLogoBackEnd
* Empty method: addPaletteColor. Backend does not store palette now, but this will be implemented later. Although front end stores palette,
we can't rely on any front end doing this.
* Changing agic numbers in a few places
    * Initial values for initial background color, pen color index, etc.
    * Will change to constants
* Moved certain assignments in SLogoBackEnd to constructor from instance fields
* Removed mySyntax, myLanguage (unused instance variables)
    * Changed constructor/methods for SLogoMemento so language is no longer
    part of it, since language is now not in history
* Removed undoing and redoing from the model (now told by the interpreter to do this)
* Removed any other unused private variables
* We may need to reduce the number of methods here, possibly by changing/removing
certain versions of startCommandResult()

CommandFactory
* Removed unused private list ALL_COMMANDS
* Will do: adding a check to the To command to identify the command
so we do not overwrite an already-existing command

CommandResultBuilder
* Potential fix to large constructor: use a method (startCommandResult) to
set data rather than pass it into constructor. This wouldn't solve the problem of having too much
data in CommandResultBuilder/CommandResult but might be more elegant.
* Setters using objects now use copies

SLogoTurtle
* Some minor stylistic changes (curly braces in control statements, declaring on multiple lines)



