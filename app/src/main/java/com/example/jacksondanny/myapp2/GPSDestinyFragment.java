package com.example.jacksondanny.myapp2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GPSDestinyFragment extends Fragment
        implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,LocationListener {

    private View mView;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private ImageView imgMyLocation;
    private double myLatitude, myLongitude;
    private double curLatitude, curLongitude;
    private Boolean b_Longpress = true;
    Geocoder geocoder;
    private static List<Address> addresses;
    private ArrayList<String> latitudeList, longitudeList, colorList;
    private MenuActivity myMenu;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    public GPSDestinyFragment() {
    }

    public static GPSDestinyFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        GPSDestinyFragment secondFragment = new GPSDestinyFragment();
        secondFragment.setArguments(args);
        return secondFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_gpsdestiny, container, false);

        myMenu = (MenuActivity)getActivity();

        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();
        colorList = new ArrayList<>();
        latitudeList = myMenu.mydb.getAllLatitude();
        longitudeList = myMenu.mydb.getAllLongitude();
        colorList = myMenu.mydb.getAllColors();

        Button btn_list = (Button) mView.findViewById(R.id.btn_list);
        //---Resize Button
        ViewGroup.LayoutParams params = btn_list.getLayoutParams();
        params.width = myMenu.screenWidth/2-10;
        btn_list.setLayoutParams(params);
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Toast.makeText(getActivity().getApplicationContext(), "How many rows? = " + String.valueOf(myMenu.mydb.numberOfRows()), Toast.LENGTH_SHORT).show();
                Fragment targetListFragment = new TargetListFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, targetListFragment); // f2_container is your FrameLayout container
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        SwitchCompat mySwitch = (SwitchCompat) mView.findViewById(R.id.switch1);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(b_Longpress)
                    b_Longpress = false;
                else
                    b_Longpress = true;
                Log.d("long press", String.valueOf(b_Longpress));
                // do something, the isChecked will be
                // true if the switch is in the On position
            }
        });

        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //  Log.i(tag, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK ) {
                    //   Log.i(tag, "onKey Back listener is working!!!");
                    //       getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getActivity().moveTaskToBack(true);
                    getActivity().finish();
                    return true;
                } else {
                    return false;
                }
            }

        });
        return mView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //    googleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247, -74.044502)).title("Statue of Liberty").snippet("I hope to go there"));
    //    CameraPosition Liberty = CameraPosition.builder().target(new LatLng(40.689247, -74.044502)).zoom(4).bearing(0).build();
    //    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));

      //  mGoogleMap.setOnMyLocationButtonClickListener(this);
      //  mGoogleMap.setMyLocationEnabled(true);
      //  enableMyLocation();


        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
          /*  if(!mGoogleMap.isMyLocationEnabled())
                mGoogleMap.setMyLocationEnabled(true);*/


            LocationManager locationManager = (LocationManager)  getActivity().getSystemService(Context.LOCATION_SERVICE);
            LocationManager lm = (LocationManager)  getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                Log.e("TAG", "GPS is on");
                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();
            //    Toast.makeText(getActivity(), "latitude:" + myLatitude + " longitude:" + myLongitude, Toast.LENGTH_SHORT).show();
            //    searchNearestPlace(voice2text);
            }


            Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (myLocation!=null){
                myLatitude = myLocation.getLatitude();
                myLongitude = myLocation.getLongitude();
            }

            if(myLocation!=null){
                LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14), 1500, null);
            }
         //   GeoLocationHelper geoLocationHelper;
            //if(myLocation!=null)
        }



        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(myLatitude, myLongitude)).icon(BitmapDescriptorFactory.fromBitmap(getCroppedBitmap(40, 40,1))));



        if (mGoogleMap != null) {
            imgMyLocation = (ImageView) mView.findViewById(R.id.imgMyLocation);
            imgMyLocation.getLayoutParams().height = myMenu.screenHeight/10;
            imgMyLocation.getLayoutParams().width = myMenu.screenWidth/8;
            imgMyLocation.requestLayout();
            imgMyLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mGoogleMap != null) {
                        //     LatLng latLng = new LatLng(Double.parseDouble(getLatitude()), Double.parseDouble(getLongitude()));
//                        myLatitude = mGoogleMap.getMyLocation().getLatitude();
  //                      myLongitude = mGoogleMap.getMyLocation().getLongitude();
                        LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(myLatitude)), Double.parseDouble(String.valueOf(myLongitude)));
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                        mGoogleMap.animateCamera(cameraUpdate);
                    }
                }
            });

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                @Override
                public void onMapLongClick(LatLng latLng) {
                    if (b_Longpress == true) {
                        Log.d("long press", String.valueOf(b_Longpress));
                        curLatitude = latLng.latitude;
                        curLongitude = latLng.longitude;
                        View myPoupView = (View) mView.findViewById(R.id.popupView);
                        showPopup(mView);
                    }
                }
            });


            for (int i = 0; i < latitudeList.size(); i++) {
                Marker melbourne = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitudeList.get(i)), Double.parseDouble(longitudeList.get(i)))).icon(getMarkerIcon(Integer.parseInt(colorList.get(i)))));
            }
            if(myMenu.mydb.numberOfLocation() != 0)
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(myMenu.mydb.getLatitudeLocation(), myMenu.mydb.getLongitudeLocation())).icon(BitmapDescriptorFactory.fromBitmap(getCroppedBitmap(40, 40,0))));

            //enableMyLocation();
        }
    }

    public BitmapDescriptor getMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    public Bitmap getCroppedBitmap(int width, int height, int flag) {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        //  paint.setColor(Color.RED);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(width / 2, height / 2,  width / 2, paint);
        if(flag == 0)
        paint.setColor(myMenu.mydb.getColorLocation());
        else
            paint.setColor(Color.rgb(15, 129, 254));
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(width / 2, height / 2,  width / 2-4, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);
        return output;
    }

    public void showPopup(final View anchorView) {

      //  View
        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // Example: If you have a TextView inside `popup_layout.xml`
      //  TextView tv = (TextView) popupView.findViewById(R.id.tv);

     //   tv.setText(....);

        // Initialize more widgets from `popup_layout.xml`
     //   ....
     //   ....

        // If the PopupWindow should be focusable
        popupWindow.setFocusable(true);

        // If you need the PopupWindow to dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Using location, the PopupWindow will be displayed right under anchorView
      //  popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, location[0], location[1] + anchorView.getHeight());
        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 17);

        Button btn_addDestination=(Button)popupView.findViewById(R.id.btn_addDestination);
        btn_addDestination.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(curLatitude, curLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    myMenu.pubLatitude = curLatitude;
                    myMenu.pubLongitude = curLongitude;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                MenuActivity myMenu = (MenuActivity)getActivity();
                myMenu.pub_addresses = addresses;
                Fragment destination_Add = new destination_add();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, destination_Add); // f2_container is your FrameLayout container
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                popupWindow.dismiss();
            }
        });

        Button btn_setStartLocation=(Button)popupView.findViewById(R.id.btn_setStartLocation);
        btn_setStartLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(curLatitude, curLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    myMenu.pubLatitude = curLatitude;
                    myMenu.pubLongitude = curLongitude;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                MenuActivity myMenu = (MenuActivity)getActivity();
                myMenu.pub_addresses = addresses;
                myMenu.pubFlag = 1;
                Fragment destination_Add = new destination_add();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, destination_Add); // f2_container is your FrameLayout container
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
                popupWindow.dismiss();
            }
        });

        Button btn_Cancel=(Button)popupView.findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindow.dismiss();
            }
        });
    }

 /*   private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mGoogleMap != null) {
            // Access to the location has been granted to the app.
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }
    */


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "My Location Button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getFragmentManager(), "dialog");
    }


    @Override
    public void onLocationChanged(Location location) {
        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (myLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            myLocation = lm.getLastKnownLocation(provider);
        }

        if(myLocation!=null){
            LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14), 1500, null);
        }
        myLatitude = myLocation.getLatitude();
        myLongitude = myLocation.getLongitude();

    }
}


