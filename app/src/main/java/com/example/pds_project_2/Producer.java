package com.example.pds_project_2;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer implements Runnable {
    private static ConnectionFactory factory;
    private String exchangeName;
    private Channel channel;

    public Producer (ConnectionFactory factory, String exchangeName) {
        this.factory = factory;
        this.exchangeName = exchangeName;


    }

    @Override
    public void run() {
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.confirmSelect();
            channel.exchangeDeclare(exchangeName, "direct");
            } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String message, String routingKey) {
            new Thread((Runnable) () -> {
                try {

                    AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                        .deliveryMode(2)
                        .build();
                channel.basicPublish(exchangeName, routingKey, properties, message.getBytes("UTF-8"));
                channel.waitForConfirmsOrDie();

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

    }

}
