package com.example.login_page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<helperclass> userList;

    // Constructor
    public UserAdapter(List<helperclass> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        helperclass user = userList.get(position);

        holder.tvName.setText("Name: " + user.getName());
        holder.tvAge.setText("Age: " + user.getAge());
        holder.tvPhone.setText("Phone: " + user.getPhonenumber());
        holder.tvPremium.setText("Premium: " + (user.isIspremiem() ? "true" : "false"));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAge, tvPremium, tvPhone;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvPremium = itemView.findViewById(R.id.tvPremium);
            tvPhone = itemView.findViewById(R.id.tvPhonenumber);
        }
    }
}
