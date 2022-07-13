package com.example.android.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.mymovies.R;
import com.example.android.mymovies.data.Trailer;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private List<Trailer> trailers;
    private OnTrailerClickListener listener;

    public interface OnTrailerClickListener {
        void onTrailerClick(String url);
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public void setOnTrailerClickListener(OnTrailerClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.textViewVideoName.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewVideoName;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVideoName = itemView.findViewById(R.id.tv_video_name);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTrailerClick(trailers.get(getAdapterPosition()).getVideoKey());
                }
            });
        }
    }
}
