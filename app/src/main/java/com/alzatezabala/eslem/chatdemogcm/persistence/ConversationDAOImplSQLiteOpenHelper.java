package com.alzatezabala.eslem.chatdemogcm.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alzatezabala.eslem.chatdemogcm.dommain.Conversation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eslem on 07/10/2014.
 */
public class ConversationDAOImplSQLiteOpenHelper extends SQLiteOpenHelper implements ConversationDAO{
    private static int DATABASE_VERSION=1;
    private String DATABASE_NAME="chat";
    private static String TABLE_NAME="conversations";

    private String KEY_ID="id";
    private String KEY_USERID="userid";
    private String KEY_USERNAME="username";
    private String KEY_LASTMESSAGE="lastMessage";

    public ConversationDAOImplSQLiteOpenHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public Conversation insert(Conversation conversation) {
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_USERID, conversation.getIdUser());
        contentValues.put(KEY_USERNAME, conversation.getUserName());
        contentValues.put(KEY_LASTMESSAGE, conversation.getLastMessage());

        int idInserted = (int) sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        conversation.setId(idInserted);
        sqLiteDatabase.close();
        return conversation;
    }

    @Override
    public Conversation get(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, new String[]{KEY_ID, KEY_USERID, KEY_USERNAME, KEY_LASTMESSAGE},
                KEY_ID +"= ?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();

            Conversation conversation = new Conversation(id, Integer.parseInt(cursor.getString(1)));
            conversation.setUserName(cursor.getString(2));
            conversation.setLastMessage(cursor.getString(3));
            return conversation;
        }
        return null;
    }

    @Override
    public Conversation getFromUser(int idUser) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, new String[]{KEY_ID, KEY_USERID, KEY_USERNAME, KEY_LASTMESSAGE},
                KEY_USERID +"=?", new String[]{String.valueOf(idUser)},
                null, null, null, null);
        if(cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();
            Conversation conversation = new Conversation(Integer.parseInt(cursor.getString(0)), idUser);
            conversation.setUserName(cursor.getString(2));
            conversation.setLastMessage(cursor.getString(3));
            return conversation;
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete= db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
        if(delete == 0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public List<Conversation> getAll() {
        List<Conversation> conversations = new ArrayList<Conversation>();

        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Conversation conversation = new Conversation(cursor.getInt(0), cursor.getInt(1));
                conversation.setUserName(cursor.getString(2));
                if(cursor.getColumnCount()>3) {
                    conversation.setLastMessage(cursor.getString(3));
                }
                conversations.add(conversation);

            }while(cursor.moveToNext());
            return conversations;
        }

        return null;
    }

    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY, "
                +KEY_USERID+ " INTEGER, "+KEY_USERNAME+" TEXT, "+KEY_LASTMESSAGE+" TEXT )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public void deleteAllData() {
        SQLiteDatabase sdb = this.getWritableDatabase();
        sdb.delete(TABLE_NAME, null, null);

    }

    @Override
    public void update(Conversation conversation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LASTMESSAGE, conversation.getLastMessage());

        // updating row
        db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(conversation.getId()) });
    }
}
