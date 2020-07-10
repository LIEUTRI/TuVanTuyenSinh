package com.b1610701.tuvantuyensinh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b1610701.tuvantuyensinh.MessageActivity;
import com.b1610701.tuvantuyensinh.R;
import com.b1610701.tuvantuyensinh.model.User;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> users;
    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = users.get(position);
        holder.profile_fullname.setText(user.getFullname());
        holder.profile_username.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.drawable.user);
        } else {
            Glide.with(context).load(user.getImageURL()).into(holder.profile_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("UID", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView profile_image;
        public TextView profile_fullname;
        public TextView profile_username;
        public ViewHolder(View itemView){
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            profile_fullname = itemView.findViewById(R.id.profile_fullname);
            profile_username = itemView.findViewById(R.id.profile_username);
        }
    }
}
