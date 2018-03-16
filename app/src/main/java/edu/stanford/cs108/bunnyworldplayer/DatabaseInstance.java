package edu.stanford.cs108.bunnyworldplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vsinghi on 3/7/18.
 */

public class DatabaseInstance {

    private SQLiteDatabase database;
    private String database_name = "gameDatabase.db";
    private String shape_table_name = "Shapes";
    private String page_table_name = "Pages";
    private String game_table_name = "Games";
    private Context context;
    private int pageNumber;
    public String pageID;
    private String gameName = "";
    private static DatabaseInstance databaseInstance;
    private ArrayList<String> nameOfImages;

    private DatabaseInstance(Context context){
        pageNumber = 1;
        this.context = context;
        database = context.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS "+shape_table_name+" (id TEXT PRIMARY KEY NOT NULL, name TEXT NOT NULL, x REAL, y REAL, text TEXT, image TEXT, movable BOOLEAN, visible BOOLEAN, actionScript TEXT, fontSize INTEGER, height REAL, width REAL);");
        database.execSQL("CREATE TABLE IF NOT EXISTS "+page_table_name+" (id TEXT, name TEXT NOT NULL, shapes TEXT);");
        database.execSQL("CREATE TABLE IF NOT EXISTS "+game_table_name+"(gameName TEXT NOT NULL, pages TEXT, starterPage TEXT);");
        nameOfImages = new ArrayList<String>(Arrays.asList("carrot","carrot2","death","duck","fire","mystic"));

    }

    public static DatabaseInstance getDBinstance(Context context) {
        if (databaseInstance == null){
            databaseInstance = new DatabaseInstance(context);
        }
        return databaseInstance;
    }

    public void addShape(Shape shape) {
        ContentValues vals = new ContentValues();
        vals.put("id", shape.getShapeId());
        vals.put("name", shape.getName());
        vals.put("x", shape.getX());
        vals.put("y", shape.getY());
        vals.put("text", shape.getText());
        vals.put("image", shape.getImage());
        vals.put("movable", shape.isMoveable());
        vals.put("visible", shape.isHidden());
        vals.put("actionScript", shape.getScript());
        vals.put("fontSize", shape.getFontSize());
        vals.put("height", shape.getHeight());
        vals.put("width", shape.getWidth());
        database.insert(this.shape_table_name, null, vals);

    }

    public int getTotalGameCount() {
        Cursor cursor = database.rawQuery("Select * from Games; ", null);
        return cursor.getCount();
    }

    public String getPageid() {
        return pageID;
    }

    public void setPageid(String pageid) {
        this.pageID = pageid;
    }

    public Boolean isShape(String id){
        Cursor cursor = database.rawQuery("SELECT * FROM SHAPES WHERE id = '"+ id+"';", null);
        boolean returnVal = true;
        if (cursor.getCount() <= 0){
            returnVal= false;
        }
        cursor.close();
        return returnVal;
    }

    public void addPage(Page page, boolean addToGame){
        ContentValues vals = new ContentValues();
        if (!page.getShapeList().isEmpty()){
            for (Shape s: page.getShapes().values()){
                if (!this.isShape(s.getShapeId())){
                    addShape(s);
                    vals.put("id", page.getPageId().toString());
                    vals.put("name", page.getName());
                    vals.put("shapes", s.getShapeId());
                    database.insert(this.page_table_name, null, vals);
                }
                else{
                    vals.put("id", page.getPageId().toString());
                    vals.put("name", page.getName());
                    vals.put("shapes", s.getShapeId());
                    database.insert(this.page_table_name, null, vals);
                }
            }
        }

        else {
            vals.put("id", page.getPageId().toString());
            vals.put("name", page.getName());
            database.insert(this.page_table_name, null, vals);
        }


    }

    public boolean gameExists(String gameName){
        Cursor cursor = database.rawQuery("SELECT * FROM Games WHERE gameName = '"+ gameName+"';", null);
        boolean returnVal = false;
        if (cursor.getCount() > 0){
            returnVal= true;
        }
        cursor.close();
        return returnVal;


    }

    public void addGame(Game game){
        ContentValues vals = new ContentValues();
        String gameName = game.getName();
        if (!game.getPages().isEmpty()){
            for (Page p: game.getPages().values()){
                addPage(p, false);
                vals.put("gameName", gameName);
                vals.put("pages", p.getPageId());
                vals.put("starterPage", game.getStarter());
                database.insert(this.game_table_name, null, vals);
            }
        }
        else{
            vals.put("starterPage", game.getStarter());
            vals.put("gameName", gameName);
            vals.put("starterPage", game.getStarter());
            database.insert(this.game_table_name, null, vals);
        }
    }

    public Shape getShape(String shapeId){
        Shape shapeReturn = new Shape(context,"temp name", "temp owner",0, 0, 100, 100);;
        Cursor cursor = database.rawQuery("SELECT * FROM SHAPES "+" WHERE id = '"+shapeId+"';", null);
        if (cursor.moveToFirst()) {
            shapeReturn.setShapeId(shapeId);
            shapeReturn.setName(cursor.getString(cursor.getColumnIndex("name")));
            shapeReturn.setText(cursor.getString(cursor.getColumnIndex("text")));
            shapeReturn.setImage(cursor.getString(cursor.getColumnIndex("image")));
            shapeReturn.setX(cursor.getInt(cursor.getColumnIndex("x")));
            shapeReturn.setY(cursor.getInt(cursor.getColumnIndex("y")));

            //source: https://stackoverflow.com/questions/4088080/get-boolean-from-database-using-android-and-sqlite
            boolean visibleValue = cursor.getInt(cursor.getColumnIndex("visible")) > 0;
            boolean movableValue = cursor.getInt(cursor.getColumnIndex("movable")) > 0;


            shapeReturn.setHidden(visibleValue);
            shapeReturn.setMoveable(movableValue);
            shapeReturn.setScriptList(cursor.getString(cursor.getColumnIndex("actionScript")));
            shapeReturn.setScriptMap();
            shapeReturn.setFontSize(cursor.getInt(cursor.getColumnIndex("fontSize")));
            shapeReturn.setHeight(cursor.getInt(cursor.getColumnIndex("height")));
            shapeReturn.setWidth(cursor.getInt(cursor.getColumnIndex("width")));
            shapeReturn.setSelected(false);
            shapeReturn.updateTextBounds();
        }
        cursor.close();
        return shapeReturn;
    }

    public void removeGame(String gameName){
        Cursor cursor = database.rawQuery("SELECT * FROM Games WHERE gameName = '"+gameName+"';", null);
        if (cursor.moveToFirst()) {
            for (int i=0; i<cursor.getCount(); i++){


                // remove the page
                Page page = getPage(cursor.getString(cursor.getColumnIndex("pages")));
                ArrayList<Shape> shapeList = page.getShapeList();
                for (Shape s: shapeList){

                    // remove the shapes
                    String query = "DELETE FROM Shapes WHERE id = '"+s.getShapeId()+"';";
                    database.execSQL(query);
                    String queryPage = "DELETE FROM Pages WHERE shapes = '"+pageID+"';";
                    database.execSQL(queryPage);

                }
                database.execSQL("DELETE FROM Pages WHERE id = '"+pageID+"';");



                database.execSQL("DELETE FROM Games WHERE pages = '"+cursor.getString(cursor.getColumnIndex("pages"))+"';");
                cursor.moveToNext();
            }
        }
        cursor.close();
        database.execSQL("DELETE FROM Games WHERE gameName = '"+gameName+"';");
    }


    public Page getPage(String pageId){

        Page pageReturn = new Page ("Temp page", 100 ,  100, "temp owner");
        pageReturn.setPageId(pageId);
        Cursor cursor = database.rawQuery("SELECT * FROM Pages WHERE id = '"+pageId +"';", null);

        if (cursor.moveToFirst()) {
            for (int i=0; i<cursor.getCount(); i++){
                pageReturn.setName(cursor.getString(cursor.getColumnIndex("name")));
                //pageReturn.setPageId(cursor.getString(cursor.getColumnIndex("id")));
                String shapes = cursor.getString(cursor.getColumnIndex("shapes"));
                if (shapes != null) {
                    Shape shape = this.getShape(shapes);
                    pageReturn.addShape(shape);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return pageReturn;
    }

    public Game getGame(String gameName){
        Game gameReturn = new Game(gameName, context);
        String starterPageId = "";

        String query = "SELECT * FROM Games WHERE gameName = '"+gameName+"';";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            for (int i=0; i<cursor.getCount(); i++){
                String pageId = cursor.getString(cursor.getColumnIndex("pages"));
                starterPageId = cursor.getString(cursor.getColumnIndex("starterPage"));

                if (pageId !=null){
                    Page page = getPage(pageId);
                    page.setGame(gameReturn);
                    gameReturn.addPage(page.getName(), page);
                    gameReturn.linkIDtoName(pageId, page.getName());

                }
                cursor.moveToNext();
            }
        }

        gameReturn.setStarter(gameReturn.pageIDtoName(starterPageId));
        gameReturn.setCurrentPage(gameReturn.getStarter());
        gameReturn.setAllShapes();

        return gameReturn;
    }


    public SQLiteDatabase getCurrentDatabase(){ return database; }


    public ArrayList<String> getAllGamesString(){

        ArrayList<String> gameString = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from Games ;", null);
        if (cursor.moveToFirst()) {
            for (int i=0; i<cursor.getCount(); i++){
                String gameName = cursor.getString(cursor.getColumnIndex("gameName"));
                if (gameString.contains(gameName)){
                    cursor.moveToNext();
                    continue;
                }
                gameString.add(gameName);
                cursor.moveToNext();

            }
        }
        cursor.close();
        return gameString;
    }


    public void setCurrentGameName(String gamename) { this.gameName = gamename;}

    public String getCurrentGameName() { return this.gameName; }

}
