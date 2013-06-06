require(['dojox/cometd', 'dojo/dom', 'dojo/domReady!'], function(cometd, dom)
{
    cometd.configure({
        url: location.protocol + '//' + location.host + config.contextPath + '/cometd',
        logLevel: 'debug'
    });

    cometd.addListener('/meta/handshake', function(message)
    {
        if (message.successful)
        {
            dom.byId('status').innerHTML += '<div>CometD handshake successful</div>';
            cometd.subscribe('/echo/1', function(message)
                          {
                           dom.byId('body').innerHTML += '<div> Received sub </div>';
                         });
            cometd.subscribe('/feed/1', function(message)
                                                   {
                                                    dom.byId('body').innerHTML += '<div> Received sub </div>';
                                                  });
        }
        else
        {
            dom.byId('status').innerHTML += '<div>CometD handshake failed</div>';
        }
    });

    dom.byId('pub').onclick = function()
    {
        cometd.publish('/service/feed/1', { messageContent: 'Hola!', channel:'/feed/1' });
        cometd.publish('/service/echo', { messageContent: 'Echoo Hola!', channel:'/echo/1' });
    };

    dom.byId('sub').onclick = function()
        {
             cometd.subscribe('/feed/1', function(message)
              {
               dom.byId('body').innerHTML += '<div> Received sub </div>';
             });
    };

    cometd.handshake();
});