package io.nexdata.realtime.data.jsondatagen;

import io.nexdata.realtime.data.idatagen.AddressGenerator;
import io.nexdata.realtime.data.idatagen.InvoiceGenerator;
import io.nexdata.realtime.data.idatagen.ItemGenerator;
import io.nexdata.realtime.model.json.JsonAddress;
import io.nexdata.realtime.model.json.JsonInvoice;
import io.nexdata.realtime.model.json.JsonItem;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@Profile({"json"})
public class JsonInvoiceGenerator  extends InvoiceGenerator<JsonInvoice> {

    private final AddressGenerator<JsonAddress> addressGenerator;

    private final ItemGenerator<JsonItem> itemGenerator;

    public JsonInvoiceGenerator() {
        super(JsonInvoice[].class);
        addressGenerator = new AddressGenerator<>(JsonAddress[].class);
        itemGenerator = new ItemGenerator<>(JsonItem[].class);
    }

    @Override
    public JsonInvoice getNextInvoice() {
        JsonInvoice invoice = invoices[getIndex()];
        int newInvoiceNumber = getNewInvoiceNumber();
        invoice.setInvoiceNumber(Integer.toString(newInvoiceNumber));
        invoice.setCreatedTime(System.currentTimeMillis());
        if (invoice.getDeliveryType().equalsIgnoreCase("HOME-DELIVERY")) {
            JsonAddress address = addressGenerator.getNextAddress();
            invoice.setAddress(address);
        }
        List<JsonItem> itemList = new ArrayList<>();
        int noOfItems = getNoOfItems();
        double totalAmount = 0.0;
        for (int i = 0; i < noOfItems; i++) {
            JsonItem nextItem = itemGenerator.getNextItem();
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
