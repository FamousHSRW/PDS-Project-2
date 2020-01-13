package com.example.pds_project_2;

import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Producer implements Runnable {
    private static ConnectionFactory factory;
    private BlockingDeque<String> queue = new LinkedBlockingDeque<>();
    private String routingKey;
    private String exchangeName;
    private Channel channel;

    public Producer (ConnectionFactory factory, String routingKey, String exchangeName) {
        this.factory = factory;
        this.routingKey = routingKey;
        this.exchangeName = exchangeName;


    }

    @Override
    public void run() {
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.confirmSelect();
            channel.exchangeDeclare(exchangeName, "direct");
            while(!Thread.interrupted()) {
                String message = queue.takeFirst();

                try {
                    AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                            .deliveryMode(2)
                            .build();
                channel.basicPublish(exchangeName, routingKey, properties, message.getBytes("UTF-8"));
                channel.waitForConfirmsOrDie();
                } catch (Exception e) {
                    Log.d("", "[f] " + message);
                    queue.putFirst(message);
                    throw e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String message) {
        try {
            queue.putLast(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getRoutingKey() { return this.routingKey; }

}
