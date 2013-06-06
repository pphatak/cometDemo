package org.cometd.tutorials;

import org.cometd.annotation.Service;
import org.cometd.bayeux.server.*;
import org.cometd.server.AbstractService;

import javax.servlet.ServletContext;

@Service
public class ProcessMessageService extends AbstractService {

    public ProcessMessageService(ServletContext context) {
        super((BayeuxServer) context.getAttribute(BayeuxServer.ATTRIBUTE), "chat");
        addService("/feed/*", "processMessage");
    }

    public void processMessage(ServerSession remote, ServerMessage message) {
        String channelName = message.getChannel();

        // Initialize the channel, making it persistent and lazy
        getBayeux().createIfAbsent(channelName, new ConfigurableServerChannel.Initializer() {
            public void configureChannel(ConfigurableServerChannel channel) {
                channel.setPersistent(true);
                channel.setLazy(true);
            }
        });

        // Publish to all subscribers
        ServerChannel channel = getBayeux().getChannel(channelName);
        channel.publish(getLocalSession(), message.getDataAsMap(), null);
    }
}