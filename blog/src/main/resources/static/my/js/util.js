/**
 * Created by 郭欣光 on 2018/10/12.
 */

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