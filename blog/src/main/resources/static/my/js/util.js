/**
 * Created by 郭欣光 on 2018/10/12.
 */

$(document).ready(function () {
        try {
            $('#show-owner-qq-img').popover({
                content: $("#owner-qq-img-hidden").val()
            });
            $('#show-owner-weibo-img').popover({
                content: $("#owner-weibo-img-hidden").val()
            });
            $('#show-owner-email').popover({
                content: $("#owner-email-hidden").val()
            });
            $('#show-owner-weixin-img').popover({
                content: $("#owner-weixin-img-hidden").val()
            });
        } catch (err) {
            console.log(err);
        }
});

function openAlert(content) {
    $("#my-alert-content").html(content);
    $('#my-alert').modal();
}

function openLoginModel() {
    document.getElementById("check-code-img").src = "/check_code/get_check_code?imageId=" + Math.random();
    $('#login-model').modal({
        relatedTarget: this,
        onConfirm: function(e) {
            login();
        },
        onCancel: function(e) {
        }
    });
}

function changeCheckCode() {
    document.getElementById("check-code-img").src = "/check_code/get_check_code?imageId=" + Math.random();
}

function login() {
    var id = $("#user-id").val();
    var password = $("#user-password").val();
    var checkCode = $("#user-verification-code").val();

    if (id == null || id.length == 0 || id == "") {
        openAlert("账号不能为空！");
    } else if (password == null || password.length == 0 || password == "") {
        openAlert("密码不能为空！");
    } else if (checkCode == null || checkCode.length == 0 || checkCode == "") {
        openAlert("验证码不能为空！");
    } else if (id.length > 8) {
        openAlert("账号长度不能大于8个字符！");
    } else {
        var obj = new Object();
        obj.id = id;
        obj.password = password;
        obj.checkCode = checkCode;
        $.ajax({
            url: "/user/login",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: loginSuccess,
            error: openAjaxErrorAlert
        });
    }
}

function loginSuccess(data) {
    var result = JSON.parse(data);
    if (result.status.trim() == "true") {
        window.location.reload();
    } else {
        openAlert(result.content);
    }
}

function openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown) {
    if (XMLHttpRequest.status >= 400 && XMLHttpRequest.status < 500) {
        openAlert("客户端请求出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
    } else if (XMLHttpRequest.status >= 500 || XMLHttpRequest.status <= 600) {
        openAlert("服务器出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
    } else if (XMLHttpRequest.status >= 300 || XMLHttpRequest.status < 400) {
        openAlert("重定向问题！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
    } else {
        openAlert("抱歉，系统好像出现一些异常！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
    }
}

function signOut() {
    $.ajax({
        url: "/user/sign_out",
        type: "POST",
        cache: false,//设置不缓存
        success: signOutSuccess,
        error: openAjaxErrorAlert
    });
}

function signOutSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        openAlert(result.content);
    }
}

function openBlogSetModel() {
    $('#blog-set-model').modal();
}

function openBaseInformationModel() {
    $("#close-blog-set-model").click();
    $('#base-information-model').modal({
        relatedTarget: this,
        onConfirm: function(e) {
            setBaseInformation();
        },
        onCancel: function(e) {
        }
    });
}

function setBaseInformation() {
    var ownerName = $("#owner-name").val();
    var ownerIntroduction = $("#owner-introduction").val();
    var ownerGithub = $("#owner-github").val();
    var ownerEmail = $("#owner-email").val();
    if (ownerName == null || ownerName == "" || ownerName.length == 0) {
        openAlert("昵称不能为空！");
    } else if (ownerIntroduction == null || ownerIntroduction == "" || ownerIntroduction.length == 0) {
        openAlert("简介不能为空！");
    } else if (ownerGithub == null || ownerGithub == "" || ownerGithub.length == 0) {
        openAlert("Github不能为空！");
    } else if (ownerEmail == null || ownerEmail == "" || ownerEmail.length == 0) {
        openAlert("邮箱不能为空！");
    } else if (ownerName.length > 10) {
        openAlert("昵称长度不能超过10字符！");
    } else if (ownerIntroduction.length > 100) {
        openAlert("简介长度不能超过100字符！");
    } else if (ownerGithub.length > 100) {
        openAlert("Github长度不能超过100字符！");
    } else if (!isUrl(ownerGithub)) {
        openAlert("Github地址不是一个标准url地址！");
    } else if (ownerEmail.length > 100) {
        openAlert("邮箱长度不能超过100字符！");
    } else if (!isEmail(ownerEmail)) {
        openAlert("邮箱不是一个标准Email格式！");
    } else {
        var obj = new Object();
        obj.ownerName = ownerName;
        obj.ownerIntroduction = ownerIntroduction;
        obj.ownerGithub = ownerGithub;
        obj.ownerEmail = ownerEmail;
        $.ajax({
            url: "/blog/set_base_information",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: setBaseInformationSuccess,
            error: openAjaxErrorAlert
        });
    }
}

function setBaseInformationSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        openAlert(result.content);
    }
}

function isUrl (str) {
    var strRegex = '^((https|http|ftp|rtsp|mms)?://)'
        + '?(([0-9a-z_!~*\'().&=+$%-]+: )?[0-9a-z_!~*\'().&=+$%-]+@)?' //ftp的user@
        + '(([0-9]{1,3}.){3}[0-9]{1,3}' // IP形式的URL- 199.194.52.184
        + '|' // 允许IP和DOMAIN（域名）
        + '([0-9a-z_!~*\'()-]+.)*' // 域名- www.
        + '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z].' // 二级域名
        + '[a-z]{2,6})' // first level domain- .com or .museum
        + '(:[0-9]{1,4})?' // 端口- :80
        + '((/?)|' // a slash isn't required if there is no file name
        + '(/[0-9a-z_!~*\'().;?:@&=+$,%#-]+)+/?)$';
    var re=new RegExp(strRegex);
    return re.test(str);
}

function isEmail(str) {
    var reg= /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    return reg.test(str);
}