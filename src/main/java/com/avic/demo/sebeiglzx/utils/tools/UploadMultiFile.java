package com.avic.demo.sebeiglzx.utils.tools;

import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;



public class UploadMultiFile {
    /**
     * 根据url，发送异步Post请求
     * @param url 提交到服务器的地址
     * @param fileNames 完整的上传的文件的路径名
     * @param callback OkHttp的回调接口
     */
    public static void upLoadFile2(String url, String  fileNames,final ProgressListener listener, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(getRequest2(url,fileNames,listener));
        call.enqueue(callback);
    }

    /**
     * 获得Request实例
     * @param url
     * @param fileNames 完整的文件路径
     * @return
     */


    private static Request getRequest2(String url,String  fileNames,final ProgressListener listener) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(getRequestBody2(fileNames,listener));
        return builder.build();

    }

    private static RequestBody getRequestBody2(String  fileNames,final ProgressListener listener) {
        //创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
            File file = new File(fileNames); //生成文件
            //根据文件的后缀名，获得文件类型
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart( //给Builder添加上传的文件
                    "image",  //请求的名字
                    file.getName(), //文件的文字，服务器端用来解析的
                    createCustomRequestBody(MediaType.parse(fileType), file,listener) //创建RequestBody，把上传的文件放入
            );


        /*builder.addFormDataPart( //给Builder添加上传的文件
                "image",  //请求的名字
                "1234.zip", //文件的文字，服务器端用来解析的

                //RequestBody.create(MediaType.parse("cache.zip"),new File(Environment.getExternalStorageDirectory().getPath()+"/Android/data/com.avic.demo.sebeigl/cache/fffzip.zip")) //创建RequestBody，把上传的文件放入
               createCustomRequestBody(MediaType.parse("1234.zip"),new File(Environment.getExternalStorageDirectory().getPath()+"/Android/data/com.avic.demo.sebeigl/1234/1234.zip"),listener)

        );*/
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


}
