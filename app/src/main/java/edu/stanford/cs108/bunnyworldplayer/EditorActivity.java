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
    ArrayList<String> pageList;
    Spinner pageSpinner;
    int pageCounter = 1;
    ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        pageList = new ArrayList<>();

        pageList.add("page" + Integer.toString(pageCounter));

        pageSpinner = (Spinner) findViewById(R.id.pageSpinner);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pageList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter);
    }

    public void onAddPage(View view) {
        // add to arraylist, create new spinner
        pageCounter += 1;
        pageList.add("page" + Integer.toString(pageCounter));
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pageList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter);

        Toast toast = Toast.makeText(
                getApplicationContext(),
                "Page Added!",
                Toast.LENGTH_SHORT);
        toast.show();

        // todo: add new page object to Game's hashmap for pages


    }

    public void onSavePage(View view) {
        // todo: update current page in Game's hashmap for pages

        Toast toast = Toast.makeText(
                getApplicationContext(),
                "Page Saved!",
                Toast.LENGTH_SHORT);
        toast.show();

    }

    public void onAddShape(View view) {

        // todo: add shape to page's data structure for shapes

        Toast toast = Toast.makeText(
                getApplicationContext(),
                "Shape Added!",
                Toast.LENGTH_SHORT);
        toast.show();

    }

    public void onEditShape(View view) {
        // todo: for the current shape selected, goes to Uzair's shape editor
    }
}
