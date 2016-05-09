package cs.ustc.MaxSATsolver;

/**
 * 
 * @author ccding 
 * 2016年3月5日下午8:03:37
 */

@SuppressWarnings("serial")
public class ParseFormatException extends Exception {


	/**
     * Constructor for ParseFormatException.
     */
    public ParseFormatException() {
        super("DIMACS Format Error");
    }

    /**
     * Constructor for ParseFormatException.
     * @param message
     */
    public ParseFormatException(String message) {
        super("DIMACS Format error: " + message);
    }

    /**
     * Constructor for ParseFormatException.
     * @param message
     * @param cause
     */
    public ParseFormatException(String message, Throwable cause) {
        super("DIMACS Format error: " + message, cause);
    }

    /**
     * Constructor for ParseFormatException.
     * @param cause
     */
    public ParseFormatException(Throwable cause) {
        super(cause);
    }

}
