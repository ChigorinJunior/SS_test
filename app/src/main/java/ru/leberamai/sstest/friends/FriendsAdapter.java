package ru.leberamai.sstest.friends;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.leberamai.sstest.R;
import ru.leberamai.sstest.chat.MessagesListActivity;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    private List<Friend> friend;

    public FriendsAdapter(List<Friend> friend) {
        this.friend = friend;
    }


    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FriendsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent,false));
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {
        Friend fr = friend.get(position);
        holder.name.setText(fr.name);
        holder.message.setText(fr.message);
        holder.date.setText(fr.date);
        Picasso.get().load(fr.avatar).into(holder.avatar);

    }



    @Override
    public int getItemCount() {
        return friend.size();
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name, message, date;
        com.makeramen.roundedimageview.RoundedImageView avatar;

        public FriendsViewHolder(View itemView){
            super(itemView);
            message = itemView.findViewById(R.id.messageText);
            name = itemView.findViewById(R.id.nameText);
            date = itemView.findViewById(R.id.dateText);
            avatar = itemView.findViewById(R.id.avatar);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            final Context c = view.getContext();
            Intent intent = new Intent(c, MessagesListActivity.class);
            c.startActivity(intent);
        }
    }



}
