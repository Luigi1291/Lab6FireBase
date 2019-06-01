package com.example.firebase.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.firebase.Models.Item;
import com.example.firebase.R;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

    int mLayoutId;
    public ItemAdapter(Context context, int layoutId, List<Item> items) {
        super(context, layoutId, items);
        mLayoutId = layoutId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Item item = getItem(position);
        String name = item.getName();
        int quantity = item.getQuantity();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(mLayoutId, parent, false);
        }

        TextView nameView = (TextView) view.findViewById(R.id.txtItemNameListV);
        nameView.setText(name);

        TextView quantityView = (TextView) view.findViewById(R.id.txtQuantityListV);
        quantityView.setText(String.valueOf(quantity));
        return view;
    }
}
