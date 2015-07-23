package gotogether.qihoo.com.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by czc on 2015/7/22.
 */
public class HttpRequest {

    private static final String baseUrl = "http://10.0.2.2:8123/?";

    public static String userInit(String userName, String address, String gender) throws IOException{
        String requestUrl = baseUrl + "type=1&" + "username=" + userName + "&address=" + address + "&gender=" + gender;

        return readUrl(requestUrl);
    }

    public static String readUrl(String requestUrl) {
        String result = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String getSuggestions(String input) {
        //input  = "qihu360";
        String baiduUrl = "";
        try {
            baiduUrl =  "http://api.map.baidu.com/place/v2/suggestion?query=" + URLEncoder.encode(input,"utf-8") + "&region=131&output=json&ak=6wPZISS0ZkeO90nZshdBZxgD";
        } catch (UnsupportedEncodingException e){
            Log.e("getSuggestion", "encode error");
        }
        try {
            JSONObject sugJson =  new JSONObject(readUrl(baiduUrl));
            //JSONObject sugResult = sugJson.getJSONObject("result");
            String result = "";
            JSONArray sugList = sugJson.getJSONArray("result");
            for(int i = 0; i < sugList.length(); ++i) {
                result += sugList.getJSONObject(i).getString("name") + ",";
            }
            return result;
        } catch (JSONException e){
            Log.e("getSuggestions", "json null");
        }
        return null;
    }

    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for(int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[ i ] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

}
