package ro.ubb.lftc.model;

public class CustomException extends Exception {
	public CustomException(String s) {
		super(s);
	}

	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}
}
