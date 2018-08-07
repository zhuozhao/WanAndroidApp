package com.zz.wanandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
public class CollectArticleListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "ArticleListAdapter";

    private List<Article> articleList;
    private Context context;

    public CollectArticleListAdapter(Context context, List<Article> articleList) {

        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(context).inflate(R.layout.fragment_home_page_list_item, parent, false);
            return new ArticleHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


            Article article = articleList.get(position);
            ArticleHolder holder1 = (ArticleHolder) holder;
            holder1.textView.setText(article.getTitle() + "");
            holder1.author.setText(article.getAuthor());
            holder1.superChapterName.setText(article.getSuperChapterName() + "/" + article.getChapterName());
            holder1.publishTime.setText(article.getNiceDate());

            if("".equals(article.getEnvelopePic())){
                holder1.envelopePic.setVisibility(View.GONE);
                holder1.publishTime.setVisibility(View.VISIBLE);
            }else {
                Glide.with(context).load(article.getEnvelopePic()).into(holder1.envelopePic);
                holder1.envelopePic.setVisibility(View.VISIBLE);
                holder1.publishTime.setVisibility(View.GONE);
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

        public ArticleHolder(View itemView,RecycleViewOnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            textView = itemView.findViewById(R.id.text);
            author = itemView.findViewById(R.id.author);
            superChapterName = itemView.findViewById(R.id.superChapterName);
            publishTime = itemView.findViewById(R.id.publishTime);
            desc = itemView.findViewById(R.id.desc);
            envelopePic = itemView.findViewById(R.id.envelopePic);
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
