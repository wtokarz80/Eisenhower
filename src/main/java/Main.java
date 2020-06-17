import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Main {
    private static Scanner scan;
    private static String choice;
    private static TodoMatrix matrix = new TodoMatrix();


    public static void main(String[] args) {

        do{
            clear();
            scan = new Scanner(System.in);
            scan.useDelimiter(System.lineSeparator());
            Display.displayStart();
            choice = getUserChoice();
            runUserChoice(choice);
        }while(!choice.equals("EXIT"));
    }

    private static void runUserChoice(String choice) {
        Map<String, Runnable> commandsMap = new HashMap<>();
        String[] commands = {"ADD", "EXIT", "ALL", "IU", "IN", "NU", "NN", "MARK", "REMOVE", "ARCHIVE", "SAVE", "LOAD"};
        Runnable[] functions = {() -> createItem(), () -> printExit(), () -> listAll(), () -> listAllUi(),
                () -> listAllIn(), () -> listAllNu(), () -> listAllNn(), () -> markItem(), () -> removeItem(), () -> archiveAll(),
                () -> saveTofile(), () -> loadFromfile()};
        for(int index = 0; index < commands.length; index++){
            commandsMap.put(commands[index], functions[index]);
        }
        commandsMap.get(choice).run();
    }

    private static String getUserChoice(){
        System.out.print("\nYour choice : ");
        choice = scan.next();
        return choice.toUpperCase();
    }

    private static void createItem(){
        boolean isImportant = false;
        String userTitle = userInput("Enter a title: ");
        String userDate = userInput("Enter a deadline [yyyy-mm-dd]: ");
        String userIsImportant = userInput("Is your item important? [I/N]");
        if(userIsImportant.equalsIgnoreCase("I")){
            isImportant = true;
        }

        matrix.addItem(userTitle,LocalDate.parse(userDate), isImportant);
        System.out.println("\nPress enter to back to menu.");
        scan.next();
    }

    private static String userInput(String imputTitle) {
        System.out.println(imputTitle);
        String userInput = scan.next().toUpperCase();
        return userInput;
    }

    private static void listAll(){
        System.out.println(matrix.toStringTable());
        System.out.println("\nPress enter to back to menu.");
        scan.next();
    }

    private static void listAllUi(){
        System.out.println("\n -- IMPORTANT & URGENT -- \n");
        System.out.println(matrix.getQuarter("IU"));
        System.out.println("\nPress enter to back to menu.");
        scan.next();
    }
    private static void listAllIn(){
        System.out.println("\n -- IMPORTANT & NOT URGENT -- \n");
        System.out.println(matrix.getQuarter("IN"));
        System.out.println("\nPress enter to back to menu.");
        scan.next();
    }
    private static void listAllNu(){
        System.out.println("\n -- NOT IMPORTANT & URGENT -- \n");
        System.out.println(matrix.getQuarter("NU"));
        System.out.println("\nPress enter to back to menu.");
        scan.next();
    }
    private static void listAllNn(){
        System.out.println("\n -- NOT IMPORTANT & NOT URGENT -- \n");
        System.out.println(matrix.getQuarter("NN"));
        System.out.println("\nPress enter to back to menu.");
        scan.next();
    }

    private static void printExit(){
        System.out.println("\nGood bye.");
    }

    private static void clear() {
        try {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void markItem(){
        System.out.println(matrix);
        String userQuarter = userInput("Type in the name of quarter:");
        String userIndex = userInput("Type in the number of task to mark:");
        int userInt = Integer.parseInt(userIndex);
        if(matrix.getQuarter(userQuarter).getItem(userInt - 1).isDone()){
            matrix.getQuarter(userQuarter).getItem(userInt - 1).unmark();
        }
        else{
            matrix.getQuarter(userQuarter).getItem(userInt - 1).mark();
        }
        System.out.println("\nPress enter to back to menu.");
        scan.next();
    }

    private static void removeItem(){
        System.out.println(matrix.toStringTable());
        String userQuarter = userInput("Type in the name of quarter:");
        String userIndex = userInput("Type in the number of task to mark:");
        int userInt = Integer.parseInt(userIndex);
        matrix.getQuarter(userQuarter).removeItem(userInt-1);
        System.out.println("\nPress enter to back to menu.");
        scan.next();

    }

    private static void archiveAll(){
        matrix.archiveItems();
    }

    private static void saveTofile(){
        matrix.saveItemsToFile("tasks.txt");
    }

    private static void loadFromfile(){
        matrix.addItemsFromFile("tasks.txt");
        // listAll();
    }

}