package com.angelo.hiniid.util;

import com.angelo.hiniid.Hiniid;
import com.angelo.hiniid.R;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author Angelo Schiavone
 */
public class FeedbackUtils {

	private static final String FEEDBACK_EMAIL_ADDRESS_HINII = "info@hinii.com";
	private static final String FEEDBACK_EMAIL_ADDRESS_AUTHOR = "angelo.schiavone@gmail.com";

	public static void sendFeedBackHinii(Context context, Hiniid hiniid) {
		sendFeedBack(context, hiniid, FEEDBACK_EMAIL_ADDRESS_HINII);
	}

	public static void sendFeedBackAuthor(Context context, Hiniid hiniid) {
		sendFeedBack(context, hiniid, FEEDBACK_EMAIL_ADDRESS_AUTHOR);
	}

	private static void sendFeedBack(Context context, Hiniid hiniid,
			String mailAddress) {
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		final String[] mailto = { mailAddress };
		sendIntent.putExtra(Intent.EXTRA_SUBJECT,
				context.getString(R.string.feedback_subject));
		sendIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
		sendIntent.setType("message/rfc822");
		try {
			context.startActivity(Intent.createChooser(sendIntent,
					context.getText(R.string.feedback_subject)));
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(context, context.getText(R.string.feedback_error),
					Toast.LENGTH_SHORT).show();
		}
	}
}
