package com.example.project;

import android.widget.BaseAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @author Leif(Yuxiang Yao)
 * @Reference: https://blog.csdn.net/xialong_927/article/details/80463120
 */
public class DeviceAdapter extends BaseAdapter {

    private List<BluetoothDevice> mData;
    private Context mContext;

    public DeviceAdapter(List<BluetoothDevice> data, Context context){
        mData = data;
        mContext = context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        //复用view，优化性能
        if(itemView == null){
            itemView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_2, viewGroup,false);
        }


        TextView line1 = itemView.findViewById(android.R.id.text1);
        TextView line2 = itemView.findViewById(android.R.id.text2);

        line1.setTextColor(Color.BLACK);
        line2.setTextColor(Color.BLACK);

        //获取对应的蓝牙设备
        BluetoothDevice device = (BluetoothDevice) getItem(i);

        //显示设备名称
        line1.setText(device.getName());
        //显示设备地址
        line2.setText(device.getAddress());

        return itemView;
    }

    //刷新列表，防止搜索结果重复出现
    public void refresh(List<BluetoothDevice> data){
        mData = data;
        notifyDataSetChanged();
    }
}
