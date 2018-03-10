package edu.stanford.cs108.bunnyworldplayer;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.support.v7.app.AppCompatActivity;

import java.util.*;

/**
 * Created by sam on 3/6/18.
 */

public class EditorActivity extends AppCompatActivity {
    Page firstPage = new Page("page1", 200, 200);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ArrayList<String> pageList = new ArrayList<String>();
        pageList.add("page1"); //test cases
        pageList.add("page2");

        Spinner pageSpinner = (Spinner) findViewById(R.id.pageSpinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(EditorActivity.this,
                android.R.layout.simple_spinner_item, pageList);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(myAdapter);
    }

    public void onAdd(View view) {

    }

    public void onSave(View view) {

    }



}
