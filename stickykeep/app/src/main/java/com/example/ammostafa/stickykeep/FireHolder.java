package com.example.ammostafa.stickykeep;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Aminov on 2/22/2018.
 */



    public class FireHolder extends RecyclerView.ViewHolder  {

        TextView dataView;

        ImageView stickyClose;
        //   @BindView(R.id.title)
        //    TextView textTitle;
        //  @BindView(R.id.company)
        //  TextView textCompany;

        public FireHolder(View itemView) {
            super(itemView);
            dataView =  itemView.findViewById(R.id.data_textview);
            stickyClose = itemView.findViewById(R.id.sticky_close);
            stickyClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });

        }
        private FireHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(FireHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }
    }

