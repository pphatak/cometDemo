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
        }
        else
        {
            dom.byId('status').innerHTML += '<div>CometD handshake failed</div>';
        }
    });

    dom.byId('pub').onclick = function()
    {
        cometd.publish('/service/feed/1', { messageContent: 'Hola!', channel:'/feed/1', sendTime: new Date().getTime()});
    };

    dom.byId('sub').onclick = function()
        {
          cometd.subscribe('/feed/1', function(message)
          {
              //var timeTaken = new Date().getTime();
              var timeTaken = new Date().getTime() - message.data.sendTime
              dom.byId('status').innerHTML += '<div>'+message.data.messageContent+' took '+ timeTaken + ' ms </div>';
          });
    };

    cometd.handshake();
});