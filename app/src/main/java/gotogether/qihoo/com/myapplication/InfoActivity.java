package gotogether.qihoo.com.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBar;
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
import android.widget.TextView;
import android.widget.Toast;


public class InfoActivity extends ActionBarActivity {

    private SharedPreferencesUtils sharedpreferences = new SharedPreferencesUtils();

    private EditText mUserName;
    private RadioGroup mUserSex;
    private TextView mUserAddress;
    private Button mSubmit;
    private Button mBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreActionBar();
        setContentView(R.layout.activity_info);
        mUserName = (EditText) findViewById(R.id.userName);
        //restore if username existed
        String userName = (String) sharedpreferences.getParam(InfoActivity.this, getString(R.string.pref_file), getString(R.string.pref_user_name), "");
        if (userName != null) mUserName.setText(userName);
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //save the user name locally, added by lin
                mSubmit.setVisibility(View.VISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {
                //save the user name locally, added by lin
                // sharedpreferences.setParam(InfoActivity.this,getString(R.string.pref_file), getString(R.string.pref_user_name) ,s.toString());
            }
        });

        mUserSex = (RadioGroup) findViewById(R.id.userSex);
        String userSex = (String) sharedpreferences.getParam(InfoActivity.this, getString(R.string.pref_file), getString(R.string.pref_user_sex), "");
        //female, or male, default male
        if (userSex.equals("female")) mUserSex.check(R.id.female);
        else
            mUserSex.check(R.id.male);
        mUserSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mSubmit.setVisibility(View.VISIBLE);
            }
        });


        mUserAddress = (TextView) findViewById(R.id.userAddress);
        String userAddress = (String) sharedpreferences.getParam(InfoActivity.this, getString(R.string.pref_file), getString(R.string.pref_user_address), "");
        if (userAddress != null)
            mUserAddress.setText(userAddress);

        mUserAddress.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                mSubmit.setVisibility(View.VISIBLE);
                Intent addressIt = new Intent(InfoActivity.this, AddressActivity.class);
                startActivityForResult(addressIt, 1);
                return false;
            }
        });

        mSubmit = (Button) findViewById(R.id.button_save_info);
        mSubmit.setVisibility(View.INVISIBLE);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //保存性别
                int radioButtonID = mUserSex.getCheckedRadioButtonId();
                RadioButton radioButtontemp = (RadioButton) InfoActivity.this.findViewById(radioButtonID);
                if (radioButtonID == R.id.male)
                    sharedpreferences.setParam(InfoActivity.this, getString(R.string.pref_file), getString(R.string.pref_user_sex), "male");
                else if (radioButtonID == R.id.female)
                    sharedpreferences.setParam(InfoActivity.this, getString(R.string.pref_file), getString(R.string.pref_user_sex), "female");
                //保存地址
                sharedpreferences.setParam(InfoActivity.this, getString(R.string.pref_file), getString(R.string.pref_user_address), mUserAddress.getText().toString());
                //保存姓名
                sharedpreferences.setParam(InfoActivity.this, getString(R.string.pref_file), getString(R.string.pref_user_name), mUserName.getText().toString());

                Toast.makeText(InfoActivity.this, "User info saved!", Toast.LENGTH_SHORT).show();
            }
        });

        mBack = (Button) findViewById(R.id.imageView_back1);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case -1:
                mUserAddress.setText("");
                mUserAddress.setText(data.getStringExtra("destination"));
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.menu_info, menu);
        restoreActionBar();
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
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.hide();
        actionBar.setDisplayShowTitleEnabled(false);
    }
}
