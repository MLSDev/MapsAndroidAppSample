package com.mlsdev.mapsappsample.placesuggestions;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mlsdev.mapsappsample.R;
import com.mlsdev.mapsappsample.databinding.PlaceSuggestionsItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private List<String> places = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public PlacesAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PlaceSuggestionsItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.place_suggestions_item,
                parent,
                false
        );

        PlaceViewModel viewModel = new PlaceViewModel(onItemClickListener);
        binding.setViewModel(viewModel);
        return new ViewHolder(binding.getRoot(), viewModel);
    }

    @Override
    public void onBindViewHolder(PlacesAdapter.ViewHolder holder, int position) {
        holder.viewModel.placeFullText.set(places.get(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private PlaceViewModel viewModel;

        public ViewHolder(View itemView, PlaceViewModel viewModel) {
            super(itemView);
            this.viewModel = viewModel;
        }
    }

    public void setData(List<String> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    public void clearData() {
        places.clear();
        notifyDataSetChanged();
    }
}
