package se.uit.battleteam.quanlicuocdidong.BackgroundService;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.WindowManager;

import java.util.Date;

import se.uit.battleteam.quanlicuocdidong.DB.CallLog;
import se.uit.battleteam.quanlicuocdidong.Manager.DateTimeManager;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.BigKool;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.BigSave;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.BillionareFive;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.BillionareThree;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.BillionareTwo;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.Economy;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.HiSchool;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.MobiCard;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.MobiGold;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.MobiQ;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.NumberHeaderManager;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.PackageFee;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.QStudent;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.QTeen;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.Qkids;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.SV2014;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.SeaPlus;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.SevenColor;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.Student;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.Tomato;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.TomatoBL;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.VMOne;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.VMax;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.VinaCard;
import se.uit.battleteam.quanlicuocdidong.NetworkPackage.VinaXtra;

//<item name="android:windowBackground">@color/myWindowBackground</item>
/**
 * Created by justinvan on 10-Oct-15.
 */
public class PhoneStateReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneStateReceiver";
    Context _context;
    String _incomingNumber;
    private int _prev_state;
    private boolean _isOutGoingCallEnd;
    //private boolean _isReceived;
    //private PhoneLogManager _phoneCallLog;
    //DAO_CallLog _callAdapter;
    //DAO_Statistic _statisticTableAdapter;
    private PackageFee _myPackageFee;
    private boolean _isAllowPopUp;
    public PhoneStateReceiver() {
        //_isReceived = false;
        _incomingNumber = "";
        _prev_state = -1;
        _isOutGoingCallEnd = false;

    }



    public void InitPackage() {
        SharedPreferences setting = _context.getSharedPreferences("MySetting", Context.MODE_PRIVATE);
        String _package = setting.getString("GoiCuoc", "Unknown");
        _isAllowPopUp = setting.getBoolean("AllowPopup",false);
        switch (_package) {
            case "Mobicard": {
                _myPackageFee = new MobiCard();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case "MobiGold": {
                _myPackageFee = new MobiGold();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case "MobiQ": {
                _myPackageFee = new MobiQ();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case "Q-Student": {
                _myPackageFee = new QStudent();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case "Q-Teen": {
                _myPackageFee = new QTeen();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case "Q-Kids": {
                _myPackageFee = new Qkids();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case "VinaCard": {
                _myPackageFee = new VinaCard();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vinaphone);
                break;
            }
            case "VinaXtra": {
                _myPackageFee = new VinaXtra();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vinaphone);
                break;
            }
            case "Economy": {
                _myPackageFee = new Economy();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case "Tomato": {
                _myPackageFee = new Tomato();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case "Student": {
                _myPackageFee = new Student();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case "Sea+": {
                _myPackageFee = new SeaPlus();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case "Hi School": {
                _myPackageFee = new HiSchool();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case "7Colors": {
                _myPackageFee = new SevenColor();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case "Buôn làng": {
                _myPackageFee = new TomatoBL();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case "Big Save": {
                _myPackageFee = new BigSave();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
                break;
            }
            case "Big & Kool": {
                _myPackageFee = new BigKool();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
                break;
            }
            case "Tỉ phú 2": {
                _myPackageFee = new BillionareTwo();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
                break;
            }
            case "Tỉ phú 3": {
                _myPackageFee = new BillionareThree();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
                break;
            }
            case "Tỉ phú 5": {
                _myPackageFee = new BillionareFive();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
                break;
            }
            case "VM One": {
                _myPackageFee = new VMOne();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vietnamobile);
                break;
            }
            case "VMax": {
                _myPackageFee = new VMax();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vietnamobile);
                break;
            }
            case "SV 2014": {
                _myPackageFee = new SV2014();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vietnamobile);
                break;
            }

        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
            return;
        _context = context;
        //_callAdapter = new DAO_CallLog(context);
        //_statisticTableAdapter = new DAO_Statistic(context);
        //_callAdapter.Open();
        // _statisticTableAdapter.Open();
        //_phoneCallLog = new PhoneLogManager(_context, _myPackageFee);
        this.InitPackage();
        _isOutGoingCallEnd = false;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        CustomPhoneStateListener customPhoneStateListener = new CustomPhoneStateListener(_context,_myPackageFee);
        telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


    }

    public class CustomPhoneStateListener extends PhoneStateListener {
            boolean _isOutGoing= false;

        private static final String TAG = "CustomPhoneStateListener";
        //  public static final String TAG_DURATION = "CallDuration";
        //  public static final String TAG_FEE = "CallFee";
        Context _context;
        PackageFee _package;
        //Intent _listenerIntent;
        boolean _isReceivingCall;
        public CustomPhoneStateListener(Context context, PackageFee packageFee)
        {
            super();
            this._context = context;
            this._package = packageFee;

            // _listenerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //  _listenerIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            _isReceivingCall = false;
        }
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (incomingNumber != null && incomingNumber.length() > 0) {
                _incomingNumber = incomingNumber;
            }
            _isReceivingCall = false;
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING: {
                    _prev_state = state;
                    _isReceivingCall = true;
                    break;
                }
                case TelephonyManager.CALL_STATE_OFFHOOK: {
                    _prev_state = state;
                    break;
                }
                case TelephonyManager.CALL_STATE_IDLE: {
                    if (_prev_state == TelephonyManager.CALL_STATE_OFFHOOK && _isReceivingCall == false)
                    {
                        _prev_state = state;
                        _isOutGoingCallEnd = true;

                    }
                    if (_prev_state == TelephonyManager.CALL_STATE_RINGING) {
                        _prev_state = state;
                    }
                    break;
                }
            }
            if(_isOutGoingCallEnd == true && _isReceivingCall == false)
            {
                CallLog lastCall = getNewCallLog();
                if(lastCall != null && lastCall.get_callDuration() >0 && _isOutGoing == true && _isAllowPopUp == true )
                {
                    try
                    {
                        final AlertDialog alertDialog = new AlertDialog.Builder(_context,AlertDialog.THEME_HOLO_LIGHT).create();
                        //final AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(_context,android.R.style.Theme_Material_Light_Dialog)).create();
                        alertDialog.setTitle("Call Information");
                        alertDialog.setMessage("Duration: " + lastCall.get_callDuration() + "secs" + "\nCost: " + lastCall.get_callFee() + " VND");

                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                            }
                        });
                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                        layoutParams.gravity = Gravity.TOP;
                        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        layoutParams.alpha = 1.0f;
                        layoutParams.buttonBrightness = 1.0f;
                        layoutParams.windowAnimations = android.R.style.Theme_Material_Light_Dialog_Alert;


                        //alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        alertDialog.getWindow().setAttributes(layoutParams);
                        alertDialog.show();
                        _isOutGoing= false;
                        _isOutGoingCallEnd = false;
                        _isReceivingCall = false;

                    }
                    catch(Exception e)
                    {
                        e.getLocalizedMessage();
                    }
                }
                _isOutGoingCallEnd = false;
                _isReceivingCall = false;
            }
        }
        public CallLog getNewCallLog()
        {
            CallLog _newCall = new CallLog();
            Cursor cursor = this._context.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, null, null,
                    null, android.provider.CallLog.Calls.DATE + " DESC");
            int number = cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
            int callType = cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE);
            int date = cursor.getColumnIndex(android.provider.CallLog.Calls.DATE);
            int duration = cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION);

            if(cursor.moveToFirst() )
            {
                String _callType = cursor.getString(callType);
                int dircode = Integer.parseInt(_callType);
                if(dircode == android.provider.CallLog.Calls.OUTGOING_TYPE) {

                    _isOutGoing = true;
                    String _number = cursor.getString(number);
                    if(_number.length() <10)
                        return null;
                    String _callDate = cursor.getString(date);
                    Date _callDayTime = new Date(Long.valueOf(_callDate));
                    int _callDuration = cursor.getInt(duration);
                    _package.set_callDuration(_callDuration);
                    _package.set_callTime(DateTimeManager.get_instance().convertToHm(_callDayTime.toString()));
                    _package.set_outGoingPhoneNumber(_number);
                    int fee = _package.CalculateCallFee();
                    _newCall.set_callDuration(_callDuration);
                    _newCall.set_callFee(fee);
                    return _newCall;
                }
            }
            return null;
        }

    }
}