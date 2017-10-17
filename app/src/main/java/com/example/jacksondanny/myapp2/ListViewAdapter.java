package com.example.jacksondanny.myapp2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<String> {

    private TargetListFragment activity;
    private List<String> friends;
    private List<String> colors;
    private List<String> latitudes;
    private List<String> longitudes;
    public int pubColor = Color.WHITE;

/**/
    public ListViewAdapter(TargetListFragment context, int resource, List<String> objects, List<String> colors, List<String> latitude, List<String> longitude) {
        super(context.getContext(), resource, objects);
        this.activity = context;
        this.friends = objects;
        this.colors = colors;
        this.latitudes = latitude;
        this.longitudes = longitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(getItem(position));
        holder.number.setText(String.valueOf(position+1));
        //holder.color.setText(colors.get(position));
        pubColor = Integer.parseInt(colors.get(position));
        holder.imageView6.setImageBitmap(getRoundedCornerBitmap(100,100,10,1));
        holder.imageView7.setImageBitmap(getRoundedCornerBitmap(103,103,10,0));

        //handling buttons event
     //   holder.btnEdit.setOnClickListener(onEditListener(position, holder));
        holder.btnDelete.setOnClickListener(onDeleteListener(position, holder));

        return convertView;
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

    /**   private View.OnClickListener onEditListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     showEditDialog(position, holder);
            }
        };
    }


     * Editting confirm dialog
     * @param position
     * @param holder

    private void showEditDialog(final int position, final ViewHolder holder) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setTitle("EDIT ELEMENT");
        final EditText input = new EditText(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setText(friends.get(position));
        alertDialogBuilder.setView(input);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result edit text
                                friends.set(position, input.getText().toString().trim());

                                //notify data set changed
                                activity.updateAdapter();
                                holder.swipeLayout.close();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog and show it
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/

    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MenuActivity myMenu = (MenuActivity)activity.getActivity();
                myMenu.mydb.deleteContact(friends.get(position));
                friends.remove(position);
                colors.remove(position);
                latitudes.remove(position);
                longitudes.remove(position);
                holder.swipeLayout.close();
                activity.updateAdapter();
            }
        };
    }

    private class ViewHolder {
        private TextView name, number, color;
        private ImageView imageView6, imageView7;
        private View btnDelete;
      //  private View btnEdit;
        private SwipeLayout swipeLayout;

        public ViewHolder(View v) {
            swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout);
            btnDelete = v.findViewById(R.id.delete);

          //  btnEdit = v.findViewById(R.id.edit_query);
            number = (TextView) v.findViewById(R.id.number);
            name = (TextView) v.findViewById(R.id.name);
            imageView6 = (ImageView)v.findViewById(R.id.imageView6);
            imageView7 = (ImageView)v.findViewById(R.id.imageView7);

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }
    }
}