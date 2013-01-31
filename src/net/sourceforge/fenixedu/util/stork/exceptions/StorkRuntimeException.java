package net.sourceforge.fenixedu.util.stork.exceptions;

public class StorkRuntimeException extends RuntimeException {

	public StorkRuntimeException() {
	}

	public StorkRuntimeException(String message) {
		super(message);
	}

	public StorkRuntimeException(Throwable cause) {
		super(cause);
	}

	public StorkRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
