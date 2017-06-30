package com.mshvdvskgmail.foralfa.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mshvdvskgmail.foralfa.MyApplication;
import com.mshvdvskgmail.foralfa.R;
import com.mshvdvskgmail.foralfa.activities.WebActivity;
import com.prof.rssparser.Article;

import java.util.List;

/**
 * Created by mshvdvsk on 28/06/2017.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private List<Article> articleList;
    private Context context;

//    public interface OnItemClickListener {
//        public void onItemClicked(int position);
//    }
//
//    public interface OnItemLongClickListener {
//        public boolean onItemLongClicked(int position);
//    }

    public NewsListAdapter(Context context, List<Article> articleList){
        this.articleList = articleList;
        this.context = context;
    }
//
    public void updateData(List<Article> articleList){
        this.articleList = articleList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(articleList.get(position).getTitle());
        holder.description.setText(articleList.get(position).getDescription());
        holder.pubDate.setText(articleList.get(position).getPubDate().toString());
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.getContext(), WebActivity.class);
                intent.putExtra("link", articleList.get(position).getLink());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.mainView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, articleList.get(position).getLink());
                Intent new_intent = Intent.createChooser(intent, "Share via");
                new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(new_intent);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView title;
        TextView description;
        TextView pubDate;
        View mainView;
        public ViewHolder(View v) {
            super(v);
            mainView = v;
            title = (TextView) v.findViewById(R.id.list_item_tv_title);
            description = (TextView) v.findViewById(R.id.list_item_tv_description);
            pubDate = (TextView) v.findViewById(R.id.list_item_tv_pubDate);
        }
    }

}
