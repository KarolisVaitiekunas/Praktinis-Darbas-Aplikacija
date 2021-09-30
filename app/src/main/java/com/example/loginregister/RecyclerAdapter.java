package com.example.loginregister;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private List<Post> postList;
    private ViewHolder.OnNoteListener mOnNoteListener;

    public RecyclerAdapter(List<Post>postList, ViewHolder.OnNoteListener onNoteListener){
        this.postList=postList;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        String _title = postList.get(position).getTitle();
        String _body = postList.get(position).getBody();
        String _postedBy = postList.get(position).getPostedBy();
        String _id = postList.get(position).getId();
        String _color = postList.get(position).getColor();

        holder.setData(_title, _body, _postedBy, _id, _color);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemTitle;
        private TextView itemBody;
        private  TextView itemPostedBy;
        private TextView itemId;
        private CardView itemColor;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemBody = itemView.findViewById(R.id.itemBody);
            itemPostedBy = itemView.findViewById(R.id.itemPostedBy);
            itemId = itemView.findViewById(R.id.itemId);
            itemColor = itemView.findViewById(R.id.card_view);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getBindingAdapterPosition());
        }

        public interface OnNoteListener{
            void onNoteClick(int position);
        }

        public void setData(String title, String body, String postedBy, String id, String color) {
            itemTitle.setText(title);
            itemBody.setText(body);
            itemPostedBy.setText(postedBy);
            itemId.setText(id);
            switch (color) {
                case "red":
                    itemColor.setCardBackgroundColor(Color.RED);
                    break;
                case "green":
                    itemColor.setCardBackgroundColor(Color.GREEN);
                    break;
                case "gray":
                    itemColor.setCardBackgroundColor(Color.GRAY);
                    break;
            }

        }
    }
}
