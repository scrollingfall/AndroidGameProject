package edu.stanford.cs108.bunnyworldplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by vsinghi on 3/7/18.
 */

public class DatabaseInstance {

    private SQLiteDatabase database;
    private String database_name = "OurDatabase.db";
    private String shape_table_name = "Shapes";
    private String page_table_name = "Pages";
    private String game_table_name = "Games";
    private Context context;
    private int pageNumber;
    public int pageID;
    private String gameName = "";
    private static DatabaseInstance databaseInstance;
    private ArrayList<String> nameOfImages;

    private DatabaseInstance(Context context){
        pageNumber = 1;
        this.context = context;
        database = context.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS "+shape_table_name+" (id REAL PRIMARY KEY NOT NULL, name TEXT NOT NULL, x REAL, y REAL, text TEXT, image TEXT, movable BOOLEAN, visible BOOLEAN, actionScript TEXT, fontSize INTEGER);");
        database.execSQL("CREATE TABLE IF NOT EXISTS "+page_table_name+" (id REAL, name TEXT NOT NULL, shapes TEXT);");
        database.execSQL("CREATE TABLE IF NOT EXISTS "+game_table_name+"(gameName TEXT NOT NULL, pages TEXT);");
        nameOfImages = new ArrayList<String>(Arrays.asList("carrot","carrot2","death","duck","fire","mystic"));
    }

    public static DatabaseInstance getDBinstance(Context context) {
        if (databaseInstance == null){
            databaseInstance = new DatabaseInstance(context);
        }
        return databaseInstance;
    }

    public void addShape(Shape shape) {
        String queryString1 = "INSERT INTO Shapes (id, name, x, y, text, image, movable, visible, actionScript, fontSize) VALUES ";
        queryString1 += shape.getShapeId() + "," + shape.getName() + "," + shape.getX() + "," + shape.getY() + "," + shape.getText() + "," + shape.getImage() + "," + shape.isMoveable() + "," + shape.isHidden() + "," + shape.getScript() + "," + shape.getFontSize();
        database.execSQL(queryString1);
        // add this shape to the page also.
    }

    public void updateShape(Shape shape){

        String queryString1 = "UPDATE  Shapes SET name=" + shape.getName() + ", x=" + shape.getX() + ", y=" + shape.getY() + ", text=" + shape.getText() + ", image=" + shape.getImage() + ", moveable =" + shape.isMoveable() + ", visible=" + shape.isHidden() + ", actionScript=" + shape.getScript() + ", fontSize=" + shape.getFontSize() ;
        queryString1 += "where shapeId=" + shape.getShapeId();
        database.execSQL(queryString1);
    }

    public int getPageid() {
        return pageID;
    }

    public void setPageid(int pageid) {
        this.pageID = pageid;
    }

    public Boolean isShape(int id){
        Cursor cursor = database.rawQuery("SELECT * FROM SHAPES WHERE id = '"+ id+"';", null);
        boolean returnVal = true;
        if (cursor.getCount() <= 0){
            returnVal= false;
        }
        cursor.close();
        return returnVal;
    }

    public Boolean isPage(int id){
        Cursor cursor = database.rawQuery("SELECT * FROM Pages WHERE id = '"+ id+"';", null);
        boolean returnVal = true;
        if (cursor.getCount() <= 0){
            returnVal= false;
        }
        cursor.close();
        return returnVal;
    }



    public void addPage(Page page, boolean addToGame){

        if (addToGame) {
            String gameName = page.getOwner();
            ContentValues vals1 = new ContentValues();
            vals1.put("pages", page.getPageId());
            database.update(game_table_name, vals1, "gameName='" + gameName + "'", null);
        }

        ArrayList<Shape> shapeList = page.getShapeList();
        database.execSQL("INSERT INTO PAGES VALUES id = " + page.getPageId() + " name = " + page.getName());
        if (!shapeList.isEmpty()){
            for (Shape shape : shapeList){
                ContentValues vals = new ContentValues();
                vals.put("shapes", shape.getShapeId());
                database.insert(this.page_table_name, null, vals);
                if (!this.isShape(shape.getShapeId())){
                    this.addShape(shape);
                }

            }


        }
    }

    public void addGame(Game game){

        ArrayList<Page> pagesList = game.getPageList();
        database.execSQL("INSERT INTO Games VALUES gameName = " + game.getName());
        if (!pagesList.isEmpty()){
            for (Page page : pagesList){
                ContentValues vals = new ContentValues();
                vals.put("pages", page.getPageId());
                database.insert(this.game_table_name, null, vals);
                if (!this.isPage(page.getPageId())){
                    this.addPage(page, false);
                }

            }

        }
    }

    public Shape getShape(String shapeId){
        Shape shapeReturn = null;
        Cursor cursor = database.rawQuery("SELECT * FROM SHAPES "+" WHERE id = '"+shapeId+"';", null);
        if (cursor.moveToFirst()) {
            String pageOwner = "temp";
            shapeReturn = new Shape(context, cursor.getString(cursor.getColumnIndex("name")), pageOwner, cursor.getFloat(cursor.getColumnIndex("x")), cursor.getFloat(cursor.getColumnIndex("y")), 100, 100);
            shapeReturn.setShapeId(Integer.parseInt(shapeId));
            shapeReturn.setScriptList(cursor.getString(cursor.getColumnIndex("scripts")));
            shapeReturn.setFontSize(cursor.getInt(cursor.getColumnIndex("fontSize")));

            if(Integer.parseInt(shapeId) == 0){
                cursor.close();
                return null;
            }
        }
        cursor.close();
        return shapeReturn;
    }


    public Page getPage(int pageId){
        Page pageReturn = null;
        String pageName = "";
        Cursor cursor = database.rawQuery("SELECT * FROM Pages WHERE id = '"+pageId+"';", null);

        if (cursor.moveToFirst()) {
            for (int i=0; i<cursor.getCount(); i++){
                pageName = cursor.getString(cursor.getColumnIndex("name"));
                String shapes = cursor.getString(cursor.getColumnIndex("shapes"));
                Shape shape = this.getShape(shapes);
                if(shape != null) pageReturn.addShape(shape);
                cursor.moveToNext();
            }
        }
        cursor.close();
        pageReturn.setName(pageName);
        pageReturn.setPageId(pageId);
        return pageReturn;
    }

    public Game getGame(String gameName){
        Game gameReturn = new Game(gameName, context);
        String finalName = null;
        String query = "SELECT * FROM Games WHERE gameName = '"+gameName+"';";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            for (int i=0; i<cursor.getCount(); i++){
                int pageId = cursor.getInt(cursor.getColumnIndex("id"));
                Page page = getPage(pageId);
                if(page!=null) gameReturn.addPage(page.getName(),page );
                cursor.moveToNext();
            }
        }
        gameReturn.setName(finalName);
        return gameReturn;
    }


    public SQLiteDatabase getCurrentDatabase(){ return database; }


    public ArrayList<String> getAllGamesString(){
        ArrayList<Game> listOfGames = new ArrayList<Game>();
        ArrayList<String> gamesAdded = new ArrayList<String>();
        Cursor cursor = database.rawQuery("select * from Games ;", null);
        if (cursor.moveToFirst()) {
            for (int i=0; i<cursor.getCount(); i++){
                String gameName = cursor.getString(cursor.getColumnIndex("gameName"));

                if (gamesAdded.contains(gameName)){
                    cursor.moveToNext();
                    continue;
                }
                gamesAdded.add(gameName);
                Game game1 = getGame(gameName);
                listOfGames.add(game1);
                cursor.moveToNext();

            }
        }
        cursor.close();
        ArrayList<String> gameString = new ArrayList<>();
        for(Game world : listOfGames)  gameString.add(world.toString());
        return gameString;
    }


    public void setCurrentGameName(String gamename) { this.gameName = gamename;}
    public String getCurrentGameName() { return this.gameName; }

}
