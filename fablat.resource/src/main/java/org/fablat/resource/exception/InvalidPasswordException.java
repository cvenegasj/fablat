package org.fablat.resource.exception;

public final class InvalidPasswordException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidPasswordException() {
		super();
	}

	public InvalidPasswordException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public InvalidPasswordException(final String message) {
		super(message);
	}

	public InvalidPasswordException(final Throwable cause) {
		super(cause);
	}
	
}
