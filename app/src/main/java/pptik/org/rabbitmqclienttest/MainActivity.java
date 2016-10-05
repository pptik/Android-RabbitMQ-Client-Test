package pptik.org.rabbitmqclienttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

import pptik.org.rabbitmqclienttest.rabbit.ManagerRabbitMQ;

public class MainActivity extends AppCompatActivity {
    ManagerRabbitMQ manage;
    private static final String EXCHANGE_NAME = "sabuga.chat";
    private static String TAG_INCOMING = "Incoming Message";
    private String TAG = getClass().getSimpleName();
    private EditText boxChat;
    private ImageButton btnSend;
    private TextView chatMain;
    private static final String ACTION_STRING_ACTIVITY = "broadcast_event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boxChat = (EditText)findViewById(R.id.boxChat);
        btnSend = (ImageButton)findViewById(R.id.btnsend);
        chatMain = (TextView)findViewById(R.id.chatMain);

        manage = new ManagerRabbitMQ(MainActivity.this);
        manage.connectToRabbitMQ();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manage.sendMessage(boxChat.getText().toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter(ACTION_STRING_ACTIVITY));
        super.onResume();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          String message = intent.getStringExtra("message");
            chatMain.setText(chatMain.getText().toString()+"\n"+message);
        }
    };
}
