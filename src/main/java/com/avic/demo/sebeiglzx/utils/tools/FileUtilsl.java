package com.avic.demo.sebeiglzx.utils.tools;

import android.os.Environment;

import java.io.File;

public class FileUtilsl {
    //private String path = Environment.getExternalStorageDirectory().toString() + "/shidoe";
    private String path=Environment.getExternalStorageDirectory().getPath()+"/Android/data/com.avic.demo.sebeigl/1234";


    public FileUtilsl() {

        File file = new File(path);

        /**

         *如果文件夹不存在就创建

         */

        if (!file.exists()) {

            file.mkdirs();

        }

    }



    /**

     * 创建一个文件

     * @param FileName 文件名

     * @return

     */

    public File createFile(String FileName) {

        return new File(path, FileName);

    }
}
