import java.util.Scanner;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

//C:\Users\klee9\CTAC\Day60\ProjectIO\src
public class FileManager {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Path of directory: "); //C:\Users\klee9\CTAC\Day60\ProjectIO\src
        String inputPath = scanner.nextLine();

        try {
            Path dirPath = Paths.get(inputPath);
            if (!Files.isDirectory(dirPath)) {
                System.out.println("Please double check, invalid directory path.");
                return;
            }
            boolean exit = false;
            while (!exit) {
                System.out.println("Select an option:");
                System.out.println("1) Display information");
                System.out.println("2) Copy file");
                System.out.println("3) Move file");
                System.out.println("4) Delete a file");
                System.out.println("5) Create a new directory");
                System.out.println("6) Delete a directory");
                System.out.println("7) Search for files");
                System.out.println("8) Exit/End Session");

                int choice = scanner.nextInt();
                scanner.nextLine();

                //-----DISPLAY INFORMATION
                if (choice == 1) {
                    displayContent(dirPath);
                    System.out.println();
                //------COPY FILE
                } else if (choice == 2) {
                    System.out.println("Enter the file name:");
                    String sourceFile = scanner.nextLine();
                    System.out.println("Customize the updated file name:");
                    String targetFile = scanner.nextLine();
                    copyFile(dirPath.resolve(sourceFile), dirPath.resolve(targetFile));
                //-----MOVE FILE
                } else if (choice == 3) {
                    System.out.println("Enter the file name:");
                    String srcFile = scanner.nextLine();
                    System.out.println("Customize the updated file name:");
                    String tgtFile = scanner.nextLine();
                    moveFile(dirPath.resolve(srcFile), dirPath.resolve(tgtFile));
                //-----DELETE A FILE
                } else if (choice == 4) {
                    System.out.println("Delete the following file:");
                    String fileToDelete = scanner.nextLine();
                    deleteFile(dirPath.resolve(fileToDelete));
                //-----CREATE DIRECTORY
                } else if (choice == 5) {
                    System.out.println("Provide the name of your new directory:");
                    String newDir = scanner.nextLine();
                    createDirectory(dirPath.resolve(newDir));
                //-----DELETE DIRECTORY
                } else if (choice == 6) {
                    System.out.println("Provide the directory name you wish to delete:");
                    String dirToDelete = scanner.nextLine();
                    deleteDirectory(dirPath.resolve(dirToDelete));
                //-----SEARCH FILE
                } else if (choice == 7) {
                    System.out.println("Search for file name:");
                    String searchTerm = scanner.nextLine();
                    searchFiles(dirPath, searchTerm);
                //-----END SESSION/END
                } else if (choice == 8) {
                    exit = true;
                //----INVALID/NON OF THE ABOVE SELECTION
                } else {
                    System.out.println("Invalid option (1-8 only).");
                }
            }
        //----INVALID PATH
        } catch (InvalidPathException e) {
            System.out.println("Invalid path: " + e.getMessage());
        }
    }

    private static void displayContent(Path dirPath) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            System.out.println("Directory information/content:");
            for (Path entry : stream) {
                BasicFileAttributes attrs = Files.readAttributes(entry, BasicFileAttributes.class);
                String type = attrs.isDirectory() ? "DIR" : "FILE";
                long size = attrs.size(); // GET THE SIZE OF THE FILE IN BYTES
                LocalDateTime lastModified = LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneOffset.UTC);

                String entryInfo = type + " | " + size + " | " + lastModified.format(formatter) + " | " + entry.getFileName();
                System.out.println(entryInfo);
            }
        } catch (IOException e) {
            System.out.println("Error reading directory contents: " + e.getMessage());
        }
    }

//PATH SOURCE: OLD NAME
//PATH TARGET: NEW NAME
    private static void copyFile(Path source, Path target) {
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully.");
        } catch (IOException e) {
            System.out.println("Error copying file: " + e.getMessage());
        }
    }

//The StandardCopyOption.REPLACE_EXISTING option can be used to
// overwrite the file if it already exists at the target location
//The Files.move() method allows for this with its third
// parameter—options of type CopyOption.
    private static void moveFile(Path source, Path target) {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved successfully.");
        } catch (IOException e) {
            System.out.println("Error moving file: " + e.getMessage());
        }
    }

    private static void deleteFile(Path file) {
        try {
            Files.delete(file);
            System.out.println("File deleted successfully.");
        } catch (IOException e) {
            System.out.println("Error deleting file: " + e.getMessage());
        }
    }

    private static void createDirectory(Path dir) {
        try {
            Files.createDirectory(dir);
            System.out.println("Directory created successfully.");
        } catch (IOException e) {
            System.out.println("Error creating directory: " + e.getMessage());
        }
    }

    private static void deleteDirectory(Path dir) {
        try {
            Files.delete(dir);
            System.out.println("Directory deleted successfully.");
        } catch (IOException e) {
            System.out.println("Error deleting directory: " + e.getMessage());
        }
    }

//Search for files within the specified directory based on file name or extension
    private static void searchFiles(Path dir, String searchTerm) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, searchTerm)) {
            System.out.println("Search results:");
            for (Path entry : stream) {
                System.out.println(entry.getFileName());
            }
        } catch (IOException e) {
            System.out.println("Error searching for files: " + e.getMessage());
        }
    }
}