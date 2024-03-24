package com.maxback.container;

import com.maxback.WordsFactoryTestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.RabbitMQContainer;

import java.util.Map;

@SpringBootTest(classes = WordsFactoryTestConfig.class)
@ExtendWith(SpringExtension.class)
public class AbstractWordsFactoryTestContainers extends ParentTestContainer {


    static {
        if (isNotPopulated()) {
            addContainer(new RabbitMQContainer("rabbitmq:3-management").withReuse(true)
                            .withExposedPorts(5672, 15672),
                    container -> Map.of(
                            "spring.rabbitmq.port", container.getMappedPort(5672) + "",
                            "spring.rabbitmq.http.port", container.getMappedPort(15672) + "",
                            "spring.rabbitmq.host", container.getHost(),
                            "spring.rabbitmq.username", "guest",
                            "spring.rabbitmq.password", "guest",
                            "rabbit.management.api.base-url", "http://" + container.getHost() + ":" + container.getMappedPort(15672) + "/api"
                    ));

            setupConnectivityProperties();
            Map<String, String> connectivityProperties = getConnectivityProperties();

            for (Map.Entry<String, String> entry : connectivityProperties.entrySet()) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        }
    }
}
