package com.avic.demo.sebeiglzx.utils.tools;


import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class UploadFile {

    /**

     * 根据url，发送异步Post请求

     * @param url 提交到服务器的地址

     * @param fileNames 完整的上传的文件的路径名

     * @param callback OkHttp的回调接口

     */

    public static void upLoadFile(String url, String fileNames,List<Map<String,Object>>listData,final ProgressListener listener, Callback callback ){

        OkHttpClient okHttpClient = new OkHttpClient();

        Call call = okHttpClient.newCall(getRequest(url,fileNames,listData,listener));
        call.enqueue(callback);
    }



    /**

     * 获得Request实例

     * @param url

     * @param fileNames 完整的文件路径

     * @return

     */

    private static Request getRequest(String url, String fileNames, List<Map<String,Object>>listData,final ProgressListener listener) {

        Request.Builder builder = new Request.Builder();

        builder.url(url)
                .post(getRequestBody(fileNames,listData,listener));
        return builder.build();

    }


    /**

     * 通过上传的文件的完整路径生成RequestBody

     * @param fileNames 完整的文件路径

     * @return

     */

    private static RequestBody getRequestBody(String fileNames, List<Map<String,Object>>listData,final ProgressListener listener) {
        //创建MultipartBody.Builder，用于添加请求的数据
       int n=1;
        String output = getRequestDatau(listData, "utf-8");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if(fileNames.equals("/storage/emulated/0/Android/data/com.avic.demo.sebeiglzx/cache/")){
            builder.addFormDataPart("params", output);
        }else {
            File file = new File(fileNames); //生成文件
            if (!file.exists()) {
                // System.out.println(file+"该图片在本机上不存在");
                Log.e(String.valueOf(file), "该图片在本机上不存在");
                builder.addFormDataPart("params", output);
            }else {
                //根据文件的后缀名，获得文件类型
                String fileType = getMimeType(file.getName());
                builder.addFormDataPart( //给Builder添加上传的文件
                        "image",  //请求的名字
                         file.getName(), //文件的文字，服务器端用来解析的
                        //  RequestBody.create(MediaType.parse(fileType), file) //创建RequestBody，把上传的文件放入
                        createCustomRequestBody(MediaType.parse(fileType), file, listener))
                        .addFormDataPart("params", output);
                Log.i(String.valueOf(n), "总共上传" + n + "图片文件");
            }
        }
        return builder.build(); //根据Builder创建请求
    }
    public static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime="";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }

    public static RequestBody createCustomRequestBody(final MediaType contentType, final File file, final ProgressListener listener) {
        return new RequestBody() {
            @Override public MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                return file.length();
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        listener.onProgress(contentLength(), remaining -= readCount, remaining == 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

  public   interface ProgressListener {
        void onProgress(long totalBytes, long remainingBytes, boolean done);
    }

    private static String getRequestDatau(List<Map<String, Object>> params, String encode) {

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < params.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            Map<String, Object> map = params.get(i);
            // 迭代器
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                try {
                    jsonObject.put(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }


}
