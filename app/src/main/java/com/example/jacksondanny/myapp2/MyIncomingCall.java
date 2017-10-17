package com.example.jacksondanny.myapp2;

import android.content.ContentUris;
import android.content.Context;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

public class MyIncomingCall extends AppCompatActivity {

    public TelephonyManager tm;
    String name = null;
    String contactId = null;
    InputStream photo_stream;
    ImageView profile;
    TextView phoneNumber;
    String incomingnumber;
    RelativeLayout myBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_incoming_call);

//        getBaseContext().getResources().getColor(Color.GREEN);
        myBackground = (RelativeLayout) findViewById(R.id.activity_my_incoming_call);

        profile = (ImageView)findViewById(R.id.img_profile);
        phoneNumber = (TextView)findViewById(R.id.phoneNumber);
        Bundle bundle =  getIntent().getExtras();
        if(bundle != null)
        {
            incomingnumber = bundle.getString("number");
        //    state = bundle.getInt("state");
        }
        contactslookup(incomingnumber);


        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        ImageView btn_accept = (ImageView) findViewById(R.id.btn_accept);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myapp", "MyIncomingCall");
                Toast.makeText(getBaseContext(), "Accept", Toast.LENGTH_SHORT).show();

                try {
                    TelephonyManager telephonyManager;
                    telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                    Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
                    Method method = classTelephony.getDeclaredMethod("getITelephony");
                    method.setAccessible(true);
                    Object telephonyInterface = method.invoke(telephonyManager);
                    Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
                    Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("acceptCall");
                    methodEndCall.invoke(telephonyInterface);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


        });

        ImageView btn_reject = (ImageView) findViewById(R.id.btn_reject);

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("myapp", "MyIncomingCall");
            //    Toast.makeText(getBaseContext(), "Reject", Toast.LENGTH_SHORT).show();

                try {
                    TelephonyManager telephonyManager;
                    telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                    Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
                    Method method = classTelephony.getDeclaredMethod("getITelephony");
                    method.setAccessible(true);
                    Object telephonyInterface = method.invoke(telephonyManager);
                    Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
                    Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
                    methodEndCall.invoke(telephonyInterface);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });
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
            phoneNumber.setText(name);
        }

        else
        {
            phoneNumber.setText(number);
            myBackground.setBackgroundColor(Color.BLACK);
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
