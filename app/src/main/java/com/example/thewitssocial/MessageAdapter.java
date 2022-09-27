package com.example.thewitssocial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private ArrayList<MessageObject> messages;

    private Context context;

    public MessageAdapter(ArrayList<MessageObject> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Toast.makeText(context, "we have sent", Toast.LENGTH_SHORT).show();
        View view = LayoutInflater.from(context).inflate(R.layout.message_holder, viewGroup, false);

        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder messageHolder, int i) {
        //messageHolder.t.setText(messages.get(i).getMessage());
        MessageObject s = messages.get(i);
        messageHolder.t.setText(s.getReceiver());
        Toast.makeText(context, "we have sent", Toast.LENGTH_SHORT).show();
        ConstraintLayout constraintLayout = messageHolder.cl;

        if(messages.get(i).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){ //we are the sender

            ConstraintSet constraintSet= new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.messcardview, ConstraintSet.LEFT);
            constraintSet.clear(R.id.txtviewmessag, ConstraintSet.LEFT);
            constraintSet.connect(R.id.messcardview, ConstraintSet.RIGHT, R.id.cclay, ConstraintSet.RIGHT, 0);
            constraintSet.connect(R.id.txtviewmessag, ConstraintSet.RIGHT, R.id.messcardview, ConstraintSet.LEFT, 0);
            constraintSet.applyTo(constraintLayout);
        }
        else{
            ConstraintSet constraintSet= new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.messcardview, ConstraintSet.RIGHT);
            constraintSet.clear(R.id.txtviewmessag, ConstraintSet.RIGHT);
            constraintSet.connect(R.id.messcardview, ConstraintSet.LEFT, R.id.cclay, ConstraintSet.LEFT, 0);
            constraintSet.connect(R.id.txtviewmessag, ConstraintSet.LEFT, R.id.messcardview, ConstraintSet.RIGHT, 0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        ConstraintLayout cl;
        TextView t;
        ImageView pp;
        public MessageHolder(@NonNull View itemview){
            super(itemview);
            cl = itemview.findViewById(R.id.cclay);
            t = itemview.findViewById(R.id.txtviewmessag);
            pp = itemview.findViewById(R.id.smallpp);

        }
    }
}
