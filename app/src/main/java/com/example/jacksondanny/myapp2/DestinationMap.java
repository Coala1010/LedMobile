package com.example.jacksondanny.myapp2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Locale;

public class DestinationMap extends Fragment implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,LocationListener{
    private View mView;

    public int pubColor = Color.WHITE;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    MenuActivity myMenu;
    Geocoder geocoder;



    public static DestinationMap newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        DestinationMap secondFragment = new DestinationMap();
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
        mView = inflater.inflate(R.layout.fragment_destination_map, container, false);
        myMenu = (MenuActivity)getActivity();
        pubColor = MenuActivity.pubColor;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            MenuActivity.pub_addresses = geocoder.getFromLocation(MenuActivity.pubLatitude, MenuActivity.pubLongitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView txt_address = (TextView) mView.findViewById(R.id.txt_address);
        if(MenuActivity.pubFlag != 0)
        txt_address.setText(MenuActivity.pub_addresses.get(0).getAddressLine(0));

        final EditText filter = (EditText) mView.findViewById(R.id.searchBox);
        filter.setText(MenuActivity.pubString);
    //    filter.setHint("\uD83D\uDD0D Search for places");
        filter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Fragment searchPlace = new SearchplaceFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, searchPlace); // f2_container is your FrameLayout container
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
           /*     if(hasFocus){
                    Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }*/
            }
        });
        final ImageView delete = (ImageView)mView.findViewById(R.id.cross);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MenuActivity.pubString = null;
                filter.setText("");
            }
        });

        final ImageView imageView6 = (ImageView)mView.findViewById(R.id.imageView6);
        final ImageView imageView7 = (ImageView)mView.findViewById(R.id.imageView7);
        imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,1));
        imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));
        imageView6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MenuActivity.switch_Scene = 3;

                int backStackEntry = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                if ( backStackEntry > 0) {
                    for (int i = 0; i < backStackEntry; i++) {
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                    }
                }

                Fragment colorWheel2 = new ColorWheel2();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, colorWheel2); // f2_container is your FrameLayout container
                ft.commit();
            }
        });

        Button btn_back = (Button) mView.findViewById(R.id.btn_back);
        btn_back.getLayoutParams().width = MenuActivity.screenWidth /5;
        btn_back.getLayoutParams().height = MenuActivity.screenHeight /18;
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

                Fragment targetListFragment = new TargetListFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, targetListFragment); // f2_container is your FrameLayout container
                ft.commit();
            }
        });

        Button btn_add = (Button) mView.findViewById(R.id.btn_add);
        btn_add.getLayoutParams().width = MenuActivity.screenWidth /5;
        btn_add.getLayoutParams().height = MenuActivity.screenHeight /18;
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

                if(MenuActivity.pubLatitude != 0)
                {
                    String address = MenuActivity.pub_addresses.get(0).getAddressLine(0);

                    if (MenuActivity.mydb.insertContact(address, String.valueOf(pubColor), String.valueOf(MenuActivity.pubLatitude), String.valueOf(MenuActivity.pubLongitude))) {
                        Toast.makeText(getActivity().getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(getActivity().getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();


                MenuActivity.pubFlag = 0;

                MenuActivity.pubColor = Color.WHITE;
                Fragment targetListFragment = new TargetListFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, targetListFragment); // f2_container is your FrameLayout container
                ft.commit();
            }
        });
        return mView;
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
        else if(col == 1)
            paint.setColor(pubColor);
        else if(col == 2)
            paint.setColor(Color.alpha(0));
        else if(col == 3)
            paint.setColor(Color.WHITE);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);

        return output;
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

    public void onMapReady(GoogleMap googleMap) {
    //    MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //    googleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247, -74.044502)).title("Statue of Liberty").snippet("I hope to go there"));
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(40.689247, -74.044502)).zoom(4).bearing(0).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
