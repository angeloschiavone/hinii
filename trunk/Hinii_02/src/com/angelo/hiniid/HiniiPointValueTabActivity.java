package com.angelo.hiniid;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.types.HiniiRandomPointResult;

/**
 * @author angelo, from AddFriendsActivity
 */
public class HiniiPointValueTabActivity extends Activity {
	private static final String TAG = "HiniiPointValueTabActivity";
	private static final boolean DEBUG = Hinii.DEBUG;

	private static String decodeClose(String morning, String afternoon,
			String night) {
		StringBuffer ret = new StringBuffer();
		if (morning != null) {
			String[] listClose = morning.split(",");
			for (int i = 0; i < listClose.length; i++) {
				if (!listClose[i].trim().equals(""))
					ret.append(getDayFromNumber(listClose[i]) + " morning, ");
			}
		}
		if (afternoon != null) {
			String[] listClose = afternoon.split(",");
			for (int i = 0; i < listClose.length; i++) {
				if (!listClose[i].trim().equals(""))
					ret.append(getDayFromNumber(listClose[i]) + " afternoon, ");
			}
		}
		if (night != null) {
			String[] listClose = afternoon.split(",");
			for (int i = 0; i < listClose.length; i++) {
				if (!listClose[i].trim().equals(""))
					ret.append(getDayFromNumber(listClose[i]) + " night, ");
			}
		}

		return ret.substring(0, ret.length() - 2);
	}

	private static String getDayFromNumber(String strNum) {

		// Sunday Monday Tuesday Wednesday Thursday Friday Saturday
		int num = Integer.parseInt(strNum.trim());
		if (num == 0)
			return "Monday";
		if (num == 1)
			return "Tuesday";
		if (num == 2)
			return "Wednesday";
		if (num == 3)
			return "Thursday";
		if (num == 4)
			return "Friday";
		if (num == 5)
			return "Saturday";
		if (num == 6)
			return "Sunday";
		return "";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG)
			Log.d(TAG, "onCreate()");
		setContentView(R.layout.hinii_point_value_tab_activity);

		if (getIntent().getExtras().containsKey(
				Hiniid.EXTRA_RANDOM_POINT_RESULT)) {
			HiniiRandomPointResult randomPoint = getIntent().getExtras()
					.getParcelable(Hiniid.EXTRA_RANDOM_POINT_RESULT);
			final String pointValueId = randomPoint.getId();

			final String category = randomPoint.getCurrentCategory();

			TextView textViewMoreOnHinii = (TextView) findViewById(R.id.lineMoreOnHinii);
			textViewMoreOnHinii.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Locale locale = Locale.getDefault();
					boolean isItalian = (locale.getCountry()
							.equalsIgnoreCase("it"));
					startActivity(new Intent( //
							Intent.ACTION_VIEW, Uri.parse("http://hinii.com/"
									+ (isItalian ? "it" : "en")
									+ "/point/view/id/" + pointValueId
									+ "/category/" + category)));
				}
			});

			// THese are the values to show in details tab
			String name = randomPoint.getName();

			String address = randomPoint.getAddress();
			String city = randomPoint.getCity();
			String country = randomPoint.getCountry();

			String typology = randomPoint.getTypology();
			String description = randomPoint.getDescription();
			String speciality = randomPoint.getSpeciality();
			String website = randomPoint.getWebsite();
			String telephone = randomPoint.getTelephone();

			String voteAvg = randomPoint.getVoteAvg();
			String voteNum = randomPoint.getVoteNum();
			String currentCategoryValue = randomPoint.getCurrentCategoryValue();

			String closedMorning = randomPoint.getClosedMorning();
			String closedAfternoon = randomPoint.getClosedAfternoon();
			String closedNight = randomPoint.getClosedNight();
			String cost = randomPoint.getCost();
			String costAvg = randomPoint.getCostAvg();

			ArrayList<String> values = new ArrayList<String>();

			TextView textViewName = (TextView) findViewById(R.id.lineName);
			if (name != null) {
				textViewName.setText(name);
			}

			TextView[] textViews = new TextView[13];
			textViews[0] = (TextView) findViewById(R.id.line0);
			textViews[1] = (TextView) findViewById(R.id.line1);
			textViews[2] = (TextView) findViewById(R.id.line2);
			textViews[3] = (TextView) findViewById(R.id.line3);
			textViews[4] = (TextView) findViewById(R.id.line4);
			textViews[5] = (TextView) findViewById(R.id.line5);
			textViews[6] = (TextView) findViewById(R.id.line6);
			textViews[7] = (TextView) findViewById(R.id.line7);
			textViews[8] = (TextView) findViewById(R.id.line8);
			textViews[9] = (TextView) findViewById(R.id.line9);
			textViews[10] = (TextView) findViewById(R.id.line10);
			textViews[11] = (TextView) findViewById(R.id.line11);
			textViews[12] = (TextView) findViewById(R.id.line12);

			if (address != null) {
				values.add(address + ((city != null) ? (", " + city) : "")
						+ ((country != null) ? (", " + country) : ""));
				// textViews[0].setText(address);
			}
			if (typology != null) {
				values.add(getString(R.string.hinii_typology) + ": " + typology);
			}
			if (description != null) {
				values.add(getString(R.string.hinii_description) + ": "
						+ description);
			}
			if (speciality != null) {
				values.add(getString(R.string.hinii_speciality) + ": "
						+ speciality);
			}

			if (voteAvg != null) {

				if (Integer.parseInt(voteNum.trim()) != 0)
					values.add(getString(R.string.hinii_vote_average)
							+ ": "
							+ voteAvg
							+ "/5 "
							+ (voteNum != null ? ("(" + voteNum + " "
									+ getString(R.string.hinii_voters) + ")")
									: ""));
			}

			if (currentCategoryValue != null) {
				values.add(getString(R.string.hinii_current_category_value)
						+ ": " + currentCategoryValue);
			}
			if ((closedMorning != null) | (closedAfternoon != null)
					| (closedNight != null)) {
				values.add(getString(R.string.hinii_closed)
						+ ": "
						+ decodeClose(closedMorning, closedAfternoon,
								closedNight));
			}
			if (cost != null) {
				if (!cost.equals("0")) {
					values.add(getString(R.string.hinii_cost) + ": " + cost);
				}
			}
			if (costAvg != null) {
				values.add(getString(R.string.hinii_cost_average) + ": "
						+ costAvg);
			}
			if (website != null) {
				values.add(getString(R.string.hinii_website) + ": " + website);
			}
			if (telephone != null) {
				values.add(getString(R.string.hinii_telephone) + ": "
						+ telephone);
			}

			int textViewCOunt = 0;
			for (int i = 0; i < values.size(); i++) {
				textViews[textViewCOunt].setText(values.get(i));

				Linkify.addLinks(textViews[textViewCOunt], Linkify.ALL);

				textViewCOunt++;
			}
			Log.e("values.size() ", "values.size() " + values.size());
			for (int i = values.size(); i < textViews.length; i++) {
				ViewGroup vg = (ViewGroup) (textViews[i].getParent());
				vg.removeView(textViews[i]);

				// textViews[i].setVisibility(View.INVISIBLE);
			}
		}
		/*
		 * 
		 * PointValue pointValue = (PointValue) getParent().getIntent()
		 * .getExtras().get(Hiniid.EXTRA_POINT_VALUE);
		 * 
		 * TextView textViewFirstLine = (TextView) findViewById(R.id.firstLine);
		 * textViewFirstLine.setText(pointValue.getPointAddress());
		 * 
		 * TextView textViewSecondLine = (TextView)
		 * findViewById(R.id.secondLine);
		 * textViewSecondLine.setText("Typology: "
		 * +pointValue.getPointTypology()); TextView timeTextView = (TextView)
		 * findViewById(R.id.timeTextView);
		 * timeTextView.setText("Duration: "+pointValue.getDuration()); TextView
		 * detailsTextView = (TextView) findViewById(R.id.thirdLine);
		 * detailsTextView
		 * .setText("More details on this place should go here.....");
		 * 
		 * 
		 * setTitle(pointValue.getPointName());
		 */

	}
}
