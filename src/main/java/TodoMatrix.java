import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TodoMatrix {

    private Map<String, TodoQuarter> todoQuarters = new HashMap<>();

    public TodoMatrix() {

        todoQuarters.put("IU", new TodoQuarter());
        todoQuarters.put("IN", new TodoQuarter());
        todoQuarters.put("NU", new TodoQuarter());
        todoQuarters.put("NN", new TodoQuarter());
    }

    public Map<String, TodoQuarter> getQuarters() {
        return todoQuarters;
    }

    public TodoQuarter getQuarter(String status) {
        return todoQuarters.get(status);
    }

    public void addItem(String title, LocalDate deadline, boolean isImportant) {
        boolean isUrgent = false;
        if ((deadline.minusDays(3).compareTo(LocalDate.now()) < 0)) {
            isUrgent = true;
        } else {
            isUrgent = false;
        }
        String statusImportant = isImportant ? "I" : "N";
        String statusUrgent = isUrgent ? "U" : "N";
        String status = statusImportant + statusUrgent;
        todoQuarters.get(status).addItem(title, deadline);
        return;
    }

    public void addItemsFromFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String row = myReader.nextLine();
                String[] listRow = row.split("[|]+");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate deadline = LocalDate.parse( listRow[1] + "-2020", formatter);
                String title = listRow[0];
                if (listRow.length > 2 && deadline.minusDays(3).compareTo(LocalDate.now()) < 0) {
                    todoQuarters.get("IU").addItem(title, deadline);
                } else if (listRow.length > 2 && deadline.minusDays(3).compareTo(LocalDate.now()) >= 0) {
                    todoQuarters.get("IN").addItem(title, deadline);
                } else if (listRow.length == 2 && deadline.minusDays(3).compareTo(LocalDate.now()) < 0) {
                    todoQuarters.get("NU").addItem(title, deadline);
                } else {
                    todoQuarters.get("NN").addItem(title, deadline);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void eraseFile(String fileName) {
        try {
            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    private void checkIfFileExist(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void saveItemsToFile(String fileName) {
        eraseFile(fileName);
        checkIfFileExist(fileName);
        File file = new File(fileName);
        try {
            FileWriter fw = new FileWriter(file, true);
            Formatter fm = new Formatter(fw);
            for (String key : todoQuarters.keySet()) {
                TodoQuarter todoQuarter = todoQuarters.get(key);
                for (TodoItem item : todoQuarter.getItems()) {
                    fm.format("%s  |%d-%d| %s\r\n", item.getTitle(), item.getDeadline().getDayOfMonth(), item.getDeadline().getMonthValue(),
                            key.charAt(0) == 'I' ? "important" : "");
                }
            }
            fm.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void archiveItems() {
        for (String key : todoQuarters.keySet()) {
            todoQuarters.get(key).archiveItems();
        }
    }

    public String toString() {
        String output = "";
        for (String key : todoQuarters.keySet()) {
            TodoQuarter todoQuarter = todoQuarters.get(key);
            output += key + "\n";
            output += todoQuarter.toString();
        }
        return output;
    }

    public String toStringTable() throws NullPointerException {
        /*
         * There program check which todoItem is the longest and returns it's length.
         * Then we check if it's even because we need it further when divide by 2 to
         * center text.
         */
        int longestItemLength = getLongestItemLength();

        String urgent = "URGENT";
        String notUrgent = "NOT URGENT";
        String important = "IMPORTANT";
        String notImportant = "NOT IMPORTANT";

        // in case there is no todoitems or its' length is lower than length of shortest
        // column header, width of table sets default to 20.

        /*
         * There it makes table header like:
         *
         *   |            URGENT              |            NOT URGENT          |
         * --|--------------------------------|--------------------------------|--
         *
         * Program counts how many empty spaces must add before and after word Urgent
         * and Not Urgent. Then uses repeat function to multiply empty spaces by proper
         * number.
         */

        StringBuilder tablestring = new StringBuilder();

        tablestring = addHeader(tablestring, longestItemLength, urgent, notUrgent);
        String dividingLine = "--|" + "-".repeat(longestItemLength) + "|" + "-".repeat(longestItemLength) + "|--\n";
        tablestring.append(dividingLine);

        /*
         * There it makes rest of table. It looks similar as in table header but instead
         * of words, we have the element of each quarter.
         *
         * I | 1. [ ] 9-6 go to the doctor |1. [ ] 14-6 buy a ticket |
         * M | 2. [ ] 21-7   Ann birthday  |                         |
         *
         * What's more, we print lines as much times as IMPORTANT word length because we
         * print single characters every line on the left side (as in example I and M).
         * It gives result of made UI quarter.
         */

        tablestring = addImportant(important, tablestring, longestItemLength);

        // Line diving Important and Not important
        tablestring.append(dividingLine);

        // This part works the same. There is only quarter status changed

        tablestring = addNotImportant(notImportant, tablestring, longestItemLength);

        // Line closing table
        tablestring.append(dividingLine);

        return tablestring.toString();
    }

    private void appendImportantLetters(String important, int i, StringBuilder tableString) {
        if (i < important.length()) {
            tableString.append(important.charAt(i));
        } else {
            tableString.append(" ");
        }
        tableString.append(" |");
    }

    private void appendNotImportantLetters(String notImportant, int i, StringBuilder tableString) {
        if (i < notImportant.length()) {
            tableString.append(notImportant.charAt(i));
        } else {
            tableString.append(" ");
        }
        tableString.append(" |");
    }

    private int getLongestItemLength() {
        String urgent = "URGENT";
        int longestItemLength = 0;

        for (String key : todoQuarters.keySet()) {
            TodoQuarter todoQuarter = todoQuarters.get(key);
            if (longestItemLength < todoQuarter.findLongestItem()) {
                longestItemLength = todoQuarter.findLongestItem();
            }
        }
        if (longestItemLength % 2 == 1) {
            longestItemLength += 1;
        }

        if (longestItemLength < urgent.length()) {
            longestItemLength = 20;
        }
        return longestItemLength;

    }

    private StringBuilder addHeader(StringBuilder tablestring, int longestItemLength, String urgent, String notUrgent) {
        int spacesCountHeaderUrgent = (longestItemLength - urgent.length()) / 2;
        int spacesCountHeaderNotUrgent = (longestItemLength - notUrgent.length()) / 2;

        if (longestItemLength < urgent.length()) {
            spacesCountHeaderUrgent = urgent.length();
        }
        if (longestItemLength < notUrgent.length()) {
            spacesCountHeaderNotUrgent = notUrgent.length();
        }
        tablestring.append(
                "  |" + " ".repeat(spacesCountHeaderUrgent) + urgent + " ".repeat(spacesCountHeaderUrgent) + "|");
        tablestring.append(
                " ".repeat(spacesCountHeaderNotUrgent) + notUrgent + " ".repeat(spacesCountHeaderNotUrgent) + "|\n");

        return tablestring;

    }

    private StringBuilder addImportant(String important, StringBuilder tablestring, int longestItemLength) {
        int uiSize = todoQuarters.get("IU").getItems().size();
        int niSize = todoQuarters.get("IN").getItems().size();

        /*
         * There it checks the height of row (UI and NI quarters). In case that list of
         * quarters' todoItems is bigger than length of word 'Important' program will
         * print additional empty lines (withouth letters on beggining).
         */

        int firstRowHeight = Math.max(Math.max(important.length(), uiSize), niSize);

        for (int i = 0; i < firstRowHeight; i++) {
            appendImportantLetters(important, i, tablestring);
            // here it checks if index (i) is not bigger or equal quarters' list size
            // (checks if it can take that index from list)
            if (i < uiSize) {
                int spaceFiller = longestItemLength - todoQuarters.get("IU").getItem(i).toString().length();
                tablestring.append(todoQuarters.get("IU").getItem(i).toString());
                tablestring.append(" ".repeat(spaceFiller));
                tablestring.append("|");
            } else {
                // while there is no more elements in that quarter
                tablestring.append(" ".repeat(longestItemLength) + "|");
            }
            if (i < niSize) {
                int spaceFiller = longestItemLength - todoQuarters.get("IN").getItem(i).toString().length();
                tablestring.append(todoQuarters.get("IN").getItem(i).toString());
                tablestring.append(" ".repeat(spaceFiller));
                tablestring.append("|\n");
            } else {
                // while there is no more elements in that quarter
                tablestring.append(" ".repeat(longestItemLength) + "|\n");
            }

        }
        return tablestring;
    }

    private StringBuilder addNotImportant(String notImportant, StringBuilder tablestring, int longestItemLength) {
        int unSize = todoQuarters.get("NU").getItems().size();
        int nnSize = todoQuarters.get("NN").getItems().size();

        int secondRowHeight = Math.max(Math.max(notImportant.length(), unSize), nnSize);

        for (int i = 0; i < secondRowHeight; i++) {
            appendNotImportantLetters(notImportant, i, tablestring);
            if (i < unSize) {
                int spaceFiller = longestItemLength - todoQuarters.get("NU").getItem(i).toString().length();
                tablestring.append(todoQuarters.get("NU").getItem(i).toString());
                tablestring.append(" ".repeat(spaceFiller));
                tablestring.append("|");
            } else {
                tablestring.append(" ".repeat(longestItemLength) + "|");
            }
            if (i < nnSize) {
                int spaceFiller = longestItemLength - todoQuarters.get("NN").getItem(i).toString().length();
                tablestring.append(todoQuarters.get("NN").getItem(i).toString());
                tablestring.append(" ".repeat(spaceFiller));
                tablestring.append("|\n");
            } else {
                tablestring.append(" ".repeat(longestItemLength) + "|\n");
            }
        }
        return tablestring;
    }
}