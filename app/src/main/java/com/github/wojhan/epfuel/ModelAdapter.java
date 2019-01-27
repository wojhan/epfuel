package com.github.wojhan.epfuel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ModelAdapter extends BaseAdapter {

    private List<Model> mModelList;

    public ModelAdapter() {

    }

    public ModelAdapter(List<Model> modelList) {
        mModelList = modelList;
    }

    @Override
    public int getCount() {
        return mModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MakeHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_make_spinner, parent, false);

            holder = new MakeHolder();
            holder.mName = convertView.findViewById(R.id.make_spinner_item_name);
            convertView.setTag(holder);
        } else {
            holder = (MakeHolder) convertView.getTag();
        }

        Model model = mModelList.get(position);
        holder.mName.setText(model.getName());

        return convertView;
    }

    class MakeHolder {
        private TextView mName;
    }
}