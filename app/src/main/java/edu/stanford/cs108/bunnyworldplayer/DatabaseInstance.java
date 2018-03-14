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
    private String database_name = "newDatabase.db";
    private String shape_table_name = "Shapes";
    private String page_table_name = "Pages";
    private String game_table_name = "Games";
    private Context context;
    private int pageNumber;
    public String pageID;
    private String gameName = "";
    private static DatabaseInstance databaseInstance;
    private ArrayList<String> nameOfImages;
    private int totalGameCount;


    private DatabaseInstance(Context context){
        System.out.println("beginning constructor");
        pageNumber = 1;
        this.context = context;
        database = context.openOrCreateDatabase(database_name, MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS "+shape_table_name+" (id REAL PRIMARY KEY NOT NULL, name TEXT NOT NULL, x REAL, y REAL, text TEXT, image TEXT, movable BOOLEAN, visible BOOLEAN, actionScript TEXT, fontSize INTEGER);");
        database.execSQL("CREATE TABLE IF NOT EXISTS "+page_table_name+" (id TEXT, name TEXT NOT NULL, shapes TEXT);");
        database.execSQL("CREATE TABLE IF NOT EXISTS "+game_table_name+"(gameName TEXT NOT NULL, pages TEXT);");
        nameOfImages = new ArrayList<String>(Arrays.asList("carrot","carrot2","death","duck","fire","mystic"));
        System.out.println("ending constructor");
    }

    public static DatabaseInstance getDBinstance(Context context) {
        System.out.println("beg of db constructor");
        if (databaseInstance == null){
            databaseInstance = new DatabaseInstance(context);
        }
        System.out.println("end of db constructor");
        return databaseInstance;
    }

    public void addShape(Shape shape) {
        String queryString1 = "INSERT INTO Shapes (id, name, x, y, text, image, movable, visible, actionScript, fontSize) VALUES (";
        String shapeText = shape.getText();
        String shapeImage = shape.getImage();
        String shapeScript = shape.getScript();

        if (shapeText == null || shapeText.isEmpty()) shapeText = "NULL";
        else shapeText = "\"" + shapeText + "\"";

        if (shapeImage == null || shapeImage.isEmpty()) shapeImage = "NULL";
        else shapeImage = "\"" + shapeImage + "\"";

        if (shapeScript == null || shapeScript.isEmpty()) shapeScript = "NULL";
        else shapeScript = "\"" + shapeScript + "\"";

        int hidden = shape.isHidden() ? 1 : 0;
        int movable = shape.isMoveable() ? 1 : 0;

        queryString1 += shape.getShapeId() + ",\"" + shape.getName() + "\"," + shape.getX() + "," + shape.getY() + "," + shapeText + "," + shapeImage + "," + movable + "," + hidden + "," + shapeScript + "," + shape.getFontSize() + ")";
        database.execSQL(queryString1 + ";");
        // add this shape to the page also.
    }

    public int getTotalGameCount() {
        Cursor cursor = database.rawQuery("Select * from Games; ", null);
        return cursor.getCount();
    }

    public void updateShape(Shape shape){

        String queryString1 = "UPDATE  Shapes SET name=" + shape.getName() + ", x=" + shape.getX() + ", y=" + shape.getY() + ", text=" + shape.getText() + ", image=" + shape.getImage() + ", moveable =" + shape.isMoveable() + ", visible=" + shape.isHidden() + ", actionScript=" + shape.getScript() + ", fontSize=" + shape.getFontSize() ;
        queryString1 += "where shapeId=" + shape.getShapeId();
        database.execSQL(queryString1 + ";");
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

//        if (addToGame) {
//            String gameName = page.getOwner();
//            ContentValues vals1 = new ContentValues();
//            vals1.put("pages", page.getPageId());
//            database.update(game_table_name, vals1, "gameName='" + gameName + "'", null);
//        }
//
//        ArrayList<Shape> shapeList = page.getShapeList();
//        database.execSQL("INSERT INTO PAGES VALUES id = " + page.getPageId() + " name = " + page.getName() + ";");
//        if (!shapeList.isEmpty()){
//            for (Shape shape : shapeList){
//                ContentValues vals = new ContentValues();
//                vals.put("shapes", shape.getShapeId());
//                database.insert(this.page_table_name, null, vals);
//                if (!this.isShape(shape.getShapeId())){
//                    this.addShape(shape);
//                }
//
//            }
//
//        }
        System.out.println("beg of add page");

        if (page.getShapes().isEmpty()){
            System.out.println("enter if statement");
            ContentValues insertValues = new ContentValues();
            insertValues.put("id", page.getPageId().toString());
            System.out.println("end of add page");
            insertValues.put("name", page.getName());
            database.insert(this.page_table_name, null, insertValues);
            System.out.println("end of add page");
            return;
        }
        for (Shape s: page.getShapes().values()){
            System.out.println("didnt enter if statement");
            if (!this.isShape(s.getShapeId())){
                System.out.println("enter if statement");
                this.addShape(s);
            }
            ContentValues insertValues = new ContentValues();
            insertValues.put("id", page.getPageId().toString());
            insertValues.put("name", page.getName());
            insertValues.put("shapes", s.getShapeId());
            database.insert(this.page_table_name, null, insertValues);

        }

        System.out.println("end of add page");


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

//        System.out.println("beg of add game");
////        database.execSQL("DELETE From Games where exists gameName = " + game.getName());
//        ArrayList<Page> pagesList = game.getPageList();
//        database.execSQL("INSERT INTO Games VALUES gameName = " + game.getName() +";");
//        System.out.println("inserted add game");
//        if (!pagesList.isEmpty()){
//            for (Page page : pagesList){
//                ContentValues vals = new ContentValues();
//                vals.put("pages", page.getPageId());
//                database.insert(this.game_table_name, null, vals);
//                if (!this.isPage(page.getPageId())){
//                    this.addPage(page, false);
//                }
//
//            }
//
//        }


        System.out.println("beg of add game");
        String gameName = game.getName();
        System.out.println("the game name in add game is " + game.getName());
        if (game.getPages().isEmpty()){
            System.out.println("enter if statement");
            ContentValues insertValues = new ContentValues();
            insertValues.put("gameName", gameName);
            database.insert(this.game_table_name, null, insertValues);
            System.out.println("beg of end game");
            return;
        }

        // Add an entry for each Page, Shape in Pages table & Shapes table
        int numpages = 0;
        for (Page p: game.getPages().values()){
            System.out.println("didnt enter if statement");
            numpages++;
            addPage(p, false);
            // Add world to world table
            ContentValues insertValues = new ContentValues();
            insertValues.put("gameName", gameName);

            insertValues.put("pages", p.getPageId());
//            System.out.println("page id while adding game is " + p.getPageId());

            database.insert(this.game_table_name, null, insertValues);
        }
//        System.out.println("beg of end game");

    }

    public Shape getShape(String shapeId){
//        System.out.println("beg of get SHape");
        Shape shapeReturn = null;
        Cursor cursor = database.rawQuery("SELECT * FROM SHAPES "+" WHERE id = '"+shapeId+"';", null);
        if (cursor.moveToFirst()) {
            String pageOwner = "temp";
            shapeReturn = new Shape(context, cursor.getString(cursor.getColumnIndex("name")), pageOwner, cursor.getFloat(cursor.getColumnIndex("x")), cursor.getFloat(cursor.getColumnIndex("y")), 100, 100);
            shapeReturn.setShapeId(Integer.parseInt(shapeId));
            shapeReturn.setScriptList(cursor.getString(cursor.getColumnIndex("actionScript")));
            shapeReturn.setFontSize(cursor.getInt(cursor.getColumnIndex("fontSize")));

            if(Integer.parseInt(shapeId) == 0){
                cursor.close();
                return null;
            }
        }
        cursor.close();
//        System.out.println("end of get SHape");
        return shapeReturn;
    }


    public Page getPage(String pageId){

        if (pageId == null) return null;
        Page pageReturn = new Page ("Temp page", 100 ,  100, "temp owner");
        String pageName = "";
//        System.out.println("before gettin cursor");
        Cursor cursor = database.rawQuery("SELECT * FROM Pages WHERE id = '"+pageId +"';", null);

        if (cursor.moveToFirst()) {
//            System.out.println("in if  cursor");
            for (int i=0; i<cursor.getCount(); i++){
//                System.out.println("in for loop of   cursor");
                pageName = cursor.getString(cursor.getColumnIndex("name"));
                String shapes = cursor.getString(cursor.getColumnIndex("shapes"));
//                System.out.println("before getting shape ");
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
//        System.out.println("the game name is " + gameReturn.getName());
//        String finalName = null;
        String query = "SELECT * FROM Games WHERE gameName = '"+gameName+"';";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
//            System.out.println("the cursoe is" + DatabaseUtils.dumpCursorToString(cursor));
            for (int i=0; i<cursor.getCount(); i++){
                String pageId = cursor.getString(cursor.getColumnIndex("pages"));
                Page page = getPage(pageId);
//                System.out.println("page id while getting game is " + page.getPageId());
//                System.out.println("page name while getting game is " + page.getName());


                if(page!=null) gameReturn.addPage(page.getName(),page );
                cursor.moveToNext();
            }
        }
//        gameReturn.setName(finalName);
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
