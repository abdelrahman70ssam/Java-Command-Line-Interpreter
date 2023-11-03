import java.util.ArrayList;

public class Parser {
    String commandName;
    ArrayList<String> args = new ArrayList<>();
    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input)
    {
        String []Parts = input.trim().split("\\s+");

        if (Parts[0].length() == 0)
            return false;

        commandName = Parts[0];
        if(Parts.length > 1){
            for (int i = 1; i < Parts.length; i++) {
                args.add(Parts[i]);
            }
        }
        return true ;
    }
    public String getCommandName(){
       return commandName;
    }
    public ArrayList<String> getArgs(){
        return args ;
    }
}