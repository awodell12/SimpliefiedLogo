package slogo.backend.commands.multiplecommands;

import java.util.List;
import slogo.CommandResult;
import slogo.backend.BackEndInternal;
import slogo.backend.CommandResultBuilder;
import slogo.backend.Turtle;

public abstract class TurtleCreationCommand {
  protected CommandResult initialTurtleResult(Turtle newlyActive, List<Integer> activeTurtleNums, BackEndInternal backEnd) {
    CommandResultBuilder builder = backEnd.startCommandResult(
        newlyActive.getHeading(),
        newlyActive.getPosition(),
        newlyActive.getVisible());
    builder.setRetVal(0);
    builder.activeTurtleIDs(activeTurtleNums);
    builder.setTurtleID(newlyActive.getId());
    return builder.buildCommandResult();
  }
}
