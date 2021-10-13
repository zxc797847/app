package com.avic.demo.sebeiglzx.utils.tools;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DownloadUtil {
    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }

        return downloadUtil;
    }

    public DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    /**
   //  * @param s
     * @param url          下载连接
    // * @param destFileDir  下载的文件储存目录
     * @param destFileName 下载文件名称
     * @param listener     下载监听
     */
    public void download( final String url, final String destFileName, final OnDownloadListener listener) {

        FormBody form = new FormBody.Builder()
                .add("params",destFileName).build();
        Request request = new Request.Builder()
                .url(url)
                .post(form)
               // .addHeader("Accept-Encoding","identity")
                .build();

        OkHttpClient client = new OkHttpClient();

        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
          public void onFailure(Call call, IOException e) {
                // 下载失败监听回调
              listener.onDownloadFailed(e);
            }

            public void onResponse(Call call, Response response) throws IOException {

                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                //储存下载文件的目录
                File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/Android/data/com.avic.demo.sebeigl/cache");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String destFile =response.header("Content-Disposition");
                String[] strs = destFile.split("=");
                String destFilel=strs[1].toString();
                File file = new File(dir,destFilel);

                try {
                    is =response.body().byteStream();
                  long total =response.body().contentLength();
                 //   System.out.println("Total輸出:"+total);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        //下载中更新进度条
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    //下载完成
                    listener.onDownloadSuccess(file);
                   String name=Environment.getExternalStorageDirectory().getPath()+"/Android/data/com.avic.demo.sebeigl/cache/"+ destFilel;
                    String namel=Environment.getExternalStorageDirectory().getPath()+"/Android/data/com.avic.demo.sebeigl/cache";
                    try {
                       ZipUtils.UnZipFolder(name,namel);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                  File files=new File(name);
                    files.delete();
                } catch (Exception e) {
                    listener.onDownloadFailed(e);
                }finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {

                    }
                }
            }



        });
    }
    public interface OnDownloadListener{
        //下载成功之后的文件
        void onDownloadSuccess(File file);
        //下载进度
        void onDownloading(int progress);
        //下载异常信息
        void onDownloadFailed(Exception e);
    }

}
