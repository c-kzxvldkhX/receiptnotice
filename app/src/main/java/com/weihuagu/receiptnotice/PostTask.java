package com.weihuagu.receiptnotice;

import java.util.List;

import android.os.AsyncTask;
import java.util.Map;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.Iterator;
import android.util.Log;

public class PostTask extends AsyncTask<Map<String, String>, Void, String> {

        public AsyncResponse asyncResponse;
        public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        public String TAG="NLService";
        OkHttpClient client = new OkHttpClient();
        String httppost(String url, String json) throws IOException {
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                try (Response response = client.newCall(request).execute()) {
                        return response.body().string();
                }
        }

        String httpspost(String url, String json) throws IOException {
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                try (Response response = client.newCall(request).execute()) {
                        return response.body().string();
                }
        }
        //fuck 竟然不导包找不到个好的map转json的
        public String map2Json(Map<String,String> map){


                String mapjson="";
                Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
                while (entries.hasNext()) {

                        Map.Entry<String, String> entry = entries.next();

                        mapjson=mapjson+'"'+entry.getKey()+'"' + ":"+'"'+entry.getValue()+'"'+",";


                }
                int strlength=(int)mapjson.length();
                mapjson=mapjson.substring(0,(strlength-1));
                mapjson="{"+mapjson+"}";
                return mapjson;


        }
        public void setOnAsyncResponse(AsyncResponse asyncResponse)
        {
                this.asyncResponse = asyncResponse;
        }

        @Override
        protected String doInBackground(Map<String,String> ... key) {
                Map<String ,String> postmap=key[0];
                if(postmap==null)
                        return null;
                String url = postmap.get("url");

                if(url==null)
                        return null;

                postmap.remove("url");
                String protocol=UrlUtil.httpOrHttps(url);
                String postjson=map2Json(postmap);
                if("http".equals(protocol)){
                        try{
                                Log.d(TAG,"post task  url:"+url);
                                Log.d(TAG,"post task postjson:"+postjson);
                                return httppost(url,postjson);
                        }catch(IOException e){}
                }
                if("https".equals(protocol)){
                        try{
                                Log.d(TAG,"post task  url:"+url);
                                Log.d(TAG,"post task postjson:"+postjson);
                                return httpspost(url,postjson);
                        }catch(IOException e){}

                }
                return null;
        }

        @Override
        protected void onPostExecute(String returnstr) {
                super.onPostExecute(returnstr);
                if (returnstr != null)
                {
                        asyncResponse.onDataReceivedSuccess(returnstr);//将结果传给回调接口中的函数
                }
                else {
                        asyncResponse.onDataReceivedFailed();
                }

        }

}
