package gotogether.qihoo.com.myapplication;

import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by czc on 2015/7/22.
 */
public class MainFragment extends Fragment{
    private Button mAppointment;
    private TextView  mDestination;
    private  TextView  mTime;

    private final int REQUEST_CODE_ADDRESS = 1;
    private final int REQUEST_CODE_TIME = 2;


    ArrayAdapter<String> destinationSuggestion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if(data == null || data.getStringExtra("destination") == null || mTime.getText().toString() == ""){
        //    Toast.makeText(getActivity(), "请输入地址和时间", Toast.LENGTH_SHORT).show();
        //} else {
        //}
        switch (resultCode) {
            case -1:
                mDestination.setText("");
                mDestination.setText(data.getStringExtra("destination"));
                break;
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, parent, false);

        mAppointment = (Button)v.findViewById(R.id.appointment);
        mAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mainFragment", "clickonYue");
                // Intent dAndT = getActivity().getIntent();
                //  if(dAndT.getStringExtra("destination") == null || mTime.getText().toString() == "") {
                if( mTime.getText() == null || mDestination.getText() == null){
                    Toast.makeText(getActivity(), "please input destination and time", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent i = new Intent(getActivity(), ChoosePersonActivity.class);
                    i.putExtra("destination", mDestination.getText().toString());
                    i.putExtra("time", mTime.getText().toString());
                    //向服务器注册约的信息

                    startActivity(i);
                }
                //Toast.makeText(getActivity(), "mainFragment action3.clickonYue", Toast.LENGTH_SHORT).show();
            }
        });

        //
        mDestination = (TextView)v.findViewById(R.id.Destination);
        mDestination.setClickable(false);//
        mDestination.setKeyListener(null);//

        mDestination.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Log.d("Test", "This is onTouch,return false");
                Toast.makeText(getActivity(), "mainFragment action3.touch mDestination", Toast.LENGTH_SHORT).show();
                Intent addressIt = new Intent(getActivity(), AddressActivity.class);

                startActivityForResult(addressIt, REQUEST_CODE_ADDRESS);
                return false;
            }
        });


        //
        mTime = (TextView)v.findViewById(R.id.goTime);
        mTime.setClickable(false);
        mTime.setKeyListener(null);

        mTime.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Log.d("Test", "This is onTouch,return false");
                //Toast.makeText(getActivity(), "mainFragment action3.touch mTime", Toast.LENGTH_SHORT).show();
                //  Intent addressIt = new Intent(getActivity(), AddressActivity.class);
                // startActivityForResult(addressIt, REQUEST_CODE_TIME);
                final SimpleDateFormat formmater = new SimpleDateFormat("yyyy-MM-dd"
                        "  HH-mm-ss");
                Dialog dialogTime = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mTime.setFocusable(false);
                                mTime.setText("");
                                Date CurDate = new Date(System.currentTimeMillis());
                                CurDate.setHours(hourOfDay);
                                CurDate.setMinutes(minute);
                                CurDate.setSeconds(0);
                                String str = formmater.format(CurDate);
                                mTime.setText(str);
                            }
                        }, 20, 00, true);//
                dialogTime.show();
                return false;
            }
        });

        return v;
    }



    private class postMatchInfo extends AsyncTask<Void, Void, Boolean> {
        //向服务器注册匹配信息
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean result = true;
            String getHttp = "";//todo 状态根据服务器端
            while(getHttp != "ok") {
                try {
                    getHttp = HttpRequest.postMatchInfo("czc", mDestination.getText().toString(), mTime.getText().toString());
                } catch (java.lang.NullPointerException e){
                    Log.e("fetchMatchedList", "null pointer");
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean p) {
            Log.e("postMatchInfo", "user init ok");
        }
    }
}
