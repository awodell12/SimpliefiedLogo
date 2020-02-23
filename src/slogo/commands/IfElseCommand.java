package slogo.commands;

import java.util.Arrays;
import java.util.List;
import slogo.AltCommand;
import slogo.CommandResult;
import slogo.SLogoBackEnd;

public class IfElseCommand implements AltCommand {

  @Override
  public int getNumArgs() {
    return 1;
  }

  @Override
  public int getNumVars() {
    return 0;
  }

  @Override
  public CommandResult execute(List<Double> arguments, List<String> vars, String[] tokens,
      SLogoBackEnd backEnd) {
    System.out.println("Beginning of IF-ELSE");
    for (String token : tokens) {
      System.out.print(token);
    }
    System.out.println();
    int firstListIndex = 2;
    System.out.println("Finding first list size.");
    int firstListSize = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,2,tokens.length));
    int secondListIndex = firstListSize + firstListIndex + 1;
    System.out.println("Finding second list size.");
    int secondListSize = backEnd.distanceToEndBracket(Arrays.copyOfRange(tokens,secondListIndex,tokens.length));
    if (arguments.get(0) != 0) {
      System.out.println("IF evaluated to TRUE");
      backEnd.parseTokens(Arrays.copyOfRange(tokens,2,tokens.length));
      System.out.println("Finished IF statement for TRUE.");
    }
    else {
      System.out.println("IF evaluated to FALSE");
      backEnd.parseTokens(Arrays.copyOfRange(tokens,secondListIndex,tokens.length));
      System.out.println("Finished IF statement for FALSE.");
    }
    return new CommandResult(0, secondListIndex+secondListSize-1);
  }
}
