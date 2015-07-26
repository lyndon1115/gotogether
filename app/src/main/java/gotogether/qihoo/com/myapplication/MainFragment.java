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
    private String  mNowTime;

    private final int REQUEST_CODE_ADDRESS = 1;
    private final int REQUEST_USERINFO = 2;

    private SharedPreferencesUtils sharedpreferences = new SharedPreferencesUtils();

    ArrayAdapter<String> destinationSuggestion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "onCreate ", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("HH:mm");
        Date    curDate    =   new    Date(System.currentTimeMillis());//
        mNowTime    =    formatter.format(curDate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if(data == null || data.getStringExtra("destination") == null || mTime.getText().toString() == ""){
        //    Toast.makeText(getActivity(), "请输入地址和时间", Toast.LENGTH_SHORT).show();
        //} else {
        //}
        Toast.makeText(getActivity(), "REQUEST_CODE"+requestCode, Toast.LENGTH_SHORT).show();
       // if (requestCode == REQUEST_CODE_ADDRESS)
            switch (resultCode) {
                case -1:
                    mDestination.setText("");
                    mDestination.setText(data.getStringExtra("destination"));
                    break;
                default:
                    break;
            }
    /*    else if (requestCode == REQUEST_USERINFO)
        {
            Toast.makeText(getActivity(), "REQUEST_USERINFO", Toast.LENGTH_SHORT).show();
            String defaultAddress = (String)sharedpreferences.getParam(getActivity(), getString(R.string.pref_file),getString(R.string.pref_user_address),"");
             if(defaultAddress !=null )
                    mDestination.setText(defaultAddress);
         }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        sharedpreferences.setParam(getActivity(), getString(R.string.pref_file), getString(R.string.mainfragment_initial_flag), "1");
        Toast.makeText(getActivity(), "onCreateView ", Toast.LENGTH_SHORT).show();
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

       String defaultAddress = (String)sharedpreferences.getParam(getActivity(), getString(R.string.pref_file),getString(R.string.pref_user_address),"");
       if(defaultAddress !=null && mDestination.getText().toString()==getString(R.string.destination))  mDestination.setText(defaultAddress);
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

        //set now time, Lin
        mTime = (TextView)v.findViewById(R.id.goTime);

        if(mNowTime != null)
            mTime.setText(mNowTime.toString());
        mTime.setClickable(false);
        mTime.setKeyListener(null);

        mTime.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Log.d("Test", "This is onTouch,return false");
                //Toast.makeText(getActivity(), "mainFragment action3.touch mTime", Toast.LENGTH_SHORT).show();
                //  Intent addressIt = new Intent(getActivity(), AddressActivity.class);
                // startActivityForResult(addressIt, REQUEST_CODE_TIME);
                final SimpleDateFormat formmater = new SimpleDateFormat("HH:mm");
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

  /*  @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "onActivityCreated "+mTime.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getActivity(), "onDestroyView "+mTime.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getActivity(), "onDestroy "+mTime.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getActivity(), "onStop "+mTime.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Toast.makeText(getActivity(), "onDetach "+mTime.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getActivity(), "onPause "+mTime.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "onStart "+mTime.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "onStart "+mTime.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(getActivity(), "onSaveInstanceState "+mTime.getText().toString(), Toast.LENGTH_SHORT).show();
    }
*/
    /*  @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "onStart", Toast.LENGTH_SHORT).show();
        String initialFlag = (String)sharedpreferences.getParam(getActivity(), getString(R.string.pref_file),getString(R.string.mainfragment_initial_flag),"");
        if(initialFlag.equals("1")) return;

        String statusAddress = (String)sharedpreferences.getParam(getActivity(), getString(R.string.pref_file),getString(R.string.mainfragment_destination),"");
        if(statusAddress !=null )
            mDestination.setText(statusAddress);
        String statusTime = (String)sharedpreferences.getParam(getActivity(), getString(R.string.pref_file),getString(R.string.mainfragment_time),"");
        if(statusTime !=null )
            mTime.setText(statusTime);
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getActivity(), "onStop", Toast.LENGTH_SHORT).show();
        sharedpreferences.setParam(getActivity(), getString(R.string.pref_file), getString(R.string.mainfragment_initial_flag), "0");
        sharedpreferences.setParam(getActivity(), getString(R.string.pref_file), getString(R.string.mainfragment_time), mTime.getText());
        sharedpreferences.setParam(getActivity(), getString(R.string.pref_file), getString(R.string.mainfragment_destination), mDestination.getText());
    }*/
}
