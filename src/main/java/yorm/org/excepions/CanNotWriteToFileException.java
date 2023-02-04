package yorm.org.excepions;

public class CanNotWriteToFileException extends RuntimeException{

    public CanNotWriteToFileException(String message) {
        super(message);
    }
}
