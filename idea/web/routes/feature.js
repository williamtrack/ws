const router = require('express').Router();
module.exports = router;
require('../constant');

router.get('/vrhouse', function (req, res) {
    return res.redirect(301, global.serverUrl+'/static/vrHouse/index.html');
});

router.get('/rtmp', function (req, res) {
    if (req.url === '/rtmp') return res.redirect(301, global.serverUrl+'/static/rtmp/index.html');
});