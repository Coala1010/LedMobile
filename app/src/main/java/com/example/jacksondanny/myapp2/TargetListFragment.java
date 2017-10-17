package com.example.jacksondanny.myapp2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TargetListFragment extends Fragment {
    View view;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> friendsList;
    private ArrayList<String> colorList;
    private ArrayList<String> latitudeList;
    private ArrayList<String> longitudeList;
    private TextView totalClassmates;
    private SwipeLayout swipeLayout;

  //  private final static String "TAG" = MainActivity.class.getSimpleName();

    public static TargetListFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("Accelerations Instance", instance);
        TargetListFragment TargetListFragment = new TargetListFragment();
        TargetListFragment.setArguments(args);

        return TargetListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_target_list, container, false);

        final MenuActivity myMenu = (MenuActivity)getActivity();

        listView = (ListView)view.findViewById(R.id.list_view);

        friendsList = new ArrayList<>();
        colorList = new ArrayList<>();
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();

        friendsList = myMenu.mydb.getAllCotacts();
        colorList = myMenu.mydb.getAllColors();
        latitudeList = myMenu.mydb.getAllLatitude();
        longitudeList = myMenu.mydb.getAllLongitude();
     //   getDataFromFile();
     //   setListViewHeader();
        setListViewAdapter();

        Button btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_add.getLayoutParams().width = myMenu.screenWidth/5;
        btn_add.getLayoutParams().height = myMenu.screenHeight/18;
        btn_add.requestLayout();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMenu.pubString = null;
                int backStackEntry = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                if ( backStackEntry > 0) {
                    for (int i = 0; i < backStackEntry; i++) {
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                    }
                }

                Fragment destinationMap = new DestinationMap();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, destinationMap); // f2_container is your FrameLayout container
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
                Fragment gpsDestinyFragment = new GPSDestinyFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, gpsDestinyFragment); // f2_container is your FrameLayout container
                ft.commit();
            }
        });
        return view;
    }

    private void getDataFromFile() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("classmates.txt"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String line = reader.readLine();
            while (line != null && !line.equals("")) {
                line = reader.readLine();
                friendsList.add(line); // add line to array list
            }
        } catch (IOException e) {
            //log the exception
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setListViewHeader() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View header = inflater.inflate(R.layout.header_listview, listView, false);
        totalClassmates = (TextView) header.findViewById(R.id.total);
        swipeLayout = (SwipeLayout)header.findViewById(R.id.swipe_layout);
        setSwipeViewFeatures();
        listView.addHeaderView(header);
    }

    private void setSwipeViewFeatures() {
        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, view.findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                Log.i("TAG", "onClose");
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                Log.i("TAG", "on swiping");
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
                Log.i("TAG", "on start open");
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                Log.i("TAG", "the BottomView totally show");
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
                Log.i("TAG", "the BottomView totally close");
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
    }

    private void setListViewAdapter() {
      //  adapter = new ListViewAdapter((MenuActivity) getActivity(), R.layout.item_listview, friendsList);
        adapter = new ListViewAdapter(this, R.layout.item_listview, friendsList, colorList, latitudeList, longitudeList);
        listView.setAdapter(adapter);

    //    totalClassmates.setText("(" + friendsList.size() + ")");
    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged(); //update adapter
//        totalClassmates.setText("(" + friendsList.size() + ")"); //update total friends in list
    }
}
