package com.lambdaapps.applock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LogsAdapter extends BaseAdapter {
    String[] result;
    Context context;
    private static LayoutInflater inflater = null;

    public LogsAdapter(Context mainActivity, String[] prgmNameList) {
        
        result = prgmNameList;
        context = mainActivity;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.log_list_item, null);
        holder.tv = (TextView) rowView.findViewById(R.id.log_line);
        holder.img = (ImageView) rowView.findViewById(R.id.log_icon);
        holder.tv.setText(result[position]);
        if (holder.tv.getText().toString().contains(context.getString(R.string.enter_failed))) {
            holder.img.setImageResource(R.drawable.ic_unlocked);
            //eroor
        }
        if (holder.tv.getText().toString().contains(context.getString(R.string.entered_successfully))) {
            holder.img.setImageResource(R.drawable.ic_unlocked);
            //succes
        }
        /*
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */

        return rowView;
    }
}