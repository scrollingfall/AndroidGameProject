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
    public String pageID;


    private ArrayList<String> nameOfImages;

    private DatabaseInstance(Context context){
        pageNumber = 1;
        this.context = context;
        database = context.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS "+shape_table_name+" (id TEXT PRIMARY KEY NOT NULL, name TEXT NOT NULL, x REAL, y REAL, text TEXT, image TEXT, movable BOOLEAN, visible BOOLEAN, actionScript TEXT, fontSize INTEGER);");
        database.execSQL("CREATE TABLE IF NOT EXISTS "+page_table_name+" (id REAL, name TEXT NOT NULL, shapes TEXT);");
        database.execSQL("CREATE TABLE IF NOT EXISTS "+game_table_name+"(gameName TEXT NOT NULL, pages TEXT);");
        nameOfImages = new ArrayList<String>(Arrays.asList("carrot","carrot2","death","duck","fire","mystic"));
    }


    public void addShape(Shape shape){
        String queryString1 = "INSERT INTO Shapes (id, name, x, y, text, image, movable, visible, actionScript, fontSize) VALUES ";
        queryString1 +=  shape.getShapeId() + "," + shape.getName() + "," + shape.getX() + "," + shape.getY() + "," + shape.getText() + "," + shape.getImage() + "," + shape.isMoveable() + "," + shape.isHidden() + "," + shape.getScript() + "," + shape.getFontSize() ;
        database.execSQL(queryString1);
        String queryString2 = "INSERT INTO Pages VALUES ";
        queryString2 += pageID + "," + pageNumber + "," + shape.getName();
        database.execSQL(queryString2);
    }

    public void updateShape(Shape shape){

        String queryString1 = "UPDATE  Shapes SET name=" + shape.getName() + ", x=" + shape.getX() + ", y=" + shape.getY() + ", text=" + shape.getText() + ", image=" + shape.getImage() + ", moveable =" + shape.isMoveable() + ", visible=" + shape.isHidden() + ", actionScript=" + shape.getScript() + ", fontSize=" + shape.getFontSize() ;
        queryString1 += "where shapeId=" + shape.getShapeId();
        database.execSQL(queryString1);
        String queryString2 = "INSERT INTO Pages VALUES ";
        queryString2 += pageNumber + "," + shape.getName();
        database.execSQL(queryString2);
    }

    public String getPageid() {
        return pageID;
    }

    public void setPageid(String pageid) {
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

    public void addPage(Page page){

        String gameName = page.getOwner();
        ContentValues vals = new ContentValues();
        vals.put("pages", page.getId());
        database.update(game_table_name, vals, "gameName='"+gameName+"'", null);
        
        ArrayList<Shape> shapeList = page.getShapeList();
        database.execSQL("INSERT INTO PAGES VALUES id = " + page.getId() + " name = " + page.getName());
        if (!shapeList.isEmpty()){
            for (Shape shape : shapeList){
                ContentValues vals = new ContentValues();
                vals.put("Shapes_id", shape.getShapeId());
                database.insert(this.page_table_name, null, vals);
                if (!this.isShape(shape.getShapeId())){
                    this.addShape(shape);
                }

            }


        }
    }


    public SQLiteDatabase getCurrentDatabase(){ return database; }

    public ArrayList<Game> getAllGames(){
        return null;

    }

    public ArrayList<String> getAllGamesString(DatabaseInstance game){
        ArrayList<Game> games = game.getAllGames();
        ArrayList<String> gameString = new ArrayList<>();
        for(Game world : games)  gameString.add(world.toString());
        return gameString;
    }

}
