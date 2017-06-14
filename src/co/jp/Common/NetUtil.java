package co.jp.Common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * NetUtil.
 *
 * @author Dulk
 * @version 20170612
 * @date 2017/6/12
 */
public class NetUtil {

    /**
     * 发送Get方式的http请求
     * @param urlStr
     * @return
     * @throws IOException
     */
    public static String sendHttpGet(String urlStr) throws IOException {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            setHttpUrlConnection(conn, "GET");
            conn.connect();
            return readResponseContent(conn.getInputStream());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    /**
     * 设置http连接属性
     * @param conn
     * @param requestMethod
     * @throws ProtocolException
     */
    private static void setHttpUrlConnection(HttpURLConnection conn, String requestMethod) throws ProtocolException {
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("Accept-Language", "zh-CN");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
        conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
        if ("POST".equals(requestMethod)) {
            conn.setDoOutput(true);
            conn.setDoInput(true);
        }
    }

    /**
     * 读取响应字节流并转为字符串
     * @param in
     * @return
     * @throws IOException
     */
    private static String readResponseContent(InputStream in) throws IOException {
        Reader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            reader = new InputStreamReader(in);
            char[] buffer = new char[1024];
            int head = 0;
            while ((head = reader.read(buffer)) > 0) {
                content.append(new String(buffer, 0, head));
            }
            return content.toString();
        } finally {
            if (null != in) in.close();
            if (null != reader) reader.close();
        }
    }


}
