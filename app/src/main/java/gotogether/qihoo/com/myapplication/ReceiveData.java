package gotogether.qihoo.com.myapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by czc on 2015/7/27.
 */
public class ReceiveData {
    private String destination;
    private String user_id;
    private Date current_time;

    public ReceiveData(String destination, String user_id) {
        this.destination = destination;
        this.user_id = user_id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setCurrent_time(String current_time) {
        final SimpleDateFormat formmater = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        try{
            this.current_time = formmater.parse(current_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getCurrent_time() {
        return current_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
