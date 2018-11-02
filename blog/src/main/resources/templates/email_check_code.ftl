<!DOCTYPE html>
<html>
<body>
<h4>${toUserName}，您好！</h4>

<p>很高兴您能够对本博客的内容提出您宝贵的建议，您本次的验证码为：<span style="color: blue">${articleCommentEmailCheckCode}</span>。</p>
<p><span style="color: red">该验证码有效期为30分钟，超时将失效，同时为了保证您的隐私安全，请不要泄露此验证码。</span></p>
<p>您的邮箱信息将不会被其他人看到，该邮箱仅用于对您提出的意见进行探讨</p>

祝：开心！
<br/>
${fromUserName}
<br/>
${time?date}

</body>
</html>