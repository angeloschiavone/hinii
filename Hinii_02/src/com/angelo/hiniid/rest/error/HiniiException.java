/**
 * Copyright 2009 Joe LaPenna
 */

package com.angelo.hiniid.rest.error;

/**
 * @author Angelo Schiavone
 */
public class HiniiException extends Exception {
	private static final long serialVersionUID = 1L;

	private String mExtra;

	public HiniiException(String message) {
		super(message);
	}

	public HiniiException(String message, String extra) {
		super(message);
		mExtra = extra;
	}

	public String getExtra() {
		return mExtra;
	}
}
