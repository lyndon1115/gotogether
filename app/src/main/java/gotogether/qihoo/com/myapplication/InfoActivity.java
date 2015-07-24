package gotogether.qihoo.com.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class InfoActivity extends ActionBarActivity {

    private SharedPreferencesUtils sharedpreferences = new SharedPreferencesUtils();

    private EditText mUserName;
    private RadioGroup mUserSex;
    private EditText mUserAddress;
    private Button mSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_basicinfo);
        mUserName = (EditText)findViewById(R.id.userName);
        //restore if username existed
        String userName = (String)sharedpreferences.getParam(InfoActivity.this, getString(R.string.pref_file),getString(R.string.pref_user_name),"");
       if(userName != null) mUserName.setText(userName);
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //save the user name locally, added by lin
                sharedpreferences.setParam(InfoActivity.this,getString(R.string.pref_file), getString(R.string.pref_user_name) ,s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
                //save the user name locally, added by lin
               // sharedpreferences.setParam(InfoActivity.this,getString(R.string.pref_file), getString(R.string.pref_user_name) ,s.toString());
            }
        });

        mUserSex = (RadioGroup)findViewById(R.id.userSex);
        mUserSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                RadioButton radioButtontemp =  (RadioButton) InfoActivity.this.findViewById(radioButtonID);
                if (radioButtonID == R.id.male)
                    sharedpreferences.setParam(InfoActivity.this, getString(R.string.pref_file), getString(R.string.pref_user_sex), "male");
                else if (radioButtonID == R.id.female)
                    sharedpreferences.setParam(InfoActivity.this, getString(R.string.pref_file), getString(R.string.pref_user_sex), "female");

            }
        });
        mUserAddress = (EditText)findViewById(R.id.userAddress);
        mUserAddress.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Intent addressIt = new Intent(InfoActivity.this, AddressActivity.class);
                startActivityForResult(addressIt, 1);
                return false;
            }
        });
     /* mSubmit.findViewById(R.id.button_changeinfo);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sharedpreferences.setParam(InfoActivity.this, getString(R.string.pref_file), getString(R.string.pref_user_address), mUserAddress.getText());
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
