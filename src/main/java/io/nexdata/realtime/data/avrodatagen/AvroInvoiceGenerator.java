package io.nexdata.realtime.data.avrodatagen;

import io.nexdata.realtime.data.idatagen.AddressGenerator;
import io.nexdata.realtime.data.idatagen.InvoiceGenerator;
import io.nexdata.realtime.data.idatagen.ItemGenerator;
import io.nexdata.realtime.model.avro.AvroAddress;
import io.nexdata.realtime.model.avro.AvroInvoice;
import io.nexdata.realtime.model.avro.AvroItem;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@Profile("avro")
public class AvroInvoiceGenerator extends InvoiceGenerator<AvroInvoice> {

    private final AddressGenerator<AvroAddress> addressGenerator;

    private final ItemGenerator<AvroItem> itemGenerator;

    public AvroInvoiceGenerator() {
        super(AvroInvoice[].class);
        addressGenerator = new AddressGenerator<>(AvroAddress[].class);
        itemGenerator = new ItemGenerator<>(AvroItem[].class);
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
