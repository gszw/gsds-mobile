/**   
* @Title: 甘肃地税移动征管系统
* @Package cn.com.gszw.mzgxt.util 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Administrator  
* @date 2013-3-28 下午3:15:43 
* @version V1.0   
*/ 

package cn.com.gszw.mzgxt.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *@Description: TODO(用一句话描述这个类的作用)
 *@Copyright: Copyright © 2013-2018
 *@Company: www.gszw.com.cn 
 *@Makedate:2013-3-28 下午3:15:43
 *@author Administrator 
 */
public class GetImage{
    public    byte[] getimage(String path) throws Exception {
        System.out.println("no111");
       URL url = new URL(path);// 设置URL
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 打开链接
       conn.setRequestMethod("GET");// 设置链接方式
       conn.setConnectTimeout(5 * 1000);// 设置链接超时
       InputStream inStream = conn.getInputStream();// 得到输入流
       
       byte[] data = readinputStream(inStream);
       return data;
   }
    public  byte[] readinputStream(InputStream inputStream) throws Exception{
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          byte[] buffer = new byte[1024];
          int lns = 0;
         while ((lns = inputStream.read(buffer)) != -1) {
             outputStream.write(buffer, 0, lns);
         }
         inputStream.close();
         return outputStream.toByteArray();
     }
}	    