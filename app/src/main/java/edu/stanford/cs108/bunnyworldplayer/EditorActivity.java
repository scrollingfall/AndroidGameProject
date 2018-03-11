package edu.stanford.cs108.bunnyworldplayer;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.support.v7.app.AppCompatActivity;

import java.util.*;

/**
 * Created by sam on 3/6/18.
 */

public class EditorActivity extends AppCompatActivity {
    Page firstPage;
    Page currPage;
    public Game newGame;
    ArrayList<String> pageList;
    Spinner pageSpinner;
    int pageCounter;
    int shapeCounter = 0;
    ArrayAdapter<String> adapter;
    EditorView editorview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editorview  = (EditorView) findViewById(R.id.previewArea);
        editorView.addShape(shape);

        // instantiate new game
        firstPage = new Page("page1", 200, 200);
        currPage = firstPage;
        newGame = new Game(firstPage, "game1", this);
        pageCounter = 1;

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
        String pageName = "page" + Integer.toString(pageCounter);
        pageList.add(pageName);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pageList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter);

        // add new page object to Game's hashmap for pages
        Page newPage = new Page(pageName, 200, 200);
        newGame.addOrUpdatePage(pageName, newPage);

        Toast toast = Toast.makeText(
                getApplicationContext(),
                "Page Added!",
                Toast.LENGTH_SHORT);
        toast.show();

        // testing
        System.out.println(newGame.getPages().keySet().toString());
    }

    public void onSavePage(View view) {
        // update current page in Game's hashmap for pages
        newGame.addOrUpdatePage(currPage.getName(), currPage);

        Toast toast = Toast.makeText(
                getApplicationContext(),
                "Page Saved!",
                Toast.LENGTH_SHORT);
        toast.show();

    }

    public void onAddShape(View view) {
        // todo: add shape to page's data structure for shapes

        String shapeName = "shape" + Integer.toString(shapeCounter);
        Shape newShape = new Shape(this, shapeName, currPage.toString(), 0, 0, 200, 200);

        //newShape.draw();
        // need to use invalidate somehow
        EditorView editorview = (EditorView) findViewById(R.id.previewArea);
        editorview.drawShape(currPage);


        currPage.addShape(newShape);

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
