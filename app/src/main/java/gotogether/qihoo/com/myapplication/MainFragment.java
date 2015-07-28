package gotogether.qihoo.com.myapplication;

import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
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
    private TextView  mRecentCallInfo;
    private Button mCancelRecentCall;

    private String  mNowTime;
    private String mSettedTime;
    private String mSelfId;

    private String mSelfStatus;

    private final int REQUEST_CODE_ADDRESS = 1;
    private final int REQUEST_USERINFO = 2;
    private final int REQUEST_FOR_YUE = 3;//czc

    private SharedPreferencesUtils sharedpreferences = new SharedPreferencesUtils();

    ArrayAdapter<String> destinationSuggestion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Toast.makeText(getActivity(), "onCreate ", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyy-MM-dd HH:mm:ss");
        Date    curDate    =   new    Date(System.currentTimeMillis());//
        mNowTime    =    formatter.format(curDate);

        mSelfStatus = "0";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        //Toast.makeText(getActivity(), "REQUEST_CODE"+requestCode, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity(), "RESULT_CODE"+resultCode, Toast.LENGTH_SHORT).show();
        Log.e("resultcode", String.valueOf(resultCode));
       if (requestCode == REQUEST_CODE_ADDRESS) {
           switch (resultCode) {
               case 2:
                   mDestination.setText("");
                   mDestination.setText(data.getStringExtra("destination"));
                   break;

               default:
                   break;
           }
       }
        else if (requestCode == REQUEST_FOR_YUE) {
            switch (resultCode) {
                case 3:
                    Log.e("-2", data.getStringExtra("selfstatus"));
                    mSelfStatus = data.getStringExtra("selfstatus");
                    //String lastestyue = data.getStringExtra("lastestyue");
                    //Log.e("lastestyue", lastestyue);
                    mRecentCallInfo.setText(data.getStringExtra("latestyue"));
                    if(data.getStringExtra("latestyue") == null) Log.e("latestyue", "null");
                    if(data.getStringExtra("latestyue") != null && data.getStringExtra("latestyue").startsWith("8654")) mRecentCallInfo.setText(R.string.chenxiansheng);
                    if(data.getStringExtra("latestyue") != null && data.getStringExtra("latestyue").startsWith("8637")) mRecentCallInfo.setText(R.string.hunvshi);
                    if(data.getStringExtra("latestyue") != null && data.getStringExtra("latestyue").startsWith("3521")) mRecentCallInfo.setText(R.string.linxianshang);
                    break;
                default:
                    break;
            }
//            Toast.makeText(getActivity(), "REQUEST_USERINFO", Toast.LENGTH_SHORT).show();
//            String defaultAddress = (String)sharedpreferences.getParam(getActivity(), getString(R.string.pref_file),getString(R.string.pref_user_address),"");
//             if(defaultAddress !=null )
//                    mDestination.setText(defaultAddress);
         }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        sharedpreferences.setParam(getActivity(), getString(R.string.pref_file), getString(R.string.mainfragment_initial_flag), "1");
        //Toast.makeText(getActivity(), "onCreateView ", Toast.LENGTH_SHORT).show();
        View v = inflater.inflate(R.layout.fragment_main, parent, false);

        startFlick(v);

        //todo ���������id
        //UUID uuid = UUID.randomUUID();
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        mSelfId = tm.getDeviceId();
        mAppointment = (Button)v.findViewById(R.id.appointment);
        mAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mainFragment", "clickonYue");
                // Intent dAndT = getActivity().getIntent();
                //  if(dAndT.getStringExtra("destination") == null || mTime.getText().toString() == "") {
                if( mTime.getText() == null || mDestination.getText() == null){
                    //Toast.makeText(getActivity(), "please input destination and time", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent i = new Intent(getActivity(), ChoosePersonActivity.class);
                    i.putExtra("id", mSelfId);
                    i.putExtra("destination", mDestination.getText().toString());

                    final SimpleDateFormat formmater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try{
                        date = formmater.parse(mTime.getText().toString());
                    } catch(ParseException e) {
                        e.printStackTrace();
                    }
                    final SimpleDateFormat formmaterToActivity = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    mSettedTime = formmaterToActivity.format(date);
                    Log.e("time", formmaterToActivity.format(date));
                    i.putExtra("time", mSettedTime);
                    i.putExtra("selfstatus", mSelfStatus);
                    //????????????????
                    //�ύƥ����Ϣ
                    new postMatchInfo().execute();

                    startActivityForResult(i, REQUEST_FOR_YUE);
                }
                //Toast.makeText(getActivity(), "mainFragment action3.clickonYue", Toast.LENGTH_SHORT).show();
            }
        });

        //
        mDestination = (TextView)v.findViewById(R.id.Destination);
        mDestination.setClickable(false);//
        mDestination.setKeyListener(null);//
        mDestination.setText(R.string.destination);
       String defaultAddress = (String)sharedpreferences.getParam(getActivity(), getString(R.string.pref_file),getString(R.string.pref_user_address),"");
       if(defaultAddress !=null && mDestination.getText().toString()==getString(R.string.destination))  mDestination.setText(defaultAddress);
        mDestination.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Log.d("Test", "This is onTouch,return false");
                //Toast.makeText(getActivity(), "mainFragment action3.touch mDestination", Toast.LENGTH_SHORT).show();
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
                final SimpleDateFormat formmater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

        mRecentCallInfo = (TextView)v.findViewById(R.id.RecentlyCallInfo);
        mRecentCallInfo.setText(R.string.lastestyuedefault);

        mCancelRecentCall = (Button)v.findViewById(R.id.Cancel);
        mCancelRecentCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecentCallInfo.getText().toString().equals(R.string.lastestyuedefault)) {
                    //Toast.makeText(getActivity(), "ni hai mei yue cheng gong  !", Toast.LENGTH_SHORT).show();
                } else {
                    //ȡ���ϴ�Լ��
                    new cancelLastYue().executeOnExecutor(ChoosePersonActivity.FULL_TASK_EXECUTOR);

                    mRecentCallInfo.setText(R.string.lastestyuedefault);
                }
            }
        });
        return v;
    }

    private class postMatchInfo extends AsyncTask<Void, Void, Boolean> {
        //????????????????
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean result = true;
            String getHttp = "";
            try {
                getHttp = HttpRequest.postMatchInfo(mSelfId, mSettedTime, mDestination.getText().toString(), null, null, null, null, null, null);
            } catch (java.lang.NullPointerException e){
                Log.e("fetchMatchedList", "null pointer");
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean p) {
            Log.e("postMatchInfo", "user init ok");
        }
    }

    private class cancelLastYue extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean result = true;
            String getHttp = "";
            try {
                getHttp = HttpRequest.cancelLatestMatch(mSelfId);
                if(getHttp.equals("")) {
                    result = false;
                }
            } catch (java.lang.NullPointerException e){
                Log.e("fetchMatchedList", "null pointer");
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean p) {
            if(p) {
                //ȡ���ɹ�
                //Toast.makeText(getActivity(), "qu xiao cheng gong", Toast.LENGTH_SHORT).show();
            } else {
                //ȡ��ʧ��
                //Toast.makeText(getActivity(), "qu xiao shi bai", Toast.LENGTH_SHORT).show();
            }
            //do nothing
        }
    }

    private void startFlick(View view) {
        if(null == view) {
            return;
        }
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(300);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation( alphaAnimation );
    }

    private void stopFlick(View view) {
        if(null == view) {
            return;
        }
        view.clearAnimation();
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
