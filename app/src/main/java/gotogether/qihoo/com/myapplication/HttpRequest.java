package gotogether.qihoo.com.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by czc on 2015/7/22.
 */
public class HttpRequest {

    private static final String baseUrl = "http://192.168.23.1:8123/?";

    public static String userInit(String userName, String address, String gender) throws IOException{
        String requestUrl = baseUrl + "type=1&" + "username=" + userName + "&address=" + address + "&gender=" + gender;

        return readUrlFromSocket(requestUrl);
    }

    public static String postMatchInfo(String user_id, String current_time, String destination,
                                       String user_name, String wait_time, String sex, String hope_sex, String distance_bias, String priority) {
        String requestUrl = "";
        if(user_name == null) {user_name = "";}
        if(sex == null) {sex = "male";}
        if(hope_sex == null) {hope_sex = "";}
        if(current_time == null) {current_time = "";}
        if(wait_time == null) {wait_time = "5";}
        if(distance_bias == null) {distance_bias = "5";}
        if(priority == null) {priority = "2";}
        try {
            requestUrl = baseUrl + "type=6&user_id=" + user_id + "&user_name=" + user_name + "&sex=" + sex +
                    "&hope_sex=" + hope_sex + "&destination=" + URLEncoder.encode(destination, "utf-8") + "&current_time=" + current_time
                    + "&wait_time=" + wait_time + "&distance_bias=" + distance_bias + "&priority=" + priority + "&match_user_id=";
            //requestUrl = baseUrl + "type=6&" + "user_id=" + id + "&destination=" + URLEncoder.encode(destination, "utf-8") + "&current_time=" + time;
        } catch (UnsupportedEncodingException e){
            Log.e("getSuggestion", "encode error");
        }

        return readUrlFromSocket(requestUrl);
    }


    public static String getSuggestions(String input) {
        //input  = "qihu360";
        String baiduUrl = "";
        try {
            baiduUrl =  "http://api.map.baidu.com/place/v2/suggestion?query=" + URLEncoder.encode(input,"utf-8") + "&region=131&output=json&ak=8gHrOl4VGX9wMpQLTLy87CIu";
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

    public static ArrayList<ReceiveData> getMatchList(String user_id, String current_time, String destination,
                                      String user_name, String wait_time, String sex, String hope_sex, String distance_bias, String priority) {
        ArrayList<ReceiveData> result = new ArrayList<ReceiveData>();
        //String result = "";
        if(user_name == null) {user_name = "";}
        if(sex == null) {sex = "male";}
        if(hope_sex == null) {hope_sex = "";}
        //if(longitude == null) {longitude = "";}
        //if(latitude == null) {latitude = "";}
        if(current_time == null) {current_time = "";}
        if(wait_time == null) {wait_time = "5";}
        if(distance_bias == null) {distance_bias = "5";}
        if(priority == null) {priority = "2";}
        String requestUrl = baseUrl + "type=2&user_id=" + user_id + "&user_name=" + user_name + "&sex=" + sex +
                "&hope_sex=" + hope_sex + "&destination=" + destination + "&current_time=" + current_time
                + "&wait_time=" + wait_time + "&distance_bias=" + distance_bias + "&priority=" + priority + "&match_user_id=";
        //{'user_id':2,'user_name':'henry','sex':'male','hope_sex':'male','longitude':130,'latitude':37,'current_time':7,'wait_time':5,'distance_bias':1  ,'priority':0x111,'isMatch':1}
        String resultFromSocket = readUrlFromSocket(requestUrl);
        if(resultFromSocket != null) Log.e("getmatchlist", resultFromSocket);
        JSONArray jsonArray;
        try{
            Log.e("result", resultFromSocket);
            JSONObject json = new JSONObject(resultFromSocket);
            jsonArray = json.getJSONArray("list");
            for(int i = 0; i < jsonArray.length(); ++i) {
                JSONObject id = (JSONObject)jsonArray.get(i);
                ReceiveData fromServer = new ReceiveData("", "");
                //String user_id_fromServer = "";
                try {
                    fromServer.setUser_id(URLDecoder.decode(id.getString("user_id"), "UTF-8"));
                    fromServer.setDestination(URLDecoder.decode(id.getString("destination"), "UTF-8"));
                    fromServer.setCurrent_time(URLDecoder.decode(id.getString("current_time"), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                result.add(fromServer);
            }
        } catch (JSONException e) {
            Log.e("getMatchList", "json decode error");
        }
        //��ʱ����һ�����Լ���
        //result = "11,22,33,44,55";
        return result;
    }

    public static String getPersonStatus(String id) {
        //获取某id的状态，自己的或者要查询的人的
        String result = "";
        //if(id.equals("null")) return result;
        String requestUrl = baseUrl + "type=0&my_id=" + id;
        String resultFromSocket = readUrlFromSocket(requestUrl);
        try{
            Log.e("result", resultFromSocket);
            JSONObject json = new JSONObject(resultFromSocket);

            result = json.getString("current_status");
            result += "," + json.getString("match_user_id");

        } catch (JSONException e) {
            Log.e("getpersonstatus", "json decode error");
            result = ",";
        }
        return result;
    }

    public static String requestForFriend(String selfId, String friendId) {
        String result = "";
        String requestUrl = baseUrl + "type=3" + "&my_id=" + selfId + "&match_user_id=" + friendId;
        String resultFromSocket = readUrlFromSocket(requestUrl);

        try{
            Log.e("result", resultFromSocket);
            JSONObject json = new JSONObject(resultFromSocket);
            result = json.getString("current_status");

        } catch (JSONException e) {
            Log.e("getpersonstatus", "json decode error");
        }
        return result;
    }

    public static String acceptRequest(String selfId) {
        String result = "";
        String requestUrl = baseUrl + "type=4" + "&my_id=" + selfId;
        String resultFromSocket = readUrlFromSocket(requestUrl);
        try{
            Log.e("result", resultFromSocket);
            JSONObject json = new JSONObject(resultFromSocket);
            result = json.getString("current_status");

        } catch (JSONException e) {
            Log.e("getpersonstatus", "json decode error");
        }
        return result;//"match ok" or "match has failed"
    }

    public static String denyRequest(String selfId) {
        String result = "";
        String requestUrl = baseUrl + "type=5" + "&my_id=" + selfId;
        String resultFromSocket = readUrlFromSocket(requestUrl);
        try{
            Log.e("result", resultFromSocket);
            JSONObject json = new JSONObject(resultFromSocket);

            result = json.getString("current_status");

        } catch (JSONException e) {
            Log.e("getpersonstatus", "json decode error");
        }
        return result;//"deny ok" or "deny has failed"
    }

    public static String readUrlFromSocket(String requestUrl) {
        String result = "";
        try{
            Socket s = new Socket("192.168.23.1", 8123);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            //String outMsg = "TCP connecting to " + "8123" + System.getProperty("line.separator");
            String outMsg = requestUrl;
            out.write(outMsg);
            out.flush();
            result = in.readLine() + System.getProperty("line.separator");
            s.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String cancelLatestMatch(String selfID) {
        String result = "";
        String requestUrl = baseUrl + "type=8" + "&my_id=" + selfID;
        String resultFromSocket = readUrlFromSocket(requestUrl);
        result = resultFromSocket;
        return result;
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
