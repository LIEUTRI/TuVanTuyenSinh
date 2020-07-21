package com.b1610701.tuvantuyensinh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.b1610701.tuvantuyensinh.MainActivity;
import com.b1610701.tuvantuyensinh.MessageActivity;
import com.b1610701.tuvantuyensinh.R;
import com.b1610701.tuvantuyensinh.model.Chat;
import com.b1610701.tuvantuyensinh.model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> users;

    private String theLastMessage;
    private String Uid;
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
                Uid = user.getId();
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("UID", Uid);
                context.startActivity(intent);
            }
        });

        lastMessage(user.getId(), holder.last_msg);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView profile_image;
        public TextView profile_fullname;
        public TextView profile_username;
        public TextView last_msg;
        public ViewHolder(View itemView){
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_image);
            profile_fullname = itemView.findViewById(R.id.profile_fullname);
            profile_username = itemView.findViewById(R.id.profile_username);
            last_msg = itemView.findViewById(R.id.last_massage);
        }
    }
    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getSender().equals(userid) && chat.getReceiver().equals(MainActivity.adminUid)){
                        theLastMessage = chat.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_msg.setText("No messages");
                        break;
                    default:
                        last_msg.setText(theLastMessage);
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
