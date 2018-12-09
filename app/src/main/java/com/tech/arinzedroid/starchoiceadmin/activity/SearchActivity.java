package com.tech.arinzedroid.starchoiceadmin.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("Search Result");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        handleIntent(getIntent());
    }


    @Override
    public void onNewIntent(Intent intent){
        handleIntent(intent);
    }

    private void handleIntent(Intent intent){
        if(intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle("Result for \"" + query + "\"");
            }

            //write code to search your data with the query
            Toast.makeText(this, "query " + query, Toast.LENGTH_SHORT).show();
        }
    }
}
