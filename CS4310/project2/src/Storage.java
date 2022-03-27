import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

class Storage{
    private String dirtyPath;

    Storage(){
        dirtyPath = "output_page_files/";
        String cleanPath = "page_files/";
        for(int i = 0; i < OperatingSystem.NUMBER_OF_PAGES; i++) {
            String hexValue = Integer.toHexString(i).toUpperCase();
            if(hexValue.length() == 1) hexValue = "0" + hexValue;

            Path source = Paths.get(cleanPath + hexValue + ".pg");
            Path destination = Paths.get(dirtyPath);

            try {
                Files.createDirectory(destination);
            } catch (IOException ignored) {}
            try {
                Files.copy(source, destination.resolve(source.getFileName()), REPLACE_EXISTING);
            } catch (IOException e) {
                System.err.println("Error copying files to from page_files/ to dirty_page_files/");
                System.exit(-1);
            }
        }

    }

    int[] loadPage(int pageNumber){
        int[] page = new int[OperatingSystem.PAGE_SIZE];

        String hexValue = Integer.toHexString(pageNumber).toUpperCase();
        if(hexValue.length() == 1) hexValue = "0" + hexValue;

        try {
            Scanner scanner = new Scanner(new File(dirtyPath + hexValue + ".pg"));
            for(int i = 0; i < OperatingSystem.PAGE_SIZE; i ++){
                page[i] = scanner.nextInt();
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("Page file not found: " + hexValue);
            System.exit(-1);
        }
        return page;
    }

    void writePageToSwap(int pageNumber, int[] frame){
        String hexValue = Integer.toHexString(pageNumber).toUpperCase();
        if(hexValue.length() == 1) hexValue = "0" + hexValue;

        try {
            FileWriter fileWriter = new FileWriter( dirtyPath + hexValue + ".pg");
            for(int i = 0; i < OperatingSystem.PAGE_SIZE; i ++){
                fileWriter.write(frame[i] + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Page file not found: " + hexValue);
            System.exit(-1);
        }
    }
}


