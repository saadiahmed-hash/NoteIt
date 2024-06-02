package com.example.noteit.ViewHolders;



import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteit.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class noteViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout noteLayout ;
    public TextView titleTxt , subTitleTxt , timeDateTxt ;
    public RoundedImageView noteImage ;
    public noteViewHolder(@NonNull View itemView) {
        super(itemView);
        noteLayout = itemView.findViewById(R.id.noteLayout);
        titleTxt = itemView.findViewById(R.id.titleTxt);
        subTitleTxt = itemView.findViewById(R.id.subTitleTxt);
        timeDateTxt = itemView.findViewById(R.id.timeDateTxt);
        noteImage = itemView.findViewById(R.id.noteImage);

    }
}
