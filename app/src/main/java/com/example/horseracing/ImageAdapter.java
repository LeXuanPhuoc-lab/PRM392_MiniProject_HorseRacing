package com.example.horseracing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<Integer> imageList;

    public ImageAdapter(List<Integer> imageList) {
        this.imageList = imageList;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView animalImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            animalImageView = itemView.findViewById(R.id.animalImageView);
        }
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal_image, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.animalImageView.setImageResource(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public void updateImages(List<Integer> newImages) {
        this.imageList = newImages;
        notifyDataSetChanged();
    }
}