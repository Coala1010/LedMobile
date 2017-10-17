package com.example.jacksondanny.myapp2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class destination_add extends Fragment {

    private View view;
    public static int pubColor = Color.WHITE;
    private ArrayList<String> friendsList;

    public static AccelerationFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("Accelerations Instance", instance);
        AccelerationFragment accelerationFragment = new AccelerationFragment();
        accelerationFragment.setArguments(args);

        return accelerationFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_destination_add, container, false);

        final MenuActivity myMenu = (MenuActivity)getActivity();
        pubColor = myMenu.pubColor;


     //   final String address = myMenu.pub_addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        final String address = myMenu.pub_addresses.get(0).getAddressLine(0);
        String city = myMenu.pub_addresses.get(0).getLocality();
        String state = myMenu.pub_addresses.get(0).getAdminArea();
        String country = myMenu.pub_addresses.get(0).getCountryName();
        String postalCode = myMenu.pub_addresses.get(0).getPostalCode();
        String knownName = myMenu.pub_addresses.get(0).getFeatureName(); // Only if available else return NULL
        TextView txt_title1 = (TextView) view.findViewById(R.id.txt_title1);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        if (myMenu.pubFlag == 1)
        {
            txt_title.setText("Start Location Address");
            txt_title1.setText("Set Start Location");
        }


        String total = address + " " + knownName; //+ " " + state + " " + country + " " + knownName;
        TextView txt_address = (TextView) view.findViewById(R.id.txt_address);
        txt_address.setText(address);


        final ImageView imageView6 = (ImageView)view.findViewById(R.id.imageView6);
        final ImageView imageView7 = (ImageView)view.findViewById(R.id.imageView7);
        imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));
        imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,1));
        imageView6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                myMenu.switch_Scene = 1;
                Fragment colorWheel2 = new ColorWheel2();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, colorWheel2); // f2_container is your FrameLayout container
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        Button btn_back = (Button) view.findViewById(R.id.btn_back);
        btn_back.getLayoutParams().width = myMenu.screenWidth/5;
        btn_back.getLayoutParams().height = myMenu.screenHeight/18;
        btn_back.requestLayout();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int backStackEntry = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                if ( backStackEntry > 0) {
                    for (int i = 0; i < backStackEntry; i++) {
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                    }
                }
                myMenu.pubFlag = 0;
                Fragment gpsDestinyFragment = new GPSDestinyFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, gpsDestinyFragment); // f2_container is your FrameLayout container
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });


        Button btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_add.getLayoutParams().width = myMenu.screenWidth/5;
        btn_add.getLayoutParams().height = myMenu.screenHeight/18;
        btn_add.requestLayout();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int backStackEntry = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                if ( backStackEntry > 0) {
                    for (int i = 0; i < backStackEntry; i++) {
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                    }
                }
                if(myMenu.pubFlag == 0) {
                    if (myMenu.mydb.insertContact(address, String.valueOf(pubColor), String.valueOf(myMenu.pubLatitude), String.valueOf(myMenu.pubLongitude))) {
                        Toast.makeText(getActivity().getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                else if(myMenu.pubFlag == 1) {
                    myMenu.mydb.deleteLocation();

                    if (myMenu.mydb.insertLocation(address, String.valueOf(pubColor), String.valueOf(myMenu.pubLatitude), String.valueOf(myMenu.pubLongitude))) {
                        Toast.makeText(getActivity().getApplicationContext(), "Start Location Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                myMenu.pubFlag = 0;

                myMenu.pubColor = Color.WHITE;
              //  Toast.makeText(getActivity().getApplicationContext(), "How many rows? = " + String.valueOf(myMenu.mydb.numberOfRows()), Toast.LENGTH_SHORT).show();
                Fragment gpsDestinyFragment = new GPSDestinyFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, gpsDestinyFragment); // f2_container is your FrameLayout container
                ft.commit();
            }
        });

        return view;
    }

    public Bitmap getRoundedCornerBitmap(int width, int height, int pixels, int col) {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        //    final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        if(col == 0)
            paint.setColor(Color.BLACK);
        else if(col == 1) {
            paint.setColor(pubColor);
        }
        else if(col == 2)
            paint.setColor(Color.alpha(0));
        else if(col == 3)
            paint.setColor(Color.WHITE);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);

        return output;
    }
}
