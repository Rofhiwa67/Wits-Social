package com.example.thewitssocial;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FriendMyViewHolder extends RecyclerView.ViewHolder {
    TextView username;
    public FriendMyViewHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.username_p);
    }
}
