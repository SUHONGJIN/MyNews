package com.jin.mynews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by SuHongJin on 2018/6/24.
 */

public class Fragment3 extends Fragment {
    private ListView listView;
    private List<Map<String,String>> datalist;
    private SwipeRefreshLayout my_swipefresh;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                MyBaseAdapter adapter=new MyBaseAdapter();
                listView.setAdapter(adapter);
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment3,container,false);
        listView=(ListView)view.findViewById(R.id.my_news_list);

        TextView tv_empty=(TextView)view.findViewById(R.id.tv_empty);
        listView.setEmptyView(tv_empty);
        my_swipefresh=(SwipeRefreshLayout)view.findViewById(R.id.my_swipefresh);
        my_swipefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Random random=new Random();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNews();
                        my_swipefresh.setRefreshing(false);
                    }
                },1500);

            }
        });
        //判断网络是否连接
        boolean neywork = NetWorkUtil.NetWorkisVaribale(getContext());
        if (neywork) {
            try {
                Toast.makeText(getContext(),"Loading...",Toast.LENGTH_SHORT).show();
                getNews();
            }catch (NullPointerException e){
                Toast.makeText(getContext(), "获取数据异常", Toast.LENGTH_SHORT).show();
            }

        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    private void getNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect("https://voice.hupu.com/soccer/tag/1106.html").get();
                    Elements elements = document.select("div.tag-list-box");
                    datalist=new ArrayList<>();
                    for (int i=0;i<elements.select("div.list").size();i++){
                        Map<String,String> map=new HashMap<>();
                        Element select = elements.select("div.list").get(i);
                        String img=select.select("img").attr("src");
                        String title=select.select("span.n1").text();
                        String url=select.select("a").attr("href");
                        String time=select.select("p.time").text();
                        map.put("img",img.replace("GIF",""));
                        map.put("title",title.replace("GIF：",""));
                        map.put("url",url);
                        map.put("time",time);
                        Log.i("tag3",img+title+url+time);
                        datalist.add(map);
                    }
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent(getContext(), NewsDeatilsActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("url",datalist.get(position).get("url"));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                    handler.sendEmptyMessage(0);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Object getItem(int position) {
            return datalist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view1=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foodball_news_list,null);

            TextView title=(TextView)view1.findViewById(R.id.tv_news_title);
            TextView time=(TextView)view1.findViewById(R.id.tv_news_time);
            ImageView iv_news_img=(ImageView)view1.findViewById(R.id.iv_news_img);

            title.setText(datalist.get(position).get("title"));
            time.setText(datalist.get(position).get("time"));

            // Glide.with(getContext()).load(datalist.get(position).get("img")).into(iv_news_img);
            Glide.with(getContext()).load(datalist.get(position).get("img")).asBitmap().into(iv_news_img);
            Log.i("tag4",datalist.get(position).get("img"));
            //iv_news_img.setImageResource(R.drawable.icon_dlt);

            return view1;
        }
    }
}