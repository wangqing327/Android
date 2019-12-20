package com.example.mypassword;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<CarCaption> list;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


    public RecyclerViewAdapter(List<CarCaption> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        CarCaption carCaption = list.get(position);
        carCaption.position = position;
        holder.text_label.setText(carCaption.label);
        if (!TextUtils.isEmpty(carCaption.bz)) {
            holder.text_bz.setText(carCaption.bz);
        } else {
            holder.text_bz.setText("");
        }
        holder.imageView.setImageResource(getID(carCaption.group));

        if (mOnItemClickListener != null) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }

        if (mOnItemLongClickListener != null) {
            holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(v, position);
                    //返回真，消费掉此事件，不然会继续执行OnClick事件
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addDate(CarCaption caption) {
        this.list.add(caption);
        notifyDataSetChanged();
    }

    public void remove_position(int position) {
        this.list.remove(position);
        notifyItemRemoved(position);
        //刷新当前位置及以后位置
        notifyItemRangeRemoved(position, getItemCount());

    }

    public void remove_id(int id) {
        for (int i = 0; i < list.size(); i++) {
            CarCaption c = list.get(i);
            if (c.id == id) {
                this.list.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeRemoved(i, getItemCount());
                return;
            }

        }
//        this.list.remove(positon);
//        notifyItemRemoved(positon);
//        //刷新当前位置及以后位置
//        notifyItemRangeRemoved(positon, getItemCount());

    }

    public void removeAll() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void updateItem(int id, String label, String group, String bz) {
        for (int i = 0; i < list.size(); i++) {
            CarCaption c = list.get(i);
            if (c.id == id) {
                c.group = group;
                c.label = label;
                c.bz = bz;
                //notifyDataSetChanged();
                notifyItemChanged(i);
                break;
            }
        }

    }

    //替换List|||||||-----------------------------
    public void thlist(List<CarCaption> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView text_label, text_bz;
        // LinearLayout layout;
        CardView layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (CardView) itemView;
            imageView = itemView.findViewById(R.id.img);
            text_label = itemView.findViewById(R.id.text);
            text_bz = itemView.findViewById(R.id.bz);

        }
    }


    public static class CarCaption {
        String label, group, bz;
        int id, position;

        public CarCaption(String label, String group, String bz, int id) {
            this.label = label;
            this.group = group;
            this.id = id;
            this.bz = bz;
        }
    }

    private int getID(String str) {
        //网页","电子邮件", "银行卡", "软件", "其他
        if (str.equals("软件")) {
            return R.drawable.ruanjian_2;
        } else if (str.equals("电子邮件")) {
            return R.drawable.youjian;
        } else if (str.equals("银行卡")) {
            return R.drawable.visa;
        } else if (str.equals("其他")) {
            return R.drawable.yonghu;
        } else if (str.equals("网页")) {
            return R.drawable.wangye;
        }
        return R.drawable.yonghu;
    }

    public static class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.onDraw(c, parent, state);

        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            //super.getItemOffsets(outRect, view, parent, state);

//            int pos = parent.getChildAdapterPosition(view);
//            int i = state.getItemCount();
//            if ((pos + 1) % 2 == 0) {
//                //第二列
//                outRect.right = 0;
//                outRect.left = 8;
//                if (pos == i - (i % 2 != 0 ? 2 : 1)) {
//                    outRect.bottom = 10;
//                } else {
//                    outRect.bottom = 0;
//                }
//
//            } else {
//                //第一列
//                outRect.left = 0;
//                outRect.right = 8;
//
//                if (pos == i - (i % 2 != 0 ? 1 : 2)) {
//                    outRect.bottom = 10;
//                } else {
//                    outRect.bottom = 0;
//                }
//            }
//
//            if (pos < 2) {
//                outRect.top = 20;
//            } else {
//                outRect.top = 10;
//            }



        }
    }


}
