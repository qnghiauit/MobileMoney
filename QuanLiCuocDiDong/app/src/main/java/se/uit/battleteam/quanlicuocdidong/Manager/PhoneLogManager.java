package se.uit.battleteam.quanlicuocdidong.Manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.provider.CallLog;

import se.uit.battleteam.quanlicuocdidong.DB.*;

import se.uit.battleteam.quanlicuocdidong.NetworkPackage.PackageFee;

/**
 * Created by justinvan on 03-Oct-15.
 */
public class PhoneLogManager {
    //List<se.uit.chichssssteam.quanlicuocdidong.DB.CallLog> _listCall;
    //List<MessageLog> _listMessage;
    Context _context;
    PackageFee _packageFee;
    DateTimeManager _dateTimeManager;
    private static PhoneLogManager _instance;


    public static  synchronized  PhoneLogManager get_instance(Context context, PackageFee packageFee)
    {
        if(_instance == null)
            _instance = new PhoneLogManager(context,packageFee);
        return _instance;
    }
    private PhoneLogManager(Context context, PackageFee packageFee)
    {
        //_listCall = new ArrayList<se.uit.chichssssteam.quanlicuocdidong.DB.CallLog>();
        //_listMessage = new ArrayList<MessageLog>();
        _context = context;
        _packageFee = packageFee;
        _dateTimeManager = DateTimeManager.get_instance();

    }

    public List<se.uit.battleteam.quanlicuocdidong.DB.CallLog> LoadCallLogFromPhone() {

        List<se.uit.battleteam.quanlicuocdidong.DB.CallLog> _logList = new ArrayList<se.uit.battleteam.quanlicuocdidong.DB.CallLog>();
        Cursor cursor = _context.getContentResolver().query(CallLog.Calls.CONTENT_URI
                ,null, CallLog.Calls.DURATION + " > 0", null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int callType = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        if (cursor.moveToFirst()) {
            do {
                String _number = cursor.getString(number);
                String _callType = cursor.getString(callType);
                String _callDate = cursor.getString(date);
                Date _callDayTime = new Date(Long.valueOf(_callDate));
                int _callDuration = cursor.getInt(duration);

                int dircode = Integer.parseInt(_callType);
                if (dircode == CallLog.Calls.OUTGOING_TYPE)
                {

                    _packageFee.set_callDuration(_callDuration);
                    _packageFee.set_outGoingPhoneNumber(_number);
                    String time = _dateTimeManager.convertToHm(_callDayTime.toString());
                    _packageFee.set_callTime(time);
                    int _fee = _packageFee.CalculateCallFee();
                    se.uit.battleteam.quanlicuocdidong.DB.CallLog newElement = new
                            se.uit.battleteam.quanlicuocdidong.DB.CallLog(-1, _callDayTime.toString(), _number, _callDuration, _fee,_packageFee.get_type());
                    _logList.add(newElement);
                }
            }
            while (cursor.moveToNext() == true);
        }
        cursor.close();
        return _logList;
    }
    public List<se.uit.battleteam.quanlicuocdidong.DB.CallLog> LoadCallLogAfterTimeSpan(long time)
    {
        List<se.uit.battleteam.quanlicuocdidong.DB.CallLog> _logList = new ArrayList<se.uit.battleteam.quanlicuocdidong.DB.CallLog>();


        Cursor cursor = _context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls.DATE + " > " + time, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int callType = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        if(cursor.moveToFirst())
        {
            do
            {
                String _number = cursor.getString(number);
                String _callType = cursor.getString(callType);
                String _callDate = cursor.getString(date);
                Date _callDayTime = new Date(Long.valueOf(_callDate));
                int _callDuration = cursor.getInt(duration);

                int dircode = Integer.parseInt(_callType);
                if (dircode == CallLog.Calls.OUTGOING_TYPE)
                {
                    _packageFee.set_callDuration(_callDuration);
                    _packageFee.set_outGoingPhoneNumber(_number);
                    String datetime = _dateTimeManager.convertToHm(_callDayTime.toString());
                    _packageFee.set_callTime(datetime);
                    int _fee = _packageFee.CalculateCallFee();
                    se.uit.battleteam.quanlicuocdidong.DB.CallLog newElement = new
                            se.uit.battleteam.quanlicuocdidong.DB.CallLog(-1, _callDayTime.toString(), _number, _callDuration, _fee,_packageFee.get_type());
                    _logList.add(newElement);
                }
            }
            while(cursor.moveToNext() ==true);
        }
        cursor.close();
        return _logList;
    }
    public List<MessageLog> LoadMessageLogFromPhone() {
        List<MessageLog> _logList = new ArrayList<MessageLog>();
        Uri _uri = Uri.parse("content://sms/sent");

        Cursor cursor = _context.getContentResolver().query(_uri, null, null, null, "date DESC");

        if (cursor.moveToFirst()) {
            do {

                String number = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                Date _messageDayTime = new Date(Long.valueOf(date));
                _packageFee.set_outGoingPhoneNumber(number);
                _packageFee.set_sendMessageTime(_messageDayTime.toString());
                int _fee = _packageFee.CalculateMessageFee();
                MessageLog newElement = new MessageLog(_messageDayTime.toString(), number, _fee, _packageFee.get_type());
                _logList.add(newElement);

            }
            while (cursor.moveToNext() == true);
        }
        cursor.close();
        return _logList;
    }

    public List<MessageLog> LoadMessageLogAfterTimeSpan(long time)
    {
        List<MessageLog> _logList = new ArrayList<MessageLog>();
        Uri _uri = Uri.parse("content://sms/sent");
        Cursor cursor = _context.getContentResolver().query(_uri, null, "date" + " > " + time, null, "date DESC");

        if (cursor.moveToFirst()) {
            do {

                String number = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                Date _messageDayTime = new Date(Long.valueOf(date));
                _packageFee.set_outGoingPhoneNumber(number);

                _packageFee.set_sendMessageTime(_messageDayTime.toString());
                int _fee = _packageFee.CalculateMessageFee();
                MessageLog newElement = new MessageLog(_messageDayTime.toString(), number, _fee, _packageFee.get_type());
                _logList.add(newElement);

            }
            while (cursor.moveToNext() == true);
        }
        cursor.close();
        return _logList;
    }
    public long GetLastedMessageTime()
    {
        Uri _uri = Uri.parse("content://sms/sent");
        Cursor cursor = _context.getContentResolver().query(_uri, null, null, null, "date DESC");

        cursor.moveToFirst();
        long time = 0;
        time = cursor.getLong(cursor.getColumnIndex("date"));
        cursor.close();
        return time;
    }
    public MessageLog GetLastedSentSMS()
    {

        Uri _uri = Uri.parse("content://sms/sent");
        Cursor cursor = _context.getContentResolver().query(_uri, null, null, null, "date DESC");

        if(cursor.moveToFirst())
        {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            Date _messageDayTime = new Date(Long.valueOf(date));
            String number = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            _packageFee.set_sendMessageTime(_messageDayTime.toString());
            _packageFee.set_outGoingPhoneNumber(number);
            int _fee = _packageFee.CalculateMessageFee();
            MessageLog row = new MessageLog(_messageDayTime.toString(),number,_fee,_packageFee.get_type());
            return row;
        }
        cursor.close();
        return null;
    }
    public long GetLastedCallTime()
    {
        Cursor cursor = _context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null,null,null,CallLog.Calls.DATE + " DESC");

        cursor.moveToFirst();
        long time = 0;
        time = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
        cursor.close();
        return time;
    }

    public se.uit.battleteam.quanlicuocdidong.DB.CallLog GetLastedOutGoingCall()
    {

        Cursor cursor = _context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int callType = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        if(cursor.moveToFirst()) {
            String _number = cursor.getString(number);
            String _callType = cursor.getString(callType);
            String _callDate = cursor.getString(date);
            Date _callDayTime = new Date(Long.valueOf(_callDate));
            int _callDuration = cursor.getInt(duration);
            int dircode = Integer.parseInt(_callType);

            if (dircode == CallLog.Calls.OUTGOING_TYPE) {
                _packageFee.set_callDuration(_callDuration);
                _packageFee.set_outGoingPhoneNumber(_number);
                String time = _dateTimeManager.convertToHm(_callDayTime.toString());
                _packageFee.set_callTime(time);
                int _fee = _packageFee.CalculateCallFee();
                se.uit.battleteam.quanlicuocdidong.DB.CallLog target;
                target = new
                        se.uit.battleteam.quanlicuocdidong.DB.CallLog(-1, _callDayTime.toString(), _number, _callDuration, _fee, _packageFee.get_type());
                return target;

            }
        }
        cursor.close();
        return null;

    }

    //public long

}
