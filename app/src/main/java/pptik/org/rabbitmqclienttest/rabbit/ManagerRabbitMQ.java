package pptik.org.rabbitmqclienttest.rabbit;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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
    private static final String EXCHANGE_NAME = "sabuga.exchange";


    String userName = "Sabuga";
    String password = "berabbit";
    String virtualHost = "/SabugaRangers";
    String serverIp = "167.205.7.229";
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

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();

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
                    Log.i("Tag", "hahaha");
                    dialog.dismiss();
               //     registerChanelHost();
                //    registerChanelListHost();
                    String msg = "{\n" +
                            "  \"status\": {\n" +
                            "    \"code\": 200,\n" +
                            "    \"success\": true,\n" +
                            "    \"msg\": \"Data ditemukan\"\n" +
                            "  },\n" +
                            "  \"data\": [\n" +
                            "    {\n" +
                            "      \"_id\": \"57f4910151a74e1dddc2c6f9\",\n" +
                            "      \"name\": \"Driver 1\",\n" +
                            "      \"email\": \"driver1@gmail.com\",\n" +
                            "      \"nomor_telepon\": \"081311415274\",\n" +
                            "      \"user\": \"driver1\",\n" +
                            "      \"pass\": \"U9tJ00U5t68be71e76f206ed3f2b6c37320c57faf0\",\n" +
                            "      \"role\": \"2\",\n" +
                            "      \"id_unit\": \"001\",\n" +
                            "      \"latitude\": \"-6.881694\",\n" +
                            "      \"longitude\": \"107.615820\",\n" +
                            "      \"location\": \"Jl. Ir H djuanda, Dago-Coblong, Bandung\",\n" +
                            "      \"date\": \"October 5th 2016, 12:34:57 pm\",\n" +
                            "      \"isVerified\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"_id\": \"57f4911b51a74e1dddc2c6fa\",\n" +
                            "      \"name\": \"Driver 2\",\n" +
                            "      \"email\": \"driver2@gmail.com\",\n" +
                            "      \"nomor_telepon\": \"081311415274\",\n" +
                            "      \"user\": \"driver2\",\n" +
                            "      \"pass\": \"NsBdrxReWn27d48576d6d46fd12612c633428d2c2f\",\n" +
                            "      \"role\": \"2\",\n" +
                            "      \"id_unit\": \"002\",\n" +
                            "      \"latitude\": \"-6.877911\",\n" +
                            "      \"longitude\": \"107.616890\",\n" +
                            "      \"location\": \"Jl. Ir H djuanda no 399, Dago-Coblong, Bandung\",\n" +
                            "      \"date\": \"October 5th 2016, 12:35:23 pm\",\n" +
                            "      \"isVerified\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"_id\": \"57f4912d51a74e1dddc2c6fb\",\n" +
                            "      \"name\": \"Driver 3\",\n" +
                            "      \"email\": \"driver3@gmail.com\",\n" +
                            "      \"nomor_telepon\": \"081311415274\",\n" +
                            "      \"user\": \"driver3\",\n" +
                            "      \"pass\": \"fe89U7PPcO3b611199a29d13442fe49bd4f5a9776b\",\n" +
                            "      \"role\": \"2\",\n" +
                            "      \"id_unit\": \"003\",\n" +
                            "      \"latitude\": \"-6.884797\",\n" +
                            "      \"longitude\": \"107.613836\",\n" +
                            "      \"location\": \"Jl. Ir H djuanda no 319, Dago-Coblong, Bandung\",\n" +
                            "      \"date\": \"October 5th 2016, 12:35:41 pm\",\n" +
                            "      \"isVerified\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"_id\": \"57f4913d51a74e1dddc2c6fc\",\n" +
                            "      \"name\": \"Driver 4\",\n" +
                            "      \"email\": \"driver4@gmail.com\",\n" +
                            "      \"nomor_telepon\": \"081311415274\",\n" +
                            "      \"user\": \"driver4\",\n" +
                            "      \"pass\": \"QlRw1ag44N8c0261f33dd511989e83ad419f474a6b\",\n" +
                            "      \"role\": \"2\",\n" +
                            "      \"id_unit\": \"004\",\n" +
                            "      \"latitude\": \"-6.889912\",\n" +
                            "      \"longitude\": \"107.613394\",\n" +
                            "      \"location\": \"Jl. Ir H djuanda no 124, Dago-Coblong, Bandung\",\n" +
                            "      \"date\": \"October 5th 2016, 12:35:57 pm\",\n" +
                            "      \"isVerified\": false\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"_id\": \"57f4915851a74e1dddc2c6fd\",\n" +
                            "      \"name\": \"Driver 5\",\n" +
                            "      \"email\": \"driver5@gmail.com\",\n" +
                            "      \"nomor_telepon\": \"081311415274\",\n" +
                            "      \"user\": \"driver5\",\n" +
                            "      \"pass\": \"cvI0cuNk4xe8274d3ca7c29f1086a7b9d2a44d5421\",\n" +
                            "      \"role\": \"2\",\n" +
                            "      \"id_unit\": \"005\",\n" +
                            "      \"latitude\": \"-6.893624\",\n" +
                            "      \"longitude\": \"107.613008\",\n" +
                            "      \"location\": \"Jl. Ir H djuanda no 324, Dago-Coblong, Bandung\",\n" +
                            "      \"date\": \"October 5th 2016, 2:03:46 pm\",\n" +
                            "      \"isVerified\": false\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";
                    byte[] messageBodyBytes = msg.getBytes();
                    mChannel.basicPublish(EXCHANGE_NAME, "sabuga.#", null, messageBodyBytes);

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

            String queueName = mChannel.queueDeclare().getQueue();
            mChannel.queueBind(queueName, EXCHANGE_NAME, "sabuga.#");

            Consumer consumer = new DefaultConsumer(mChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {

                    //Verify if device that send the info is different of the are receiver
               //     getHeader(properties);

                    String message = new String(body, "UTF-8");

  //                  Gson gson = new Gson();
//                    Message message1 = gson.fromJson(message, Message.class);
                    Log.i("mah", message);

                }
            };

            mChannel.basicConsume(queueName, true, consumer);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private  void getHeader(AMQP.BasicProperties properties){

        Map<String, Object> headers = properties.getHeaders();

        Object deviceId =  headers.get("extraContent");

    }
}
