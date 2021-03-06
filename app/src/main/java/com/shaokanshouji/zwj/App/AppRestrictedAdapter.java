package com.shaokanshouji.zwj.App;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by zwj on 2018/3/29.
 */

public class AppRestrictedAdapter extends ArrayAdapter<AppRestricted> {
    private int mResourceId;

    public AppRestrictedAdapter(Context context, int textViewResourceId, List<AppRestricted> objects) {
        super(context, textViewResourceId, objects);
        mResourceId = textViewResourceId;
    }

    //update item which has the same pkgName with a AppRestricted Object
    void update(AppRestricted appNew){
        for(int i = 0;i<getCount();++i){
            if(getItem(i).getPackageName().equals(appNew.getPackageName())){
                remove(getItem(i));
                insert(appNew,i);
            }
        }
    }

    //!!!
    //remove the first one has the same pkgName
    @Override
    public void remove(AppRestricted remove) {
        for (int i = 0; i < getCount(); ++i) {
            if(getItem(i).getPackageName().equals(remove.getPackageName()))
                super.remove(getItem(i));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null)//
        {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
            viewHolder.mTextViewAppName = view.findViewById(R.id.tv_app_name);
            viewHolder.mTextViewDuration = view.findViewById(R.id.tv_duration);
            viewHolder.mTextViewLeftAndSum = view.findViewById(R.id.tv_left_and_sum);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        AppRestricted appRestricted = getItem(position);
        viewHolder.mTextViewAppName.setText(appRestricted.getName());
        viewHolder.mTextViewDuration.setText(appRestricted.getDuration());
        viewHolder.mTextViewLeftAndSum.setText(appRestricted.getLeftAndSum());
        return view;
    }

    class ViewHolder {
        private TextView mTextViewAppName;
        private TextView mTextViewDuration;
        private TextView mTextViewLeftAndSum;
    }
}
