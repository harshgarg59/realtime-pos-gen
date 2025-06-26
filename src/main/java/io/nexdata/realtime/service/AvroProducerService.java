package io.nexdata.realtime.service;

import io.nexdata.realtime.iservice.ProducerService;
import io.nexdata.realtime.model.avro.AvroInvoice;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@Profile("avro")
public class AvroProducerService implements ProducerService<AvroInvoice> {

    @Value("${application.configs.topic.name}")
    private String TOPIC_NAME;

    @Autowired
    private KafkaTemplate<String, AvroInvoice> kafkaTemplate;

    @Override
    public void sendMessage(AvroInvoice avroInvoice) {
        log.info(String.format("Producing AvroInvoice No: %s Customer Type: %s",
                avroInvoice.getInvoiceNumber(),
                avroInvoice.getCustomerType()));
        kafkaTemplate.send(TOPIC_NAME, avroInvoice.getStoreID(), avroInvoice);
    }
}
