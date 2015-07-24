package gotogether.qihoo.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class AddressActivity extends Activity {

    private EditText mDestinationInput;
    private ListView mDestinationSuggestion;
    private String mDestinationUserInput;
    ArrayList<String> mSugData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        mDestinationInput = (EditText)findViewById(R.id.DestinationInputText);
        mDestinationSuggestion = (ListView)this.findViewById(R.id.DestinationSuggestion);

        mDestinationInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mDestinationUserInput = s.toString();
                new FetchSuggestions().execute();
            }
        });

        mDestinationSuggestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("destination", mSugData.get(position));
                setResult(RESULT_OK, data);
                finish();
                //finish();
            }
        });
        //setupListViewAdapter();

    }

    void setupListViewAdapter() {
        if(mSugData != null) {
            mDestinationSuggestion.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, mSugData));
        }
        else {
            mDestinationSuggestion.setAdapter(null);
        }
    }

    private class FetchSuggestions extends AsyncTask<Void, Void, ArrayList<String> > {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> result = new ArrayList<String>();
            try {
                String getHttp = HttpRequest.getSuggestions(mDestinationUserInput);
                if(getHttp == null) return result;
                String[] relList = getHttp.split(",");
                if(relList.length == 0) return result;
                for(int i = 0; i < relList.length; ++i) {
                    result.add(relList[i]);
                }
            } catch (java.lang.NullPointerException e){
                Log.e("fetchSuggestions", "null pointer");
            }
            return result;
        }
        @Override
        protected void onPostExecute(ArrayList<String> sugList) {
            mSugData = sugList;
            setupListViewAdapter();
        }
    }
}