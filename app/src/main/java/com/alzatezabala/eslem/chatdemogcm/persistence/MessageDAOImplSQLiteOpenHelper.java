package com.alzatezabala.eslem.chatdemogcm.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alzatezabala.eslem.chatdemogcm.dommain.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eslem on 07/10/2014.
 */
public class MessageDAOImplSQLiteOpenHelper extends SQLiteOpenHelper implements MessagesDAO {
    private static int DATABASE_VERSION = 1;
    private String DATABASE_NAME = "chat";
    private static String TABLE_NAME = "messages";

    private String KEY_ID = "id";
    private String KEY_IDCONVERSATION = "conversationid";
    private String KEY_OWN = "own";
    private String KEY_TIME = "timeDate";
    private String KEY_MESSAGE = "message";

    public MessageDAOImplSQLiteOpenHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
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
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_IDCONVERSATION + " INTEGER, "
                + KEY_OWN + " INTEGER, "
                + KEY_MESSAGE + " TEXT,"
                + KEY_TIME + " INTEGER )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    @Override
    public Message insert(Message message) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int own = (message.isOwn()) ? 1 : 0;

        contentValues.put(KEY_IDCONVERSATION, message.getIdConversation());
        contentValues.put(KEY_OWN, own);
        contentValues.put(KEY_MESSAGE, message.getMessage());
        contentValues.put(KEY_TIME, message.getTime());


        int idInserted = (int) sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        message.setId(idInserted);
        sqLiteDatabase.close();
        return message;
    }

    @Override
    public Message get(int id) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, new String[]{KEY_ID, KEY_IDCONVERSATION, KEY_OWN, KEY_MESSAGE, KEY_TIME},
                KEY_ID + "= ?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Boolean own = (cursor.getInt(2) == 1) ? true : false;

            Message message = new Message(cursor.getInt(0), cursor.getInt(1), own, cursor.getString(3), cursor.getLong(4));
            return message;
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        if (delete == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<Message>();

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Boolean own = (cursor.getInt(2) == 1) ? true : false;
                Message message = new Message(cursor.getInt(0), cursor.getInt(1), own, cursor.getString(3), cursor.getLong(4));
                messages.add(message);

            } while (cursor.moveToNext());
            return messages;
        }

        return null;
    }

    @Override
    public List<Message> getAllConversation(int idConversation) {
        List<Message> messages = new ArrayList<Message>();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_IDCONVERSATION + "=" + idConversation;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Boolean own = (cursor.getInt(2) == 1) ? true : false;
                Message message = new Message(cursor.getInt(0), cursor.getInt(1), own, cursor.getString(3), cursor.getLong(4));
                messages.add(message);
            } while (cursor.moveToNext());
            return messages;
        }

        return null;
    }

    public void deleteAllData() {
        SQLiteDatabase sdb = this.getWritableDatabase();
        sdb.delete(TABLE_NAME, null, null);

    }
}
