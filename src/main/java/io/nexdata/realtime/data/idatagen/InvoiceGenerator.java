package io.nexdata.realtime.data.idatagen;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nexdata.realtime.model.json.JsonAddress;
import io.nexdata.realtime.model.json.JsonInvoice;
import io.nexdata.realtime.model.json.JsonItem;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class InvoiceGenerator<T> {

    private final Random invoiceIndex;
    private final Random invoiceNumber;
    private final Random numberOfItems;
    protected final T[] invoices;

    public int getIndex() {
        return invoiceIndex.nextInt(100);
    }

    public int getNewInvoiceNumber() {
        return invoiceNumber.nextInt(99999999) + 99999;
    }

    public int getNoOfItems() {
        return numberOfItems.nextInt(4) + 1;
    }

    public InvoiceGenerator(Class<T[]> clazz) {
        String DATAFILE = "src/main/resources/data/invoice.json";
        this.invoiceIndex = new Random();
        this.invoiceNumber = new Random();
        this.numberOfItems = new Random();
        final ObjectMapper mapper = new ObjectMapper();
        try {
            this.invoices = mapper.readValue(new File(DATAFILE), clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract T getNextInvoice() ;
}


