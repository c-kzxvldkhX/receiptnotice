package com.weihuagu.receiptnotice;

import java.util.List;
import java.io.PrintWriter;  
import java.io.StringWriter;  

import android.os.AsyncTask;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLSocketFactory;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.ConnectionSpec;
import java.util.Iterator;
import android.util.Log;
import android.os.Build;

public class PostTask extends AsyncTask<Map<String, String>, Void, String[]> {

        public AsyncResponse asyncResponse;
        public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        public String TAG="NLService";
        public String randomtasknum;
        public Map<String ,String> recordpostmap;
        public void setRandomTaskNum(String num){
                this.randomtasknum=num;
        }
        OkHttpClient client = new OkHttpClient();
        String httppost(String url, String json) throws IOException {
                RequestBody body = RequestBody.create(JSON, json);
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                        .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                        .build();
                Request.Builder request = new Request.Builder()
                        .url(url)
                        .post(body);
                try (Response response = client.newCall(request.build()).execute()) {
                        return response.body().string();
                }
        }

        String httpspost(String url, String json)  throws IOException{
            if (Build.VERSION.SDK_INT >= 22 )
                return httppost(url, json);
            try {
                RequestBody body = RequestBody.create(JSON, json);
                SSLSocketFactory factory = new SSLSocketFactoryCompat();
                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build();

            List<ConnectionSpec> specs = new ArrayList<>();
            specs.add(cs);
            specs.add(ConnectionSpec.COMPATIBLE_TLS);
            specs.add(ConnectionSpec.CLEARTEXT);

    
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                        .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                        .sslSocketFactory(factory)
                        .connectionSpecs(specs)
                        .build();
                
                Request.Builder request = new Request.Builder()
                        .url(url)
                        .post(body);
                        Response response = client.newCall(request.build()).execute();
                        return response.body().string();
                }
                catch( IOException e){
                        StringWriter sw = new StringWriter();  
                        PrintWriter pw = new PrintWriter(sw);  
                        e.printStackTrace(pw);  
                        LogUtil.debugLog(sw.toString()); 
                        return null;
                }
                catch (KeyManagementException e) {
                       e.printStackTrace();
                        return null;
                } catch (NoSuchAlgorithmException e) {
                     e.printStackTrace();
                     return null;
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
        protected String[] doInBackground(Map<String,String> ... key) {
                recordpostmap=key[0];
                Map<String ,String> postmap=new HashMap<String,String>();
                postmap.putAll(key[0]);
                if(postmap==null)
                        return null;
                String url = postmap.get("url");

                if(url==null)
                        return null;

                String[] resultstr=new String[3];
                resultstr[0]=this.randomtasknum;
                postmap.remove("url");
                String protocol=UrlUtil.httpOrHttps(url);
                String postjson=map2Json(postmap);
                if("http".equals(protocol)){
                        try{
                                Log.d(TAG,"post task  url:"+url);
                                Log.d(TAG,"post task postjson:"+postjson);
                                String returnstr=httppost(url,postjson);
                                resultstr[1]="true";
                                resultstr[2]=returnstr;
                                return resultstr;
                        }catch(IOException e){}
                }
                if("https".equals(protocol)){
                        try{
                                Log.d(TAG,"post task  url:"+url);
                                Log.d(TAG,"post task postjson:"+postjson);
                                String returnstr=httpspost(url,postjson);
                                resultstr[1]="true";
                                resultstr[2]=returnstr;
                                return resultstr;
                        }catch(IOException e){}

                }
                return null;
        }

        @Override
        protected void onPostExecute(String[] resultstr) {
                super.onPostExecute(resultstr);
                if (resultstr != null)
                {
                        asyncResponse.onDataReceivedSuccess(resultstr);//将结果传给回调接口中的函数
                }
                else {
                        String [] errstr=new String[3];
                        errstr[0]=this.randomtasknum;
                        errstr[1]="false";
                        errstr[2]="";
                        if(recordpostmap.get("repeatnum")!=null){
                             String repeatnumstr=recordpostmap.get("repeatnum");
                             int num=Integer.parseInt(repeatnumstr)+1;
                             recordpostmap.put("repeatnum",String.valueOf(num));
                            //key 存在
                        }
                        else
                            recordpostmap.put("repeatnum","1");
                        asyncResponse.onDataReceivedFailed(errstr,recordpostmap);
                }

        }

}
