



public class BadImageContentException extends RuntimeException{
    /**
     * Construct an instance of this exception with a custom message
     *
     * @param message the custom message to be wrapped around by this exception
     */
    public BadImageContentException(String message){
        super(message);
    }

    /**
     * Construct an instance of this exception without any message
     */
    public BadImageContentException(){
        super();
    }
}
