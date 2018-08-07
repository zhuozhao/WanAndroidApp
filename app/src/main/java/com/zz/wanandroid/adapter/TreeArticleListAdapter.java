package com.zz.wanandroid.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zz.wanandroid.R;
import com.zz.wanandroid.bean.Article;
import com.zz.wanandroid.bean.TreeChildren;

import java.util.List;

/**
 * @author zhaozhuo
 * @date 2018/7/17 14:31
 */
public class TreeArticleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TITLE = 0;
    private static final int CONTENT = 1;
    private List<Object> items;
    private Context context;

    public TreeArticleListAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(context);

        switch (viewType) {
            case TITLE:
                TitleHolder titleHolder = new TitleHolder(mInflater.inflate(R.layout.tree_article_title, parent, false));
                return titleHolder;
            case CONTENT:
                ArticleHolder articleHolder = new ArticleHolder(mInflater.inflate(R.layout.fragment_home_page_list_item, parent, false),onItemClickListener);
                return articleHolder;
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof TitleHolder){

            TreeChildren children = (TreeChildren) items.get(position);
            TitleHolder titleHolder = (TitleHolder) holder;
            titleHolder.titleView.setText(children.getName());
        }

        if(holder instanceof ArticleHolder){

            Article article = (Article) items.get(position);
            ArticleHolder holder1 = (ArticleHolder) holder;
            holder1.textView.setText(article.getTitle() + "");
            holder1.author.setText(article.getAuthor());
            holder1.publishTime.setText(article.getNiceDate());
            holder1.superChapterName.setVisibility(View.GONE);
           // holder1.superChapterName.setText(article.getSuperChapterName() + "/" + article.getChapterName());
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

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position) instanceof TreeChildren) {
            return TITLE;
        } else {
            return CONTENT;
        }
    }

    private RecycleViewOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecycleViewOnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;

    }

    /**
     * 标题holder
     */
    class TitleHolder extends RecyclerView.ViewHolder {

        TextView titleView;

        public TitleHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.titleView);
        }
    }

    /**
     * 内容holder
     */
    class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RecycleViewOnItemClickListener onItemClickListener;
        TextView textView, author, superChapterName, publishTime, desc;
        ImageView envelopePic;
        AppCompatImageView collectImg;
        public ArticleHolder(View itemView, RecycleViewOnItemClickListener onItemClickListener) {
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
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
