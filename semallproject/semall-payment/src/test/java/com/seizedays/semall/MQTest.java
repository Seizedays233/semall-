package com.seizedays.semall;

import com.seizedays.semall.mq.ActiveMQUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MQTest {

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Test
    public void mqTest() throws JMSException {
        ConnectionFactory connectionFactory = activeMQUtil.getConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        System.out.println(connection.hashCode());
    }
}
