package com.angelo.hiniid;

// http://hinii
// com.angelo.hiniid.HiniiSearchActivity
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.error.LocationException;
import com.angelo.hiniid.util.MenuUtils;

/**
 * alpha
 * 
 * @author angelo, from AddFriendsActivity
 */
public class HiniiSearchActivity extends Activity {

	private SearchLocationObserver mSearchLocationObserver = new SearchLocationObserver();
	private static final String TAG = "HiniiSearchActivity";
	private static final boolean DEBUG = Hinii.DEBUG;
	public static final String EXTRA_HINII_SEARCH_COMPANY = "EXTRA_HINII_SEARCH_COMPANY";
	public static final String EXTRA_HINII_SEARCH_DAYKIND = "EXTRA_HINII_SEARCH_DAYKIND";
	public static final String EXTRA_HINII_SEARCH_DAYPART = "EXTRA_HINII_SEARCH_DAYPART";
	public static final String EXTRA_HINII_SEARCH_CITY = "EXTRA_HINII_SEARCH_CITY";
	public static final String EXTRA_HINII_SEARCH_CITY_ELSEWHERE = "EXTRA_HINII_SEARCH_CITY_ELSEWHERE";
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	String strSelectionCompany = "0";
	String strSelectionDaykind = "0";
	String strSelectionDaypart = "0";
	CharSequence strSelectionCity;
	TextView textViewWhereToSearch;
	Dialog dialog;
	Spinner spinnerSearchCompany;
	Spinner spinnerSearchDaykind;
	Spinner spinnerSearchDaypart;
	int lastSelectedCompany = 2;
	int lastSelectedDaykind = 0;
	int lastSelectedDaypart = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG)
			Log.d(TAG, "onCreate()");
		((Hiniid) getApplication())
				.requestLocationUpdates(mSearchLocationObserver);
		setContentView(R.layout.hinii_search_activity);

		textViewWhereToSearch = (TextView) findViewById(R.id.textViewWhereToSearch);
		strSelectionCity = textViewWhereToSearch.getText();
		spinnerSearchCompany = (Spinner) findViewById(R.id.SpinnerSearchCompany);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.hinii_search_company_item,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// spinnerSearchCompany.setSelection(2);

		spinnerSearchCompany.setAdapter(adapter);
		spinnerSearchCompany.setSelection(lastSelectedCompany);

		spinnerSearchCompany
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> adapter,
							View view, int position, long id) {
						lastSelectedCompany = position;

						String[] values = HiniiSearchActivity.this
								.getResources()
								.getStringArray(
										R.array.hinii_search_company_item_values);
						strSelectionCompany = values[position];

						if ((position != 1) & (lastSelectedDaykind == 1)) {
							spinnerSearchDaykind.setSelection(0);
							lastSelectedDaykind = 0;
						} else if ((position == 4) & (lastSelectedDaykind == 3)) {
							spinnerSearchDaykind.setSelection(0);
							lastSelectedDaykind = 0;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		// ------------------------

		spinnerSearchDaykind = (Spinner) findViewById(R.id.SpinnerSearchDaykind);
		String[] values = HiniiSearchActivity.this.getResources()
				.getStringArray(R.array.hinii_search_daykind_item);
		ArrayAdapterDaykind adapterDaykind = new ArrayAdapterDaykind(this,
				android.R.layout.simple_spinner_item, values);
		adapterDaykind
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerSearchDaykind.setAdapter(adapterDaykind);
		spinnerSearchDaykind
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> adapter,
							View view, int position, long id) {

						lastSelectedDaykind = position;
						String[] values = HiniiSearchActivity.this
								.getResources()
								.getStringArray(
										R.array.hinii_search_daykind_item_values);
						strSelectionDaykind = values[position];

						// quando si seleziona sport o golf, bisognerebbe
						// disattivare Notte
						if (((position == 3) | (position == 6))
								& (lastSelectedDaypart == 2)) {
							spinnerSearchDaypart.setSelection(3);
							lastSelectedDaypart = 3;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		// ------------------------
		spinnerSearchDaypart = (Spinner) findViewById(R.id.SpinnerSearchDaypart);
		values = HiniiSearchActivity.this.getResources().getStringArray(
				R.array.hinii_search_daypart_item);
		ArrayAdapterDaypart adapterDaypart = new ArrayAdapterDaypart(this,
				android.R.layout.simple_spinner_item, values);
		adapterDaypart
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerSearchDaypart.setAdapter(adapterDaypart);
		spinnerSearchDaypart.setSelection(lastSelectedDaypart);

		spinnerSearchDaypart
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> adapter,
							View view, int position, long id) {
						lastSelectedDaypart = position;
						String[] values = HiniiSearchActivity.this
								.getResources()
								.getStringArray(
										R.array.hinii_search_daypart_item_values);
						strSelectionDaypart = values[position];
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		LinearLayout hiniiSearchElsewhere = (LinearLayout) findViewById(R.id.hiniiSearchElsewhere);
		hiniiSearchElsewhere.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = new Dialog(HiniiSearchActivity.this);
				dialog.setContentView(R.layout.hinii_search_city_elsewhere);
				dialog.setTitle(R.string.hinii_type_a_place_or_speak);
				dialog.setCancelable(true);

				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
						HiniiSearchActivity.this,
						android.R.layout.simple_dropdown_item_1line, CITIES);
				final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) dialog
						.findViewById(R.id.AutoCompleteTextView01);
				autoCompleteTextView.setAdapter(adapter2);

				final LinearLayout searchElsewhereVoiceRecognitionandroid = (LinearLayout) dialog
						.findViewById(R.id.hiniiSearchElsewhereVoiceRecognition);

				PackageManager pm = getPackageManager();
				List<ResolveInfo> activities = pm
						.queryIntentActivities(new Intent(
								RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
				if (activities.size() != 0) {
					autoCompleteTextView
							.setHint(R.string.hinii_enter_place_using_keyboard_or_voice);
					searchElsewhereVoiceRecognitionandroid
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
									intent.putExtra(
											RecognizerIntent.EXTRA_LANGUAGE_MODEL,
											RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
									String strExtraPrompt = getResources()
											.getString(
													R.string.hinii_voice_recognition_prompt);
									intent.putExtra(
											RecognizerIntent.EXTRA_PROMPT,
											strExtraPrompt);
									startActivityForResult(intent,
											VOICE_RECOGNITION_REQUEST_CODE);
								}
							});
				} else {
					// imageViewSearchElsewhereVoiceRecognitionandroid.setEnabled(false);
					searchElsewhereVoiceRecognitionandroid
							.setVisibility(View.INVISIBLE);
				}

				final CheckBox checkBoxNearby = (CheckBox) dialog
						.findViewById(R.id.checkBoxNearby);
				checkBoxNearby
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								autoCompleteTextView.setEnabled(!isChecked);
								searchElsewhereVoiceRecognitionandroid
										.setVisibility(isChecked ? View.INVISIBLE
												: View.VISIBLE);
							}
						});

				Button btnSearchCityElsewhere = (Button) dialog
						.findViewById(R.id.btnSearchCityElsewhere);
				btnSearchCityElsewhere
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {

								String strInput = autoCompleteTextView
										.getText().toString();
								if (checkBoxNearby.isChecked()
										| (strInput.equals("")))
									strSelectionCity = getResources()
											.getString(
													R.string.hinii_search_city);
								else
									strSelectionCity = strInput;

								textViewWhereToSearch.setText(strSelectionCity);
								dialog.dismiss();
							}
						});
				dialog.show();
			}
		});

		Button btnSearch = (Button) findViewById(R.id.btnHiniiSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(HiniiSearchActivity.this,
						HiniiSearchResultsActivity.class);
				// PointValueListActivity.class);
				intent.putExtra(EXTRA_HINII_SEARCH_COMPANY, strSelectionCompany);
				intent.putExtra(EXTRA_HINII_SEARCH_DAYKIND, strSelectionDaykind);
				intent.putExtra(EXTRA_HINII_SEARCH_DAYPART, strSelectionDaypart);

				intent.putExtra(EXTRA_HINII_SEARCH_CITY, strSelectionCity);

				if (strSelectionCity == null
						| (strSelectionCity.equals(""))
						| (strSelectionCity.toString()
								.equalsIgnoreCase(getResources().getString(
										R.string.hinii_search_where_nearby)))) {
					// if (strSelectionCity.equals("Nearby")) {
					Location location;
					try {
						location = ((Hiniid) getApplication())
								.getLastKnownLocationOrThrow();
					} catch (LocationException e) {
						location = null;
					}
					if (location == null) {
						showDialogNoLocation();

						return;
					}
				}

				startActivity(intent);
			}
		});

		// textView.setTokenizer(new
		// MultiAutoCompleteTextView.CommaTokenizer());

	}

	void showDialogNoLocation() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.localization_failed)
				.setPositiveButton(R.string.localization_go_enable,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent myIntent = new Intent(
										Settings.ACTION_SECURITY_SETTINGS);
								startActivity(myIntent);
								((Hiniid) getApplication())
										.requestLocationUpdates(mSearchLocationObserver);
							}
						})
				.setNegativeButton(R.string.localization_do_not_enable,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								((Hiniid) getApplication())
										.requestLocationUpdates(mSearchLocationObserver);
							}
						});
		builder.show();
	}

	class ArrayAdapterDaykind extends ArrayAdapter<CharSequence> {

		public ArrayAdapterDaykind(Context c, int intero, String[] st) {
			super(c, intero, st);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			View view = super.getDropDownView(position, convertView, parent);
			int selectedCompany = spinnerSearchCompany
					.getSelectedItemPosition();
			// quando non in coppia, disabilita romantica
			if ((selectedCompany != 1) & (position == 1)) {
				view.setEnabled(false);
				view.setClickable(true);
			}
			// quando coi colleghi, disabilita sportiva
			else if ((selectedCompany == 4) & (position == 3)) {
				view.setEnabled(false);
				view.setClickable(true);
			} else {
				view.setEnabled(true);
				view.setClickable(false);
			}
			return view;
		}
	}

	class ArrayAdapterDaypart extends ArrayAdapter<CharSequence> {

		public ArrayAdapterDaypart(Context c, int intero, String[] st) {
			super(c, intero, st);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			View view = super.getDropDownView(position, convertView, parent);
			// quando si seleziona sport o golf, bisognerebbe disattivare Notte
			int selectedDaykind = spinnerSearchDaykind
					.getSelectedItemPosition();
			if (((selectedDaykind == 3) | (selectedDaykind == 6))
					& (position == 2)) {
				view.setEnabled(false);
				view.setClickable(true);
			} else {
				view.setEnabled(true);
				view.setClickable(false);
			}
			return view;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuUtils.addPreferencesToMenu(this, menu);

		return true;
	}

	/**
	 * Handle the results from the recognition activity.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// Fill the list view with the strings the recognizer thought it
			// could have heard
			ArrayList<String> matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) dialog
					.findViewById(R.id.AutoCompleteTextView01);

			if (autoCompleteTextView != null) {
				if (!matches.isEmpty()) {
					String voiceResult = (String) matches.get(0);
					autoCompleteTextView.setText(voiceResult);
					autoCompleteTextView.dismissDropDown();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class SearchLocationObserver implements Observer {
		@Override
		public void update(Observable observable, Object data) {
		}
	}

	static final String[] CITIES = new String[] {

	"Roma", "Milano", "Napoli", "Torino", "Palermo", "Genova", "Bologna",
			"Firenze", "Bari", "Catania", "Venezia", "Verona", "Messina",
			"Trieste", "Padova", "Taranto", "Brescia", "Reggio Calabria",
			"Modena", "Prato", "Cagliari", "Parma", "Livorno", "Foggia",
			"Perugia", "Reggio Emilia", "Salerno", "Ravenna", "Ferrara",
			"Rimini", "Siracusa", "Sassari", "Monza", "Pescara", "Bergamo",
			"Forlì", "Latina", "Vicenza", "Terni", "Trento", "Novara",
			"Ancona", "Giugliano in Campania", "Andria", "Piacenza",
			"Catanzaro", "Udine", "Bolzano", "Barletta", "Arezzo", "La Spezia",
			"Pesaro", "Cesena", "Torre del Greco", "Pisa", "Brindisi",
			"Alessandria", "Pistoia", "Lecce", "Casoria", "New York",
			"Los Angeles", "San Francisco", "Chicago", "Sidney", "Melbourne",
			"London", "Paris", "Moskow", "Berlin", "Hamburg", "Munich",
			"Lisbon", "Madrid", "Barcelona", "Bilbao", "Wien", "Zurich",
			"Bern", "Geneve", "Warszawa",

			"Kabul", "Tirana", "Algiers", "Andorra la Vella", "Luanda",
			"St. John's", "Buenos Aires", "Yerevan", "Canberra", "Baku",
			"Nassau", "Manama", "Dhaka", "Bridgetown", "Minsk", "Brussels",
			"Belmopan", "Brasília", "Ottawa", "Havana", "Prague", "Copenhagen",
			"Helsinki", "Budapest", "Reykjavík", "New Delhi", "Jakarta",
			"Dublin", "Tokyo", "Seoul"

	};
}
