const router = require('express').Router();
module.exports = router;

const multer = require('multer');
const fs = require('fs');
const request = require('request');
const qs = require('querystring');

// const java =require('java');
// java.classpath.push('./facelib.jar');

//响应get请求
router.get('/', function (req, res) {
    console.log(req.query.id)
    console.log(req.baseUrl)
    let data = {};
    data.id = '001'
    data.state = 'get-ok'
    res.writeHead(200, {'Content-Type': 'application/json'})
    // res.writeHead(200, {'Content-Type': 'text/plain; charset=utf-8'})
    // res.end("get-ok")
    res.end(JSON.stringify(data))
})

//响应post请求
router.post('/', function (req, res, next) {
    console.log(req.body);
    console.log(req.headers);
    // console.log(req.data);
    res.end('post-ok');
})

//string-url-json互转
// router.get('/',function (req,res) {
//     let json_data = {
//         'para1': 1,
//         'para2': [2,3],
//         'para3': {'child1': '4', 'child2': '5'}
//     }
//     let str_data ="{\"a\":1,\"b\":2}"
//     const a00 = qs.stringify(json_data); //json转URL字符串,无法识别子类json
//     const a01 = JSON.stringify(json_data); //JSON转字符串
//     const a02 = JSON.parse(str_data); //字符串转json
//     console.log(a00)
//     console.log(a01)
//     console.log(a02['a'])
//     console.log(a02.b)
//     res.end('ok')
// })

// //主动发送get和post请求
// router.get('/', function (req, res) {
//     // //发送get请求
//     // let url ='http://192.168.6.155/test/read'
//     // request(url, function (error, response, body) {
//     //     console.log(body)
//     //     res.end(body)
//     // });
//
//     //发送post请求
//     let url = 'http://192.168.6.155/test'
//     let post_body = {
//         'para1': 1,
//         'para2': [2,3],
//         'para3': {'child1': '中文', 'child2': '5'}
//     }
//     request({
//         url: url,
//         method: "POST",
//         json: true,
//         headers: {
//             'content-type': 'application/json; charset=UTF-8',
//             'acceptLanguage': 'zh-CN', //识别中文
//             // 'content-length': JSON.stringify(post_body).length
//         },
//         body:post_body
//     }, function (error, response, body) {
//         // console.log(response.statusCode)
//         // console.log(body)
//         // if (!error && response.statusCode == 200) {
//         //     console.log(body) // 请求成功的处理逻辑
//         // }
//         res.end(body)
//     });
// });

//向客户发送文本
router.get('/read', function (req, res) {
    //因为启动node时在根目录，所以不能以test.js为参考用../
    fs.readFile('./pages/public/test.txt', function (err, data) {
        if (err) {
            console.log(err)
            res.end('error');
        } else {
            //文本必须要编码utf-8，否则不能识别中文；mp4格式会自动下载，而不是网页播放
            res.writeHead(200, {'Content-Type': 'text/plain; charset=utf-8'})
            // res.writeHead(200,{'Content-Type':'image/jpg'})
            res.end(data)
        }
    });
});

//向客户发送文件
router.get('/getFile', function (req, res) {
    // let form = fs.readFileSync('./res/upload.html', {encoding: 'utf8'});
    // res.send(form);
    fs.readFile('./upload/app/picture2', function (err, data) {
        if (err) {
            console.log(err)
            res.end('error')
        } else {
            // res.writeHead(200,{'Content-Type':'image/jpeg'});
            // res.writeHead(200, {'Content-Type': 'text/plain; charset=utf-8'}) //IE默认
            res.end(data) //浏览器直接解析,如果chrome解析异常则下载
            // res.send(data) //浏览器直接下载,不能加head
        }
    });
});

//创建文件夹
let uploadFolder = __dirname + '/../upload';
let createFolder = function (folder) {
    try {
        fs.accessSync(folder);
    } catch (e) {
        fs.mkdirSync(folder);
    }
};
createFolder(uploadFolder);

// 通过 filename 属性定制
let storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, uploadFolder);    // 保存的路径，备注：需要自己创建
    },
    filename: function (req, file, cb) {
        // 将保存文件名设置为 字段名 + 时间戳，比如 logo-1478521468943
        // cb(null, file.fieldname + '-' + Date.now()+'.jpg');
        // console.log(file);
        cb(null, file.fieldname + '-' + Date.now()); //.fieldname 键名 /.originalname键值
    }
});

// 通过 storage 选项来对 上传行为 进行定制化
const upload = multer({storage: storage});
const uploads = multer({storage: storage}).array('file');

// Multipart/form-data
router.post('/upload', upload.single('file'), function (req, res) {
    res.send('successfully upload!');
});

// Multipart/form-data
router.post('/uploads', function (req, res, next) {
    uploads(req, res, function (err) {
        if (err) {
            console.error('1.[System] ' + err.message);
            res.end('上传失败');
        } else {
            //循环处理
            let imgPath = [];
            req.files.forEach(function (i) {
                //获取临时文件的存储路径
                imgPath.push(i.path);
                console.log("i.path:", i.path)
            });

            //所有文件上传成功
            //回复信息
            let response = {
                message: 'Files uploaded successfully',
                imgPath
            };
            //返回
            res.end(JSON.stringify(response));
        }
    });
});