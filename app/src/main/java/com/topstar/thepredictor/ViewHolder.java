package com.topstar.thepredictor;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mnview;

    public ViewHolder(View itemView) {
        super(itemView);
        mnview = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getBindingAdapterPosition());

            }
        });
        //item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });


    }

    public void setTitle(String title) {
        TextView tvTitle =  mnview.findViewById(R.id.postTitle);
        tvTitle.setText(title);
    }

    public void setDetails(String details) {

        TextView txtdetails = mnview.findViewById(R.id.post);
        txtdetails.setText(details);

    }


    public void setTime(String date) {

        TextView txtTime = mnview.findViewById(R.id.postTime);
        //long elapsedDays=0,elapsedWeeks = 0, elapsedHours=0,elapsedMin=0;
                txtTime.setText(date);

    }

    private ClickListener mClickListener;

    // Interface to handle callbacks
    public interface  ClickListener {
        void onItemClick (View view, int position);
        void onItemLongClick (View view, int position);
    }

    public void setOnClickListener (ClickListener clickListener) {
        mClickListener = clickListener;
    }
}

