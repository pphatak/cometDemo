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
        addService("/service/feed/*", "processMessage");
    }

    public void processMessage(ServerSession remote, ServerMessage message) {
        Map<String, Object> data = message.getDataAsMap();
        String channelName = (String) data.get("channel");
        System.out.printf("Received message '%s' on channel %s \n", data.get("messageContent"), channelName);

        // Initialize the channel, making it persistent and lazy
        getBayeux().createIfAbsent(channelName, new ConfigurableServerChannel.Initializer() {
            public void configureChannel(ConfigurableServerChannel channel) {
                channel.setPersistent(true);
                channel.setLazy(true);
            }
        });

        // Publish to all subscribers
        ServerChannel channel = getBayeux().getChannel(channelName);
        channel.publish(getLocalSession(), message, null);
    }
}