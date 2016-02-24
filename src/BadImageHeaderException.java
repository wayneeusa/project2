


public class BadImageHeaderException extends RuntimeException{

    /**
     * Construct an instance of this exception with a custom message
     *
     * @param message the custom message to be wrapped around by this exception
     */
    public BadImageHeaderException(String message){
        super(message);
    }

    /**
     * Construct an instance of this exception without any message
     */
    public BadImageHeaderException(){
        super();
    }
}

