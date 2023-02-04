package yorm.org.excepions;

public class FileNotFoundException extends RuntimeException{

    public FileNotFoundException(String path) {
        super(path + " was not found");
    }
}
