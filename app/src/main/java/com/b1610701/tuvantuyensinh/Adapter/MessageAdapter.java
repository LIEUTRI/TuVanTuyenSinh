package com.b1610701.tuvantuyensinh.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b1610701.tuvantuyensinh.MessageActivity;
import com.b1610701.tuvantuyensinh.R;
import com.b1610701.tuvantuyensinh.model.Chat;
import com.b1610701.tuvantuyensinh.model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    boolean isRight = false;
    private Context context;
    private List<Chat> chats;
    private String imgURL;

    FirebaseUser firebaseUser;
    public MessageAdapter(Context context, List<Chat> chats, String imgURL) {
        this.context = context;
        this.chats = chats;
        this.imgURL = imgURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        Log.d("test", "msg: "+chat.getMessage()+"S: "+chat.getSender()+" R: "+chat.getReceiver());
        holder.show_msg.setText(chat.getMessage());
        holder.show_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
        if (isRight){
            if (imgURL.equals("default")){
                holder.img_msg.setImageResource(R.drawable.user);
            } else {
                Glide.with(context).load(imgURL).into(holder.img_msg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView img_msg;
        public TextView show_msg;
        public ViewHolder(View itemView){
            super(itemView);
            img_msg = itemView.findViewById(R.id.img_message);
            show_msg = itemView.findViewById(R.id.show_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        if (chats.get(position).getSender().equals(firebaseUser.getUid())){
            isRight = true;
            return MSG_TYPE_RIGHT;
        } else {
            isRight = false;
            return MSG_TYPE_LEFT;
        }
    }
}
