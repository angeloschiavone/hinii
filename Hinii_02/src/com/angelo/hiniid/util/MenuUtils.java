/**
 * Copyright 2009 Joe LaPenna
 */

package com.angelo.hiniid.util;

import com.angelo.hiniid.HiniiPreferenceActivity;
import com.angelo.hiniid.R;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;

/**
 * Collection of common functions which are called from the menu
 * 
 * @author Alex Volovoy (avolovoy@gmail.com)
 */
public class MenuUtils {
	// Common menu items
	private static final int MENU_PREFERENCES = -1;

	private static final int MENU_GROUP_SYSTEM = 20;

	public static void addPreferencesToMenu(Context context, Menu menu) {
		Intent intent = new Intent(context, HiniiPreferenceActivity.class);
		menu.add(MENU_GROUP_SYSTEM, MENU_PREFERENCES, Menu.CATEGORY_SECONDARY,
				R.string.preferences_label)
				.setIcon(R.drawable.ic_menu_preferences).setIntent(intent);
	}
}
