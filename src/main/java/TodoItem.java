import java.time.LocalDate;

public class TodoItem{
    private String title;
    private LocalDate deadline;
    private boolean isDone;

    public TodoItem (String title, LocalDate deadline){
        this.title = title;
        this.deadline = deadline;
    }

    public String getTitle(){
        return this.title;
    }

    public LocalDate getDeadline(){
        return this.deadline;
    }

    public boolean isDone(){
        return this.isDone;
    }

    public void mark(){
        isDone = true;
    }

    public void unmark(){
        isDone = false;
    }

    public String toString(){
        // [x] 12-6 submit assignment
        String sign = isDone ? "x" : " ";
        int date = deadline.getDayOfMonth();
        int month = deadline.getMonthValue();

        return String.format("[%s] %d-%d %s", sign, date, month, title);
    }

}