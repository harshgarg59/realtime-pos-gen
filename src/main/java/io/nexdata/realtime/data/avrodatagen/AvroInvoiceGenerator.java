package io.nexdata.realtime.data.avrodatagen;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nexdata.realtime.data.idatagen.AddressGenerator;
import io.nexdata.realtime.data.idatagen.InvoiceGenerator;
import io.nexdata.realtime.data.idatagen.ItemGenerator;
import io.nexdata.realtime.model.avro.AvroAddress;
import io.nexdata.realtime.model.avro.AvroInvoice;
import io.nexdata.realtime.model.avro.AvroItem;
import io.nexdata.realtime.model.json.JsonAddress;
import io.nexdata.realtime.model.json.JsonItem;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Log4j2
@Profile("avro")
public class AvroInvoiceGenerator extends InvoiceGenerator<AvroInvoice> {

    private final AddressGenerator<AvroAddress> addressGenerator=new AddressGenerator<>(AvroAddress[].class);

    private final ItemGenerator<AvroItem> itemGenerator=new ItemGenerator<>(AvroItem[].class);

    public AvroInvoiceGenerator() {
        super(AvroInvoice[].class);
    }

    @Override
    public AvroInvoice getNextInvoice() {
        AvroInvoice invoice = invoices[getIndex()];
        int newInvoiceNumber = getNewInvoiceNumber();
        invoice.setInvoiceNumber(Integer.toString(newInvoiceNumber));
        invoice.setCreatedTime(System.currentTimeMillis());
        if (invoice.getDeliveryType().equalsIgnoreCase("HOME-DELIVERY")) {
            AvroAddress address = addressGenerator.getNextAddress();
            invoice.setAddress(address);
        }
        List<AvroItem> itemList = new ArrayList<>();
        int noOfItems = getNoOfItems();
        double totalAmount = 0.0;
        for (int i = 0; i < noOfItems; i++) {
            AvroItem nextItem = itemGenerator.getNextItem();
            totalAmount = totalAmount + nextItem.getTotalValue();
            itemList.add(nextItem);
        }
        invoice.setNumberOfItems(noOfItems);
        invoice.setItems(itemList);
        invoice.setTotalAmount(totalAmount);
        invoice.setTaxableAmount(totalAmount);
        invoice.setCGST(totalAmount * 0.025);
        invoice.setSGST(totalAmount * 0.025);
        invoice.setCESS(totalAmount * 0.00125);
        return invoice;
    }
}
