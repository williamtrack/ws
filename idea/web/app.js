const express = require('express');
const app = express();
require('./constant');

const createError = require('http-errors');
const path = require('path');
const cookieParser = require('cookie-parser');
const morgan = require('morgan');
const SqlQuery = require('./utils/sqlQuery');
const SqlQueryD = require('./utils/sqlQueryDaily');
sqlQuery = new SqlQuery();
sqlQueryD = new SqlQueryD();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');

app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser());
// app.use(express.static(path.join(__dirname, '/public')));
// app.use(express.static(__dirname + '/public/images'));
app.use(express.static(__dirname + '/pages/public'));
app.use(express.static(__dirname + '/pages/public/img'));
app.use('/static', express.static(__dirname + '/pages'));

//访问/public/video.mp4,直接在浏览器中输入http://127.0.0.1/video.mp4 即可（也可重定向），即可在线预览

// morgan.token('localDate',function(req) {
//     return;
// });
app.use(morgan('\x1B[34m:status \x1B[39m::remote-addr :method :url \x1B[36m:res[content-length]B \x1B[39m:response-time ms'));

//路由
app.use('/', function (req, res, next) {
    if (req.url === '/') return res.redirect(301, global.serverUrl + '/index');
    next();//加上next表示如果links后面还有东西，则跳出进行下一步，否则卡死
});
// app.use('/index', require('./routes/index'));
app.use('/index', function (req, res, next) { // 只能是../index; ../index/others不行
    if (req.url === '/') return res.redirect(301, global.serverUrl + '/static/login/index.html');
    next();
});
app.use('/rtmp', function (req, res) { // ../rtmp或者../rtmp/others 都行
    return res.redirect(301, global.serverUrl + '/static/rtmp/index.html');
});
app.use('/play', function (req, res) {
    return res.redirect(301, global.serverUrl + '/static/player/index.html');
});
app.use('/login', function (req, res) {
    return res.redirect(301, global.serverUrl + '/static/login/index.html');
});
app.use('/vrhouse', function (req, res) {
    return res.redirect(301, global.serverUrl + '/static/vrHouse/index.html');
});
app.use('/version', require('./routes/version'));
app.use('/unity', require('./routes/unity'));
app.use('/test', require('./routes/test'));
app.use('/f', require('./routes/feature'));
app.use('/daily',require('./routes/daily')); //公司日报系统
app.use('/wxdaily',require('./routes/wxdaily')); //企业微信日报

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    // next(createError(404));
    next();
});

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('error');
});

module.exports = app;
