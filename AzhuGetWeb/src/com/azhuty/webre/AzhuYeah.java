package com.azhuty.webre;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AzhuYeah {

	private static final String testFileName = "E:/20150515.jpg";
	
	public static void main(String[] args)  {
		
		String ttUrl = "";
		try {
			ttUrl = method2();
		} catch (Exception e) {
			e.printStackTrace();
		}
		AzhuGetResult(ttUrl);
		
	}
	
    private static String method2() throws Exception {
    	
        File pcmFile = new File(testFileName);
        //long j=pcmFile.length();
        String serverURL = "http://stu.baidu.com/n/image?fr=html5&target=pcSearchImage&needJson=true&id=WU_FILE_0&name=psu.jpg&type=image%2Fjpeg&lastModifiedDate=Wed+May+13+2015+8%3A27%3A56+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&size=172927";
		HttpURLConnection conn = (HttpURLConnection) new URL(serverURL).openConnection();
		//若改为自动上传并识别图片，需要做的是用代码获取图片基本信息，并按照该基本信息相应的修改上面的URL及下面的size Date name type lastModifiedDate等
		// add request header
		conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "image/jpeg");
        conn.setRequestProperty("Connection", "Keep-Alive");  
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)"); 
        conn.setRequestProperty("host", "stu.baidu.com");
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("Content-Length", "172927");
        conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        // send request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.write(loadFile(pcmFile));
        wr.flush();
        wr.close();
        return printResponse(conn);
     }

     private static String printResponse(HttpURLConnection conn) throws Exception {
         if (conn.getResponseCode() != 200) {
             // request error
             return "";
         }
         InputStream is = conn.getInputStream();
         BufferedReader rd = new BufferedReader(new InputStreamReader(is));
         String line;
         StringBuffer response = new StringBuffer();
         while ((line = rd.readLine()) != null) {
             response.append(line);
             response.append('\r');
         }
         rd.close();
         
         //System.out.println(response.toString());
         
         String tang = response.toString();
         char azhu[] = tang.toCharArray();
         int i = 0;
         int len = azhu.length;
         char azhu2[] = null;
         azhu2 = new char[len];
         while('U' != azhu[i] && i < len) 
         	i++;
         while(i< len){
         	if('e' == azhu[i-1] && 'g' == azhu[i-2] && 'a' == azhu[i-3] && 'p' == azhu[i-4]){ //pageU
         		i = i+6;
         		int j = 0;

         		while(azhu[i] != '\"'){
         			if('\\' != azhu[i]){
         				azhu2[j] = azhu[i];
             			i++;
             			j++;
         			}
         			else
         				i++;  			
         		}
         	}
         	i++;
         }
         
         String azhuUrl = new String(azhu2);
         //System.out.print(azhuUrl);
        
         return azhuUrl;
     }

     private static byte[] loadFile(File file) throws IOException {
         InputStream is = new FileInputStream(file);

         long length = file.length();
         byte[] bytes = new byte[(int) length];

         int offset = 0;
         int numRead = 0;
         while (offset < bytes.length
                 && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
             offset += numRead;
         }

         if (offset < bytes.length) {
             is.close();
             throw new IOException("Could not completely read file " + file.getName());
         }

         is.close();
         return bytes;
     }
     
     public static void AzhuGetResult(String str){
    	 try {
    		 
    		 URL url = new URL(str);  
    		 HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
    		 InputStreamReader input = new InputStreamReader(httpConn
    				 .getInputStream(), "utf-8");  
    		 BufferedReader bufReader = new BufferedReader(input);  
    		 String strtt = "";  
    		 StringBuilder contentBuf = new StringBuilder();  
    		 while ((strtt = bufReader.readLine()) != null) {  
    			 contentBuf.append(strtt);  
    		 }  
    		 //System.out.print(contentBuf.toString());
    		 String buf = contentBuf.toString();  
    		 int beginIx = buf.indexOf("guess-info-title");  
    		 int endIx = buf.indexOf("图片尺寸");  
    		 String resultRaw = buf.substring(beginIx, endIx);  
    		 //System.out.println("captureHtml()的结果：\n" + resultRaw);  
    		 
    		 int i = 0, j = 0;   		
    		 char temp[] = resultRaw.toCharArray();
    		 int len = temp.length;
    		 //System.out.print(temp);
    		 
    		 char myResult[] = new char[100];
    		 while(i<len){
    			 if(isChinese(temp[i])){
    				 myResult[j++] = temp[i];
    			 }
    			 i++;
    		 }
    		 System.out.print(myResult);    		    		 
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		}	       	 
     }
     
     private static boolean isChinese(char c) {
         Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
         if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                 || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                 || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                 || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
             return true;
         }
         return false;
     }
     
}

