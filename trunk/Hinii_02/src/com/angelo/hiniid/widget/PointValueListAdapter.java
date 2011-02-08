package com.angelo.hiniid.widget;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelo.hiniid.R;
import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.types.Group;
import com.angelo.hiniid.rest.types.PointValue;
import com.angelo.hiniid.util.RemoteResourceManager;

public class PointValueListAdapter extends BaseGroupAdapter<PointValue> implements
		ObservableAdapter {
	
	private static final String TAG = "PointValueListAdapter";
	private static final boolean DEBUG = Hinii.DEBUG;

	private LayoutInflater mInflater;

	private RemoteResourceManager mRrm;
	private RemoteResourceManagerObserver mResourcesObserver;
	private Handler mHandler = new Handler();
	
	public PointValueListAdapter(Context context, RemoteResourceManager rrm) {
		super(context);
		mInflater = LayoutInflater.from(context);
		mRrm = rrm;
		mResourcesObserver = new RemoteResourceManagerObserver();

		mRrm.addObserver(mResourcesObserver);
	}

	public void removeObserver() {
		mRrm.deleteObserver(mResourcesObserver);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// A ViewHolder keeps references to children views to avoid unnecessary
		// calls to findViewById() on each row.
		final ViewHolder holder;

		// When convertView is not null, we can reuse it directly, there is no
		// need to re-inflate it. We only inflate a new View when the
		// convertView supplied by ListView is null.
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.pointvalue_list_item, null);

			// Creates a ViewHolder and store references to the two children
			// views we want to bind data to.
			holder = new ViewHolder();
			holder.photo = (ImageView) convertView.findViewById(R.id.photo);
			holder.firstLine = (TextView) convertView
					.findViewById(R.id.firstLine);
			holder.secondLine = (TextView) convertView
					.findViewById(R.id.secondLine);

			holder.thirdLine = (TextView) convertView
			.findViewById(R.id.thirdLine);
			holder.fourthLine = (TextView) convertView
			.findViewById(R.id.fourthLine);
			
			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}
		

		PointValue pointValue = (PointValue) getItem(position);
		//final User user = checkin.getUser();
		final Uri photoUri = Uri.parse(pointValue.getImage());
		
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(mRrm
					.getInputStream(photoUri));
			holder.photo.setImageBitmap(bitmap);
			Log.e(TAG, "* got photoUri "+photoUri);
		} catch (IOException e) {
			
			Log.e(TAG, "* Ex (mRrm==null) "+(mRrm==null), e );
			
			holder.photo.setImageResource(R.drawable.blank_point);
		}

		String pointValueMsgLine1 = pointValue.getPointName();
		String pointValueMsgLine2 = pointValue.getPointAddress();
		String pointValueMsgLine3 = pointValue.getPointCity();
		String pointValueMsgLine4 = pointValue.getPointTypology();
		holder.firstLine.setText(pointValueMsgLine1);
		if (!TextUtils.isEmpty(pointValueMsgLine2)) {
			holder.secondLine.setVisibility(View.VISIBLE);
			holder.secondLine.setText(pointValueMsgLine2);
		} else {
			holder.secondLine.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(pointValueMsgLine3)) {
			holder.thirdLine.setVisibility(View.VISIBLE);
			holder.thirdLine.setText(pointValueMsgLine3);
		} else {
			holder.thirdLine.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(pointValueMsgLine4)) {
			holder.fourthLine.setVisibility(View.VISIBLE);
			holder.fourthLine.setText(pointValueMsgLine4);
		} else {
			holder.fourthLine.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public void setGroup(Group<PointValue> g) {
		super.setGroup(g);
	
		for (PointValue it : g) {
			Uri photoUri = Uri.parse(it.getImage());
			if (!mRrm.exists(photoUri)) {
				mRrm.request(photoUri);
			}
		}
	}

	private class RemoteResourceManagerObserver implements Observer {
		@Override
		public void update(Observable observable, Object data) {
			if (DEBUG)
				Log.d(TAG, "Fetcher got: " + data);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					notifyDataSetChanged();
				}
			});
		}
	}

	private static class ViewHolder {
		ImageView photo;
		TextView firstLine;
		TextView secondLine;
		TextView thirdLine;
		TextView fourthLine;
	}
}
