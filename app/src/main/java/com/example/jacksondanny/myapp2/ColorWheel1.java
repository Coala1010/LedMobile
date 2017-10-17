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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class ColorWheel1 extends Fragment {

    Timer timer;
    MyTimerTask myTimerTask = new MyTimerTask();
    public int flag = 0, pubColor = 0, flag1 = 0;
    String redString, greenString, blueString;
    int redValue, greenValue, blueValue, progressValue = 3;
    Bitmap bmp;
    View view;
    MenuActivity myMenu;

    public static ColorWheel1 newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        ColorWheel1 colorWheel1 = new ColorWheel1();
        colorWheel1.setArguments(args);
        return colorWheel1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_color_wheel1, container, false);
        myMenu = (MenuActivity)getActivity();

        final TextView textView4 = (TextView)view.findViewById(R.id.textView2);
        textView4.setTextColor(Color.BLACK);
        final TextView textView5 = (TextView)view.findViewById(R.id.zText);
        textView5.setTextColor(Color.BLUE);
        final TextView textView6 = (TextView)view.findViewById(R.id.gText);
        textView6.setTextColor(Color.GREEN);
        textView4.setTextSize(15);

        final ImageView col_wheel = (ImageView) view.findViewById(R.id.imageView1);

        col_wheel.getLayoutParams().height = myMenu.screenHeight / 2;
        col_wheel.getLayoutParams().width = myMenu.screenHeight / 2;
        col_wheel.requestLayout();

        final TextView textView2 = (TextView)view.findViewById(R.id.gText);
        final TextView textView3 = (TextView)view.findViewById(R.id.zText);
        final ImageView imageView6 = (ImageView)view.findViewById(R.id.imageView6);
        final ImageView imageView7 = (ImageView)view.findViewById(R.id.imageView7);
        final TextView textViewCol = (TextView)view.findViewById(R.id.rText);

        final ImageView myCirCur = (ImageView) view.findViewById(R.id.imageView4);
        imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,3));
        imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));

        timer = new Timer();
        myTimerTask = new MyTimerTask();

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

                Bitmap resized = Bitmap.createScaledBitmap(bitmap, myMenu.screenHeight/2, myMenu.screenHeight/2, true);
                int pixel = 0;
                if(x<0) x=0;
                if(y<0) y=0;
                if(x>myMenu.screenHeight/2) x=myMenu.screenHeight/2;
                if(y>myMenu.screenHeight/2) y=myMenu.screenHeight/2;
                if(x<myMenu.screenHeight/2 && y<myMenu.screenHeight/2) {
                    pixel = resized.getPixel(x, y);
                    if(pixel != 0) {
                        pubColor = pixel;
                        myCirCur.setImageBitmap( getCroppedBitmap(70,70) );
                        myCirCur.setX(x + viewCoords[0] - 70 / 2);
                        myCirCur.setY(y + viewCoords[1] - 45 - myMenu.screenHeight/2 / 13 );
                     //   myCirCur.setY(y + viewCoords[1] - 70 - myMenu.screenWidth/20 - 15);

                        redValue = Color.red(pixel);
                        blueValue = Color.blue(pixel);
                        greenValue = Color.green(pixel);
                        redString = String.format("R = %s", redValue);
                        greenString = String.format("G = %s", greenValue);
                        blueString = String.format("B = %s", blueValue);
                        textViewCol.setTextColor(Color.RED);
                        textView2.setTextColor(Color.GREEN);
                        textView3.setTextColor(Color.BLUE);
                        textViewCol.setText(redString);
                        textView2.setText(greenString);
                        textView3.setText(blueString);
                    }
                }
                imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,1));
                imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));
                if(flag1 == 0) {
                    timer.cancel();
                    timer = new Timer();
                    myTimerTask = new MyTimerTask();
                    timer.schedule(myTimerTask, progressValue);
                    flag1 =1;
                }
                return true;
            }
        });

        SeekBar seekBar = (SeekBar)view.findViewById(R.id.seekBar1);
        seekBar.getLayoutParams().width = myMenu.screenHeight/10*9;
        seekBar.requestLayout();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub
                flag1 =1;
                progressValue = 10;
                if(progress != 0)
                    progressValue = progress * 10;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                timer.cancel();
                timer = new Timer();
                myTimerTask = new MyTimerTask();
                timer.schedule(myTimerTask, progressValue);

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

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if (getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    ImageView imageView6 = (ImageView)view.findViewById(R.id.imageView6);
                    ImageView imageView7 = (ImageView)view.findViewById(R.id.imageView7);
                    if(flag == 1) {
                        imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,2));
                        imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,2));
                        flag = 0;
                    }
                    else {
                        imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,1));
                        imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));
                        flag = 1;
                    }
                    if(progressValue == 1000) {
                        imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,1));
                        imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));
                        timer.cancel();
                    }
                    else {
                        timer = new Timer();
                        myTimerTask = new MyTimerTask();
                        timer.schedule(myTimerTask, progressValue);
                    }
                }});
        }
    }
}
