cometDemo
=========

Lightweight server to publish and subscribe

To run:
1. cd cometDemo
2. mvn install jetty:run
3. Open localhost:8080 and hit publish ... you should see the message at the server and on the browser
* This currently publishes to stagehammer-002, to publish to the local server change it to localhost in application.js

Message fields :
messageContent - content of the message being delivered (try to keep this the size of p95 online now length)
sendTime - UTC timestamp of the send time (this will be used for benchmarking)
channel - channel to broadcast to (required when publishing to a service channel)

Publish on broadcast channel
cometd.publish('/feed/1', { messageContent: 'Hello', channel:'/feed/1', sendTime: new Date().getTime()});

Publish on service channel
cometd.publish('/service/feed/1', { messageContent: 'Hello', channel:'/feed/1', sendTime: new Date().getTime()});