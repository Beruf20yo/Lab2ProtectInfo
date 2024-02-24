package lab2;

import lombok.Getter;

import java.util.Comparator;

public class FileObj {
    @Getter
    private String filling;

    public FileObj(String fileName) {
        this.fileName = fileName;
    }
    @Getter
    private final String fileName;
    public static class FileComp implements Comparator<FileObj> {
        @Override
        public int compare(FileObj o1, FileObj o2) {
            return o1.fileName.hashCode() - o2.fileName.hashCode();
        }
    }
    public void writeToFile(String letter){
        filling = filling + " "+ letter;
    }
}
