package com.avic.demo.sebeiglzx.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Environment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Image_album_showActivity extends Activity {
    private ImageView imageView;
    private ImageView iv_back;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF startPoint = new PointF();
    private PointF midPoint = new PointF();
    private float oriDis = 1f;
    private Handler handler;
    static final int GET_ERROR = 0;
    static final int GET_OK = 1;
    private Map<String, Bitmap> bitmapCache = new HashMap<String, Bitmap>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_album_show);
        imageView = (ImageView) findViewById(R.id.V_Image);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        Intent intent = getIntent();
        String sbtp = intent.getStringExtra("sbtp");
        int falg = intent.getIntExtra("falg", 1);
        if (falg == 1) {
            getLoadMageView(sbtp);
        } else if (falg == 0) {
            getIamgeView(sbtp);
        }
        //????????????
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imageView.setScaleType(ImageView.ScaleType.MATRIX);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    // ??????
                    case MotionEvent.ACTION_DOWN:
                        matrix.set(imageView.getImageMatrix());
                        savedMatrix.set(matrix);
                        startPoint.set(motionEvent.getX(), motionEvent.getY());
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oriDis = distance(motionEvent);
                        if (oriDis > 10f) {
                            savedMatrix.set(matrix);
                            midPoint = middle(motionEvent);
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {                    // ?????????????????????
                            matrix.set(savedMatrix);
                            matrix.postTranslate(motionEvent.getX() - startPoint.x, motionEvent.getY() - startPoint.y);
                        } else if (mode == ZOOM) {
                            // ??????????????????
                            float newDist = distance(motionEvent);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oriDis;
                                matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            }
                        }
                        break;
                }        // ??????ImageView???Matrix
                imageView.setImageMatrix(matrix);
                return true;
            }

        });

        //??????
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    /*
     * ???????????????????????????
     * */
    private void getLoadMageView(final String sbtp) {
        //???????????????????????????ui
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //??????UI
                switch (msg.what) {
                    case GET_OK:
                        super.handleMessage(msg);
                        if((Bitmap) msg.obj==null){
                            Toast.makeText(Image_album_showActivity.this, "??????????????????:????????????????????????????????????,?????????????????????!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            imageView.setImageBitmap((Bitmap) msg.obj);
                        }
                        break;
                    case GET_ERROR:
                        super.handleMessage(msg);
                        Toast.makeText(Image_album_showActivity.this, "????????????!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
        //????????????????????????????????????
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = URLHelper.getBaseUrl(Image_album_showActivity.this) + "/appController.do?getPicture&fileName=" + sbtp;
                //??????????????????????????????
                Bitmap bitmap = bitmapCache.get(path);
                if (bitmap != null) {
                    //???????????????????????????
                    Message msg = handler.obtainMessage();
                    msg.obj = bitmap;
                    msg.what = GET_OK;
                    handler.sendMessage(msg);
                } else {
                    try {
                        URL url = new URL(path);  //??????????????????????????????URL??????
                        //?????????????????????????????????????????????????????????????????????
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET"); //?????????????????????
                        connection.setRequestProperty("Content-type", "text/html");
                        connection.setRequestProperty("Accept-Charset", "utf-8");
                        connection.setRequestProperty("contentType", "utf-8");
                        connection.setDoInput(true);
                        connection.setDoOutput(false);  // GET ?????????????????????????????????
                        connection.setConnectTimeout(5000);  //??????????????????
                        connection.setReadTimeout(5000);     //??????????????????
                        connection.connect();  //???????????????????????????
                        //??????????????????200?????????????????????
                        if (connection.getResponseCode() == 200) {
                            //??????????????????????????????
                            InputStream is = connection.getInputStream();
                            //?????????????????????????????????bitmap??????
                            bitmap = BitmapFactory.decodeStream(is);
                            //????????????ui??????
                            Message msg = handler.obtainMessage();
                            msg.obj = bitmap;
                            msg.what = GET_OK;
                            handler.sendMessage(msg);
                            is.close();
                        } else {
                            //???????????????????????????
                            Message msg = handler.obtainMessage();
                            msg.what = GET_ERROR;
                            handler.sendMessage(msg);
                        }
                        connection.disconnect();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        //?????????????????????????????????
                        bitmapCache.put(path, bitmap);
                    }
                }
            }
        }).start();


    }


    /*
     * ??????????????????
     * */
    private void getIamgeView(String sbtp) {
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Android/data/com.avic.demo.sebeiglzx/cache");
        //**??????????????????????????????????????????*//*
        String[] allFiles;
        allFiles = folder.list();
        String str = sbtp;

        for (int i = 0; i < allFiles.length; i++) {
            if (allFiles[i].equals(str)) {
                String scanpath = folder + "/" + allFiles[i];
                Bitmap bitmap = BitmapFactory.decodeFile(scanpath);
                imageView.setImageBitmap(bitmap);
                break;
            }
        }
    }

    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return Float.valueOf(String.valueOf(Math.sqrt(x * x + y * y)));
    }

    private PointF middle(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        return new PointF(x / 2, y / 2);
    }


}
