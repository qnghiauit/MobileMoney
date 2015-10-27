package se.uit.chichssssteam.quanlicuocdidong.DB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import se.uit.chichssssteam.quanlicuocdidong.Manager.DateTimeManager;

/**
 * Created by justinvan on 03-Oct-15.
 */



public class DAO_MessageLog
{

    private SQLiteDatabase _database;
    private DbHelper _dbHelper;
    private String[] _listColumn = {_dbHelper.MESS_ID, _dbHelper.MESSAGE_DATE, _dbHelper.RECEIVER, _dbHelper.MESSAGE_FEE, _dbHelper.MESSAGE_TYPE};
    private DateTimeManager _dateTimeManager;
    public DAO_MessageLog(Context context) {
        _dbHelper = _dbHelper.getInstance(context);
    }

    public void Open() throws SQLException
    {
        _database = _dbHelper.getWritableDatabase();
        _database = _dbHelper.getReadableDatabase();
    }

    public void Close()
    {
        _dbHelper.close();
    }


    public MessageLog CursortoMessageLog(Cursor c)
    {
        MessageLog row = new MessageLog();
        row.set_messageId(c.getInt(0));
        Date temp = new Date(c.getLong(1));
        row.set_messageDate(temp.toString());
        row.set_recieverNumber(c.getString(2));
        row.set_messageFee(c.getInt(3));
        return row;
    }


    public MessageLog CreateMessageLogRow(String messageDate, String receiver, int messageFee, int type)
    {
        MessageLog msgLog  = new MessageLog();
        ContentValues values = new ContentValues();

        values.put(_dbHelper.MESSAGE_DATE, _dateTimeManager.convertToMilisec(messageDate));
        values.put(_dbHelper.RECEIVER , receiver);
        values.put(_dbHelper.MESSAGE_FEE, messageFee);
        values.put(_dbHelper.MESSAGE_TYPE, type );
        long insertId = _database.insert(_dbHelper.MESSAGE_TABLE,null,values);
        Cursor cursor = _database.query(_dbHelper.MESSAGE_TABLE,_listColumn, _dbHelper.MESS_ID + " = " + insertId, null,null,null,null);
        cursor.moveToFirst();
        msgLog = CursortoMessageLog(cursor);
        cursor.close();
        return msgLog;

    }

    public void CreateMessageLogRow(MessageLog messageLog)
    {
        ContentValues values = new ContentValues();
        values.put(_dbHelper.MESSAGE_DATE, _dateTimeManager.convertToMilisec(messageLog.get_messageDate()));
        values.put(_dbHelper.RECEIVER, messageLog.get_receiverNumber());
        values.put(_dbHelper.MESSAGE_FEE,messageLog.get_messageFee());
        values.put(_dbHelper.MESSAGE_TYPE, messageLog.get_messageType());
        _database.insert(_dbHelper.MESSAGE_TABLE,null,values);
    }
    public void DeleteMessageLog(int _id)
    {
        _database.delete(_dbHelper.MESSAGE_TABLE, _dbHelper.MESS_ID + " = " + _id, null);
    }

    public List<MessageLog> GetAllMessageLog()
    {
        List<MessageLog> _listMessage = new ArrayList<MessageLog>();
        Cursor cursor = _database.query(_dbHelper.MESSAGE_TABLE,_listColumn,null,null,null,null,null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast())
        {
            MessageLog temp = CursortoMessageLog(cursor);
            _listMessage.add(temp);
            cursor.moveToNext();

        }
        cursor.close();
        return _listMessage;
    }

    public MessageLog FindMessageLogbyId(int _id)
    {
        MessageLog messageLog;
        Cursor cursor = _database.query(_dbHelper.MESSAGE_TABLE, _listColumn, _dbHelper.MESS_ID + " = " + _id, null, null, null, null);
        if(!cursor.moveToFirst())
            return null;
        else
        {

            messageLog = CursortoMessageLog(cursor);
        }
        cursor.close();
        return messageLog;
    }
    public long GetLastedMessageTime()
    {
        long lastedMessageLog;
        Cursor cursor = _database.query(_dbHelper.MESSAGE_TABLE,_listColumn,null,null,null,null,_dbHelper.MESSAGE_DATE + " DESC",null);
        if(cursor.moveToFirst())
        {
            MessageLog lastMessage = CursortoMessageLog(cursor);

            lastedMessageLog = _dateTimeManager.convertToMilisec(lastMessage.get_messageDate());
            return lastedMessageLog;
        }
        return 0;

    }
    public boolean isDatabaseOpening()
    {
        if(_database.isOpen())
            return true;
        return false;
    }
}
