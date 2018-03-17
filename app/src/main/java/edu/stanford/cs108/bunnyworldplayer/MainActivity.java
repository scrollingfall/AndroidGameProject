package edu.stanford.cs108.bunnyworldplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private int gameCounter = 0;
    private static String currGameName;
    DatabaseInstance databaseinstance;
    private Game bunnyworldgame;

    private void openFile (File file) {
        //read lines

    }

    /* ---------- Sam's content for testing (should be in EditorActivity.java ------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseinstance = DatabaseInstance.getDBinstance(getApplicationContext());



        if (!databaseinstance.gameExists("Bunnyworld Default Game")){
            createBunnyWorldGame();
            databaseinstance.addGame(bunnyworldgame);
        }
        else{
            databaseinstance.removeGame("Bunnyworld Default Game");
        }



    }

    private void createBunnyWorldGame(){
        bunnyworldgame = new Game("Bunnyworld Default Game", getApplicationContext());

        // starter page
        Page starterPage = new Page("starterPage", 100, 100,"Bunnyworld Default Game" );

        Shape starterShape1 = new Shape(getApplicationContext(), "starterShape1", "starterPage", 900, 200, 200, 10);
        starterShape1.setText("Bunny World!");
        starterShape1.setFontSize(60);
        starterShape1.updateTextBounds();
        starterPage.addShape(starterShape1);

        Shape starterShape2 = new Shape(getApplicationContext(), "starterShape2", "starterPage", 700, 300, 1400, 10);
        starterShape2.setText("You are in a maze of twisty little passages, all alike.");
        starterShape2.setFontSize(60);
        starterShape2.updateTextBounds();
        starterPage.addShape(starterShape2);

        Shape visibleDoor = new Shape(getApplicationContext(), "visibleDoor", "starterPage", 600, 470, 150, 90);
        visibleDoor.setScriptList("on-click goto mysticRoom;");
        visibleDoor.setHidden(false);
        starterPage.addShape(visibleDoor);


        Shape hiddenDoor1 = new Shape(getApplicationContext(), "hiddenDoor1", "starterPage", 850, 470, 150, 90);
        hiddenDoor1.setScriptList("on-click goto fireRoom;");
        hiddenDoor1.setHidden(true);
        starterPage.addShape(hiddenDoor1);


        Shape hiddenDoor2 = new Shape(getApplicationContext(), "hiddenDoor2", "starterPage", 1050, 470, 150, 90);
        hiddenDoor2.setScriptList("on-click goto deathRoom;");
        hiddenDoor2.setHidden(false);
        starterPage.addShape(hiddenDoor2);

        bunnyworldgame.setStarter(starterPage.getName());
        bunnyworldgame.addPage(starterPage.getName(), starterPage);


        //mystic room page
        Page mysticRoom = new Page("mysticRoom", 100, 100,"Bunnyworld Default Game" );

        Shape bunnyShape = new Shape(getApplicationContext(), "bunnyShape", "mysticRoom", 1000, 100, 400, 400);
        bunnyShape.setScriptList("on-click hide carrot1;on-click play munching;on-enter show hiddenDoor1;");
        bunnyShape.setImage("mystic");
        mysticRoom.addShape(bunnyShape);

        Shape door = new Shape(getApplicationContext(), "door", "mysticRoom", 800, 250, 100, 150);
        door.setScriptList("on-click goto starterPage;");
        mysticRoom.addShape(door);

        Shape bunnyText = new Shape(getApplicationContext(), "bunnyText", "mysticRoom", 700, 700, 350, 350);
        bunnyText.setText("Mystic Bunny - Rub my tummy, for a big surprise!");
        bunnyText.setFontSize(40);
        bunnyText.updateTextBounds();
        mysticRoom.addShape(bunnyText);

        bunnyworldgame.addPage(mysticRoom.getName(), mysticRoom);


        // fire room page
        Page fireRoom = new Page("fireRoom", 100, 100,"Bunnyworld Default Game" );

        Shape fire = new Shape(getApplicationContext(), "fire", "fireRoom", 300, 250, 500, 350);
        fire.setScriptList("on-enter play fire;");
        fire.setImage("fire");
        fireRoom.addShape(fire);

        Shape door2 = new Shape(getApplicationContext(), "door2", "fireRoom", 800, 250, 100, 150);
        door2.setScriptList("on-click goto starterPage;");
        fireRoom.addShape(door2);

        Shape shapeFireRoom = new Shape(getApplicationContext(), "shapeFireRoom", "fireRoom", 0, 0, 100, 100);
        shapeFireRoom.setScriptList("on click goto mysticRoom;");
        fireRoom.addShape(shapeFireRoom);

        Shape carrot1 = new Shape(getApplicationContext(), "carrot1", "fireRoom", 100, 100, 200, 200);
        carrot1.setMoveable(true);
        carrot1.setImage("carrot");
        fireRoom.addShape(carrot1);

        Shape fireText = new Shape(getApplicationContext(), "fireText", "fireRoom", 900, 900, 200, 200);
        fireText.setText("Eek! Fire-Room. Run Away!");
        fireText.setFontSize(40);
        fireText.updateTextBounds();
        fireRoom.addShape(fireText);

        bunnyworldgame.addPage(fireRoom.getName(), fireRoom);

        // death room
        Page deathRoom = new Page("deathRoom", 100, 100,"Bunnyworld Default Game" );

        Shape deathBunny = new Shape(getApplicationContext(), "deathBunny", "deathRoom", 500, 200, 500, 350);
        deathBunny.setScriptList("on-enter play evillaugh;on-drop carrot1 hide carrot1;on-drop carrot1 play munching;on-drop carrot1 hide deathBunny;on-drop carrot1 show door3;on-click play evillaugh;");
        deathBunny.setImage("death");
        deathRoom.addShape(deathBunny);

        Shape door3 = new Shape(getApplicationContext(), "door3", "deathRoom", 500, 200, 100, 150);
        door3.setHidden(true);
        door3.setScriptList("on-click goto winningRoom;");
        deathRoom.addShape(door3);

        Shape deathRoomText = new Shape(getApplicationContext(), "deathRoomText", "deathRoom", 900, 900, 200, 200);
        deathRoomText.setText("You must appease the bunny of death!");
        deathRoomText.setFontSize(40);
        deathRoomText.updateTextBounds();
        deathRoom.addShape(deathRoomText);

        bunnyworldgame.addPage(deathRoom.getName(), deathRoom);

        // winning room
        Page winningRoom = new Page("winningRoom", 100, 100,"Bunnyworld Default Game" );

        Shape carrot2 = new Shape(getApplicationContext(), "carrot2", "winningRoom", 200, 200, 150, 150);
        carrot2.setImage("carrot");
        winningRoom.addShape(carrot2);

        Shape carrot3 = new Shape(getApplicationContext(), "carrot3", "winningRoom", 500, 300, 150, 150);
        carrot3.setImage("carrot");
        winningRoom.addShape(carrot3);

        Shape carrot4 = new Shape(getApplicationContext(), "carrot4", "winningRoom", 800, 300, 150, 150);
        carrot4.setImage("carrot");
        winningRoom.addShape(carrot4);

        Shape winningText = new Shape(getApplicationContext(), "winningText", "winningRoom", 900, 900, 200, 200);
        winningText.setText("You Win! Yay!");
        winningText.setFontSize(40);
        winningText.updateTextBounds();
        winningText.setScriptList("on-enter play hooray;");
        winningRoom.addShape(winningText);

        bunnyworldgame.addPage(winningRoom.getName(), winningRoom);

    }

    public void createGame(View view){
        gameCounter = databaseinstance.getTotalGameCount()  +1 ;
        currGameName = "game" + gameCounter;

        while (databaseinstance.gameExists(currGameName)){
            gameCounter ++;
            currGameName = "game" + gameCounter;
        }


        Page page = new Page ("page1", 100, 100, currGameName);
        page.setStarter(true, page.getWidth(), page.getHeight());
        databaseinstance.setPageid(page.getPageId());

        Game newGame = new Game(currGameName, page, this);
        page.setGame(newGame);
        newGame.addPage("page1", page);
        newGame.setStarter(page.getPageId());
        databaseinstance.addGame(newGame);
        databaseinstance.setCurrentGameName(currGameName);

        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);
    }

    public void editGame(View view){
        Intent intent = new Intent(MainActivity.this, GameListEdit.class);
        startActivity(intent);
    }

    public void playGame(View view){
        Intent intent = new Intent(MainActivity.this, GameListPlay.class);
        startActivity(intent);
    }

    public static String getCurrGameName() {
        return currGameName;
    }
}
