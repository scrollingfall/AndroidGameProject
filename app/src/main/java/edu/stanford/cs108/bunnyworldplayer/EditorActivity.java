package edu.stanford.cs108.bunnyworldplayer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    static Page currPage;
    static Game newGame;
    ArrayList<String> pageList;
    Spinner pageSpinner;
    int pageCounter;
    int shapeCounter = 0;
    ArrayAdapter<String> adapter;
    EditorView editorview;
    TextView xField;
    TextView yField;
    TextView widthField;
    TextView heightField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editorview = (EditorView) findViewById(R.id.previewArea);

        // instantiate new game
        firstPage = new Page("page1", 200, 200, "game1");
        currPage = firstPage;
        firstPage.setStarter(true, firstPage.getWidth(), firstPage.getHeight());

        // todo: create game-naming system in MainActivity.java
        newGame = new Game(firstPage, "game1", this);
        pageCounter = 1;

        pageList = new ArrayList<>();
        pageList.add("page" + Integer.toString(pageCounter));
        pageSpinner = (Spinner) findViewById(R.id.pageSpinner);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pageList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter);

        xField = (TextView) findViewById(R.id.xField);
        yField = (TextView) findViewById(R.id.yField);
        widthField = (TextView) findViewById(R.id.widthField);
        heightField = (TextView) findViewById(R.id.heightField);
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
        Page newPage = new Page(pageName, 200, 200, "game1");
        newGame.addOrUpdatePage(pageName, newPage);

        Toast toast = Toast.makeText(
                getApplicationContext(),
                "Page Added!",
                Toast.LENGTH_SHORT);
        toast.show();

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

    public void onDeletePage(View view) {
        // todo
    }

    public void onAddShape(View view) {
        shapeCounter += 1;
        String shapeName = "shape" + Integer.toString(shapeCounter);
        float randX = (float) Math.random() * 1000 + 350;
        float randY = (float) Math.random() * 500 + 150;
        Shape newShape = new Shape(this, shapeName, currPage.toString(), randX, randY, 200, 200);
        currPage.addShape(newShape);

        // unselect the previously selected shape if there was one
        Shape selectedShape = currPage.getSelectedShape();
        if (selectedShape != null) selectedShape.setSelected(false);

        currPage.setSelectedShape(newShape);

        EditorView editorview = (EditorView) findViewById(R.id.previewArea);
        editorview.drawPage(currPage);

        xField.setText(Float.toString(newShape.getX()));
        yField.setText(Float.toString(newShape.getY()));
        widthField.setText(Float.toString(newShape.getWidth()));
        heightField.setText(Float.toString(newShape.getHeight()));

        Toast toast = Toast.makeText(
                getApplicationContext(),
                "Shape Added!",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onEditShape(View view) {

        if (currPage.getSelectedShape() == null) {
            Toast.makeText(this, "No shape is currently selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ShapeEditor.class);
        startActivity(intent);
    }

    public void onDeleteShape(View view) {
        Shape selectedShape = currPage.getSelectedShape();
        if (selectedShape != null) {
            selectedShape.setSelected(false);
            currPage.removeShape(selectedShape.getName());
            currPage.setSelectedShape(null);
            EditorView editorview = (EditorView) findViewById(R.id.previewArea);
            editorview.drawPage(currPage);

            xField.setText("--");
            yField.setText("--");
            widthField.setText("--");
            heightField.setText("--");

            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    "Shape Deleted!",
                    Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    "Please select a shape to delete!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
