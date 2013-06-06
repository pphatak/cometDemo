cometDemo
=========

Lightweight server to publish and subscribe

To run:
1. cd cometDemo
2. mvn install jetty:run
3. Open localhost:8080 and hit publish ... you should see the message at the server
   example publish message :
   cometd.publish('/service/feed/1', { messageContent: 'Hola!', channel:'/feed/1' });

