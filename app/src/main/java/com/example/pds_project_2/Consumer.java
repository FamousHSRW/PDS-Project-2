package com.example.pds_project_2;

import android.app.Activity;
import android.widget.TextView;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Consumer implements Runnable {
    private ConnectionFactory factory;
    private ArrayList<String> routingKey = new ArrayList<>();
    private Channel channel;
    private String exchangeName;
    private  Activity activity;

    public Consumer(Activity activity, ConnectionFactory factory, String routingKey, String exchangeName) {
        this.factory = factory;
        if(!this.routingKey.contains(routingKey)) this.routingKey.add(routingKey);
        this.exchangeName = exchangeName;
        this.activity = activity;
    }

    @Override
    public void run() {
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare("cnn", true, false, true, null);
            channel.queueDeclare("bbc", true, false, true, null);
            channel.exchangeDeclare(exchangeName, "direct");

            consumeMessages(routingKey.get(0));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void consumeMessages(String routingKey)  {
        new Thread((Runnable) () -> {
            try {
                channel.queueBind(routingKey, exchangeName, routingKey);

                DeliverCallback deliverCallback = ((consumerTag, delivery) -> {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("ran");
                            try {
                                String message = new String(delivery.getBody(), "UTF-8");
                                TextView tv = activity.findViewById(R.id.messages_received);
                                Date now = new Date();
                                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                                tv.append(ft.format(now) + ' ' + delivery.getEnvelope().getRoutingKey().toUpperCase() + ": " + message + '\n');
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                });

                channel.basicConsume(routingKey, true, deliverCallback, consumerTag -> {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        if(!this.routingKey.contains(routingKey)) this.routingKey.add(routingKey);

    }

}
