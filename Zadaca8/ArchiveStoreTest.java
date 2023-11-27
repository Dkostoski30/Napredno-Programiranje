package Zadaca8;


import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
enum Type{
    SPECIAL,
    LOCKED
}
class NonExistingItemException extends Exception{
    private int id;

    public NonExistingItemException(int id) {
        super();
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Item with id %d doesn't exist", id);
    }
}
abstract class Archive{
    private int id;
    private LocalDate dateArchived;

    public Archive(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = LocalDate.of(dateArchived.getYear(), dateArchived.getMonth(), dateArchived.getDayOfMonth());
    }

    public LocalDate getDateArchived() {
        return dateArchived;
    }
    public abstract Type getType();
    public abstract boolean openArchive(LocalDate date);
}
class LockedArchive extends Archive{
    private LocalDate dateToOpen;
    public LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public Type getType() {
        return Type.LOCKED;
    }
    public LocalDate getDateToOpen(){
        return dateToOpen;
    }

    @Override
    public boolean openArchive(LocalDate date) {
        return date.isAfter(dateToOpen);
    }
}
class SpecialArchive extends Archive{
    private int maxOpen, timesOpened;
    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        this.timesOpened = 0;
    }

    @Override
    public Type getType() {
        return Type.SPECIAL;
    }

    public int getMaxOpen() {
        return maxOpen;
    }
    public void Open(){
        timesOpened++;
    }

    @Override
    public boolean openArchive(LocalDate date) {
        return maxOpen>timesOpened++;
    }
}
class ArchiveStore{
    private List<Archive> archiveList;
    private StringBuilder log;
    public ArchiveStore() {
        archiveList = new ArrayList<>();
        log = new StringBuilder();
    }
    public void archiveItem(Archive item, LocalDate date){

        if(item.getType().equals(Type.LOCKED)){
            LockedArchive lockedArchive = (LockedArchive) item;
            lockedArchive.setDateArchived(date);
            archiveList.add(lockedArchive);
        }else{
            SpecialArchive specialArchive = (SpecialArchive) item;
            specialArchive.setDateArchived(date);
            archiveList.add(specialArchive);
        }
        log.append(String.format("Item %d archived at %s\n", item.getId(), item.getDateArchived().toString()));
    }
    public void openItem(int id, LocalDate date) throws NonExistingItemException{
        Archive item = archiveList.stream().filter(archive -> archive.getId()==id).findFirst().orElse(null);
        if(item==null){
            throw new NonExistingItemException(id);
        }else{
            if(item.getType().equals(Type.LOCKED)) {
                LockedArchive lockedArchive = (LockedArchive) item;
                if(item.openArchive(date)){
                    log.append(String.format("Item %d opened at %s\n", id, date));
                }else{
                    log.append(String.format("Item %d cannot be opened before %s\n", id, lockedArchive.getDateToOpen()));
                }
            }else{
                SpecialArchive specialArchive = (SpecialArchive) item;
                if(item.openArchive(date)){
                    log.append(String.format("Item %d opened at %s\n", id, date));
                }else{
                    log.append(String.format("Item %d cannot be opened more than %d times\n", id, specialArchive.getMaxOpen()));
                }
            }
        }
    }
    public String getLog(){
        return log.toString();
    }
}
public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}