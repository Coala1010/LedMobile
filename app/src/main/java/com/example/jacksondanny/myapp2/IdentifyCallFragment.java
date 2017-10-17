package com.example.jacksondanny.myapp2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;

public class IdentifyCallFragment extends Fragment {

    View view;
    private ArrayList<String> contactNameList;
    private ArrayList<Integer> colorList;
    private ArrayAdapter<String> adapter;
    private static final int REQUEST_READ_CONTACTS = 0;

    public static IdentifyCallFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        IdentifyCallFragment identifyCall = new IdentifyCallFragment();
        identifyCall.setArguments(args);
        return identifyCall;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_identify_call, container, false);


        final MenuActivity myMenu = (MenuActivity)getActivity();

        contactNameList = new ArrayList<>();
        colorList = new ArrayList<>();
        Iterator myVeryOwnIterator = MyGlobalValues.contacts_colorList.keySet().iterator();
        while(myVeryOwnIterator.hasNext()) {
            String key=(String)myVeryOwnIterator.next();
            contactNameList.add(key);
            colorList.add(MyGlobalValues.contacts_colorList.get(key));
          //  String value=(String)myMenu.contacts_colorList.get(key);
         //   Toast.makeText(ctx, "Key: "+key+" Value: "+value, Toast.LENGTH_LONG).show();
        }

     /*   ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                contactNameList.add(name);
            //    colorList.add(Color.GREEN);

                if (cur.getInt(cur.getColumnIndex(
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
                }
            }
        }*/
        FragmentManager myFragmentManager = getFragmentManager();

        adapter = new ContactViewAdapter(this, R.layout.item_listview, contactNameList, colorList, myFragmentManager);
        ListView contactList = (ListView) view.findViewById(R.id.list_contact);
        contactList.setAdapter(adapter);

        return view;
    }

}
