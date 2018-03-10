package edu.stanford.cs108.bunnyworldplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> pageList;
    Spinner pageSpinner;
    int pageCounter = 1;
    ArrayAdapter<String> adapter;

    private void openFile(File file){
        //read lines

    }

    /* ---------- Sam's content for testing (should be in EditorActivity.java ------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // #SAM'S CODE FOR NOW

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

        // todo: more stuff here?

    }

    public void onSavePage(View view) {
        // todo: save current page to game's data structure for pages
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

    /* ------------------ Vidushi's Content ------------------------------- */

    public void createGame(View view){
        Intent intent = new Intent(this, EditorActivity.class);
        this.startActivity(intent);
    }

    public void editGame(View view){

        Intent intent = new Intent(this, GameListEdit.class);
        this.startActivity(intent);
    }

    public void playGame(View view){


        Intent intent = new Intent(this, GameListPlay.class);
        this.startActivity(intent);
    }
}
