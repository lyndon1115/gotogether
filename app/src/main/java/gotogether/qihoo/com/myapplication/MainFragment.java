package gotogether.qihoo.com.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by czc on 2015/7/22.
 */
public class MainFragment extends Fragment{
    private AutoCompleteTextView mDestination;

    private Button mAppointment;

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

            }
        });
        return v;
    }
}
