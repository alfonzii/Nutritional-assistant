package cz.cuni.mff.nutritionalassistant.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.R;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodLightweight;

public class FoodAddingAdapter extends RecyclerView.Adapter<FoodViewHolder> {

    private List<FoodLightweight> data;
    private Context context;

    public FoodAddingAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
    }

    public void addItems(final List<FoodLightweight> newItems) {
        data.addAll(newItems);
        // Keep RecyclerView scrolling state
        this.notifyItemRangeChanged(
                this.getItemCount() - newItems.size(), this.getItemCount());
    }

    public void clearItems() {
        data.clear();
        this.notifyItemRangeRemoved(0, this.getItemCount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_food_adapter_layout, parent, false);
        return new FoodViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int position) {
        viewHolder.setDetails(data.get(position));
    }
}
