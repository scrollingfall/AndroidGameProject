/**
 * Created by uzair on 3/10/18.
 */


package edu.stanford.cs108.bunnyworldplayer;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.io.*;
import java.util.*;

public class ShapeEditor extends AppCompatActivity {

    ArrayList<String> allSounds;
    ArrayList<String> pageNames;
    ArrayList<String> allShapes;
    ArrayList<String> allImages;
    String triggerSelected;
    String shapeSelected;
    String actionSelected;
    String resSelected;
    String imageSelected;
    Game currGame;
    ArrayList<String> overallScript;
    boolean DEBUG = true;
    Page currPage;
    Shape selectedShape;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_editor);

        overallScript = new ArrayList<String>();

        currGame = EditorActivity.newGame;
        if (currGame == null) {
            System.out.println("WARNING: newGame object is NULL");
            return;
        }

        HashMap<String, Page> pagesInGame = currGame.getPages();

        // All pages: goto
        pageNames = new ArrayList<>();
        pageNames.addAll(pagesInGame.keySet());

        // All shapes: show/hide
        allShapes = new ArrayList<>();

        for (Page curr : pagesInGame.values()) {
            HashMap<String, Shape> shapesOnPage = curr.getShapes();
            allShapes.addAll(shapesOnPage.keySet());
        }

        // All sounds: play
        allSounds = new ArrayList<>();
        allSounds.addAll(currGame.getMusicResources().keySet());

        allImages = new ArrayList<>();
        allImages.add("");
        allImages.addAll(currGame.getImgResources().keySet());

        currPage = EditorActivity.currPage;
        selectedShape = currPage.getSelectedShape();

        if (selectedShape == null) {
            toastify("Warning: No shape is selected");
            finish();
        }

        imageSelected = "";

        showActions();
        populateImageSpinner(); // Spinner with images to choose from

        populateFields(selectedShape);

        populateTriggerSpinner(); // Triggers
        showShapes(); // Shapes to go with triggers

    }

    private void populateFields (Shape shape) {

        if (shape == null) {
            toastify("No shape was selected");
            finish();
            return;
        }

        String name = shape.getName();
        float x = shape.getX();
        float y = shape.getY();
        float width = shape.getWidth();
        float height = shape.getHeight();
        ArrayList<String> scripts = shape.getScripts();

        if (scripts != null && !scripts.isEmpty()) {
            overallScript = scripts;
            previewScript();
        } else {
            overallScript = new ArrayList<String>();
        }

        if (name != null && !name.isEmpty()) {
            TextView nameField = (TextView) findViewById(R.id.shapeName);
            nameField.setText(name);
        }

        // Setting X
        TextView xField = (TextView) findViewById(R.id.shapeX);
        xField.setText(x + "");

        // Setting Y
        TextView yField = (TextView) findViewById(R.id.shapeY);
        yField.setText(y + "");

        // Setting Width
        TextView widthField = (TextView) findViewById(R.id.shapeWidth);
        widthField.setText(width + "");

        // Setting Height
        TextView heightField = (TextView) findViewById(R.id.shapeHeight);
        heightField.setText(height + "");

        // Setting image, if available
        String imageText = shape.getImage();
        if (imageText != null && !imageText.isEmpty()) {
            setImageText(imageText);
        }

        String text = shape.getText();
        int fontSize = shape.getFontSize();

        if (text != null && !text.isEmpty()) {
            TextView textField = (TextView) findViewById(R.id.shapeText);
            textField.setText(text);
            TextView fontField = (TextView) findViewById(R.id.shapeFont);
            fontField.setText(fontSize + "");
        }

        CheckBox visible = (CheckBox) findViewById(R.id.visible);
        visible.setChecked(!selectedShape.isHidden());

        CheckBox movable = (CheckBox) findViewById(R.id.movable);
        movable.setChecked(selectedShape.isMoveable());

    }

    private void setImageText(String imageText) {
        Spinner images = (Spinner) findViewById(R.id.imageSpinner);

        int index = -1;

        for (int i = 0; i < allImages.size(); i++) {
            if (allImages.get(i).equalsIgnoreCase(imageText)) index = i;
        }

        if (index == -1) return;

        images.setSelection(index);
        imageSelected = allImages.get(index);
    }

    // Populates the first spinner with 'Trigger Commands' like 'on-click' - Static
    private void populateTriggerSpinner() {

        Spinner trigger = (Spinner) findViewById(R.id.trigger);
        trigger.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                triggerSelected = adapter.getItemAtPosition(pos).toString();
                if (triggerSelected.equals("on-drop")) {
                    showShapes();
                } else {
                    hideShapes();
                }
            }

        });

        ArrayAdapter<CharSequence> triggerOptions = ArrayAdapter.createFromResource(this,
                R.array.shape_options, android.R.layout.simple_spinner_item);
        triggerOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trigger.setAdapter(triggerOptions);

    }

    // For debugging purposes
    private void toastify(String str) {
        if (!DEBUG) return;
        System.out.println("Output: " + str);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    // Populates the second spinner with 'Shapes' - Dynamic
    private void showShapes() {
        Spinner shapes = (Spinner) findViewById(R.id.shapeSpinner);
        shapes.setEnabled(true);
        shapes.setClickable(true);
        ArrayAdapter<String> shapesAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                allShapes);
        shapes.setAdapter(shapesAdapter);

        shapes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                shapeSelected = adapter.getItemAtPosition(pos).toString();
            }

        });

    }

    // Hides shapes from second spinner
    private void hideShapes() {
        Spinner shapes = (Spinner) findViewById(R.id.shapeSpinner);
        shapes.setEnabled(false);
        shapes.setClickable(false);
    }


    // Populates the third spinner with 'Actions' - Static
    private void showActions() {
        Spinner actions = (Spinner) findViewById(R.id.actionSpinner);
        ArrayAdapter<CharSequence> actionsOptions = ArrayAdapter.createFromResource(this,
                R.array.action_options, android.R.layout.simple_spinner_item);
        actions.setAdapter(actionsOptions);

        actions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                actionSelected = adapter.getItemAtPosition(pos).toString();
//                toastify(actionSelected);
                if (actionSelected.equals("show") || actionSelected.equals("hide")) {
                    populateFourth("shapes");
                } else if (actionSelected.equals("play")) {
                    populateFourth("sounds");
                } else { // goto
                    populateFourth("pages");
                }
            }

        });
    }

    // Populates the fourth spinner based on the option
    // Can be "sounds", "pages", or "shapes"
    // Case-insensitive
    private void populateFourth(String option) {

        Spinner resources = (Spinner) findViewById(R.id.resourceSpinner);

        option = option.toLowerCase(); // Just to avoid bugs

        if (option.equals("shapes")) {

            ArrayAdapter<String> resourceOptions =
                    new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allShapes);
            resources.setAdapter(resourceOptions);

        } else if (option.equals("sounds")) {
            ArrayAdapter<String> resourceOptions =
                    new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allSounds);
            resources.setAdapter(resourceOptions);

        } else if (option.equals("pages")) {
            ArrayAdapter<String> resourceOptions =
                    new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pageNames);
            resources.setAdapter(resourceOptions);
        } else {
            System.out.println("WARNING: Attempting to set fourth spinner to " + option + ", which is not a valid option.");
        }

        resources.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                resSelected = "";
            }

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                resSelected = adapter.getItemAtPosition(pos).toString();
            }

        });

    }

    private void populateImageSpinner() {
        Spinner imgSpinner = (Spinner) findViewById(R.id.imageSpinner);
        ArrayAdapter<String> images = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allImages);

        imgSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                imageSelected = "";
            }

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                imageSelected = adapter.getItemAtPosition(pos).toString();
//                toastify(imageSelected);
            }

        });

        imgSpinner.setAdapter(images);

    }

    public void addClicked(View view) {
        String toAdd = "";
        String trigger = triggerSelected;
        String shape = shapeSelected;
        String action = actionSelected;
        String res = resSelected;

        if (trigger.isEmpty() || action.isEmpty() || res.isEmpty()) return;

        toAdd += (trigger + " ");

        if (!shape.isEmpty()) {
            toAdd += (shape + " ");
        }

        toAdd += (action + " ");
        toAdd += (res + ";");

        overallScript.add(toAdd);
        previewScript();
    }

    private String listToString(ArrayList<String> arr) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < arr.size(); i++) {
            s.append(arr.get(i));
            if (i != arr.size() - 1) {
                s.append(" ");
            }
        }
        return s.toString();
    }

    public void cancelPressed(View view) {
        finish();
    }

    public void removePrevPressed(View view) {
        if (overallScript.isEmpty()) return;
        overallScript.remove(overallScript.size() - 1);
        previewScript();
    }

    private void previewScript() {
        TextView shapePreview = (TextView) findViewById(R.id.shapePreview);
        shapePreview.setText(listToString(overallScript));
    }

    public void savePressed(View view) {
        String name = ((EditText) findViewById(R.id.shapeName)).getText().toString();
        String x = ((EditText) findViewById(R.id.shapeX)).getText().toString();
        String y = ((EditText) findViewById(R.id.shapeY)).getText().toString();
        String width = ((EditText) findViewById(R.id.shapeWidth)).getText().toString();
        String height = ((EditText) findViewById(R.id.shapeHeight)).getText().toString();


        if (name.isEmpty() || x.isEmpty() || y.isEmpty() || width.isEmpty() || height.isEmpty()) {
            toastify ("Please fill out all fields");
            return;
        }

        String shapeText = ((EditText) findViewById(R.id.shapeText)).getText().toString();
        String fontSize = ((EditText) findViewById(R.id.shapeFont)).getText().toString();

        boolean movable = ((CheckBox) findViewById(R.id.movable)).isChecked();
        boolean visible = ((CheckBox) findViewById(R.id.visible)).isChecked();

        System.out.println("OUTPUT: name = " + name + ", x = " + x + ", y = " + y + ", width = " + width + ", height = " + height);

        float xVal = Float.parseFloat(x);
        if (xVal <= 0) xVal = 1f;

        float yVal = Float.parseFloat(y);
        if (yVal <= 0) yVal = 1f;

        float heightVal = Float.parseFloat(height);
        if (heightVal < 1) heightVal = 1.0f;

        float widthVal = Float.parseFloat(width);
        if (widthVal < 1) widthVal = 1.0f;

        selectedShape.setName(name);
        selectedShape.setX(xVal);
        selectedShape.setY(yVal);

        selectedShape.resize(widthVal, heightVal);
        selectedShape.setScripts(overallScript);

        Spinner imageSpinner = (Spinner) findViewById(R.id.imageSpinner);

//        toastify(imageSpinner.getSelectedItem().toString());

        selectedShape.setImage(imageSpinner.getSelectedItem().toString());

        selectedShape.setMoveable(movable);
        selectedShape.setHidden(!visible);

        if (!shapeText.isEmpty())
            selectedShape.setText(shapeText);

        if (!fontSize.isEmpty())
            selectedShape.setFontSize(Integer.parseInt(fontSize));

        toastify("Changes saved!");
        finish();
        return;
    }

}
