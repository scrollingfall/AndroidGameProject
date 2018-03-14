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
    Page starterPage;
    static Page currPage;
    static Game newGame;
    ArrayList<String> pageNamesList;
    Spinner pageSpinner;
    int pageCounter;
    int shapeCounter = 0;
    ArrayAdapter<String> adapter;
    EditText gameNameField;
    EditText pageNameField;
    EditorView editorview;
    TextView shapeNameField;
    TextView xField;
    TextView yField;
    TextView widthField;
    TextView heightField;
    DatabaseInstance databaseinstance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editorview = (EditorView) findViewById(R.id.previewArea);
        databaseinstance = DatabaseInstance.getDBinstance(getApplicationContext());

        // instantiate new game
        gameNameField = (EditText) findViewById(R.id.gameNameField);

//        System.out.println("page id is of getPageId of db is " + databaseinstance.getPageid());

        starterPage = databaseinstance.getPage(databaseinstance.getPageid());
//        System.out.println("starter page page id is: " + starterPage.getPageId());

        currPage = starterPage;
//        System.out.println("current page page id is: " + currPage.getPageId());
        starterPage.setStarter(true, starterPage.getWidth(), starterPage.getHeight());

        newGame = databaseinstance.getGame(databaseinstance.getCurrentGameName());
<<<<<<< HEAD
        System.out.println("new game name is: " + newGame.getName());
=======
        newGame.setEditorMode(true);
//        System.out.println("new game name is: " + newGame.getName());
>>>>>>> e4db194557530cba9242ed0c07ecdfce0456c007


        pageCounter = 1;

        pageNamesList = new ArrayList<>();
        pageNamesList.add("page" + Integer.toString(pageCounter));
        pageSpinner = (Spinner) findViewById(R.id.pageSpinner);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pageNamesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter);


        pageNameField = (EditText) findViewById(R.id.pageNameField);
        shapeNameField = (TextView) findViewById(R.id.shapeNameField);
        xField = (TextView) findViewById(R.id.xField);
        yField = (TextView) findViewById(R.id.yField);
        widthField = (TextView) findViewById(R.id.widthField);
        heightField = (TextView) findViewById(R.id.heightField);
    }

    public void onSaveGame(View view) {

        // todo: save the pages and shapes in the game.
        String gameName = gameNameField.getText().toString().trim();

        if (databaseinstance.gameExists(gameName)) giveToast("Game name already exists. Please enter another game name");



        else if (gameName.length() > 0) {
            if (!gameName.equals(newGame.getName())) {
                newGame.setName(gameName);
                // update hashmap
                for (Page p : newGame.getPages().values()) {
                    p.setOwner(gameName);
                }
                // update arraylist
                for (Page p : newGame.getPageList()) {
                    p.setOwner(gameName);
                }
            }

            databaseinstance.addGame(newGame);

            giveToast("Game \"" + gameName + "\" saved");
        } else {
            giveToast("Please name the game before saving");
        }
    }

    public void onDeleteGame(View view) {
        // todo: delete game from db

        giveToast("Game \"" + newGame.getName() + "\" deleted");
        Intent intent = new Intent(EditorActivity.this, GameListEdit.class);
        startActivity(intent);
    }

    public void onAddPage(View view) {
        // add to arraylist, create new spinner
        pageCounter += 1;
        String pageName = "page" + Integer.toString(pageCounter);
        pageNamesList.add(pageName);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pageNamesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter);

        // add new page object to Game's hashmap for pages
        Page newPage = new Page(pageName, 200, 200, newGame.getName());
        newGame.addPage(pageName, newPage);

        giveToast("Page added");

    }

    public void onMakeStarter(View view) {
        String prevStarterName = newGame.getStarter();

        if (prevStarterName != currPage.getName()) {
            // undo old starter
            HashMap<String, Page> pages = newGame.getPages();
            Page prevStarterPage = pages.get(prevStarterName);
            prevStarterPage.setStarter(false, prevStarterPage.getWidth(), prevStarterPage.getHeight());

            // make currPage new starter
            currPage.setStarter(true, currPage.getWidth(), currPage.getHeight());
            newGame.setStarter(currPage.getName());

            starterPage = currPage;

            giveToast("\"" + currPage.getName() + "\" set to starter page");
        } else {
            giveToast("\"" + currPage.getName() + "\" is already the starter page");
        }
    }

    public void onSavePage(View view) {
        // remove page with old name
        newGame.removePage(currPage.getName(), currPage);

        // update spinner
        String currPageName = pageNameField.getText().toString().trim();
        int index = pageNamesList.indexOf(currPage.getName());
        System.out.println("Index: " + index);
        System.out.println("currPage name: " + currPage.getName());
        pageNamesList.set(index, currPageName);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pageNamesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter);

        // add page with new name
        currPage.setName(currPageName);
        newGame.addPage(currPage.getName(), currPage);

        // update name for starter page
        newGame.setStarter(currPageName);

        giveToast("Page \"" + currPage.getName() + "\" saved");
    }

    public void onDeletePage(View view) {
        if (!currPage.isStarter()) {
            // go back to starter page and draw it; remove currPage from Game
            newGame.removePage(currPage.getName(), currPage);
            giveToast("Page \"" + currPage.getName() + "\" deleted");
            currPage = starterPage;

            EditorView editorview = (EditorView) findViewById(R.id.previewArea);
            editorview.drawPage(currPage);

        } else {
            giveToast("Cannot delete starter page");
        }
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

        shapeNameField.setText(newShape.getName());
        xField.setText(Float.toString(newShape.getX()));
        yField.setText(Float.toString(newShape.getY()));
        widthField.setText(Float.toString(newShape.getWidth()));
        heightField.setText(Float.toString(newShape.getHeight()));

        giveToast("Shape added");
    }

    public void onEditShape(View view) {
        if (currPage.getSelectedShape() != null) {
            Intent intent = new Intent(this, ShapeEditor.class);
            startActivity(intent);
        } else {
            giveToast("Please select a shape to edit");
        }
    }

    public void onDeleteShape(View view) {
        Shape selectedShape = currPage.getSelectedShape();
        if (selectedShape != null) {
            selectedShape.setSelected(false);
            currPage.removeShape(selectedShape.getName());
            currPage.setSelectedShape(null);
            EditorView editorview = (EditorView) findViewById(R.id.previewArea);
            editorview.drawPage(currPage);

            shapeNameField.setText("--");
            xField.setText("--");
            yField.setText("--");
            widthField.setText("--");
            heightField.setText("--");

            giveToast("Shape deleted");
        } else {
            giveToast("Please select a shape to delete");
        }
    }

    private void giveToast(String msg) {
        Toast toast = Toast.makeText(
                getApplicationContext(),
                msg,
                Toast.LENGTH_SHORT);
        toast.show();
    }

}
