package ru.aston.notification.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import ru.aston.notification.kafka.dto.UserEvent;

import java.util.Map;

@SpringBootTest(properties = {
        "app.kafka.user-events-topic=user-events",
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.group-id=notification-service-test",
        "spring.kafka.consumer.auto-offset-reset=earliest"
})
@EmbeddedKafka(topics = {"user-events"}, partitions = 1)
@ExtendWith(OutputCaptureExtension.class)
class UserEventListenerIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Test
    void whenUserCreated_eventConsumed(CapturedOutput output) throws InterruptedException {
        // given
        String topic = "user-events";
        String email = "test@mail.com";
        String operation = "CREATE";
        String expectedLogLine = "Consumed user event: operation=CREATE email=test@mail.com";

        KafkaTemplate<String, UserEvent> kafkaTemplate = givenKafkaTemplate();

        // when
        whenUserEventSent(kafkaTemplate, topic, email, operation);

        // then
        thenLogLinePresentWithinTimeout(output, expectedLogLine, 5000);
    }

    private KafkaTemplate<String, UserEvent> givenKafkaTemplate() {
        return kafkaTemplate();
    }

    private static void whenUserEventSent(
            KafkaTemplate<String, UserEvent> kafkaTemplate,
            String topic,
            String email,
            String operation
    ) {
        kafkaTemplate.send(topic, email, new UserEvent(operation, email));
    }

    private static void thenLogLinePresentWithinTimeout(
            CapturedOutput output,
            String expected,
            long timeoutMs
    ) throws InterruptedException {
        long deadlineMs = System.currentTimeMillis() + timeoutMs;
        while (System.currentTimeMillis() < deadlineMs) {
            if (output.getOut().contains(expected) || output.getErr().contains(expected)) {
                return;
            }
            Thread.sleep(50);
        }

        throw new AssertionError("Expected log line was not found within timeout. Expected to contain: " + expected
                + "\nCaptured output:\n" + output);
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