import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.io.IOException;
import java.io.File;
import java.nio.file.*;
import java.nio.file.Files;
import java.io.*;
import java.io.FileReader;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Terminal {
    private Path currentDirectory ;
    private Parser parser ;
    ArrayList<String> commandHistory ;

    Terminal()
    {
        parser = new Parser();
        commandHistory = new ArrayList<>();
        currentDirectory = Paths.get(System.getProperty("user.dir"));
    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(currentDirectory.toString() + "> ");
            String command = scanner.nextLine();
            try{
                if(!parser.parse(command))
                {
                    throw new IOException("Error: Command not recognized.");
                }
                else{
                    chooseCommandAction();
                    parser.getArgs().clear();
                }
            }
            catch (IOException e){
                System.err.println("An exception occurred: " + e.getMessage());
            }
        }
    }
    public void chooseCommandAction() {
        String currentCommand = parser.getCommandName();
        switch (currentCommand){
            case "pwd": // 1
                pwd();
                break;
            case "echo": // 2
                echo();
                break;
            case "ls": // 3 & 4 (implements both command ls and ls -r)
                ls();
                break;
            case "history": // 5
                history();
                break;
            case "cd": // 6
                cd();
                break;
            case "mkdir": // 7
                mkdir();
                break;
            case "rmdir":// 8
                rmdir();
                break;
            case "cat": // 9
                cat();
                break;
            case "touch":// 10
                touch();
                break;
            case "cp": // 11
                cp();
                break;
            case "rm": // 12
                rm();
                break;
            case "wc": // 13
                wc();
                break;
            case "exit":
                exit();
                break;
            default:
                System.out.println("Syntax Error , try again");
                break;
        }
    }

    public void exit(){
        System.exit(0);
    }
    public void pwd(){
        System.out.println(currentDirectory);
        commandHistory.add("pwd");
    }

    public void echo() {
        ArrayList<String>currentArgs = parser.getArgs();
        int StartWithQuote = 0 , endWithQuote = 0;
        for (int i = 0; i < currentArgs.size(); i++) {
            if(currentArgs.get(i).startsWith("\"")){
                StartWithQuote++;
                currentArgs.get(i).replace("\"" , "");
            }
            if(currentArgs.get(i).endsWith("\"")){
                endWithQuote++;
                currentArgs.get(i).replace("\"" , "");
            }
        }
        try {
            if (StartWithQuote == 1 && endWithQuote == 1) {
                for (int i = 0; i < currentArgs.size(); i++) {
                    System.out.print(currentArgs.get(i) + " ");
                }
                System.out.println();
                commandHistory.add("echo");
            } else {
                throw new IOException("Error : should provide exact 1 argument");
            }
        }
        catch (IOException e){
            System.err.println("An exception occurred: " + e.getMessage());
        }
    }

    public void ls()
    {
        try{
            int rev = 0;
            if(parser.args.size() > 1){
                throw new IOException("Error : should provide  NO argument");
            } else if (parser.args.size() == 1) {
                if( parser.args.get(0).equals("-r")){
                    rev = 1;
                }
                else{
                    throw new IOException("Error : should provide NO argument");
                }
            }

            ArrayList<String>fileList= new ArrayList<>();
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentDirectory);
            for (Path entry : directoryStream) {
                fileList.add(entry.getFileName().toString());
            }

            if(rev == 0){
                Collections.sort(fileList);
                commandHistory.add("ls");
            }
            else{
                Collections.sort(fileList , Collections.reverseOrder());
                commandHistory.add("ls -r");
            }
            for (String fileName : fileList) {
                System.out.println(fileName);
            }

        }
        catch (IOException e){
            System.err.println("An exception occurred: " + e.getMessage());
        }
    }



    public void cd() {
        try {
            if(parser.getArgs().size() > 1){
                throw new IOException("Error : should provide at most 1 argument");
            }
            else{
                if(parser.getArgs().size() == 0){
                    currentDirectory = Paths.get(System.getProperty("user.home"));
                }
                else{
                    String arg = parser.getArgs().get(0);
                    if(arg.equals("..")){
                        Path parent = currentDirectory.getParent();
                        if (parent == null) {
                            throw new Exception("Invalid path: The path is null.");
                        }
                        if(!Files.isDirectory(parent)){
                            throw new Exception("Not a directory");
                        }
                        if (!Files.exists(parent)){
                            throw new Exception("Not exists");
                        }
                        currentDirectory = parent ;
                    }
                    else{
                        Path srcPath = Paths.get(arg);
                        if(!Files.exists(srcPath)){
                            throw new Exception("Not exists");
                        }
                        currentDirectory = currentDirectory.resolve(srcPath);
                    }
                }
                commandHistory.add("cd");
            }
        }
        catch (Exception e){
            System.err.println("An exception occurred: " + e.getMessage());
        }

    }


    public void mkdir()
    {
        try{
            if(parser.getArgs().size() == 0){
                throw new Exception("Error : should provide at Least 1 argument") ;
            }
            else{
                ArrayList<String>args = parser.getArgs();
                for (String name : args) {
                    Path srcPath = Paths.get(name);
                    Path newDir = currentDirectory.resolve(srcPath);
                    Files.createDirectory(newDir);
                }
                commandHistory.add("mkdir");
            }
        }
        catch (Exception e){
            System.err.println("An exception occurred: " + e.getMessage());
        }
    }

    public void rm()
    {
        try{
            if(parser.getArgs().size() == 0){
                throw new Exception("Error : should provide exact 1  argument") ;
            }
            else{
                Path src = Paths.get(parser.getArgs().get(0));
                Path filepath = currentDirectory.resolve(src);
                if(!Files.isRegularFile(filepath)){
                    throw new Exception("Not File") ;
                }
                if(!Files.exists(filepath)){
                    throw new Exception("Not exist in current directory") ;
                }
                Files.delete(src);
                System.out.println("File deleted successfully");
                commandHistory.add("rm");
            }
        }
        catch (Exception e){
            System.err.println("An exception occurred: " + e.getMessage());
        }
    }


    public void cat() {
        ArrayList<String> currentArgs = parser.getArgs();
        try {
            if (currentArgs.size() == 1) {
                String filePath = currentArgs.get(0);
                File file = new File(filePath);

                if (!file.exists() || !file.isFile()) {
                    throw new IOException("Error: The specified file does not exist.");
                }

                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                commandHistory.add("cat");
            } else if (currentArgs.size() == 2) {
                String filePath1 = currentArgs.get(0);
                String filePath2 = currentArgs.get(1);

                File file1 = new File(filePath1);
                File file2 = new File(filePath2);

                if (!file1.exists() || !file1.isFile() || !file2.exists() || !file2.isFile()) {
                    throw new IOException("Error: Both specified files should exist.");
                }

                try (BufferedReader reader1 = new BufferedReader(new FileReader(file1));
                     BufferedReader reader2 = new BufferedReader(new FileReader(file2))) {
                    String line;

                    while ((line = reader1.readLine()) != null) {
                        System.out.println(line);
                    }
                    while ((line = reader2.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                commandHistory.add("cat");
            } else {
                throw new IOException("Error: The 'cat' command requires 1 or 2 arguments.");
            }
        } catch (IOException e) {
            System.err.println("An exception occurred: " + e.getMessage());
        }
    }

    public void cp() {
        ArrayList<String> currentArgs = parser.getArgs();
        try {
            if (currentArgs.size() != 2) {
                throw new IOException("Error: The 'cp' command requires exactly 2 arguments.");
            }

            String sourcePath = currentArgs.get(0);
            String destinationPath = currentArgs.get(1);

            File sourceFile = new File(sourcePath);
            File destinationFile = new File(destinationPath);

            if (!sourceFile.isFile() || !destinationFile.isFile()) {
                throw new IOException("Error: Both source and destination should be valid files.");
            }

            try (FileInputStream fis = new FileInputStream(sourceFile);
                 FileOutputStream fos = new FileOutputStream(destinationFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("File copied successfully.");
            commandHistory.add("cp");
        } catch (IOException e) {
            System.err.println("An exception occurred: " + e.getMessage());
        }
    }
    public void touch() {
        ArrayList<String> currentArgs = parser.getArgs();
        try {
            if (currentArgs.size() != 1) {
                throw new IOException("Error: The 'touch' command requires exactly 1 argument (file path).");
            }

            String filePath = currentArgs.get(0);

            File newFile = new File(filePath);
            if (newFile.exists()) {
                throw new IOException("Error: File already exists at the specified path.");
            }

            if (newFile.createNewFile()) {
                System.out.println("File created successfully.");
                commandHistory.add("touch");
            } else {
                throw new IOException("Error: Failed to create the file.");
            }
        } catch (IOException e) {
            System.err.println("An exception occurred: " + e.getMessage());
        }
    }

    public void wc()
    {
            try{
                if(parser.getArgs().size() == 0){
                    throw new Exception("Error : should provide exact 1  argument") ;
                }
                else{
                    Path src = Paths.get(parser.getArgs().get(0));
                    Path filepath = currentDirectory.resolve(src);
                    if(!Files.isRegularFile(filepath)){
                        throw new Exception("Not File") ;
                    }
                    if(!Files.exists(filepath)){
                        throw new Exception("Not exist in current directory") ;
                    }
                    int lines = 0 , characters = 0 , words = 0;
                    BufferedReader reader = new BufferedReader(new FileReader(filepath.toFile()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            lines++;
                            characters += line.length();
                            String[] wordsArray = line.split("\\s+"); // Split by whitespace
                            words += wordsArray.length;
                        }
                    System.out.println("number of lines" + lines);
                    System.out.println("number of words" + words);
                    System.out.println("number of charachters" + characters);
                    System.out.println("fileName is " + filepath.toString());
                }
                commandHistory.add("wc");
            }
            catch (Exception e){
                System.err.println("An exception occurred: " + e.getMessage());
            }

    }

    public void rmdir() {
        String dirName = parser.getArgs().get(0);
        if (dirName.equals("*")) {
            File currentDir = new File(currentDirectory.toString());
            File[] filesList = currentDir.listFiles();
            if (filesList != null) {
                for (File file : filesList) {
                    if (file.isDirectory() && Objects.requireNonNull(file.list()).length == 0) {
                        file.delete();
                    }
                }
            }
            commandHistory.add("rmdir *");
        } else {
            Path dirPath = Paths.get(currentDirectory.toString(), dirName);
            File dir = new File(dirPath.toString());
            if (dir.isDirectory() && Objects.requireNonNull(dir.list()).length == 0) {
                dir.delete();
            }
            commandHistory.add("rmdir");
        }
    }

    public void history()
    {
        commandHistory.add("history");
        for (String item: commandHistory) {
            System.out.println(item);
        }
    }


}
