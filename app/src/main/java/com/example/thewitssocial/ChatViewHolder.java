package com.example.thewitssocial;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    CircleImageView prop1, prop2;
    TextView txt1, txt2;
    ImageView recivedImage;
    ImageView sentImage;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        prop1 = itemView.findViewById(R.id.user1prop);      //from a friend
        prop2 = itemView.findViewById(R.id.user2prop);       //from myself
        txt1 = itemView.findViewById(R.id.user1text);
        txt2 = itemView.findViewById(R.id.user2text);
        recivedImage =itemView.findViewById(R.id.receivedImage);
        sentImage = itemView.findViewById(R.id.sentImage);


    }
}
