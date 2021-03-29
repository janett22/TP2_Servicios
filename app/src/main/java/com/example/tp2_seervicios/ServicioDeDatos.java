package com.example.tp2_seervicios;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import java.util.Date;

public class ServicioDeDatos extends Service {
    public ServicioDeDatos() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final Uri sms = Telephony.Sms.CONTENT_URI;
        final ContentResolver cr = getContentResolver();

        Runnable leer = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Cursor c = cr.query(sms, null, null, null, null);

                    if (c.getCount() > 0) {
                        int i = 0;
                        while (c.moveToNext() && i < 5) {
                            String nro = c.getString(c.getColumnIndex(Telephony.Sms.Inbox.ADDRESS));
                            String fecha = c.getString(c.getColumnIndex(Telephony.Sms.Inbox.DATE));
                            long dateLong = Long.parseLong(fecha);
                            Date date = new Date(dateLong);
                            String mensaje = c.getString(c.getColumnIndex(Telephony.Sms.Inbox.BODY));
                            Log.d("mensaje", "Sms del numero " + nro + "recibido el: " + date.toString() + "-- " + mensaje);
                            i++;
                        }
                        try {
                            Thread.sleep(9000);
                            c.moveToFirst();
                        } catch (InterruptedException e) {
                            Log.e("Error ", e.getMessage());
                        }
                    }
                }
            }
        };
        Thread trabajador = new Thread(leer);
        trabajador.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}