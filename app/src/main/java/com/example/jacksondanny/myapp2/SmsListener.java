package com.example.jacksondanny.myapp2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Jackson Danny on 5/10/2017.
 */

public class SmsListener extends BroadcastReceiver{

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){

            Intent intent_ = null;
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        Log.d("From", msg_from);
                        Log.d("Message content", msgBody);

                        intent_ = new Intent(context,MyIncomingSMS.class);
                        intent_.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent_.putExtra("number",msg_from);
                        intent_.putExtra("message_content",msgBody);




                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
            context.startActivity(intent_);
            
        }
    }
}