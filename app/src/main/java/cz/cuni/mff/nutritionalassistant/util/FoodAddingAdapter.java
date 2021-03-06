package cz.cuni.mff.nutritionalassistant.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.nutritionalassistant.R;
import cz.cuni.mff.nutritionalassistant.foodtypes.FoodAdapterType;

public class FoodAddingAdapter extends RecyclerView.Adapter<FoodViewHolder> {

    private List<FoodAdapterType> data;
    private Context context;

    public FoodAddingAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
    }

    public void addItems(final List<FoodAdapterType> newItems) {
        data.addAll(newItems);
        // Keep RecyclerView scrolling state
        this.notifyItemRangeInserted(
                this.getItemCount() - newItems.size(), this.getItemCount());
    }

    public void clearItems() {
        int oldSize = data.size();
        data.clear();
        this.notifyItemRangeRemoved(0, oldSize);
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
        return new FoodViewHolder(view, context);
        }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int position) {
        viewHolder.setDetails(data.get(position));
    }
}
