package asia.fourtitude.interviewq.jumble.exception;

public class JumbleEngineException extends RuntimeException {

    // Default constructor
    public JumbleEngineException() {
        super();
    }

    // Constructor that accepts a message
    public JumbleEngineException(String message) {
        super(message);
    }

    // Constructor that accepts a message and a cause
    public JumbleEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that accepts a cause
    public JumbleEngineException(Throwable cause) {
        super(cause);
    }
}