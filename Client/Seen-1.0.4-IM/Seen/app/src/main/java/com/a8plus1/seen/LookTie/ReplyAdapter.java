package com.a8plus1.seen.LookTie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a8plus1.seen.R;

import java.util.ArrayList;
import java.util.List;


class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Tiezi> mtiezi = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener clickListener;

    public ReplyAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 创建一个方法供外面操作此数据
     *
     * @param list
     */
    public void addList(List<Tiezi> list) {
        mtiezi.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Tiezi.TYPE_ONE:
                return new TypeOneViewHolder(mLayoutInflater.inflate(R.layout.item_context_looktie, parent, false));
            case Tiezi.TYPE_TWO:

                return new TypeTwoViewHolder(mLayoutInflater.inflate(R.layout.item_reply_looktie, parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TypeAbstractViewHolder) holder).bindHolder(mtiezi.get(position));
    }


    public int getItemViewType(int position) {
        //得到不同的布局类型
        return mtiezi.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mtiezi.size();
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public static interface OnItemClickListener {
        void onClick(View view, int position);
    }

    abstract class TypeAbstractViewHolder extends RecyclerView.ViewHolder {

        public TypeAbstractViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindHolder(Tiezi tz);
    }

    class TypeOneViewHolder extends TypeAbstractViewHolder {


        public TextView context;
        public ImageView Image1,Image2,Image3;

        public TypeOneViewHolder(View itemView) {
            super(itemView);
            context = (TextView) itemView.findViewById(R.id.context);
            Image1=(ImageView)itemView.findViewById(R.id.Image1);
            Image2=(ImageView)itemView.findViewById(R.id.Image2);
            Image3=(ImageView)itemView.findViewById(R.id.Image3);
        }

        @Override
        public void bindHolder(Tiezi tz) {
            context.setText(tz.getContent());
            Image1.setImageBitmap(tz.getImage1());
            Image2.setImageBitmap(tz.getImage2());
            Image3.setImageBitmap(tz.getImage3());
        }


    }

    class TypeTwoViewHolder extends TypeAbstractViewHolder implements View.OnClickListener{

        public ImageView headImage;
        public TextView nickname;
        public TextView time;
        public TextView content;


        public TypeTwoViewHolder(View itemView) {
            super(itemView);
            headImage = (ImageView) itemView.findViewById(R.id.reply_headImage);
            nickname = (TextView) itemView.findViewById(R.id.reply_nickName);
            time = (TextView) itemView.findViewById(R.id.reply_time);
            content = (TextView) itemView.findViewById(R.id.reply_context);

        }

        @Override
        public void bindHolder(Tiezi tz) {
            if (tz.getHeadIamge() == null){
                headImage.setImageResource(R.drawable.de_head);
            }else {
                headImage.setImageBitmap(tz.getHeadIamge());
            }
            nickname.setText(tz.getNickname());
            content.setText(tz.getContent());
            time.setText(tz.getTime().substring(5, 16));
            headImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.onClick(itemView,getAdapterPosition());
            }
        }
    }
}
