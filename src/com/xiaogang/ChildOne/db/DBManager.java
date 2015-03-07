package com.xiaogang.ChildOne.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.xiaogang.ChildOne.entity.Message;
import com.xiaogang.ChildOne.entity.NotifMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 14-9-15.
 */
public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public void add(NotifMessage message){
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO lx_message VALUES(null, ?, ?, ?, ?)", new Object[]{message.getTitle(), message.getContent(), message.getCustom(), message.getTime()});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public void deleteMessage(Message message) {
        db.delete("lx_message", "id=?", new String[]{String.valueOf(message.getId())});
    }

    public List<NotifMessage> query() {
        ArrayList<NotifMessage> messages = new ArrayList<NotifMessage>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            NotifMessage message = new NotifMessage();
            message.setId(c.getInt(c.getColumnIndex("id")));
            message.setTitle(c.getString(c.getColumnIndex("title")));
            message.setContent(c.getString(c.getColumnIndex("content")));
            message.setCustom(c.getString(c.getColumnIndex("custom")));
            message.setTime(c.getString(c.getColumnIndex("time")));
            messages.add(message);
        }
        c.close();
        return messages;
    }

    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM lx_message order by id desc", null);
        return c;
    }

    public void closeDB() {
        db.close();
    }


}
