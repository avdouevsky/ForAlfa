package com.mshvdvskgmail.foralfa.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mshvdvskgmail.foralfa.R;
import com.prof.rssparser.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mshvdvsk on 28/06/2017.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private List<Article> articleList;

    public NewsListAdapter(List<Article> articleList){
        this.articleList = articleList;
    }
//
//    public void setData(List<Article> articleList){
//        this.articleList = articleList;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(articleList.get(position).getTitle());
        holder.description.setText(articleList.get(position).getDescription());
        holder.pubDate.setText(articleList.get(position).getPubDate().toString());
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
        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.list_item_tv_title);
            description = (TextView) v.findViewById(R.id.list_item_tv_description);
            pubDate = (TextView) v.findViewById(R.id.list_item_tv_pubDate);
        }
    }

}
