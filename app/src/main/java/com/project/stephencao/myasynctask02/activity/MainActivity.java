package com.project.stephencao.myasynctask02.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.project.stephencao.myasynctask02.R;
import com.project.stephencao.myasynctask02.adapter.MyNewsAdapter;
import com.project.stephencao.myasynctask02.bean.NewsBean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private MyNewsAdapter mMyNewsAdapter;
    private static final String RESOURCE_URL = "http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        new MyAsyncTask().execute(RESOURCE_URL);
    }

    private List<NewsBean> getJsonData(String url) {
        List<NewsBean> newsBeans = new ArrayList<>();
        String result = readStream(url);
        JSONObject jsonObject;
        NewsBean newsBean;
        try {
            jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                newsBean = new NewsBean((String) object.get("name"), (String) object.get("picSmall"), (String) object.get("description"));
                newsBeans.add(newsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsBeans;
    }

    private String readStream(String url) {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URLConnection connection = new URL(url).openConnection();
            inputStream = connection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuffer.toString();
    }

    private void initView() {
        mListView = findViewById(R.id.id_main_listview);
    }

    class MyAsyncTask extends AsyncTask<String, Void, List<NewsBean>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<NewsBean> doInBackground(String... strings) {
            return getJsonData(strings[0]);
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeans) {
            super.onPostExecute(newsBeans);
            mMyNewsAdapter = new MyNewsAdapter(newsBeans, MainActivity.this, mListView);
            mListView.setAdapter(mMyNewsAdapter);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}
