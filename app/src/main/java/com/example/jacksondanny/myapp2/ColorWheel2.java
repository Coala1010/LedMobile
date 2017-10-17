package com.example.jacksondanny.myapp2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;


public class ColorWheel2 extends Fragment {

    String redString, greenString, blueString;
    public int redValue, greenValue, blueValue, pubColor = Color.WHITE;
    TextView rText, gText, bText;
    View view;
    MenuActivity myMenu;

    public static ColorWheel2 newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        ColorWheel2 colorWheel2 = new ColorWheel2();
        colorWheel2.setArguments(args);
        return colorWheel2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_color_wheel2, container, false);
        myMenu = (MenuActivity)getActivity();

        rText = (TextView)view.findViewById(R.id.rText);
        rText.setTextColor(Color.RED);
        gText = (TextView)view.findViewById(R.id.gText);
        gText.setTextColor(Color.GREEN);
        bText = (TextView)view.findViewById(R.id.bText);
        bText.setTextColor(Color.BLUE);

        final Button btn_sel = (Button) view.findViewById(R.id.btn_select);
        //btn_sel.getLayoutParams().height = myMenu.screenHeight/4;
        btn_sel.getLayoutParams().width = myMenu.screenWidth/5;
        btn_sel.getLayoutParams().height = myMenu.screenHeight/18;
        btn_sel.requestLayout();
        final Button btn_can = (Button) view.findViewById(R.id.btn_cancel);
        btn_can.getLayoutParams().width = myMenu.screenWidth/5;
        btn_can.getLayoutParams().height = myMenu.screenHeight/18;
        btn_can.requestLayout();
        final ImageView col_wheel = (ImageView) view.findViewById(R.id.imageView1);

        col_wheel.getLayoutParams().height = myMenu.screenHeight / 2;
        col_wheel.getLayoutParams().width = myMenu.screenHeight / 2;
        col_wheel.requestLayout();

        final ImageView myCirCur = (ImageView) view.findViewById(R.id.imageView4);
        final ImageView imageView6 = (ImageView)view.findViewById(R.id.imageView6);
        final ImageView imageView7 = (ImageView)view.findViewById(R.id.imageView7);
        imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,3));
        imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));

        btn_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity myMenu = (MenuActivity)getActivity();
                myMenu.pubColor = pubColor;

                int backStackEntry = getActivity().getSupportFragmentManager().getBackStackEntryCount();
                if ( backStackEntry > 0) {
                    for (int i = 0; i < backStackEntry; i++) {
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                    }
                }

                if(myMenu.switch_Scene == 1)
                {
                    Fragment dest_add = new destination_add();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, dest_add);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
                else if(myMenu.switch_Scene == 2)
                {
                    Fragment accelerationFragment = new AccelerationFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, accelerationFragment); // f2_container is your FrameLayout container
                    ft.commit();
                }
                else if(myMenu.switch_Scene == 3)
                {
                    Fragment destinationMap = new DestinationMap();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, destinationMap); // f2_container is your FrameLayout container
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
                else if(myMenu.switch_Scene == 4)
                {
               //     myMenu.contacts_colorList.set(myMenu.contact_sel_pos, myMenu.pubColor);
                    MyGlobalValues.contacts_colorList.put(myMenu.contact_sel_pos, myMenu.pubColor);


                    ArrayList<String> contactNameList = new ArrayList<>();
                    Iterator myVeryOwnIterator = MyGlobalValues.contacts_colorList.keySet().iterator();
                    while(myVeryOwnIterator.hasNext()) {
                        String key=(String)myVeryOwnIterator.next();
                        contactNameList.add(key);
                    }

                    for(int i=0; i<contactNameList.size(); i++)
                        if(myMenu.contact_sel_pos == contactNameList.get(i)) {
                            myMenu.mydb.removePhoneContact(i);
                            myMenu.mydb.insertPhoneContact(myMenu.contact_sel_pos, String.valueOf(myMenu.pubColor));
                            Log.d("asdfasdf", "updated");
                        }


                    Fragment identifyCall = new IdentifyCallFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, identifyCall); // f2_container is your FrameLayout container
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }

            }
        });
        btn_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity myMenu = (MenuActivity)getActivity();
                if(myMenu.switch_Scene == 1)
                {
                    Fragment dest_add = new destination_add();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, dest_add);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(myMenu.switch_Scene == 2)
                {
                    Fragment accelerationFragment = new AccelerationFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, accelerationFragment); // f2_container is your FrameLayout container
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(myMenu.switch_Scene == 3)
                {
                    Fragment destinationMap = new DestinationMap();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, destinationMap); // f2_container is your FrameLayout container
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(myMenu.switch_Scene == 4)
                {
                    Fragment identifyCall = new IdentifyCallFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, identifyCall); // f2_container is your FrameLayout container
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            }
        });

        col_wheel.setOnTouchListener(new ImageView.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int x= (int)event.getX();
                int y= (int)event.getY();
                ImageView imageView = ((ImageView)v);
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                int[] viewCoords = new int[2];
                col_wheel.getLocationOnScreen(viewCoords);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, myMenu.screenHeight / 2, myMenu.screenHeight / 2, true);
                int pixel = 0;
                if(x<0) x=0;
                if(y<0) y=0;
                if(x>myMenu.screenHeight / 2) x=myMenu.screenHeight / 2;
                if(y>myMenu.screenHeight / 2) y=myMenu.screenHeight / 2;
                if(x<myMenu.screenHeight / 2 && y<myMenu.screenHeight / 2) {
                    pixel = resized.getPixel(x, y);
                    if(pixel != 0) {
                        pubColor = pixel;
                        myCirCur.setImageBitmap( getCroppedBitmap(70,70) );
                        myCirCur.setX(x + viewCoords[0] - 70 / 2);
                        myCirCur.setY(y + viewCoords[1] - 45 - myMenu.screenHeight / 2 / 13 );
                        //myCirCur.setY(y + viewCoords[1] - 70 - 85);
                        redValue = Color.red(pixel);
                        blueValue = Color.blue(pixel);
                        greenValue = Color.green(pixel);
                        redString = String.format("R = %s", redValue);
                        greenString = String.format("G = %s", greenValue);
                        blueString = String.format("B = %s", blueValue);
                        rText.setTextColor(Color.RED);
                        gText.setTextColor(Color.GREEN);
                        bText.setTextColor(Color.BLUE);
                        rText.setText(redString);
                        gText.setText(greenString);
                        bText.setText(blueString);
                    }
                }
                imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,1));
                imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));
                Log.d("pubColor", String.valueOf(pubColor));
                return true;
            }
        });



        // Inflate the layout for this fragment
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

    public Bitmap getCroppedBitmap(int width, int height) {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        //  paint.setColor(Color.RED);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(width / 2, height / 2,  width / 2, paint);
        paint.setColor(pubColor);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(width / 2, height / 2,  width / 2-2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);
        return output;
    }

}
