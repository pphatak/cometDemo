require(['dojox/cometd', 'dojo/dom', 'dojo/domReady!'], function(cometd, dom)
{
    cometd.configure({
        url: location.protocol + '//' + 'stagehammer-002:8080' + config.contextPath + '/cometd',
        logLevel: 'debug'
    });

    cometd.websocketEnabled = true;
    var timer_is_on=0;
    var t;
    var interval = 1000;

    cometd.addListener('/meta/handshake', function(message)
    {
        if (message.successful)
        {
            dom.byId('status').innerHTML += '<div>CometD handshake successful</div>';
             cometd.subscribe('/feed/*', function(message)
             {
              var timeTaken = new Date().getTime() - message.data.sendTime
              dom.byId('status').innerHTML += '<div>'+message.data.messageContent+' took '+ timeTaken + ' ms </div>';
             });
        }
        else
        {
            dom.byId('status').innerHTML += '<div>CometD handshake failed</div>';
        }
    });

    dom.byId('pub').onclick = function()
    {
         if (!timer_is_on)
         {
            timer_is_on=1;
            interval = document.getElementById('txt').value;
            timedPublish();
         }
    };

    dom.byId('stop').onclick = function()
    {
        dom.byId('status').innerHTML += '<div> stop publishing </div>';
        clearTimeout(t);
        timer_is_on=0;
    };

    function timedPublish()
    {
        cometd.publish('/service/feed/1', { messageContent: 'Hello - This is a service test message! How long would this take ?', channel:'/feed/1', sendTime: new Date().getTime()});
        t = setTimeout(function(){timedPublish()},interval);
    };

    cometd.handshake();
});