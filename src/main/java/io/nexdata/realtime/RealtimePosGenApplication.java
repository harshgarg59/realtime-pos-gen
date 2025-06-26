package io.nexdata.realtime;

import io.nexdata.realtime.data.idatagen.InvoiceGenerator;
import io.nexdata.realtime.iservice.ProducerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class RealtimePosGenApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(RealtimePosGenApplication.class, args);
    }

    @Autowired
    private ProducerService producerService;

    @Autowired
    private InvoiceGenerator invoiceGenerator;

    @Value("${application.configs.invoice.count}")
    private int INVOICE_COUNT;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (int i = 0; i < INVOICE_COUNT; i++) {
            producerService.sendMessage(invoiceGenerator.getNextInvoice());
            Thread.sleep(1000);
        }
    }

}
