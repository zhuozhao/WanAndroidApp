package com.zz.wanandroid.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zz.wanandroid.R;
import com.zz.wanandroid.bean.Article;

import java.util.List;

/**
 * @author zhaozhuo
 * @date 2018/7/12 15:09
 */
public class ProjectListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "ArticleListAdapter";

    private List<Article> articleList;
    private Context context;

    public ProjectListAdapter(Context context, List<Article> articleList) {

        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.fragment_project_list_item, parent, false);
            return new ArticleHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            Article article = articleList.get(position);
            ArticleHolder holder1 = (ArticleHolder) holder;
            holder1.textView.setText(article.getTitle() + "");
            holder1.author.setText(article.getAuthor());
            holder1.superChapterName.setText(article.getSuperChapterName() + "/" + article.getChapterName());

            if (article.getDesc() == null || "".equals(article.getDesc())) {
                holder1.desc.setVisibility(View.GONE);
            } else {
                holder1.desc.setText(article.getDesc().trim());
                holder1.desc.setVisibility(View.VISIBLE);
            }

            long ptime = System.currentTimeMillis() - article.getPublishTime();

            int m = (int) (ptime / 60 * 1000);
            int h = m / 60;
            int d = h / 24;

            if (m < 60) {
                holder1.publishTime.setText(m + "分钟前");
            }

            if (h < 24) {
                holder1.publishTime.setText(h + "小时前");
            }

            if (d < 2) {
                holder1.publishTime.setText(d + "天前");
            }

            if (d > 2 || d < 0) {
                holder1.publishTime.setText(article.getNiceDate());
            }

            if("".equals(article.getEnvelopePic())){
                holder1.envelopePic.setVisibility(View.GONE);
            }else {
                Glide.with(context).load(article.getEnvelopePic()).into(holder1.envelopePic);
                holder1.envelopePic.setVisibility(View.VISIBLE);
            }

            if(article.isCollect()){
                holder1.collectImg.setImageResource(R.drawable.ic_favorite_black_24dp);
            }else {
                holder1.collectImg.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }



    }
    private RecycleViewOnItemClickListener onItemClickListener;
    public void setOnItemClickListener(RecycleViewOnItemClickListener onItemClickListener){

        this.onItemClickListener = onItemClickListener;

    }



    @Override
    public int getItemCount() {
        return articleList == null ? 0 : articleList.size() ;
    }



    class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RecycleViewOnItemClickListener onItemClickListener;
        TextView textView, author, superChapterName, publishTime, desc;
        ImageView envelopePic;
        AppCompatImageView collectImg;
        public ArticleHolder(View itemView,RecycleViewOnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            textView = itemView.findViewById(R.id.text);
            author = itemView.findViewById(R.id.author);
            superChapterName = itemView.findViewById(R.id.superChapterName);
            publishTime = itemView.findViewById(R.id.publishTime);
            desc = itemView.findViewById(R.id.desc);
            envelopePic = itemView.findViewById(R.id.envelopePic);
            collectImg = itemView.findViewById(R.id.collectImg);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }


}
