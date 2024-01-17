package Zadaca21;

import java.util.*;

/*
* Да се имплементира класа за именик PhoneBook со следните методи:

void addContact(String name, String number) - додава нов контакт во именикот. Ако се обидеме да додадеме контакт со веќе постоечки број, треба да се фрли исклучок од класа DuplicateNumberException со порака Duplicate number: [number]. Комплексноста на овој метод не треба да надминува $O(log N)$ за $N$ контакти.
void contactsByNumber(String number) - ги печати сите контакти кои во бројот го содржат бројот пренесен како аргумент во методот (минимална должина на бројот [number] е 3). Комплексноста на овој метод не треба да надминува $O(log N)$ за $N$ контакти.
void contactsByName(String name) - ги печати сите контакти кои даденото име. Комплексноста на овој метод треба да биде $O(1)$.
Во двата методи контактите се печатат сортирани лексикографски според името, а оние со исто име потоа според бројот.
* */
class DuplicateNumberException extends Exception{
    public DuplicateNumberException(String message) {
        super(message);
    }
}
class PhoneBook{
    Map<String, String> numbersNameMap;
    static Comparator<Map.Entry<String, String>> comparatorNumbers = Comparator.comparing(Map.Entry::getKey);
    static Comparator<Map.Entry<String, String>> comparatorNames = Comparator.comparing(Map.Entry::getValue);
    static Comparator<Map.Entry<String, String>> final_Comparator = comparatorNames.thenComparing(Map.Entry::getKey);
    public PhoneBook() {
        numbersNameMap = new HashMap<>();
    }
    public void addContact(String name, String number)throws DuplicateNumberException{
        if(numbersNameMap.containsKey(number)){
            throw new DuplicateNumberException(String.format("Number %s already exists", number));
        }else{
            numbersNameMap.put(number, name);
        }
    }
    public void contactsByNumber(String number){
        if(numbersNameMap.entrySet()
                .stream()
                .noneMatch(entry->entry.getKey().contains(number))){
            System.out.println("NOT FOUND");
            return;
        }
        numbersNameMap.entrySet()
                .stream()
                .filter(entry->entry.getKey().contains(number))
                .sorted(final_Comparator)
                .forEach(entry -> System.out.println(String.format("%s %s", entry.getValue(), entry.getKey())));
    }
    public void contactsByName(String name){
        if(numbersNameMap.entrySet()
                .stream()
                .noneMatch(entry -> entry.getValue().equals(name))){
            System.out.println("NOT FOUND");
            return;
        }
        numbersNameMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(name))
                .sorted(final_Comparator)
                .forEach(entry -> System.out.println(String.format("%s %s", entry.getValue(), entry.getKey())));    }
}
public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде

