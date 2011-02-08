package com.angelo.hiniid.rest.error;

/**
 * @author Angelo Schiavone
 */
public class LocationException extends HiniiException {

	public LocationException() {
		super("Unable to determine where you are.");
	}

	public LocationException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
