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

    public static String postMatchInfo(String id, String destination, String time) {
        String requestUrl = baseUrl + "type=6&" + "user_id=" + id + "&destination=" + destination + "&current_time=" + time;
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

    public static String getMatchList(String user_id, String current_time, String destination,
                                      String user_name, String wait_time, String sex, String hope_sex, String distance_bias, String priority) {
        if(user_name == null) {user_name = "";}
        if(sex == null) {user_name = "";}
        if(hope_sex == null) {hope_sex = "";}
        //if(longitude == null) {longitude = "";}
        //if(latitude == null) {latitude = "";}
        if(current_time == null) {current_time = "";}
        if(wait_time == null) {wait_time = "";}
        if(distance_bias == null) {distance_bias = "";}
        if(priority == null) {priority = "";}
        String requestUrl = baseUrl + "type=2&user_id=" + user_id + "&user_name=" + user_name + "&sex=" + sex +
                "&hope_sex=" + hope_sex + "&destination=" + destination + "&current_time=" + current_time
                + "&wait_time=" + wait_time + "&distance_bias=" + distance_bias + "&priority=" + priority;
        //{'user_id':2,'user_name':'henry','sex':'male','hope_sex':'male','longitude':130,'latitude':37,'current_time':7,'wait_time':5,'distance_bias':1  ,'priority':0x111,'isMatch':1}
        String result = readUrl(requestUrl);

        //暂时返回一个测试集合
        result = "11,22,33,44,55";
        return result;
    }

    public static String getPersonStatus(String id) {
        //查询某人有没有被约
        String result;
        //todo
        result = "ok";
        return result;
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
