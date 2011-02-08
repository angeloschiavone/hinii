package com.angelo.hiniid;

import com.angelo.hiniid.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * @author Angelo Schiavone
 */
public class ChangelogActivity extends Activity {

	private String CHANGELOG_HTML_FILE;
	private WebView mWebViewChanges;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changelog_activity);
		CHANGELOG_HTML_FILE = "file:///android_asset/"
				+ getResources().getString(R.string.changelog_html_file);
		ensureUi();
		
	}

	private void ensureUi() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		LinearLayout llMain = (LinearLayout) findViewById(R.id.layoutMain);

		// We'll force the dialog to be a certain percentage height of the
		// screen.
		mWebViewChanges = new WebView(this);
		mWebViewChanges.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, (int) Math.floor(display
						.getHeight() * 0.5)));

		mWebViewChanges.loadUrl(CHANGELOG_HTML_FILE);

		llMain.addView(mWebViewChanges);
	}
}
