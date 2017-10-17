package com.example.jacksondanny.myapp2;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    public static int pubColor = Color.WHITE;
    public static int pubSlider = 0;
    public static int switch_Scene = 1;
    public static List<Address> pub_addresses;
    public static DBHelper mydb;
    public static double pubLatitude = 0;
    public static double pubLongitude = 0;
    public static int pubFlag = 0;
    public static String pubString;
    public static int screenWidth;
    public static int screenHeight;
    private BottomNavigationView bottomNavigationView;

//    public static ArrayList<Integer> contacts_colorList;
    public static String contact_sel_pos;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mydb = new DBHelper(this);

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
    //    contacts_colorList = new ArrayList<>();

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                //    colorList.add(Color.GREEN);

                MyGlobalValues.contacts_colorList.put(name,Color.GREEN);

             /*   if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //    Toast.makeText(getContext(), "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                    }
                    pCur.close();
                }*/
            }
        }

        ArrayList<String> tempContactNameList = new ArrayList<>();
        ArrayList<String> contactNameList = new ArrayList<>();
        ArrayList<String> tempContactColorList = new ArrayList<>();

        if(mydb.numberOfPhoneContact() == 0)
        {
            Iterator myVeryOwnIterator = MyGlobalValues.contacts_colorList.keySet().iterator();
            while(myVeryOwnIterator.hasNext()) {
                String key=(String)myVeryOwnIterator.next();
                mydb.insertPhoneContact(key, String.valueOf(MyGlobalValues.contacts_colorList.get(key)));
           //     Toast.makeText(getBaseContext(), key + " Added", Toast.LENGTH_SHORT).show();
            }
         //   MyGlobalValues.contacts_colorList.clear();
        }
        Iterator myVeryOwnIterator = MyGlobalValues.contacts_colorList.keySet().iterator();
        while(myVeryOwnIterator.hasNext()) {
            String key=(String)myVeryOwnIterator.next();
            contactNameList.add(key);
        }


        tempContactNameList = mydb.getAllPhoneContactName();
        tempContactColorList = mydb.getAllPhoneContactColor();
        for(int i=0; i<mydb.numberOfPhoneContact(); i++)
        {
            MyGlobalValues.contacts_colorList.put(tempContactNameList.get(i), Integer.parseInt(tempContactColorList.get(i)));

        }
        // Loop arrayList2 items
     /*   for (String tempContactName : tempContactNameList) {
            // Loop arrayList1 items
            boolean found = false;
            for (String contactName : contactNameList) {
                if (tempContactName == contactName) {
                    found = true;
                }
                else
                {
                    mydb.insertPhoneContact(contactName, String.valueOf(Color.GREEN));
                    Toast.makeText(getBaseContext(), "Added Contact", Toast.LENGTH_SHORT).show();
                }
            }
        }*/


        final GPSDestinyFragment gpsDestinyFragment = new GPSDestinyFragment();

        Bundle args = new Bundle();
        GPSDestinyFragment secondFragment = new GPSDestinyFragment();
        secondFragment.setArguments(args);

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, gpsDestinyFragment);
        ft.commit();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        //   bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.rgb(142, 142, 142)));

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                pubSlider = 0;
                pubColor = Color.WHITE;

                int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
                if ( backStackEntry > 0) {
                    for (int i = 0; i < backStackEntry; i++) {
                        getSupportFragmentManager().popBackStackImmediate();
                    }
                }

                if (item.getItemId() == R.id.gpsItem) {
                    Fragment gpsDestinyFragment = new GPSDestinyFragment();
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, gpsDestinyFragment);
                    ft.commit();
                } else if (item.getItemId() == R.id.accelItem) {
                    Fragment accelerationFragment = new AccelerationFragment();
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, accelerationFragment);
                    ft.commit();
                } else if (item.getItemId() == R.id.colorItem) {
                    Fragment colorpicker = new ColorpickerFragment();
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, colorpicker);
                    ft.commit();

                } else if (item.getItemId() == R.id.identItem) {
                    Fragment identifycall = new IdentifyCallFragment();
                    android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, identifycall);
                    ft.commit();

                } else if (item.getItemId() == R.id.settingItem) {

                }
                return true;
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Menu Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
