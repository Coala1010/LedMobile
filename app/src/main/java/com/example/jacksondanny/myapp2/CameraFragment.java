package com.example.jacksondanny.myapp2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraFragment extends Fragment implements SurfaceHolder.Callback {

    public Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    public Camera.PictureCallback rawCallback;
    public Camera.ShutterCallback shutterCallback;
    public Camera.PictureCallback jpegCallback;
    public ImageView ivCam;
    String redString, greenString, blueString;
    int redValue, greenValue, blueValue, pubColor = 0;
    ImageButton capture;
    View view;

    private boolean hasSurface;

    public static CameraFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        CameraFragment cameraFragment = new CameraFragment();
        cameraFragment.setArguments(args);
        return cameraFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera, container, false);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        }

        capture = (ImageButton) view.findViewById(R.id.btn_capture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        surfaceView = (SurfaceView)view.findViewById(R.id.surfaceView1);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        //  surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        rawCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("Log", "onPictureTaken - raw");
            }
        };

        /** Handles data for jpeg picture */
        shutterCallback = new Camera.ShutterCallback() {
            public void onShutter() {
                Log.i("Log", "onShutter'd");
            }
        };
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                Log.d("jpegCallback", "Capture Image");
                try {
                    outStream = new FileOutputStream(String.format("/sdcard/%d.jpg", System.currentTimeMillis()));
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
                Log.d("Log", "onPictureTaken - jpeg");
                camera.stopPreview();
            }
        };

        final TextView textView5 = (TextView)view.findViewById(R.id.textView5);
        final TextView textView7 = (TextView)view.findViewById(R.id.textView7);
        final TextView textView8 = (TextView)view.findViewById(R.id.textView8);
        final ImageView imageView6 = (ImageView)view.findViewById(R.id.imageView6);
        final ImageView imageView7 = (ImageView)view.findViewById(R.id.imageView7);
        imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,1));
        imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));
        ivCam = (ImageView) view.findViewById(R.id.imageView1);
        ivCam.setVisibility(View.GONE);
        surfaceView.setOnTouchListener(new SurfaceView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //    if (event.getAction() == MotionEvent.ACTION_MOVE) {
                Log.d("SurfaceView Touch", "Now manual focus!");

                int x= (int)event.getX();
                int y= (int)event.getY();

                ImageView ivCam = (ImageView) view.findViewById(R.id.imageView1);
                Bitmap bitmap = ((BitmapDrawable)ivCam.getDrawable()).getBitmap();
                int pixel = 0;
                if(x < 0) x=0;
                if(y < 0) y=0;
                if(x > bitmap.getWidth())
                    x = bitmap.getWidth();
                if(y > bitmap.getHeight())
                    y = bitmap.getHeight();
                if(x < bitmap.getWidth() && y < bitmap.getHeight())
                {
                    pixel = bitmap.getPixel(x,y);
                    if(pixel != 0) {
                        pubColor = pixel;
                        int[] viewCoords = new int[2];
                        ivCam.getLocationOnScreen(viewCoords);
                        ImageView myCur1 = (ImageView) view.findViewById(R.id.imageView5);
                        myCur1.setX(x + viewCoords[0] - myCur1.getWidth() / 2);
                        myCur1.setY(y + viewCoords[1] - myCur1.getHeight() - 20);
                        redValue = Color.red(pixel);
                        blueValue = Color.blue(pixel);
                        greenValue = Color.green(pixel);
                        redString = String.format("R = %s", redValue);
                        greenString = String.format("G = %s", greenValue);
                        blueString = String.format("B = %s", blueValue);
                        textView5.setText(redString);
                        textView7.setText(greenString);
                        textView8.setText(blueString);
                    }
                }
                imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,1));
                imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));
                return true;
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
            paint.setColor(Color.WHITE);
        else if(col == 1)
            paint.setColor(pubColor);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(output, rect, rect, paint);

        return output;
    }

    public void captureImage() {
        // TODO Auto-generated method stub
        capture.setVisibility(View.GONE);
        Log.d("Capture", "Capture Image");
        camera.takePicture(shutterCallback, rawCallback, mPicture);
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
            Log.d("mPicture", "Capture Image");
            camera.stopPreview();
        }
    };

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        camera.startPreview();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        try
        {
            camera = Camera.open();
             Log.d("camera", String.valueOf(camera));

            Camera.Parameters params = camera.getParameters();

            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
            {
                params.set("orientation", "portrait");
                camera.setDisplayOrientation(90);
            }
            camera.setPreviewDisplay(holder);

            camera.setPreviewCallback(new Camera.PreviewCallback() {

                public void onPreviewFrame(byte[] _data, Camera _camera) {

                    Camera.Parameters params = camera.getParameters();
                    params.setPreviewFormat(ImageFormat.NV21); //or ImageFormat.YU2
                    int w = params.getPreviewSize().width;
                    int h = params.getPreviewSize().height;
                    int format = params.getPreviewFormat();
                    YuvImage image = new YuvImage(_data, format, w, h, null);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    Rect area = new Rect(0, 0, w, h);
                    image.compressToJpeg(area, 50, out);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig=Bitmap.Config.RGB_565;
                    Bitmap bm = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size(),options);
                    Matrix matrix = new Matrix();
                    matrix.preRotate(90);
                    Bitmap resized = Bitmap.createScaledBitmap(bm, w, h, true);
                    Bitmap adjustedBitmap = Bitmap.createBitmap(resized, 0, 0, w, h, matrix, true);
                    SurfaceView mySur = (SurfaceView) view.findViewById(R.id.surfaceView1);
                    resized = Bitmap.createScaledBitmap(adjustedBitmap, mySur.getWidth(), mySur.getHeight(), true);

                    ivCam.setImageBitmap(resized);
                    ImageView myCur1 = (ImageView) view.findViewById(R.id.imageView5);
                    myCur1.setImageResource(R.drawable.pointer);
                }
            });
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace(System.out);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }
}
