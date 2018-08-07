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
import com.zz.wanandroid.HomePageFragment;
import com.zz.wanandroid.R;
import com.zz.wanandroid.bean.Article;

import java.util.List;

/**
 * @author zhaozhuo
 * @date 2018/7/12 15:09
 */
public class ArticleListAdapter extends RecyclerView.Adapter {

    private static final String TAG = "ArticleListAdapter";

    private List<Article> articleList;
    private Context context;
    private int viewTypeHead = 0;
    private int viewTyoeContent =1;
    private View headView;

    public ArticleListAdapter(Context context, List<Article> articleList) {

        this.context = context;
        this.articleList = articleList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == viewTypeHead) {
            return new HeadHolder(headView);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_home_page_list_item, parent, false);
            return new ArticleHolder(view,onItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HeadHolder) {
           // HeadHolder headHolder = (HeadHolder) holder;
           // headHolder.viewPager.setAdapter(pageAdapter);
        } else {
            if(headView!=null){
                position = position-1;
            }
            Article article = articleList.get(position);
            ArticleHolder holder1 = (ArticleHolder) holder;
            holder1.textView.setText(article.getTitle() + "");
            holder1.author.setText(article.getAuthor());
            holder1.superChapterName.setText(article.getSuperChapterName() + "/" + article.getChapterName());

//
//            if (article.getDesc() == null || "".equals(article.getDesc())) {
//                holder1.desc.setVisibility(View.GONE);
//            } else {
//                holder1.desc.setText(article.getDesc().trim());
//                holder1.desc.setVisibility(View.VISIBLE);
//            }
            holder1.publishTime.setText(article.getNiceDate());

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


    }
    private RecycleViewOnItemClickListener onItemClickListener;
    public void setOnItemClickListener(RecycleViewOnItemClickListener onItemClickListener){

        this.onItemClickListener = onItemClickListener;

    }

    @Override
    public int getItemViewType(int position) {

        if(headView == null){
            return viewTyoeContent;
        }

        if (position == 0) {
            return viewTypeHead;
        }
        return viewTyoeContent;
    }


    @Override
    public int getItemCount() {

        if (headView==null){
            return articleList == null ? 0 : articleList.size() ;
        }
        return articleList == null ? 1 : articleList.size() +1;
    }

    /**
     * 设置头部布局
     * @param headView
     */
    public  void setHeadView(View headView){

        this.headView = headView;
        Log.d(TAG, "setHeadView: ");
        notifyItemInserted(0);

    }

    class HeadHolder extends RecyclerView.ViewHolder {
        ViewPager viewPager;
        public HeadHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.viewPager);
        }
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
