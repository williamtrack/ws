const router = require('express').Router();
module.exports = router;

const moment = require('moment');

//=======更新==========

//- [更新所有项目数据](http://36.7.158.99/daily/updatedata)
//- [更新所有KPI](http://36.7.158.99/daily/updatekpi)

//- [更新所有人员信息](http://36.7.158.99/daily/update?table=staff)
//- [更新所有项目担当](http://36.7.158.99/daily/update?table=principal)
//- [更新所有硬件项目](http://36.7.158.99/daily/update?table=hardware)
//- [更新所有项目日程](http://36.7.158.99/daily/update?table=schedule)

router.post('/update', function (req, res) {
    // console.log(req.body);

    // //传进来是非json数据
    // let data = JSON.parse(JSON.stringify(req.body));
    // let content = JSON.parse(data.data);

    //传进来是json数据
    try {
        let tableName = req.query.table;
        let data = req.body.data;
        // console.log(data);
        if (data === undefined) throw new Error('undefined');
        // let content = JSON.parse(data); //如果data是字符串则如此,如果已经是json则用下面
        let content = data;
        // console.log(content);
        for (let i = 0; i < content.length; i++) {
            // console.log(content[i].Date);
            // console.log(changeDate(content[i].Date));
            ifExisted(tableName, "Id", content[i].Id).then(() => {
                // resetIdAuto();
                update(tableName, content[i], "Id");
            }, () => {
                insert(tableName, content[i], "Id");
            });
        }
        res.send("succeed");
    } catch (e) {
        console.log(e);
        res.send('error 023');
    }
});


//========查询==========
//- [获取所有项目数据](http://36.7.158.99/daily/select?table=data)
//- [获取所有KPI](http://36.7.158.99/daily/select?table=kpi)
//- [获取所有人员信息](http://36.7.158.99/daily/select?table=staff)
//- [获取所有硬件项目](http://36.7.158.99/daily/select?table=hardware)
//- [获取所有项目担当](http://36.7.158.99/daily/select?table=principal)
//- [获取所有项目日程](http://36.7.158.99/daily/select?table=schedule)
//- [根据日期获取项目数据](http://36.7.158.99/daily/selectbydate?table=data&start=2021-11-1&end=2021-11-4)
//- [根据日期获取KPI](http://36.7.158.99/daily/selectbydate?table=kpi&start=2021-11-1&end=2021-11-4)

router.get('/select', function (req, res) {
    let sqlCmd = "SELECT * FROM " + req.query.table;
    selectData(sqlCmd, res);
});

router.get('/selectsort', function (req, res) {
    let sqlCmd = "SELECT * FROM " + req.query.table + " ORDER BY Date ASC";
    selectData(sqlCmd, res);
});


router.get('/selectbydate', function (req, res) {
    // let sqlCmd = "SELECT * FROM staff WHERE BINARY id=" + "'" + req.query.date + "'";
    // let sqlCmd = "SELECT * FROM data WHERE date between \'2021-11-01\' and \'2021-11-02\'";
    let sqlCmd = "SELECT * FROM " + req.query.table + " WHERE Date BETWEEN " + "'" + req.query.start + "'" + " AND " + "'" + req.query.end + "'" + " ORDER BY Date ASC";
    selectData(sqlCmd, res);
});


//====================工具=========

//====查询表中的所有数据===
//查询是否存在
function selectData(sqlCmd, res) {
    sqlQueryD.query(sqlCmd).then((response) => {
            //发送处理后的日期格式
            let len = response.length;
            for (let i = 0; i < len; i++) {
                for (let key in response[i]) {
                    if (key.substring(0, 4) === 'Date') {
                        response[i][key] = changeDateS(response[i][key]);
                    }
                }
            }
            res.send(response)
        }, () => {
            res.end('fail');
            console.log('fetch err.');
        }
    );
}

//====新增表中的数据====
function insert(tableName, json, index) {
    //'INSERT INTO eyeHistory (id,childId,leftEye,rightEye,addTime) VALUES(0,?,?,?,?)'
    let cmdParams = key2array(tableName, json, index);
    let sqlParams = value2array(json, index);
    let len = cmdParams.length;
    let str = "INSERT INTO " + cmdParams[0] + " (";
    let str2 = '';
    for (let i = 1; i < len; i++) {
        str = str + cmdParams[i] + ","
        str2 = str2 + '?,';
    }
    let sqlCmd = str.substring(0, str.length - 1) + " ) VALUES (" + str2.substring(0, str2.length - 1) + ")";
    console.log(sqlCmd);
    // console.log(sqlParams);
    sqlQueryD.query(sqlCmd, sqlParams).then((response) => {
        // console.log(response);
        //res.send(response);
    }, (err) => {
        //res.end();
        console.log(err, 'update err.');
    });
}

function insertAuto(tableName, json, index) {
    //'INSERT INTO eyeHistory (id,childId,leftEye,rightEye,addTime) VALUES(0,?,?,?,?)'
    let cmdParams = key2array(tableName, json, index);
    let sqlParams = value2array(json, index);
    let len = cmdParams.length;
    let str = "INSERT INTO " + cmdParams[0] + " (";
    let str2 = '';
    for (let i = 1; i < len; i++) {
        str = str + cmdParams[i] + ","
        str2 = str2 + '?,';
    }
    let sqlCmd = str.substring(0, str.length - 1) + " ) VALUES (" + str2.substring(0, str2.length - 2) + "0)";
    console.log(sqlCmd);
    console.log(sqlParams);
    sqlQueryD.query(sqlCmd, sqlParams).then((response) => {
        // console.log(response);
        //res.send(response);
    }, (err) => {
        //res.end();
        console.log(err, 'update err.');
    });
}

//===更新表中的数据===
//自动创建查询语句 并查询
function update(tableName, json, index) {
    let cmdParams = key2array(tableName, json, index);
    let sqlParams = value2array(json, index);
    let len = cmdParams.length;
    let str = "UPDATE " + cmdParams[0] + " SET ";
    for (let i = 1; i < len - 1; i++) {
        str = str + cmdParams[i] + "=?,"
    }
    let sqlCmd = str.substring(0, str.length - 1) + " WHERE " + cmdParams[len - 1] + " =?";
    console.log(sqlCmd);
    // console.log(sqlParams);
    sqlQueryD.query(sqlCmd, sqlParams).then((response) => {
        // console.log(response);
        //res.send(response);
    }, (err) => {
        //res.end();
        console.log(err, 'update err.');
    });
}

//判断指定id是否存在数据
function ifExisted(tableName, listName, listValue) {
    return new Promise(
        function (resolve, reject) {
            let sqlCmd = "SELECT * FROM " + tableName + " WHERE BINARY " + listName + " = " + listValue;
            sqlQueryD.query(sqlCmd).then((response) => {
                    // console.log(response.length);
                    if (response.length >= 1) {
                        resolve(response);
                    } else reject();
                }, () => {
                    reject();
                }
            );
        }
    )
}

//将json键名值转为数组
function key2array(tableName, json, index) {
    let array = [];
    array.push(tableName);
    for (let key in json) {
        if (key != index) array.push(key);
    }
    array.push(index);
    return array;
}

//将json键值值转为数组
function value2array(json, index) {
    let array = [];
    for (let key in json) {
        if (key.substring(0, 4) === "Date") array.push(changeDate(json[key])); //把Date开头的键名特殊处理
        else if (key != index) array.push(json[key]);
    }
    array.push(json[index]);
    return array;
}

//====工具函数====
//UTC时间根据时区自动转换
function changeDate(date) {
    return moment(date).format('YYYY-MM-DD hh:mm:ss'); //2021-10-31T17:48:05.000Z -> 2021-11-01 01:48:05
}

function changeDateS(date) {
    return moment(date).format('YYYY-MM-DD'); //2021-10-31T17:48:05.000Z -> 2021-11-01
}

function resetIdAuto() {
    let sqlCmd = "ALTER TABLE staff AUTO_INCREMENT = 0; "
    sqlQueryD.query(sqlCmd).then((response) => {
        console.log(response);
    }, (err) => {
        console.log(err, 'update err.');
    });
}