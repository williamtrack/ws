var crypto = require('crypto');

function WXBizDataCrypt(appId, sessionKey) {
  this.appId = 'wx2096a9d342339ba9';
  this.sessionKey = '507c780aa28a5484bec695e4bbb29809'
}

WXBizDataCrypt.prototype.decryptData = function (encryptedData, iv) {
  // const buf1 = Buffer.from('507c780aa28a5484bec695e4bbb29809','base64');
  // console.log(buf1.toString('base64'));

  var sessionKey = Buffer.from(this.sessionKey, 'base64');
  encryptedData = Buffer.from(encryptedData, 'base64');
  iv = Buffer.from(iv, 'base64');

  try {
     // 解密
    // console.log(sessionKey)
    let decipher = crypto.createDecipheriv('aes-128-cbc', sessionKey, iv);
    // 设置自动 padding 为 true，删除填充补位
    console.log('未绑定在微信开放平台上，执行到上一步报错');
    decipher.setAutoPadding(true);
    var decoded = decipher.update(encryptedData, 'binary', 'utf8');
    decoded += decipher.final('utf8');
    
    decoded = JSON.parse(decoded)

  } catch (err) {
    throw new Error('Illegal Buffer')
  }

  if (decoded.watermark.appid !== this.appId) {
    throw new Error('Illegal Buffer')
  }

  return decoded
};

module.exports = WXBizDataCrypt;
