package io.nexdata.realtime.data.idatagen;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nexdata.realtime.model.avro.AvroItem;
import io.nexdata.realtime.model.json.JsonItem;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Random;

public class ItemGenerator<T> {

    private final Random random;
    private final T[] items;
    private final Random qty;

    private int getIndex() {
        return random.nextInt(100);
    }

    private int getQuantity() {
        return qty.nextInt(2) + 1;
    }

    public ItemGenerator(Class<T[]> clazz) {
        final String DATAFILE = "src/main/resources/data/item.json";
        final ObjectMapper mapper= new ObjectMapper();
        this.random = new Random();
        this.qty = new Random();
        try {
            items = mapper.readValue(new File(DATAFILE), clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T getNextItem() {
        T item = items[getIndex()];
        if (item instanceof JsonItem) {
            JsonItem jsonItem = (JsonItem) item;
            jsonItem.setItemQty(getQuantity());
            jsonItem.setTotalValue(jsonItem.getItemPrice() * jsonItem.getItemQty());
            return (T) jsonItem;
        } else if (item instanceof AvroItem) {
            AvroItem avroItem = (AvroItem) item;
            avroItem.setItemQty(getQuantity());
            avroItem.setTotalValue(avroItem.getItemPrice() * avroItem.getItemQty());
            return (T) avroItem;
        }
        return null;
    }
}
