package no.ssb.dc.content.provider.rawdata;

import no.ssb.dc.api.content.ContentStream;
import no.ssb.dc.api.content.ContentStreamProducer;
import no.ssb.rawdata.api.RawdataClient;
import no.ssb.rawdata.api.RawdataMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RawdataClientContentStream implements ContentStream {

    private final RawdataClient client;
    private final Map<String, ContentStreamProducer> producerMap = new ConcurrentHashMap<>();

    public RawdataClientContentStream(RawdataClient client) {
        this.client = client;
    }

    @Override
    public String lastPosition(String namespace) {
        RawdataMessage message = client.lastMessage(namespace);
        return message != null ? message.position() : null;
    }

    @Override
    public ContentStreamProducer producer(String namespace) {
        return producerMap.computeIfAbsent(namespace, p -> new RawdataClientContentStreamProducer(client.producer(namespace)));
    }

}