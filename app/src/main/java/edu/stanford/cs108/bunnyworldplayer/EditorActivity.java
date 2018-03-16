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
    ArrayList<String> pageNamesList = new ArrayList<>();
    Spinner pageSpinner;
    int pageCounter;
    int shapeCounter;
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
    String currentGameName;
    String currentPageName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        editorview = (EditorView) findViewById(R.id.previewArea);
        databaseinstance = DatabaseInstance.getDBinstance(getApplicationContext());

        // instantiate new game
        currentGameName = databaseinstance.getCurrentGameName();
        gameNameField = (EditText) findViewById(R.id.gameNameField);
        gameNameField.setText(currentGameName);
        newGame = databaseinstance.getGame(currentGameName);
        newGame.setEditorMode(true);

        starterPage = databaseinstance.getPage(databaseinstance.getPageid());

        for(Page page: newGame.getPageList()){
            if (newGame.getStarter().equals(page.getName())) {
                page.setStarter(true, page.getWidth(), page.getHeight());
            }
        }

        starterPage.setStarter(true, starterPage.getWidth(), starterPage.getHeight());
        newGame.setStarter(starterPage.getName());
        currPage = starterPage;

        gameNameField.setText(databaseinstance.getCurrentGameName());


        pageCounter = newGame.getPageList().size();

        for (Page page : newGame.getPageList()) pageNamesList.add(page.getName());

        pageSpinner = (Spinner) findViewById(R.id.pageSpinner);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, pageNamesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter);

        pageNameField = (EditText) findViewById(R.id.pageNameField);
        pageNameField.setText(pageNamesList.get(0));

        shapeNameField = (TextView) findViewById(R.id.shapeNameField);
        xField = (TextView) findViewById(R.id.xField);
        yField = (TextView) findViewById(R.id.yField);
        widthField = (TextView) findViewById(R.id.widthField);
        heightField = (TextView) findViewById(R.id.heightField);



        pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                String pageName = adapter.getItemAtPosition(pos).toString();
                Page toGet = newGame.getPage(pageName);

                if (toGet != null) currPage = toGet;

                Shape selectedShape = currPage.getSelectedShape();
                if (selectedShape != null) selectedShape.setSelected(false);

                EditorView editorview = (EditorView) findViewById(R.id.previewArea);
                editorview.drawPage(currPage);

                pageNameField.setText(pageName);
            }

        });
    }


    public void onSaveGame(View view) {

        // todo: save the pages and shapes in the game.
        String gameName = gameNameField.getText().toString().trim();

        if (databaseinstance.gameExists(gameName) && !gameName.equals(newGame.getName())) {
            giveToast("Game name already exists. Please enter another game name");
        }



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

            databaseinstance.removeGame(currentGameName);
            currentGameName = gameName;
            databaseinstance.setCurrentGameName(currentGameName);
            databaseinstance.addGame(newGame);

            giveToast("Game \"" + gameName + "\" saved");
        } else {
            giveToast("Please name the game before saving");
        }
    }

    public void onDeleteGame(View view) {
        databaseinstance.removeGame(currentGameName);
        giveToast("Game \"" + newGame.getName() + "\" deleted");
        Intent intent = new Intent(EditorActivity.this, GameListEdit.class);
        startActivity(intent);
    }

    public void onAddPage(View view) {
        // add to arraylist, create new spinner
        pageCounter += 1;
        String pageName = "page" + Integer.toString(pageCounter);
        pageNamesList.add(pageName);

        // add new page object to Game's hashmap for pages
        Page newPage = new Page(pageName, 200, 200, newGame.getName());
        newGame.addPage(pageName, newPage);

        giveToast("Page added");

    }

    public void onMakeStarter(View view) {
        String prevStarterId = starterPage.getPageId();

        if (!prevStarterId.equals(currPage.getPageId())) {
            // undo old starter
            HashMap<String, Page> pages = newGame.getPages();
            Page prevStarterPage = pages.get(prevStarterId);
            if (prevStarterPage != null) prevStarterPage.setStarter(false, prevStarterPage.getWidth(), prevStarterPage.getHeight());

            // make currPage new starter
            currPage.setStarter(true, currPage.getWidth(), currPage.getHeight());
            newGame.setStarter(currPage.getPageId());

            starterPage = currPage;

            giveToast("\"" + currPage.getName() + "\" set to starter page");
        } else {
            giveToast("\"" + currPage.getName() + "\" is already the starter page");
        }
    }

    public void onSavePage(View view) {


        String currPageName = pageNameField.getText().toString().trim();

        if (currPageName.length() == 0) {
            giveToast("Please give the page a name");
            return;
        }

        // perform page name error checking
        ArrayList<Page> pageList = newGame.getPageList();
        for (Page p : pageList) {
            if (p.getName().equals(currPageName) && p != currPage) {
                giveToast("Oops! Looks like there's another page with that name...");
            }
        }

        newGame.removePage(currPage.getName(), currPage);

        // update spinner
        int index = pageNamesList.indexOf(currPage.getName());
        pageNamesList.set(index, currPageName);

        // add page with new name
        currPage.setName(currPageName);
        newGame.addPage(currPage.getName(), currPage);

        // update name for Game's starter page if currPage is a starter page
        if (currPage.isStarter()) {
            newGame.setStarter(currPage.getPageId());
        }

        giveToast("Page \"" + currPage.getName() + "\" saved");

    }

    public void onDeletePage(View view) {
//        System.out.println(currPage.isStarter());
//        System.out.println(currPage.getPageId());
//        System.out.println(currPage.getName());
//        System.out.println(starterPage.isStarter());
//        System.out.println(starterPage.getName());
//        System.out.println(starterPage.getPageId());
        if (!currPage.isStarter()) {
            pageNamesList.remove(currPage.getName());//does nothing
            adapter.notifyDataSetChanged();
            // go back to starter page and draw it; remove currPage from Game
            newGame.removePage(currPage.getName(), currPage);
            giveToast("Page \"" + currPage.getName() + "\" deleted");
            currPage = starterPage;

            EditorView editorview = (EditorView) findViewById(R.id.previewArea);
            editorview.drawPage(currPage);

            pageNameField.setText(starterPage.getName());

        } else {
            giveToast("Cannot delete starter page");
        }
    }

    public void onAddShape(View view) {
        shapeCounter = 0;
        for (Page p : newGame.getPageList()) {
            shapeCounter += p.getShapeList().size();
        }
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
