package gotogether.qihoo.com.myapplication;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by czc on 2015/7/22.
 */
public class HttpRequest {

    private  String baseURL ;
    private String postParams;
    private String result;

    public HttpRequest(String baseURL, String postParams) {
        this.baseURL = baseURL;
        this.postParams = postParams;
    }

    public String fatchFromURL() throws IOException{
       // String requestUrl = baseUrl + "type=1&" + "username=" + userName + "&address=" + address + "&gender=" + gender;

        String requestUrl = this.baseURL + this.postParams;
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
