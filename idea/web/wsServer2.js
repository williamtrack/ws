const WebSocket = require('ws');
const url = require('url');
global.wsDemo = {};

function heartbeat() {
    this.isAlive = true;
}

module.exports = function (e) {
    const ws = new WebSocket.Server(e);
    ws.on('connection', function connection(wsClient, req) {
        const pathname = url.parse(req.url, true).pathname;
        const deviceid = url.parse(req.url, true).query.deviceid;
        const toId = url.parse(req.url, true).query.toId;
        console.log('device id is:' + deviceid);

        // if (pathname === '/wsServer') {
        if (true) {
            wsClient.isAlive = true;
            wsClient.on('pong', heartbeat);

            wsDemo[deviceid] = wsClient;
            wsClient.on('message', function incoming(msg) {
                console.log(msg);
                let json_data = {
                    'para1': 1,
                    'para2': ["array1", "array2"],
                    'para3': {'child1': 'Mike', 'child2': 'John'}
                }
                wsDemo[deviceid].send(JSON.stringify(json_data))
                if (deviceid !== toId && wsDemo[toId]) {
                    wsDemo[toId].send(msg);
                    console.log("send msg to", toId)
                }
            });

            wsClient.on('close', function close() {
                console.log('\x1B[34mWS \x1B[39m%s is closed!', deviceid);
                if (wsDemo[toId]) {
                    wsDemo[toId].send('isOnNo');
                }
                delete (wsDemo[deviceid]);
            });
        }
    });
};