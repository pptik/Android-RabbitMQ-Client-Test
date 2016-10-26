package pptik.org.rabbitmqclienttest.rabbit;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import pptik.org.rabbitmqclienttest.utilities.Message;

/**
 * Created by hynra on 23/12/15.
 */
public class ManagerRabbitMQ {

    protected Channel mChannel = null;
    protected Connection mConnection;
    private static final String EXCHANGE_NAME = "semut.opang";
    private static final String ACTION_STRING_ACTIVITY = "broadcast_event";


    String userName = "semut";
    String password = "Semut";
    String virtualHost = "/semut";
    String serverIp = "167.205.7.226";
    int port = 5672;


    protected boolean running;

    private Context context;

    public ManagerRabbitMQ(Context context) {
        this.context = context;
    }

    public void dispose(){

        running = false;

        try {
            if (mConnection!=null)
                mConnection.close();
            if (mChannel != null)
                mChannel.abort();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void connectToRabbitMQ() {


        if (mChannel != null && mChannel.isOpen()){//already declared
            running = true;
        }

        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {

                try{

                    final ConnectionFactory connectionFactory = new ConnectionFactory();
                    connectionFactory.setUsername(userName);
                    connectionFactory.setPassword(password);
                    connectionFactory.setVirtualHost(virtualHost);
                    connectionFactory.setHost(serverIp);
                    connectionFactory.setPort(port);
                    connectionFactory.setAutomaticRecoveryEnabled(true);

                    mConnection = connectionFactory.newConnection();
                    mChannel = mConnection.createChannel();
                    Log.i("Connect To host", "connected");
            //        dialog.dismiss();
                    registerChanelHost();

                    return true;

                } catch (Exception e){
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                running = aBoolean;
            }


        }.execute();
    }

    private void registerChanelListHost(){

        try{

            mChannel.exchangeDeclare(EXCHANGE_NAME, "topic", true);

            String queueName = mChannel.queueDeclare().getQueue();
            mChannel.queueBind(queueName, EXCHANGE_NAME, "sabuga.#");

            Consumer consumer = new DefaultConsumer(mChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {

                    //Verify if device that send the info is different of the are receiver
                    getHeader(properties);

                    String message = new String(body, "UTF-8");

                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Message>>() {}.getType();

                    List<Message> messageList = gson.fromJson(message, type);

                }
            };

            mChannel.basicConsume(queueName, true, consumer);
            //   byte[] messageBodyBytes = "Hello, world!".getBytes();
            //   mChannel.basicPublish(EXCHANGE_NAME, routingKey, null, messageBodyBytes);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void registerChanelHost(){

        try{

            mChannel.exchangeDeclare(EXCHANGE_NAME, "topic", true);

            while(true){
                final String queueName = mChannel.queueDeclare("opang.user.199", false, false, true, null).getQueue();
                mChannel.queueBind(queueName, EXCHANGE_NAME, "");

                Consumer consumer = new DefaultConsumer(mChannel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope,
                                               AMQP.BasicProperties properties, byte[] body) throws IOException {

                        String message = new String(body, "UTF-8");
                        //sendBroadcast(message);
                        Log.i("Response Type: ", properties.getType());
                        if(properties.getType().equals("callback")){
//                        System.out.println(" [x] Received response from service '" + envelope.getRoutingKey() + "':'" + message);
//                        channel.basicAck(envelope.getDeliveryTag(), false);
                            Log.i("Messege Response: ", message);
                            sendBroadcast("CALLBACK: "+message);
                        }

                        //consume bid
                        if(properties.getType().equals("bid")) {
//                        System.out.println(" [x] Received bid': " + envelope.getRoutingKey() + "':'" + message);
                            Log.i("Messege Response: ", message);
                            sendBroadcast("BID: "+message);
                        }

                    }
                };
                mChannel.basicConsume(queueName, true, consumer);
                break;
            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendBroadcast(String msg) {
        Intent intent = new Intent(ACTION_STRING_ACTIVITY);
        intent.putExtra("message", msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private  void getHeader(AMQP.BasicProperties properties){

        Map<String, Object> headers = properties.getHeaders();

        Object deviceId =  headers.get("extraContent");

    }

    public void sendMessage(String msg, String routingKey,  AMQP.BasicProperties props){
        byte[] messageBodyBytes = msg.getBytes();
        try {
            Log.i("publish request: ", msg);
            mChannel.basicPublish(EXCHANGE_NAME, routingKey, props, messageBodyBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
