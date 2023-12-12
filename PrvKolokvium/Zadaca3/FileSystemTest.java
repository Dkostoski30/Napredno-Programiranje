package Zadaca3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

enum TYPE{
    FILE,
    FOLDER
}
class FileNameExistsException extends Exception{
    private String name, folderName;

    public FileNameExistsException(String name, String folderName) {
        this.name = name;
        this.folderName = folderName;
    }

    @Override
    public String getMessage() {
        return String.format("There is already a file named %s in the folder %s", name, folderName);
    }
}
interface IFile extends Comparable<IFile> {
    String getFileName();
    long getFileSize();
    String getFileInfo(int n);
    void sortBySize();
    long findLargestFile();
    TYPE getType();
    int compareTo(IFile o);
}
class File implements IFile{
    private String name;
    private long size;

    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }
    public File(String line){
        String[] parts = line.split("\\s+");
        this.name = parts[0];
        this.size = Long.parseLong(parts[1]);
    }

    @Override
    public String getFileName() {
        return this.name;
    }

    @Override
    public long getFileSize() {
        return this.size;
    }

    @Override
    public String getFileInfo(int n) {
        return "    ".repeat(Math.max(0, n)) +
                String.format("File name: %10s File size: %10d\n", this.name, this.size);
    }

    public void sortBySize() {}

    @Override
    public long findLargestFile() {
        return size;
    }

    @Override
    public TYPE getType() {
        return TYPE.FILE;
    }



    @Override
    public int compareTo(IFile o) {
        return Long.compare(getFileSize(), o.getFileSize());
    }
}
class Folder implements IFile{
    private List<IFile> files;
    private String name;

    public Folder(String name) {
        this.name = name;
        files = new ArrayList<>();
    }

    @Override
    public TYPE getType() {
        return TYPE.FOLDER;
    }

    @Override
    public long findLargestFile() {
        return files.stream().mapToLong(IFile::findLargestFile).max().orElse(0);
    }

    @Override
    public String getFileName() {
        return this.name;
    }

    @Override
    public long getFileSize() {
       return files.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public void sortBySize() {
        files.sort(IFile::compareTo);
        files.forEach(IFile::sortBySize);
    }
    public void addFile(IFile file) throws FileNameExistsException {
        if (files.stream().anyMatch(file1 -> file1.getFileName().equals(file.getFileName()))){
            throw new FileNameExistsException(file.getFileName(), getFileName());
        }else{
            files.add(file);
        }
    }

    @Override
    public String getFileInfo(int n) {
        StringBuilder rez = new StringBuilder();
        rez.append("    ".repeat(Math.max(0, n))).append(String.format("Folder name: %10s Folder size: %10d\n", this.name, this.getFileSize()));
        files.forEach(file -> rez.append(file.getFileInfo(n+1)));
        return rez.toString();
    }

    @Override
    public int compareTo(IFile o) {
        return Long.compare(getFileSize(), o.getFileSize());
    }
}
class FileSystem{
    private Folder rootDirectory;

    public FileSystem() {
        this.rootDirectory = new Folder("root");
    }
    public void addFile(IFile file) throws FileNameExistsException{
        rootDirectory.addFile(file);
    }
    public long findLargestFile(){
        return rootDirectory.findLargestFile();
    }
    public void sortBySize(){
        rootDirectory.sortBySize();
    }

    @Override
    public String toString() {
        return rootDirectory.getFileInfo(0);
    }
}
public class FileSystemTest {

    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}