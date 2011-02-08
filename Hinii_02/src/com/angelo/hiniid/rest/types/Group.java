/**
 * Copyright 2009 Joe LaPenna
 */

package com.angelo.hiniid.rest.types;

import java.util.ArrayList;

/**
 * @author Joe LaPenna (joe@joelapenna.com)
 */
public class Group<T extends HiniiType> extends ArrayList<T> implements
		HiniiType {

	private static final long serialVersionUID = 1L;

	private String mType;

	public void setType(String type) {
		mType = type;
	}

	public String getType() {
		return mType;
	}
}
