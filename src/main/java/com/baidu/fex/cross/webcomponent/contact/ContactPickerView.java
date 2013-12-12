package com.baidu.fex.cross.webcomponent.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

import com.baidu.fex.cross.R;

public class ContactPickerView extends ImageButton implements OnTouchListener{

	public static final int CONTACT_PICKER_REQUEST_CODE = 1001;
	
	private OnSizeChangedListener onSizeChangedListener;
	
	private static final String TAG = ContactPickerView.class.getName();
	
	public ContactPickerView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public ContactPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ContactPickerView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		setBackgroundResource(R.drawable.ic_contact);
		setOnTouchListener(this);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setClickable(true);
	}

	public void lunchPicker() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				ContactsContract.Contacts.CONTENT_URI);
		Log.d(TAG, "contact picker start...");
		((Activity)getContext()).startActivityForResult(intent, CONTACT_PICKER_REQUEST_CODE);
	}
	
	private String parseContactPhone(Cursor cursor) {
		cursor.moveToFirst();
		int phoneColumn = cursor
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		String phoneResult = "";
		if (phoneNum > 0) {
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			Cursor phones = getContext().getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			if (phones.moveToFirst()) {
				for (; !phones.isAfterLast(); phones.moveToNext()) {
					int index = phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
					int typeindex = phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
					int phone_type = phones.getInt(typeindex);
					String phoneNumber = phones.getString(index);
					switch (phone_type) {
					case 2:
						phoneResult = phoneNumber;
						break;
					}
				}
				if (!phones.isClosed()) {
					phones.close();
				}
			}
		}
		return phoneResult.replaceAll("\\s","").replaceFirst("^\\+86", "");
	}
	
	public String getPickResult(Intent data){
		Uri uri = data.getData();
		Cursor c = getContext().getContentResolver().query(uri, null, null,
				null, null);
		return parseContactPhone(c);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if(onSizeChangedListener != null){
			onSizeChangedListener.sizeChanged(w, h);
		}
	}
	
	public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener){
		this.onSizeChangedListener = onSizeChangedListener;
	}
	
	interface OnSizeChangedListener {
		void sizeChanged(int width, int height);
	}

	public boolean onTouch(View view, MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			lunchPicker();
			break;
		}
		return false;
	}
	
	
}
