package org.fablat.resource.exception;

public final class DuplicateEmailException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateEmailException() {
		super();
	}

	public DuplicateEmailException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DuplicateEmailException(final String message) {
		super(message);
	}

	public DuplicateEmailException(final Throwable cause) {
		super(cause);
	}

}
