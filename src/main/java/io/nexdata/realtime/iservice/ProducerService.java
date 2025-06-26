package io.nexdata.realtime.iservice;

public interface ProducerService<I> {

    void sendMessage(I invoice);
}
