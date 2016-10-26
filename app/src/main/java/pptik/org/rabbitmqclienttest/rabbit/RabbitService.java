package pptik.org.rabbitmqclienttest.rabbit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by fiyyanp on 10/26/2016.
 */
public class RabbitService extends Service{
    private ManagerRabbitMQ manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("service:", "oncreate");
        manager = new ManagerRabbitMQ(getApplicationContext());
        manager.connectToRabbitMQ();
    }
}
