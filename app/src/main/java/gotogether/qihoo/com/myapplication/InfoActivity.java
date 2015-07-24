package gotogether.qihoo.com.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;


public class InfoActivity extends ActionBarActivity {

    private  static final String PREF_FILE =  "gotogether.qihoo.com.myapplication.preference_file";
    private  static final String PREF_NAME =  "gotogether.qihoo.com.myapplication.preference_name";


    private SharedPreferencesUtils sharedpreferences = new SharedPreferencesUtils();

    private EditText mUserName;
    private RadioGroup mUserSex;
    private EditText mUserAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_basicinfo);
        mUserName = (EditText)findViewById(R.id.userName);
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sharedpreferences.setParam(InfoActivity.this, PREF_FILE, PREF_NAME,s.toString());
            }
        });

        mUserSex = (RadioGroup)findViewById(R.id.userSex);
        mUserAddress = (EditText)findViewById(R.id.userAddress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
