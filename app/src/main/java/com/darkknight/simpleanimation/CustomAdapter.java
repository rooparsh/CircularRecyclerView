package com.darkknight.simpleanimation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Rooparsh Kalia on 22/02/18.
 */

public class CustomAdapter extends RecyclerView.Adapter<VH> {
    private onItemClickListener mOnItemClickListener;

    CustomAdapter(onItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {

        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.tv.setText("asdasdasd");
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mOnItemClickListener.onClick(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}


class VH extends RecyclerView.ViewHolder {

    TextView tv;
    ImageView mImageView;

    VH(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.tv_item);
        mImageView = (ImageView) itemView.findViewById(R.id.image);
    }
}

interface onItemClickListener {
    void onClick(final View v);
}

