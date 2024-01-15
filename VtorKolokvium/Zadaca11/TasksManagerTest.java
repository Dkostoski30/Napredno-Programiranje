
package Zadaca11;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

class DeadlineNotValidException extends Exception{
    public DeadlineNotValidException() {
    }

    public DeadlineNotValidException(String message) {
        super(message);
    }
}
interface TaskComponent{
    LocalDateTime getDeadline();
    int getPriority();
}
class Task implements TaskComponent{
    private String name, description;
    //private boolean hasDeadline, hasPriority;

    public Task(String name, String description) {
        //this.category = category;
        this.name = name;
        this.description = description;
    }

    @Override
    public LocalDateTime getDeadline() {
        return LocalDateTime.MAX;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }



    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'';
    }
}
abstract class TaskDecorator implements TaskComponent{
    protected TaskComponent task;

    public TaskDecorator(TaskComponent task) {
        this.task = task;
    }

}
class DeadLineTaskDecorator extends TaskDecorator{
    private LocalDateTime deadline;
    private static LocalDateTime today = LocalDateTime.of(2020,6,2,23,59,59, 000);
    public DeadLineTaskDecorator(TaskComponent task, LocalDateTime deadline) throws DeadlineNotValidException {
        super(task);
        if(today.isAfter(deadline)){
            throw new DeadlineNotValidException(String.format("The deadline 2020-06-01T23:59:59 has already passed"));
        }
        this.deadline = deadline;
    }
    @Override
    public LocalDateTime getDeadline() {
        return deadline;
    }
    @Override
    public int getPriority() {
        return task.getPriority();
    }

    public static LocalDateTime getToday() {
        return today;
    }

    @Override
    public String toString() {
        return task+", deadline=" + deadline;
    }
}
class PriorityTaskDecorator extends TaskDecorator{
    private int priority;

    public PriorityTaskDecorator(TaskComponent task, int priority) {
        super(task);
        this.priority = priority;
    }

    @Override
    public LocalDateTime getDeadline() {
        return task.getDeadline();
    }

    @Override
    public int getPriority() {
        return priority;
    }
    @Override
    public String toString() {
        return task+", priority=" + priority;
    }
}
interface TaskFactory{
    static TaskComponent createTask(String data) throws DeadlineNotValidException{
        String [] parts = data.split(",");
        TaskComponent baseComponent= new Task(parts[1], parts[2]);
        if (parts.length == 4 || parts.length == 5){
            if(parts[3].length() > 10){
                //Deadline
                baseComponent = new DeadLineTaskDecorator(baseComponent, LocalDateTime.parse(parts[3]));
            }else{
                baseComponent = new PriorityTaskDecorator(baseComponent, Integer.parseInt(parts[3]));
            }
        }
        if (parts.length == 5){
            if(parts[4].length() > 10){
                baseComponent = new DeadLineTaskDecorator(baseComponent, LocalDateTime.parse(parts[4]));
            }else{
                baseComponent = new PriorityTaskDecorator(baseComponent, Integer.parseInt(parts[4]));
            }
        }
        return baseComponent;
    }
}
class TaskManager{
    private Map<String, List<TaskComponent>> categoryListMap;
    private List<TaskComponent> taskComponents;

    public TaskManager() {
        categoryListMap = new HashMap<>();
        taskComponents = new ArrayList<>();
    }
    public void readTasks (InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            try {
                TaskComponent task = TaskFactory.createTask(line);
                taskComponents.add(task);
                categoryListMap.putIfAbsent(parts[0], new ArrayList<>());
                categoryListMap.get(parts[0]).add(task);
            }catch (DeadlineNotValidException exception){
                System.out.println(exception.getMessage());
            }
        }
    }
    public void printTasks(OutputStream os, boolean includePriority, boolean includeCategory){
        PrintWriter printWriter = new PrintWriter(os);
        Comparator<TaskComponent> comparator;
        if(includePriority){
            comparator = Comparator.comparing(TaskComponent::getPriority).reversed().thenComparing(taskComponent -> Duration.between(taskComponent.getDeadline(), LocalDateTime.now()));
        }else{
            comparator = Comparator.comparing(taskComponent -> Duration.between(taskComponent.getDeadline(), LocalDateTime.now()).toSeconds());
        }
        if(includeCategory){
            categoryListMap.keySet().forEach(key ->{
                printWriter.println(key.toUpperCase());
                categoryListMap.get(key).stream()
                        .sorted(comparator.reversed())
                        .forEach(taskComponent -> printWriter.println(taskComponent + "}"));
            });
        }else{
            categoryListMap.values().stream().flatMap(List::stream)
                    .sorted(comparator.reversed())
                    .forEach(taskComponent -> printWriter.println(taskComponent + "}"));
        }
        printWriter.flush();
    }
}
public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}

