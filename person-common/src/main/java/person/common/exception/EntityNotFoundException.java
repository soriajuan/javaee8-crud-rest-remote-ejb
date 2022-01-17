package person.common.exception;

public class EntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 872268864924063486L;

	public EntityNotFoundException(String message) {
		super(message);
	}

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
