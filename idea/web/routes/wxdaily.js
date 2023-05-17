const router = require('express').Router();
module.exports = router;

const request = require('request');
const qs = require('querystring');

const data = {
    corpid:'wwa6645eb049e8f375',
    corpsecret:'NL3t9XPge9SSZbfSPMCK8MJOM7GZS2ZQF_6lq422QUU'}; //公司
let access_token = null;

const content = qs.stringify(data); //json转URL字符串

router.get('/',function (req,res) {

    //第一步: 发送get请求
    let url ='https://qyapi.weixin.qq.com/cgi-bin/gettoken?' + content
    request(url, function (error, response, body) {
        access_token = JSON.parse(body)['access_token']; //字符串转json
        // console.log(body)
        // 第二步:根据token发送post
        let access_token_data = { access_token: access_token };
        let contents = qs.stringify(access_token_data); //json转URL字符串
        let contents_body = {
            // 'starttime': StartTime,  //'1626192000' StartTime.toString()
            // 'endtime': CurrentTime,   //'1626278400'  CurrentTime.toString()
            'starttime': 1639423652.3,  //'1626192000' StartTime.toString()
            'endtime': 1639466852.3,   //'1626278400'  CurrentTime.toString()
            'cursor': '0',
            'limit': '50',
            'filters':[
                {
                    'key': 'template_id',
                    'value': '3WK7BMTd7YyBdUBY7dvhMNu5Zqx1mC6tkzvVWKLy'  //BsAd6tPgWU6idTZLsqYhLR6KfwvdmaDtcZFHxepuT
                }
            ]
        }
        let post_data = JSON.stringify(contents_body);
        url = 'https://qyapi.weixin.qq.com/cgi-bin/oa/journal/get_record_list?' + contents;
        request({
            url: url,
            method: "POST",
            json: true,
            headers: {
                //Accept: '*/*',
                //AcceptEncoding: 'UTF-8',
                //AcceptLanguage: 'zh-CN',
                //Connection: 'keep-alive',
                'Content-Type':'application/json; charset=UTF-8',  //x-www.js-form-urlencoded
                'Content-Length':post_data.length
            },
            body:contents_body
        }, function(error, response, body) {
            response.setEncoding('utf-8');
            // 第三步
            url ='https://qyapi.weixin.qq.com/cgi-bin/oa/journal/get_record_detail?'+contents;
            let contents_id = {
                'journaluuid': body.journaluuid_list[0]
            }
            request({
                url: url,
                method: "POST",
                json: true,
                headers: {
                    'Content-Type':'application/json; charset=UTF-8',  //x-www.js-form-urlencoded
                    // 'Content-Length':post_data.length
                },
                body:contents_id
            }, function(error, response, body) {
                // console.log(response.body)
                console.log((JSON.stringify(body.info)).length)
            });
        });
        res.end('ok')
    });

});