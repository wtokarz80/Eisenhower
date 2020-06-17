import java.util.Arrays;
import java.util.List;

public class Display {

    private static List<String> menuList = Arrays.asList("Choose a status of shown TODO items [IU, IN, NU, NN]",
            "Add item [ADD]", "Mark or unmark item [MARK]", "Remove item [REMOVE]", "Archive all done items [ARCHIVE]",
            "List all items [ALL]", "Save to file [SAVE]", "Load tasks from file [LOAD]", "Exit program [EXIT]");

    public static void displayStart(){
        displayHello();
        for (int i = 0; i < menuList.size(); i++){
            System.out.println((i+1) + ". " + menuList.get(i));
        }
    }
    private static void displayHello(){
        System.out.println("\n --- WELCOME TO EINSENHOWER MATRIX --- \n");
        System.out.println("Tasks description:\n");
        System.out.println("'IU' - means important & urgent");
        System.out.println("'IN' - means important & not urgent");
        System.out.println("'NU' - means not important & urgent");
        System.out.println("'NN' - means not important & not urgent\n");
        System.out.println("Enter the command given in the menu.\n");
        System.out.println("MENU:\n");

    }

    public static void displayAll(){
        Character[] important = new Character[]{' ', ' ', 'I', 'M', 'P', 'O', 'R', 'T', 'A', 'N', 'T', ' ', ' '};
        Character[] notImportant = new Character[]{'N', 'O', 'T',' ', 'I', 'M', 'P', 'O', 'R', 'T', 'A','N', 'T'};
        String urgent = "URGENT";
        String notUrgent = "NOT URGENT";
        String item1 = "";
        String item2 = "";
        String dash = "---------------------------------------------------------------";
        System.out.println(dash);
        System.out.printf("  |%10s %s %-10s|%8s %s %-8s|  %n", " ", urgent, " ", " ", notUrgent, " ");
        System.out.println(dash);
        for(int i = 0; i < important.length; i++){
            System.out.printf("%c |%-28s|%-28s|  %n",important[i], item1, item2);
        }
        System.out.println(dash);
        for(int i = 0; i < notImportant.length; i++){
            System.out.printf("%c |%-28s|%-28s|  %n",notImportant[i], item1, item2);
        }
        System.out.println(dash);
    }

}