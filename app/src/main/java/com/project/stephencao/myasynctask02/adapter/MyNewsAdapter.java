package com.project.stephencao.myasynctask02.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.project.stephencao.myasynctask02.R;
import com.project.stephencao.myasynctask02.bean.NewsBean;
import com.project.stephencao.myasynctask02.util.ImageLoader;

import java.util.List;

public class MyNewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener {
    private List<NewsBean> mNewsBeans;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private int mStart;
    private int mEnd;
    public static String[] URLS;
    private boolean mFirstIn;

    public MyNewsAdapter(List<NewsBean> mNewsBeans, Context context, ListView listView) {
        this.mNewsBeans = mNewsBeans;
        mInflater = LayoutInflater.from(context);
        mImageLoader = new ImageLoader(listView);
        mFirstIn = true; // a flag for loading the first page of the list view when entering the activity at the first time
        listView.setOnScrollListener(this);
        URLS = new String[mNewsBeans.size()];
        for (int i = 0; i < mNewsBeans.size(); i++) {
            URLS[i] = mNewsBeans.get(i).getPicSmall();
        }
    }

    @Override
    public int getCount() {
        return mNewsBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.view_listview_item, parent, false);
            viewHolder.mIcon = convertView.findViewById(R.id.id_listview_item_image);
            viewHolder.mTitle = convertView.findViewById(R.id.id_listview_title);
            viewHolder.mDescription = convertView.findViewById(R.id.id_listview_description);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NewsBean newsBean = mNewsBeans.get(position);
        viewHolder.mIcon.setImageResource(R.mipmap.ic_launcher);
        viewHolder.mIcon.setTag(newsBean.getPicSmall());
//        new ImageLoader().displayImageWithThread(viewHolder.mIcon,newsBean.getPicSmall());
        mImageLoader.displayImageWithAsyncTask(viewHolder.mIcon, newsBean.getPicSmall());
        viewHolder.mTitle.setText(newsBean.getName());
        viewHolder.mDescription.setText(newsBean.getDescription());
        return convertView;
    }

    /**
     * load content when the listview is idle
     * stop loading when the listview is scrolling
     * to improve the performance of the listview
     *
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            mImageLoader.loadImages(mStart, mEnd);
        } else {
            mImageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem + visibleItemCount;
        if (mFirstIn && visibleItemCount > 0) {
            mImageLoader.loadImages(mStart,mEnd);
            mFirstIn = false;
        }
    }

    class ViewHolder {
        ImageView mIcon;
        TextView mTitle, mDescription;
    }
}
