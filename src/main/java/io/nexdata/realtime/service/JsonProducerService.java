package io.nexdata.realtime.service;

import io.nexdata.realtime.iservice.ProducerService;
import io.nexdata.realtime.model.json.JsonInvoice;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Log4j2
@Profile({"json","default"})
public class JsonProducerService implements ProducerService<JsonInvoice> {

    @Value("${application.configs.topic.name}")
    private String TOPIC_NAME;

    @Autowired
    private KafkaTemplate<String, JsonInvoice> kafkaTemplate;

    @Override
    public void sendMessage(JsonInvoice jsonInvoice) {
        log.info(String.format("Producing JsonInvoice No: %s Customer Type: %s",
                jsonInvoice.getInvoiceNumber(),
                jsonInvoice.getCustomerType()));
        kafkaTemplate.send(TOPIC_NAME, jsonInvoice.getStoreID(), jsonInvoice);
    }


}
