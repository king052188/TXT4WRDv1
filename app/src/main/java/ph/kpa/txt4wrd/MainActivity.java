package ph.kpa.txt4wrd;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btnSend;

    TextView lblLogs, lblUuid;

    public static String Mobile;
    public static String Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().hide();

        String uniqueID = UUID.randomUUID().toString();
        lblUuid = (TextView)findViewById(R.id.lblUuid);
        lblLogs = (TextView)findViewById(R.id.lblLogs);

        lblUuid.setText(uniqueID);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // click handling code
                sendSMS("09995233848", "Message Count " );
            }
        });


    }

    private void sendSMS(String phoneNumber, String message) {

        Mobile = phoneNumber;
        Message = message;

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        lblLogs.setText("Message Sent to " + Mobile);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        lblLogs.setText("Generic failure | " + Mobile);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        lblLogs.setText("No service | " + Mobile);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        lblLogs.setText("Null PDU | " + Mobile);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        lblLogs.setText("Radio off | " + Mobile);
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        lblLogs.setText("Message delivered to " + Mobile);
                        break;
                    case Activity.RESULT_CANCELED:
                        lblLogs.setText("SMS not delivered to " + Mobile);
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();

//        ArrayList<String> messageParts = sms.divideMessage(message);

        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

//        sms.sendMultipartTextMessage(phoneNumber, null, messageParts, null, null);

//        Toast.makeText(this, "Sent.", Toast.LENGTH_SHORT).show();
    }
}
