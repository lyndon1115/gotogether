package gotogether.qihoo.com.myapplication;

import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by czc on 2015/7/22.
 */
public class MainFragment extends Fragment{
    private Button mAppointment;
    private  EditText  mDestination;
    private  EditText  mTime;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, parent, false);

        mAppointment = (Button)v.findViewById(R.id.appointment);
        mAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mainFragment", "clickonYue");
                Toast.makeText(getActivity(), "mainFragment action3.clickonYue", Toast.LENGTH_SHORT).show();
            }
        });

        //处理输入地址，智能提示
        mDestination = (EditText)v.findViewById(R.id.Destination);
       mDestination.setClickable(false);//防止点击同时激活onTouch和onClick
        mDestination.setKeyListener(null);//禁止键盘弹出

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


        //获取时间
        mTime = (EditText)v.findViewById(R.id.goTime);
        mTime.setClickable(false);
        mTime.setKeyListener(null);


        mTime.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                Log.d("Test", "This is onTouch,return false");
                Toast.makeText(getActivity(), "mainFragment action3.touch mTime", Toast.LENGTH_SHORT).show();
              //  Intent addressIt = new Intent(getActivity(), AddressActivity.class);
               // startActivityForResult(addressIt, REQUEST_CODE_TIME);
                Dialog dialogTime = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mTime.setFocusable(false);
                                mTime.setText("");
                                mTime.setText(  hourOfDay + ":" + minute);
                            }
                        }, 20, 00, true);//默认时间的小时，分钟和24小时制
                dialogTime.show();
                return false;
            }
        });




        return v;
    }
}
