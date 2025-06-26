package io.nexdata.realtime.data.idatagen;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nexdata.realtime.model.json.JsonAddress;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Random;

public class AddressGenerator<T> {

    private final Random random;
    private final T[] addresses;

    public AddressGenerator(Class<T[]> clazz) {
        final String DATAFILE = "src/main/resources/data/address.json";
        final ObjectMapper mapper= new ObjectMapper();
        this.random = new Random();
        try {
            this.addresses = mapper.readValue(new File(DATAFILE), clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private int getIndex() {
        return random.nextInt(100);
    }

    public T getNextAddress(){
        return addresses[getIndex()];
    }
}
