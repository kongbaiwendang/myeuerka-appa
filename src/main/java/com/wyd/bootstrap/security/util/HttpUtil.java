package com.wyd.bootstrap.security.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * get请求方法
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    public static String sendGetRequest(String url, Map<String,Object> data) throws Exception {
        CloseableHttpClient httpCilent = HttpClients.createDefault();
        StringBuffer sburl = new StringBuffer(url);
        if(data != null){
            sburl.append('?');
            for(Map.Entry entry : data.entrySet()){
                sburl.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
            }
        }
        logger.info("请求微信的Url:{}",sburl.toString());

        HttpGet httpGet = new HttpGet(sburl.substring(0,sburl.length()-1));
        try {
            CloseableHttpResponse closeableHttpResponse = httpCilent.execute(httpGet);
            closeableHttpResponse.getEntity();
            return EntityUtils.toString(closeableHttpResponse.getEntity());
        } catch (IOException e) {
            logger.error("http接口连接失败。",e);
            throw new Exception(e);
        }finally {
            try {
                httpCilent.close();//释放资源
            } catch (IOException e) {
                logger.error("http接口资源释放失败。",e);
                throw new Exception(e);
            }
        }
    }

    /**
     * post请求方法
     * @param url
     * @param requestStr
     * @return
     */
    public static String sendPostRequest(String url,String requestStr) throws Exception {
        HttpPost post = null;
        HttpClient httpClient = HttpClientBuilder.create().setConnectionTimeToLive(30000, TimeUnit.MILLISECONDS).build();
        post = new HttpPost(url);
        post.setHeader("Content-type", "text/xml; charset=utf-8");

        StringEntity entity = new StringEntity(requestStr, Charset.forName("UTF-8"));
        entity.setContentEncoding("UTF-8");
        post.setEntity(entity);

        HttpResponse response = null;
        try {
            response = httpClient.execute(post);
        } catch (IOException e) {
            logger.error("接口连接请求出错: ",e);
            throw new Exception("接口连接异常。");
        }

        // 检验返回码
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode != HttpStatus.SC_OK){
            logger.info("接口请求出错: "+statusCode);
            throw new Exception("接口请求出错。");
        }else {
            HttpEntity httpEntity = response.getEntity();
            String responseXml = null;
            try {
//                responseXml = EntityUtils.toString(httpEntity);
                responseXml = getResponseString(response);
            } catch (IOException e) {
                logger.error("解析接口返回内容异常。",e);
                throw new Exception("解析接口返回内容异常。");
            }
            return responseXml;
        }
    }


    private static String getResponseString(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();//响应实体类
        StringBuilder result = new StringBuilder();//响应正文
        if (entity != null) {
            InputStream instream = entity.getContent();
            byte[] bytes = new byte[4096];
            int size = 0;
            try {
                while ((size = instream.read(bytes)) > 0) {
                    String str = new String(bytes, 0, size, "utf-8");
                    result.append(str);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    instream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }
}
