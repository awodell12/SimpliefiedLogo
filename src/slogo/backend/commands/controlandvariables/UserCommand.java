package slogo.backend.commands.controlandvariables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import slogo.backend.Command;
import slogo.backend.BackEndInternal;
import slogo.backend.ParseException;
import slogo.CommandResult;

public class UserCommand implements Command {

  private List<String> myArguments;
  private Collection<String> myInstructions;

  public UserCommand(List<String> arguments, Collection<String> commands) {
    myArguments = arguments;
    myInstructions = commands;
  }

  public Collection<String> getCommands() {
    return new ArrayList<>(myInstructions);
  }

  @Override
  public int getNumArgs() {
    return myArguments.size();
  }

  @Override
  public int getNumVars() {
    return 0;
  }

  @Override
  public List<CommandResult> execute(List<Double> arguments, List<String> vars, String[] tokens,
      BackEndInternal backEnd) throws ParseException {
    double returnVal;
    List<CommandResult> results = new ArrayList<>();
    for (int i = 0; i < arguments.size(); i++) {
      backEnd.setVariable(myArguments.get(i),arguments.get(i));
    }
    results.addAll(backEnd.parseCommandsList(myInstructions.toArray(new String[0])));
    returnVal = results.get(results.size()-1).getReturnVal();
    return results;
  }

  @Override
  public List<String> findVars(String[] tokenList) {
    return null;
  }
}
