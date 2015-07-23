package gotogether.qihoo.com.myapplication;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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

    ArrayAdapter<String> destinationSuggestion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mDestination.setClickable(true);
        mDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mainFragment", "clickonmDestination");
                Intent addressIt = new Intent(getActivity(), AddressActivity.class);
                startActivityForResult(addressIt,1);
            }
        });
     /*   String[] books = new String[]{
                "aaa",
                "aaaa",
                "ab"
        };
        destinationSuggestion = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, books);
        TextWatcher watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = HttpRequest.getSuggestions(s.toString());
                String[] strs = result.split(",");
                destinationSuggestion.clear();
                for(String addName : strs) {
                    destinationSuggestion.add(addName);
                }
            }
        };
        mDestination.addTextChangedListener(watcher);
        mDestination.setAdapter(destinationSuggestion);*/

        //处理时间选择
       /* final TimePicker mSetTime = (TimePicker)v.findViewById(R.id.goTimePicker);
        Calendar mNowTime = Calendar.getInstance();
        int hours = mNowTime.get(Calendar.HOUR);
        int minutes = mNowTime.get(Calendar.MINUTE);
        mSetTime.setIs24HourView(true);
        mSetTime.setCurrentHour(hours);
        mSetTime.setCurrentMinute(minutes);
        mSetTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int intentHour = hourOfDay;
                int intentMinute = minute;

            }
        });*/

        return v;
    }
}
