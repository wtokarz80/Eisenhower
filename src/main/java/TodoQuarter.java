import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TodoQuarter {

    private List<TodoItem> todoItems;
    // private static List<String> todoItemsString = new ArrayList<>();

    public TodoQuarter(){
        todoItems = new ArrayList<>();

    }

    // Constructs a TodoQuarter object with list of TodoItem objects

    public void addItem(String title, LocalDate deadline){
        // Append TodoItem object to todoItems sorted decreasing by deadline.
        TodoItem todoItem = new TodoItem(title, deadline);
        todoItems.add(todoItem);
        sortTodoItems();

    }

    public void removeItem(int index){
        todoItems.remove(index);
        // Removes TodoItem object using index of list todoItems

    }

    public void archiveItems(){
        // Removes all TodoItem objects with a parameter isDone set to true from list todoItems.
        for(int i = 0; i < todoItems.size(); i++){
            if(this.todoItems.get(i).isDone()){
                this.todoItems.remove(i);
            }
        }
    }

    public List<TodoItem> getItems(){
        return todoItems;

    }


    public TodoItem getItem(int index){
        return todoItems.get(index);

    }

    public String toString(){
        todoItems.sort(Comparator.comparing(TodoItem::getDeadline));
        String output = "";
        int index = 1;
        for(TodoItem todoItem : todoItems){
            output += index++ + ". " + todoItem.toString() + "\n";
        }

        return output;
    }

    public int findLongestItem(){
        int longestItemLength = 0;
        for (TodoItem todoItem : todoItems){
            if(todoItem.toString().length() + 4 > longestItemLength){
                longestItemLength = todoItem.toString().length();
            }
        }
        return longestItemLength;
    }


    public void sortTodoItems(){
        boolean sorted = false;
        TodoItem temp;
        while(!sorted) {
            sorted = true;
            for (int itemIndex = 0; itemIndex < todoItems.size() - 1; itemIndex++) {

                if (todoItems.get(itemIndex).getDeadline().isAfter(todoItems.get(itemIndex + 1).getDeadline())) {
                    temp = todoItems.get(itemIndex);
                    todoItems.set(itemIndex, todoItems.get(itemIndex + 1));
                    todoItems.set(itemIndex + 1, temp);
                    sorted = false;
                }
            }
        }
    }




}