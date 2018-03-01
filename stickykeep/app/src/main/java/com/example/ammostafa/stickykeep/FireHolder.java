package com.example.ammostafa.stickykeep;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Aminov on 2/22/2018.
 */



    public class FireHolder extends RecyclerView.ViewHolder  {

        String color;

        EditText dataView;

        ImageView stickyClose,stickyAdd;
        //   @BindView(R.id.title)
        //    TextView textTitle;
        //  @BindView(R.id.company)
        //  TextView textCompany;

        public FireHolder(View itemView) {
            super(itemView);
            dataView =  itemView.findViewById(R.id.data_textview);
            stickyClose = itemView.findViewById(R.id.sticky_close);
            stickyAdd =  itemView.findViewById(R.id.sticky_add);

            stickyClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition(),ClickEventType.DELETE);

                }
            });

            stickyAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition(),ClickEventType.ADD);

                }
            });



            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                  //  mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });


           dataView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mTextChangeListener.onTextChanged(charSequence,getAdapterPosition());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    mTextChangeListener.afterTextChanged(editable,getAdapterPosition());
                }
            });
           // dataView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        }
        private FireHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            public void onItemClick(View view, int position,@NonNull ClickEventType type);
            public void onItemLongClick(View view, int position,@NonNull ClickEventType type);
        }

        public void setOnClickListener(FireHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }


        //         text change

    private FireHolder.TextChangeListener mTextChangeListener;

    //Interface to send callbacks...
    public interface TextChangeListener{
        public void onTextChanged(CharSequence charSequence, int position);
        public void afterTextChanged(Editable editable, int position);
    }

    public void setOnTextChangeListener(FireHolder.TextChangeListener textChangeListener){
        mTextChangeListener = textChangeListener;
    }


    public void setColor(String color){

        this.color = color;
    }

    public String getColor(){
        return this.color;
    }
    }

