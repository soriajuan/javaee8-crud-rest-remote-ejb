package person.common.exception;

public class EntityExistsException extends RuntimeException {

	private static final long serialVersionUID = 68957668535514551L;

	public EntityExistsException(String message) {
		super(message);
	}

	public EntityExistsException(String message, Throwable cause) {
		super(message, cause);
	}

}
