package gotogether.qihoo.com.myapplication;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


public class AddressActivity extends Activity {

    private AutoCompleteTextView mDestinationInput;
    ArrayAdapter<String> destinationSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        mDestinationInput = (AutoCompleteTextView)findViewById(R.id.DestinationInput);
        String[] books = new String[]{
                "aaa",
                "aaaa",
                "ab"
        };
        destinationSuggestion = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, books);
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
        mDestinationInput.addTextChangedListener(watcher);
        mDestinationInput.setAdapter(destinationSuggestion);
    }
}