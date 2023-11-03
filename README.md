Command Line Interpreter (CLI) in Java
Overview
This is a simple Command Line Interpreter (CLI) implemented in Java. The CLI allows users to enter various commands to interact with the operating system. The program provides basic functionality like echoing text, changing directories, listing files, creating directories, and more.

Project Structure
The project consists of two main classes:

Parser: This class is responsible for parsing user input and extracting the command name and arguments.
Terminal: This class contains methods for executing various commands, such as echo, pwd, cd, and others. It also handles the choice of which command to execute.
Supported Commands
The following commands are implemented in this CLI:

echo: Takes 1 argument and prints it.

pwd: Takes no arguments and prints the current path.
cd: Supports changing the current directory to the home directory, the previous directory, or a specified path.
ls: Lists files and directories in the current directory.
ls -r : lists the contents of the current directory in reverse order.
mkdir: Creates new directories.
rmdir: Removes empty directories.
rm: Removes files.

wc : Wc stands for “word count,” and as the name suggests, it is mainly used for counting purpose. By default, it displays four-columnar output.
First column shows number of lines present in a file specified,
second column shows number of words present in the file, third
column shows number of characters present in file and fourth
column itself is the file name which are given as argument.

cat: Displays the contents of a file.
exit: Exits the CLI.



Usage
To use the CLI, follow these steps:

Compile the Java source files.
Run the Terminal class, which serves as the main entry point.
