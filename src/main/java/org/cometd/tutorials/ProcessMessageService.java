package org.cometd.tutorials;

import org.cometd.annotation.Service;
import org.cometd.bayeux.server.*;
import org.cometd.server.AbstractService;

import javax.servlet.ServletContext;
import java.util.Map;

@Service
public class ProcessMessageService extends AbstractService {

    public ProcessMessageService(ServletContext context) {
        super((BayeuxServer) context.getAttribute(BayeuxServer.ATTRIBUTE), "chat");
        addService("/feed/*", "processMessage");
        addService("/service/feed/*", "processServiceMessage");
    }

    public void processMessage(ServerSession remote, ServerMessage message) {
        System.out.println("Received data :"+message.getDataAsMap());

        // Initialize the channel, making it persistent and lazy
        getBayeux().createIfAbsent(message.getChannel(), new ConfigurableServerChannel.Initializer() {
            public void configureChannel(ConfigurableServerChannel channel) {
                channel.setPersistent(true);
            }
        });

        // Publish to all subscribers
        ServerChannel channel = getBayeux().getChannel(message.getChannel());
        channel.publish(getLocalSession(), message.getDataAsMap(), null);
    }

    public void processServiceMessage(ServerSession remote, ServerMessage message) {
        Map<String, Object> data = message.getDataAsMap();
        String channelName = (String) data.get("channel");
        System.out.print("Received message :\t");
        for(Map.Entry<String, Object> entry: data.entrySet()){
            System.out.print(entry.getKey() + ":" + entry.getValue()+"\t");
        }
        System.out.println("");

        // Initialize the channel, making it persistent and lazy
        getBayeux().createIfAbsent(channelName, new ConfigurableServerChannel.Initializer() {
            public void configureChannel(ConfigurableServerChannel channel) {
                channel.setPersistent(true);
            }
        });

        // Publish to all subscribers
        ServerChannel channel = getBayeux().getChannel(channelName);
        channel.publish(getLocalSession(), data, null);
    }
}