package com.avic.demo.sebeiglzx.utils.tools;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtiltwoe {
	/**
	 * 解压到指定目录
	 * 
	 * @param zipPath
	 * @param descDir
	 * @author isea533
	 */
	@Deprecated
	public static void unZipFiles(String zipPath, String descDir)
			throws IOException {
		unZipFiles(new File(zipPath), descDir);
	}

	/**
	 * 解压文件到指定目录
	 * 
	 * @param zipFile
	 * @param descDir
	 * @author isea533
	 */
	@SuppressWarnings("rawtypes")
	public static void unZipFiles(File zipFile, String descDir)
			throws IOException {
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
			outPath = new String(outPath.getBytes("utf-8"), "ISO8859-1");
			;
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
			// 输出文件路径信息
			//System.out.println(outPath);

			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
	}

	/**
	 * 递归压缩文件
	 * 
	 * @param source
	 *            源路径,可以是文件,也可以目录
	 * @param destinct
	 *            目标路径,压缩文件名
	 * @throws IOException
	 */
	public static void compress(String source, String destinct)
			throws IOException {
		List fileList = loadFilename(new File(source));
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
				new File(destinct)));

		byte[] buffere = new byte[8192];
		int length;
		BufferedInputStream bis;

		for (int i = 0; i < fileList.size(); i++) {
			File file = (File) fileList.get(i);
			zos.putNextEntry(new ZipEntry(getEntryName(source, file)));
			bis = new BufferedInputStream(new FileInputStream(file));

			while (true) {
				length = bis.read(buffere);
				if (length == -1)
					break;
				zos.write(buffere, 0, length);
			}
			bis.close();
			zos.closeEntry();
		}
		zos.close();
	}

	/**
	 * 递归获得该文件下所有文件名(不包括目录名)
	 * 
	 * @param file
	 * @return
	 */
	private static List loadFilename(File file) {
		List filenameList = new ArrayList();
		if (file.isFile()) {
			filenameList.add(file);
		}
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				filenameList.addAll(loadFilename(f));
			}
		}
		return filenameList;
	}

	/**
	 * 递归压缩文件
	 * 

	 *            源路径,可以是文件,也可以目录
	 * @param destinct
	 *            目标路径,压缩文件名
	 * @throws IOException
	 */
	public static void compress(List<File> fileList, String destinct)
			throws IOException {

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
				new File(destinct)));

		byte[] buffere = new byte[8192];
		int length;
		BufferedInputStream bis;
         int n=0;
		for (int i = 0; i < fileList.size(); i++) {
			File file = (File) fileList.get(i);
			if (!file.exists()){
				// System.out.println(file+"该图片在本机上不存在");
				Log.e(String.valueOf(file),"该图片在本机上不存在,不加入压缩包");
				continue;
			}

			zos.putNextEntry(new ZipEntry(getEntryName(file)));
			bis = new BufferedInputStream(new FileInputStream(file));
			while (true) {
				length = bis.read(buffere);
				if (length == -1)
					break;
				zos.write(buffere, 0, length);
			}
			bis.close();
			zos.closeEntry();
			n++;
		}
		Log.e(String.valueOf(n),"总共压缩"+n+"个图片文件");
		zos.close();
	}
	
	
	
	/**
	 * 获得zip entry 字符串
	 * 
	 * @param base
	 * @param file
	 * @return
	 */
	private static String getEntryName(String base, File file) {
		File baseFile = new File(base);
		String filename = file.getPath();
		// int index=filename.lastIndexOf(baseFile.getName());
		if (baseFile.getParentFile().getParentFile() == null)
			return filename.substring(baseFile.getParent().length());
		return filename.substring(baseFile.getParent().length() + 1);
	}
	

	private static String getEntryName(File file) {
		
		return file.getName();
	}
	
	 public static void main(String[] args) throws IOException {
//		 System.out.println("开始解压：");
//		 long starttime = System.currentTimeMillis();
//		 unZip("E:\\Ins_UHF_RFID_LocalService.rar","E:\\234\\");
//		 System.out.println("解压完成！耗时："+(System.currentTimeMillis()-starttime));
		 
		 
		 System.out.println("开始压缩：");
		 long starttime = System.currentTimeMillis();
		 compress("E:\\testupload","E:\\testupload\\testZip.zip");
		 System.out.println("压缩完成！耗时："+(System.currentTimeMillis()-starttime));
		 
		 String[] strarr = new String[]{"11111111.jpg","11111112.jpg","11111113.jpg"};
		 List<File> listfile = new ArrayList<File>();
		 for(String str : strarr){
			 
			 listfile.add(new File("E:\\testupload\\"+str));
			 
		 }
		 compress(listfile,"E:\\testupload\\downloadZip.zip");
		 
	 }
	 
	 private static final int buffer = 2048;   

  /*
   * 解压Zip文件
  * @param path 文件目录
   *//*
  public static void unZip(File path,String savepath)
     {
     int count = -1;
       File file = null;
      InputStream is = null;
      FileOutputStream fos = null;
      BufferedOutputStream bos = null;
      new File(savepath).mkdir(); //创建保存目录
      // ZipFile zipFile = null;
        try
       {
		   ZipFile zipFile = new ZipFile(path,"GBK"); //解决中文乱码问题
		ZipEntry entries = zipFile.getEntry(null);

           while(entries.hasMoreElements())
           {
              byte buf[] = new byte[buffer];

              ZipEntry entry = (ZipEntry)entries.nextElement();

            String filename = entry.getName();
                boolean ismkdir = false;
                if(filename.lastIndexOf("/") != -1){ //检查此文件是否带有文件夹
                   ismkdir = true;
                }
               filename = savepath + filename;

                if(entry.isDirectory()){ //如果是文件夹先创建
                   file = new File(filename);
                   file.mkdirs();                    continue;
                }
                file = new File(filename);
               if(!file.exists()){ //如果是目录先创建
                  if(ismkdir){
                   new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建
                   }
               }
               file.createNewFile(); //创建文件
               is = zipFile.getInputStream(entry);
              fos = new FileOutputStream(file);
               bos = new BufferedOutputStream(fos, buffer);

               while((count = is.read(buf)) > -1)
               {
                    bos.write(buf, 0, count);
                }
                bos.flush();
               bos.close();
               fos.close();

               is.close();
           }

           zipFile.close();

       }catch(IOException ioe){
          ioe.printStackTrace();
        }finally{
               try{
               if(bos != null){
                   bos.close();
              }
              if(fos != null) {
                   fos.close();
               }
              if(is != null){
                   is.close();
               }
               if(zipFile != null){
                   zipFile.close();
               }
              }catch(Exception e) {
                  e.printStackTrace(); 		}
         }
      }*/

   /* public static void unZip(File zipFile, String folderPath) throws IOException {
        OutputStream os = null;
        InputStream is = null;
        ZipFile zf = null;
        try {
            zf = new ZipFile(zipFile,"GBK");//这里可以指定编码。可以试试GBK和UTF-8，我自己用GBK还是有乱码，只是从一串问号变成了一串乱码。
            String directoryPath = "";
            directoryPath = folderPath;
            Enumeration entryEnum = zf.entries();
            if (null != entryEnum) {
                ZipEntry zipEntry = null;
                while (entryEnum.hasMoreElements()) {
                    zipEntry = (ZipEntry) entryEnum.nextElement();
                    if (zipEntry.isDirectory()) {  //不处理文件夹
                        directoryPath = directoryPath + File.separator
                                + zipEntry.getName();
                        System.out.println(directoryPath);
                        continue;
                    }
                    if (zipEntry.getSize() > 0) {

                        File targetFile = new File(directoryPath+ File.separator + zipEntry.getName());
                        if (!targetFile.exists()) {  //如果不存在就创建
                            File fileParentDir = targetFile.getParentFile();
                            if (!fileParentDir.exists()) {
                                fileParentDir.mkdirs();
                            }
                            targetFile.createNewFile();
                        }
                        //Log.i(TAG, new String(zipEntry.getName().getBytes(),"UTF-8"));
                        os = new BufferedOutputStream(new FileOutputStream(targetFile));
                        is = zf.getInputStream(zipEntry);
                        byte[] buffer = new byte[4096];
                        int readLen = 0;
                        while ((readLen = is.read(buffer, 0, 1024)) >= 0) {
                            os.write(buffer, 0, readLen);
                        }

                        os.flush();
                        os.close();
                    }
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (null != zf) {
                zf = null;
            }
            if (null != is) {
                is.close();
            }
            if (null != os) {
                os.close();
            }
        }
    }
*/
   @RequiresApi(api = Build.VERSION_CODES.N)
   public static void unzip(String zipFilePath, String targetPath)
           throws IOException {    OutputStream os = null;
           InputStream is = null;    ZipFile zipFile = null;
           try {
               //1.获取要解压的文件 ， 并指定解压格式为“GBK”
                 zipFile = new ZipFile(zipFilePath, Charset.forName("GBK"));
               // 读取zip文件中每一个文件及文件夹
                  Enumeration<?> entryEnum = zipFile.entries();
                   if (null != entryEnum) {
                 ZipEntry zipEntry = null;
                 //总共有多少文件或是文件夹就循环多少次
                       while (entryEnum.hasMoreElements())
                       {                //获取下一个文件或者文件夹
                              zipEntry = (ZipEntry) entryEnum.nextElement();
                              if (zipEntry.getSize() > 0) {
                                  // 文件  File.separator就是“/”
                                          File targetFile = new File(targetPath
                                                  + File.separator + zipEntry.getName());
                                          //将文件读取到指定文件位置
                                          os = new BufferedOutputStream(new FileOutputStream(targetFile));
                                          is = zipFile.getInputStream(zipEntry);
                                          byte[] buffer = new byte[4096];
                                          int readLen = 0;
                                          while ((readLen = is.read(buffer, 0, 4096)) >= 0) {
                                              os.write(buffer, 0, readLen);
                                              os.flush();
                                          }
                                          is.close();
                                          os.close();
                              }
                              //如果是文件夹，则创建文件夹
                                    if (zipEntry.isDirectory()) {
                                  String pathTemp = targetPath + File.separator
                                          + zipEntry.getName();
                                  File file = new File(pathTemp);
                                  file.mkdirs();
                              }            }
                   }
           } catch (IOException ex) {
               throw ex;
           } finally {
               //关闭流
                     if (null != zipFile) {
                         zipFile.close();
                         zipFile = null;
                     }        if (null != is) {
                         is.close();
                     }        if (null != os) {
                         os.close();
                     }
           }
   }

}
