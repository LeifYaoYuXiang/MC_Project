package com.example.project;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.List;

/**
 * @author Leif(Yuxiang Yao)
 */
public class PreferenceAdapter extends RecyclerView.Adapter<PreferenceAdapter.ViewHolder> {
    private Context context;
    private List<Preference> preferenceList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            imageView=itemView.findViewById(R.id.preference_image);
            textView=itemView.findViewById(R.id.preference_description);
        }
    }

    private onRecycleViewClickListener listener;

    public void setItemClickListener(onRecycleViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    public PreferenceAdapter(List<Preference> preferenceList){
        this.preferenceList=preferenceList;
    }


    @Override
    public PreferenceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(context==null){
            context=viewGroup.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.preference_item,viewGroup,false);

        if(listener != null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(v);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClickListener(v);
                    return true;
                }
            });
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Preference preference=this.preferenceList.get(i);
        viewHolder.textView.setText(preference.getDescription());
        viewHolder.textView.setTypeface(EasyFonts.windSong(context));
        Glide.with(context).load(preference.getImageViewId()).into(viewHolder.imageView);

    }


    @Override
    public int getItemCount() {
        return this.preferenceList.size();
    }
}
