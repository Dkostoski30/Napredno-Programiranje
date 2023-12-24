package Zadaca6;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

enum TYPE{
    HOURLY_EMPLOYEE,
    FREELANCE_EMPLOYEE
}
abstract class Employee{
    private final String id, level;
    private double salary;
    public static Comparator<Employee> COMPARATOR = Comparator.comparingDouble(Employee::getSalary).thenComparing(Employee::getLevel).reversed();
    public Employee(String id, String level) {
        this.id = id;
        this.level = level;
        salary = 0;
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public abstract void calculateSalary(double rate);
    public abstract TYPE getType();
    @Override
    public abstract String toString();
}
class HourlyEmployee extends Employee{
    double hours;
    public HourlyEmployee(String data) {
        super(data.split(";")[1], data.split(";")[2]);
        this.hours = Double.parseDouble(data.split(";")[3]);
    }
    public double getOvertimeHours() {
        return (hours - 40 > 0) ? (hours - 40) : 0;
    }

    public double getHoursRegular() {
        return (hours < 40) ? hours : 40;
    }
    @Override
    public TYPE getType() {
        return TYPE.HOURLY_EMPLOYEE;
    }

    @Override
    public void calculateSalary(double rate) {
        setSalary(getHoursRegular()*rate + getOvertimeHours()*rate*1.5);
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",
                getId(), getLevel(), getSalary(),getHoursRegular(), getOvertimeHours());
    }
}
class FreelanceEmployee extends Employee{
    List<Integer> ticketPoints;

    public FreelanceEmployee(String data) {
        super(data.split(";")[1], data.split(";")[2]);
        this.ticketPoints = new ArrayList<>();
        String[] parts = data.split(";");
        for (int i = 3; i < parts.length; i++) {
            ticketPoints.add(Integer.parseInt(parts[i]));
        }
    }

    @Override
    public void calculateSalary(double rate) {
        setSalary(ticketPoints.stream().mapToDouble(Integer::doubleValue).sum()*rate);
    }

    @Override
    public TYPE getType() {
        return TYPE.FREELANCE_EMPLOYEE;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",
                getId(), getLevel(), getSalary(), ticketPoints.size(), ticketPoints.stream().mapToInt(Integer::intValue).sum());
    }
}
class PayrollSystem{
    private final Map<String,Double> hourlyRateByLevel, ticketRateByLevel;
    private final Map<String, Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        employees = new HashMap<>();
    }
    public void readEmployees (InputStream is){
        Scanner scanner = new Scanner(is);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.charAt(0)=='H'){
                HourlyEmployee hourlyEmployee = new HourlyEmployee(line);
                hourlyEmployee.calculateSalary(hourlyRateByLevel.get(hourlyEmployee.getLevel()));
               // System.out.println(String.format("ADDING: %s\n with salary rate %.2f", hourlyEmployee, hourlyRateByLevel.get(hourlyEmployee.getLevel())));
                employees.putIfAbsent(hourlyEmployee.getId(), hourlyEmployee);
            }else{
                FreelanceEmployee freelanceEmployee = new FreelanceEmployee(line);
                freelanceEmployee.calculateSalary(ticketRateByLevel.get(freelanceEmployee.getLevel()));
                employees.putIfAbsent(freelanceEmployee.getId(), freelanceEmployee);
            }
        }
    }
    public Map<String, Collection<Employee>> printEmployeesByLevels (OutputStream os, Set<String> levels){
        Map<String, List<Employee>> employeesByLevel = new HashMap<>();
        PrintWriter printWriter = new PrintWriter(os);
        levels.forEach(level -> {
            employeesByLevel.putIfAbsent(level, new ArrayList<>());
        });
        employeesByLevel.keySet().forEach(key ->{
            employeesByLevel.put(key, employees.values().stream()
                    .filter(employee -> employee.getLevel().equals(key))
                    .sorted(Employee.COMPARATOR)
                    .collect(Collectors.toCollection(ArrayList::new)));
        });
        employeesByLevel.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .sorted(Comparator.comparing(entry -> entry.getKey()))
                .forEach(entry -> {
                    printWriter.println(String.format("LEVEL: %s", entry.getKey()));
                    printWriter.println("Employees: ");
                    entry.getValue().forEach(employee -> {
                        printWriter.println(employee);
                    });
                    printWriter.println("------------");
                });

        printWriter.flush();
        return employeesByLevel.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, HashMap::new));
    }
}
public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        levels.add("level" + 10);
        for (int i = 5; i <= 9; i++) {
            levels.add("level" + i);
        }
        Map<String, Collection<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
    }
}
