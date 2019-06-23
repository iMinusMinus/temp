package ${package}.api.except;

/**
 * @author iMinusMinus
 * @date 2019-05-03
 */
public class GenericException extends Exception {

    public GenericException() {
        super();
    }

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, Exception ex) {
        super(message, ex);
    }

    public GenericException(Exception ex) {
        super(ex);
    }

}