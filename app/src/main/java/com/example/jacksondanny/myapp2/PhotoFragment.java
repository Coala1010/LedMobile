package com.example.jacksondanny.myapp2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class PhotoFragment extends Fragment {
    public static final int IMAGE_GALLERY_REQUEST = 20;
    private static final int MAX_IMAGE_DIMENSION = 500;
    public int myWidth, myHeight;

    int redValue, greenValue, blueValue, pubColor = 0;
    String redString, greenString, blueString;
    public ImageView imgPicture;
    public int m_rotationInDegrees;
    
    View view;

    public static PhotoFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        PhotoFragment photoFragment = new PhotoFragment();
        photoFragment.setArguments(args);
        return photoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_photo, container, false);
        
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
        String pictrueDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictrueDirectoryPath);
        photoPickerIntent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);

        final TextView textView1 = (TextView)view.findViewById(R.id.textView1);
        final TextView textView2 = (TextView)view.findViewById(R.id.textView2);
        final TextView textView3 = (TextView)view.findViewById(R.id.rText);
        final ImageView imageView6 = (ImageView)view.findViewById(R.id.imageView6);
        final ImageView imageView7 = (ImageView)view.findViewById(R.id.imageView7);
        imageView6.setImageBitmap(getRoundedCornerBitmap(200,200,20,1));
        imageView7.setImageBitmap(getRoundedCornerBitmap(206,206,20,0));
        final ImageView myCur1 = (ImageView) view.findViewById(R.id.imageView);

        imgPicture = (ImageView)view.findViewById(R.id.imgPicture);
        imgPicture.setOnTouchListener(new ImageView.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int x= (int)event.getX();
                int y= (int)event.getY();
                int[] viewCoords = new int[2];
                imgPicture.getLocationOnScreen(viewCoords);
                ImageView imgView = ((ImageView)v);
                Bitmap bitmap = ((BitmapDrawable)imgView.getDrawable()).getBitmap();
                int pixel = 0;
                if(x<0) x=0;
                if(y<0) y=0;
                if(x > bitmap.getWidth())
                    x = bitmap.getWidth();
                if(y > bitmap.getHeight())
                    y = bitmap.getHeight();
                if(x < bitmap.getWidth() && y < bitmap.getHeight())
                {
                    pixel = bitmap.getPixel(x,y);
                    if(pixel != 0){
                        pubColor = pixel;
                        myCur1.setX(x+viewCoords[0]-myCur1.getWidth()/2);
                        myCur1.setY(y+viewCoords[1]-myCur1.getHeight()-18.3f);

                        redValue = Color.red(pixel);
                        blueValue = Color.blue(pixel);
                        greenValue = Color.green(pixel);
                        redString = String.format("R = %s", redValue);
                        greenString = String.format("G = %s", greenValue);
                        blueString = String.format("B = %s", blueValue);
                        textView1.setText(redString);
                        textView2.setText(greenString);
                        textView3.setText(blueString);
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

    public static Bitmap scaleImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();
        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            cursor.close();
            return -1;
        }

        cursor.moveToFirst();
        int orientation = cursor.getInt(0);
        cursor.close();
        cursor = null;
        return orientation;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == IMAGE_GALLERY_REQUEST)
            {
                Uri imageUri = data.getData();
                InputStream inputStream;
                try{
                 //   inputStream = getContentResolver().openInputStream(imageUri);
                    inputStream = getActivity().getApplicationContext().getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig=Bitmap.Config.RGB_565;
                    File myFile = new File(imageUri.getPath());

                    image = scaleImage(getActivity(), imageUri);   //code for samsung
                    //image = scaleImage(getActivity(), getImageContentUri(getActivity(), myFile));

                    float aspectRatio = 1;
                    int screenWidth = 0, screenHeight;
                    if(image.getWidth() > image.getHeight())
                    {
                        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                        aspectRatio = (float) screenWidth / image.getWidth();
                    }
                    else
                    {
                        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
                        float tempRatio = (float) screenHeight / image.getHeight();
                        if(image.getWidth() * tempRatio > screenWidth)
                            aspectRatio = (float) screenWidth / image.getWidth();
                        else
                            aspectRatio = tempRatio;
                    }
                    myWidth = (int) (image.getWidth() * (float)aspectRatio);
                    myHeight = (int) (image.getHeight() * (float)aspectRatio);
                    Bitmap resized = Bitmap.createScaledBitmap(image, myWidth, myHeight, true);
                    imgPicture = (ImageView)view.findViewById(R.id.imgPicture);
                    imgPicture.setImageBitmap(resized);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                    //Toast.makeText(this, "Unable to open image", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}