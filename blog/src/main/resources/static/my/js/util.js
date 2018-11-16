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


function openOtherInformationModel() {
    $("#close-blog-set-model").click();
    $('#other-information-model').modal({
        relatedTarget: this,
        onConfirm: function(e) {
            setOtherInformation();
        },
        onCancel: function(e) {
        }
    });
}

function setOtherInformation() {
    var informationName = $('#informationName option:selected').val();
    var file =  document.getElementById('uploadBlogImage').files[0];
    var filePath = $('#uploadBlogImage').val().toLowerCase().split(".");
    var fileType =  filePath[filePath.length - 1];
    if (informationName == null || informationName == "" || informationName.length == 0) {
        openAlert("请求不符合要求！");
    } else if (informationName != "logo-ico" && informationName != "logo" && informationName != "head-img" && informationName != "my-qq" && informationName != "my-weibo" && informationName != "my-weixin") {
        openAlert("请求不符合要求！");
    }else if (file == null) {
        openAlert("请选择要上传的图片！");
    } else if (fileType != "bmf" && fileType != "png" && fileType != "gif" && fileType != "jpg" && fileType != "jpeg") {
        openAlert("上传的文件必须为图片类型！");
    } else if (file.size > 50 * 1024 * 1024) {
        //file.size 以字节为单位
        openAlert("图片大小不能超过50MB！");
    } else {
        var formData = new FormData($('#other-information-form')[0]);
        $("#upload-file-progress").html("0.00%");
        $("#upload-file-progress").css("width", "0.00%");
        $('#upload-file-progress-model').modal({
            relatedTarget: this,
            closeViaDimmer: false
        });
        var url = "/blog/set_other_information";
        // XMLHttpRequest 对象
        var xhr = new XMLHttpRequest();
        //true为异步处理
        xhr.open('post', url, true);
        //上传开始执行方法
        xhr.onloadstart = function() {
            console.log('开始上传');
        };
        xhr.upload.addEventListener('progress', uploadBlogImageProgressFunction, false);
        xhr.addEventListener("load", uploadBlogImageComplete, false);
        xhr.addEventListener("error", uploadBlogImageFailed, false);
        xhr.send(formData);
    }
}

function uploadBlogImageProgressFunction(evt) {
    // debugger;
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)
            + '%';
        $("#upload-file-progress").html(completePercent);
        $("#upload-file-progress").css("width", completePercent);
    }
}

function uploadBlogImageComplete(evt) {
    var result = JSON.parse(evt.target.responseText);
    if (result.status == "true") {
        window.location.reload();
    } else {
        $("#close-upload-file-process-model").click();
        if (result.hasOwnProperty("message")) {
            openAlert("上传失败！（错误码：" + result.status + "， 错误类型：" + result.error + ")");
        } else {
            openAlert(result.content);
        }
    }
}

function uploadBlogImageFailed(evt) {
    $("#close-upload-file-process-model").click();
    console.log(evt.target.responseText);
    openAlert("上传失败！原因：" + evt.target.responseText);
}

function openResetPassword() {
    $('#reset-password-model').modal({
        relatedTarget: this,
        onConfirm: function(e) {
            resetPassword();
        },
        onCancel: function(e) {
        }
    });
}

function resetPassword() {
    var oldPassword = $("#old-password").val();
    var newPassword = $("#new-password").val();
    var repeatNewPassword = $("#repeat-new-password").val();
    if (oldPassword == null || oldPassword == "" || oldPassword.length == 0) {
        openAlert("原密码不能为空！");
    } else if (newPassword == null || newPassword == "" || newPassword.length == 0) {
        openAlert("新密码不能为空！");
    } else if (repeatNewPassword == newPassword) {
        var obj = new Object();
        obj.oldPassword = oldPassword;
        obj.newPassword = newPassword;
        obj.repeatNewPassword = repeatNewPassword;
        $.ajax({
            url: "/user/password/reset",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: resetPasswordSuccess,
            error: openAjaxErrorAlert
        });
    } else {
        openAjaxErrorAlert("两次密码不一致！");
    }
}

function resetPasswordSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        openAlert(result.content);
    }
}

function openAddUserModel() {
    $('#add-user-model').modal({
        relatedTarget: this,
        onConfirm: function(e) {
            addUser();
        },
        onCancel: function(e) {
        }
    });
}

function addUser() {
    var addUserId = $("#add-user-id").val();
    var addUserPassword = $("#add-user-password").val();
    var addUserRepeatPassword = $("#add-user-repeat-password").val();
    if (addUserId == null || addUserId == "" || addUserId.length == 0) {
        openAlert("账号不能为空！");
    } else if (addUserPassword == null || addUserPassword == "" || addUserPassword.length == 0) {
        openAlert("密码不能为空！");
    } else if (addUserId.length > 8) {
        openAlert("账号长度不能超过8个字符！");
    } else if (addUserPassword == addUserRepeatPassword) {
        var obj = new Object();
        obj.addUserId = addUserId;
        obj.addUserPassword = addUserPassword;
        obj.addUserRepeatPassword = addUserRepeatPassword;
        $.ajax({
            url: "/user/add",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: addUserSuccess,
            error: openAjaxErrorAlert
        });
    } else {
        openAddUserModel("两次密码不一致！");
    }
}

function addUserSuccess(data) {
    var result = JSON.parse(data);
    openAlert(result.content);
}

function openManageUserModel() {
    $('#manage-user-model').modal();
}

function openDeleteUserModel() {
    $("#close-manage-user-model").click();
    openLoadingModel("正在加载，请稍后...");
    $.ajax({
        url: "/user/list",
        type: "GET",
        cache: false,//设置不缓存
        success: function (data) {
            var result = JSON.parse(data);
            if (result.status == "true") {
                var str = "";
                for (var i = 0; i < result.content.length; i++) {
                    var user = result.content[i];
                    str += '<option value="' + user.id + '">' + user.id + '</option>';
                }
                $("#delete-user-list").html(str);
                closeLoadingModel();
                $('#delete-user-model').modal({
                    relatedTarget: this,
                    onConfirm: function(e) {
                        deleteUser();
                    },
                    onCancel: function(e) {
                    }
                });
            } else {
                closeLoadingModel();
                openAlert(result.content);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            closeLoadingModel();
            openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
        }
    });
}

function deleteUser() {
    var userId = $("#delete-user-list option:selected").val();
    var obj = new Object();
    obj.userId = userId;
    $.ajax({
        url: "/user/delete",
        type: "POST",
        cache: false,//设置不缓存
        data: obj,
        success: deleteUserSuccess,
        error: openAjaxErrorAlert
    });
}

function deleteUserSuccess(data) {
    var result = JSON.parse(data);
    openAlert(result.content);
}