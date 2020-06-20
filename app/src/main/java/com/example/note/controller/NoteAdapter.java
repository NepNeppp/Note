package com.example.note.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.note.model.pojo.Note;
import com.example.note.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter{     //为什么note_layout显示在listview中？？！！多亏了适配器 ！
                                                  // 这个适配器说白了就是把listview和note_layout联系起来
    private Context mycontext;

    private List<Note> backlist;   //备份原始数据
    private List<Note> noteList;   //会变
    private MyFilter mFilter;
    public NoteAdapter(Context mycontext,List<Note> noteList){
        this.mycontext = mycontext;
        this.noteList = noteList;
        backlist = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mycontext, R.layout.note_layout,null);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_content.setText(noteList.get(position).getContent());
        tv_time.setText(noteList.get(position).getTime());
        view.setTag(noteList.get(position).getId());                    //保存主键id

        if(noteList.get(position).getTag()==-1){                                                     //根据tag的不同，选择不同的drawable资源作为一条note的背景
            Drawable drawable = ContextCompat.getDrawable(mycontext,R.drawable.note_shape_top);
            view.setBackground(drawable);
        }else{
            Drawable drawable = ContextCompat.getDrawable(mycontext,R.drawable.note_shape);
            view.setBackground(drawable);
        }
        return view;
    }

    public Filter getFilter(){
        if (mFilter ==null){
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    class MyFilter extends Filter {                     //内部类，过滤器
        //我们在performFiltering(CharSequence charSequence)这个方法中定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<Note> list;
            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                list = backlist;
            } else {//否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                for (Note note : backlist) {
                    if (note.getContent().contains(charSequence)) {
                        list.add(note);
                    }

                }
            }
            result.values = list; //将得到的集合保存到FilterResults的value变量中
            result.count = list.size();//将集合的大小保存到FilterResults的count变量中

            return result;
        }
        //在publishResults方法中告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            noteList = (List<Note>)filterResults.values;
            if (filterResults.count>0){
                notifyDataSetChanged();//通知数据发生了改变
            }else {
                notifyDataSetInvalidated();//通知数据失效
            }
        }
    }
}
