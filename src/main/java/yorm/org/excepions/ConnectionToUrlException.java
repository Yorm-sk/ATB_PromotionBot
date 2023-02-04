package yorm.org.excepions;

public class ConnectionToUrlException extends RuntimeException{

    public ConnectionToUrlException(String url) {
        super("Can`t connect to " + url);
    }
}
