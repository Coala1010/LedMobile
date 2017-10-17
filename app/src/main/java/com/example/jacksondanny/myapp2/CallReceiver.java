package com.example.jacksondanny.myapp2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public class CallReceiver extends PhonecallReceiver {

    public CallReceiver() {
    }

    @Override
    protected void onIncomingCallReceived(Context context, String number1, Date start, Intent intent)
    {


    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start)
    {
        //
        Log.d("myapp","CallReceiver");
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        //
        Log.d("myapp","CallReceiver");
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start)
    {
        //
        Log.d("myapp","CallReceiver");
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
    {
        //
        Log.d("myapp","CallReceiver");
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        //
        Log.d("myapp","CallReceiver");
    }

}