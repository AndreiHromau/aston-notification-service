package ru.aston.notification.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import ru.aston.notification.mail.MailSender;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = {
        "app.kafka.user-events-topic=user-events",
        "spring.kafka.consumer.group-id=notification-service-test",
        "spring.kafka.consumer.auto-offset-reset=earliest"
})
@EmbeddedKafka(topics = {"user-events"}, partitions = 1)
class UserEventListenerIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @MockBean
    private MailSender mailSender;

    @Test
    void whenUserCreated_eventConsumed_andMailSent() {
        KafkaTemplate<String, UserEvent> kafkaTemplate = kafkaTemplate();

        kafkaTemplate.send("user-events", "test@mail.com",
                new UserEvent("CREATE", "test@mail.com"));

        verify(mailSender, timeout(5000)).send(
                eq("test@mail.com"),
                eq("Notification"),
                eq("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.")
        );
    }

    private KafkaTemplate<String, UserEvent> kafkaTemplate() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafka);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producerProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);

        DefaultKafkaProducerFactory<String, UserEvent> pf = new DefaultKafkaProducerFactory<>(producerProps);
        return new KafkaTemplate<>(pf);
    }
}