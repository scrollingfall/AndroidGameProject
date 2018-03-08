package edu.stanford.cs108.bunnyworldplayer;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sam on 3/6/18.
 */

public class EditorActivity extends AppCompatActivity {
    Page firstPage = new Page("page1", 200, 200);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
    }

    public void onAdd(View view) {

    }

    public void onSave(View view) {

    }
}
