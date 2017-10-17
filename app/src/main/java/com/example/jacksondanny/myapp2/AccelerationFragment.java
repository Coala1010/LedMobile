package com.example.jacksondanny.myapp2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class AccelerationFragment extends Fragment implements SensorEventListener {

    private TextView rText, gText, bText;
    double x_val = 0, y_val = 0, z_val = 0;
    private Sensor mySensor;
    private SensorManager SM;
    public int sensor_val = 0;
    public static int pubColor = Color.WHITE;
    String redString, greenString, blueString;
    public int redValue, greenValue, blueValue;

    private static final Random RANDOM = new Random();
    private LineGraphSeries<DataPoint> series, series1, series2;
    private float lastX = 0, lastY = 0, lastZ = 0;
    GraphView graph;
    ImageView topRect, topRect1;
    View view;
    MenuActivity myMenu;

    public AccelerationFragment()
    {

    }

    public static AccelerationFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("Accelerations Instance", instance);
        AccelerationFragment accelerationFragment = new AccelerationFragment();
        accelerationFragment.setArguments(args);

        return accelerationFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_acceleration, container, false);
        myMenu = (MenuActivity)getActivity();
        lastX = lastY = lastZ =0;

        FragmentManager fm = getActivity().getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        pubColor = myMenu.pubColor;

        graph = (GraphView) view.findViewById(R.id.graph);


        graph.getLayoutParams().height = myMenu.screenHeight/4;
        graph.requestLayout();
        // data
        series = new LineGraphSeries<DataPoint>();
        series1 = new LineGraphSeries<DataPoint>();
        series2 = new LineGraphSeries<DataPoint>();
        // styling series
        series.setTitle("X Value Curve 1");
        series.setColor(Color.RED);
        series.setThickness(5);

        series1.setTitle("Y Value Curve 2");
        series1.setColor(Color.GREEN);
        series1.setThickness(5);

        series2.setTitle("Z Value Curve 3");
        series2.setColor(Color.BLUE);
        series2.setThickness(5);

        //number of labels on x nd y axis
        graph.getGridLabelRenderer().setNumHorizontalLabels(0);
        graph.getGridLabelRenderer().setNumVerticalLabels(7);
        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(-3);
        viewport.setMaxY(3);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(100);
        viewport.setScrollable(true);
        graph.addSeries(series);
        graph.addSeries(series1);
        graph.addSeries(series2);

        rText = (TextView) view.findViewById(R.id.rText);
        gText = (TextView) view.findViewById(R.id.gText);
        bText = (TextView) view.findViewById(R.id.zText);
        final ImageView imageView6 = (ImageView)view.findViewById(R.id.imageView6);
        final ImageView imageView7 = (ImageView)view.findViewById(R.id.imageView7);
        imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));
        imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,1));
        topRect = (ImageView) view.findViewById(R.id.imageView2);
        topRect1 = (ImageView) view.findViewById(R.id.imageView1);
        topRect1.getLayoutParams().height = myMenu.screenHeight/4;
        topRect1.requestLayout();
        topRect.getLayoutParams().height = myMenu.screenHeight/4-4;
        topRect.getLayoutParams().width = myMenu.screenWidth-4;
        topRect.requestLayout();
        topRect1.setImageBitmap(getRoundedCornerBitmap(myMenu.screenWidth,myMenu.screenHeight/4,30,0));
        topRect.setImageBitmap(getRoundedCornerBitmap(myMenu.screenWidth-4,myMenu.screenHeight/4-4,30,3));

        imageView6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myMenu.switch_Scene = 2;

                Fragment colorWheel2 = new ColorWheel2();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, colorWheel2); // f2_container is your FrameLayout container
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        redValue = Color.red(pubColor);
        blueValue = Color.blue(pubColor);
        greenValue = Color.green(pubColor);
        redString = String.format("R = %s", redValue);
        greenString = String.format("G = %s", greenValue);
        blueString = String.format("B = %s", blueValue);
        rText.setTextColor(Color.RED);
        gText.setTextColor(Color.GREEN);
        bText.setTextColor(Color.BLUE);
        rText.setText(redString);
        gText.setText(greenString);
        bText.setText(blueString);

        SM = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        final TextView txt_percent = (TextView) view.findViewById(R.id.txt_percent);

        SeekBar seekBar = (SeekBar)view.findViewById(R.id.seekBar2);
        seekBar.getLayoutParams().width = myMenu.screenWidth/4*3;
        seekBar.requestLayout();
        sensor_val = myMenu.pubSlider;
        txt_percent.setText(String.valueOf(sensor_val) + "%");
        seekBar.setProgress(sensor_val);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                sensor_val = progress;
                txt_percent.setText(String.valueOf(progress) + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                myMenu.pubSlider = sensor_val;
            }
        });

        FrameLayout topFrame = (FrameLayout)view.findViewById(R.id.topFrame);
        topFrame.getLayoutParams().height = myMenu.screenHeight/4;
        topFrame.requestLayout();

        Button btn_reset = (Button) view.findViewById(R.id.btn_reset);
        btn_reset.getLayoutParams().height = myMenu.screenHeight/17;
        btn_reset.getLayoutParams().width = myMenu.screenWidth/4;
        btn_reset.requestLayout();
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topRect = (ImageView) view.findViewById(R.id.imageView2);
                topRect1 = (ImageView) view.findViewById(R.id.imageView1);
                topRect.setImageBitmap(getRoundedCornerBitmap(myMenu.screenWidth-4, myMenu.screenHeight/4,30,3));
                topRect1.setImageBitmap(getRoundedCornerBitmap(myMenu.screenWidth, myMenu.screenHeight/4,30,0));
            }
        });
        // Inflate the layout for this fragment

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
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

    @Override
    public void onSensorChanged(SensorEvent event) {

        myMenu = (MenuActivity)getActivity();
        series.appendData(new DataPoint(lastX++, (double)event.values[0]/10), true, 2000);
        series1.appendData(new DataPoint(lastY++, (double)event.values[1]/10), true, 2000);
        series2.appendData(new DataPoint(lastZ++, (double)event.values[2]/10), true, 2000);
        double isDrop = ((double)event.values[0]/10 - x_val) * ((double)event.values[0]/10 - x_val) + ((double)event.values[1]/10 - y_val) * ((double)event.values[1]/10 - y_val) + ((double)event.values[2]/10 - z_val) * ((double)event.values[2]/10 - z_val);
        if (Math.sqrt(isDrop) > (double)sensor_val/10 && sensor_val != 0) {
            Log.d("isDrop", String.valueOf(Math.sqrt(isDrop)));
            Log.d("isDrop", String.valueOf(Math.sqrt(sensor_val)));
            topRect = (ImageView) view.findViewById(R.id.imageView2);
            topRect1 = (ImageView) view.findViewById(R.id.imageView1);
            topRect1.setImageBitmap(getRoundedCornerBitmap(myMenu.screenWidth, myMenu.screenHeight/4, 30, 0));
            topRect.setImageBitmap(getRoundedCornerBitmap(myMenu.screenWidth - 4, myMenu.screenHeight/4 - 4, 30, 1));

        }
        x_val = (double)event.values[0]/10;
        y_val = (double)event.values[1]/10;
        z_val = (double)event.values[2]/10;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
