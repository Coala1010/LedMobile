package com.example.jacksondanny.myapp2;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class MyIncomingSMS extends AppCompatActivity {

    public TelephonyManager tm;
    String name = null;
    String contactId = null;
    InputStream photo_stream;
    ImageView profile;
    TextView phoneNumber;
    TextView msgContent;
    String incomingnumber;
    String message_content;
    RelativeLayout myBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_incoming_sms);

//        getBaseContext().getResources().getColor(Color.GREEN);
        myBackground = (RelativeLayout) findViewById(R.id.activity_my_incoming_call);
        myBackground.setBackgroundColor(Color.WHITE);

        profile = (ImageView)findViewById(R.id.img_profile);
        phoneNumber = (TextView)findViewById(R.id.phoneNumber);
        Bundle bundle =  getIntent().getExtras();
        if(bundle != null)
        {
            incomingnumber = bundle.getString("number");
            message_content = bundle.getString("message_content");
        //    state = bundle.getInt("state");
        }
        msgContent = (TextView)findViewById(R.id.smstext);
        msgContent.setText(message_content);
        contactslookup(incomingnumber);



    }

    private void contactslookup(String number)
    {

        Log.v("ffnet", "Started uploadcontactphoto...");

        //InputStream input = null;

        // define the columns I want the query to return
        String[] projection = new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        // encode the phone number and build the filter URI
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        // query time
        Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);

        if (cursor.moveToFirst())
        {
            // Get values from contacts database:
            contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
            name =      cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            myBackground.setBackgroundColor(MyGlobalValues.contacts_colorList.get(name));
         //   phoneNumber.setTextColor(MyGlobalValues.contacts_colorList.get(name));
         //   msgContent.setTextColor(MyGlobalValues.contacts_colorList.get(name));
            phoneNumber.setText(name);
        }

        else
        {
            phoneNumber.setText(number);
            myBackground.setBackgroundColor(Color.BLACK);
        //    phoneNumber.setTextColor(Color.BLACK);
        //    msgContent.setTextColor(Color.BLACK);
            return; // contact not found
        }


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= 14)
        {
            Uri my_contact_Uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
            photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), my_contact_Uri, true);
        }
        else
        {
            Uri my_contact_Uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
            photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), my_contact_Uri);
        }

        if(photo_stream != null)
        {
            BufferedInputStream buf =new BufferedInputStream(photo_stream);
            Bitmap my_btmp = BitmapFactory.decodeStream(buf);
            profile.setImageBitmap(my_btmp);
        }
        else
        {
            profile.setImageResource(R.drawable.usericon);
        }

        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==123 && requestCode==12){
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
