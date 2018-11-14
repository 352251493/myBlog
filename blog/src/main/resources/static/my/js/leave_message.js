/**
 * Created by 郭欣光 on 2018/11/13.
 */

var messagePage = 0;

function getMessage() {
    if (messagePage > 0) {
        $("#load-more-message-button").attr("disabled","disabled");
        var str = '<i class="am-icon-spinner am-icon-spin"></i>正在加载...';
        $("#load-more-message-button").html(str);
    }
    messagePage = messagePage + 1;
    var obj = new Object();
    obj.messagePage = messagePage;
    $.ajax({
        url: "/message/list/",
        type: "POST",
        cache: false,//设置不缓存
        data: obj,
        success: getMessageSuccess,
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log("请求出错：" + XMLHttpRequest + textStatus + errorThrown);
            messagePage = messagePage -1;
            if (messagePage > 0) {
                // $("#article-comment-list").append(str);
                $("#load-more-message-button").html("点击加载更多");
                $("#load-more-message-button").removeAttr("disabled");
            }
        }
    });
}

function getMessageSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        if (messagePage == 1) {
            if (result.isHasMessage == "true") {
                var str = "";
                str += '<ul class="am-comments-list am-comments-list-flip" id="leave-message-list-content">';
                str += messageListStrProcess(result);
                str += '</ul>';
                $("#leave-message-list").html(str);
                str = '<button type="button" class="am-btn am-btn-default am-btn-block" id="load-more-message-button" onclick="getMessage();">点击加载更多</button>';
                $("#show-load-more-message-button").html(str);
            } else {
                console.log(result.content);
            }
        } else {
            if (result.isHasMessage == "true") {
                var str = messageListStrProcess(result);
                $("#leave-message-list-content").append(str);
                $("#load-more-message-button").html("点击加载更多");
                $("#load-more-message-button").removeAttr("disabled");
            } else {
                $("#load-more-message-button").html("没有更多啦~");
            }
        }
    } else {
        console.log(result.content);
        messagePage = messagePage - 1;
        if (messagePage > 0) {
            // $("#article-comment-list").append(str);
            $("#load-more-message-button").html("点击加载更多");
            $("#load-more-message-button").removeAttr("disabled");
        }
    }
}

function messageListStrProcess(result) {
    var str = "";
    for (var i = 0; i < result.content.length; i++) {
        var leaveMessage = result.content[i];
        str += '<li class="am-comment">';
        str += '<img src="' + leaveMessage.headImg + '" alt="" class="am-comment-avatar" width="48" height="48"/>';
        str += '<div class="am-comment-main">';
        str += '<header class="am-comment-hd">';
        str += '<div class="am-comment-meta am-cf">';
        str += '<span class="am-comment-author">' + leaveMessage.name + '</span>\n';
        str += '发表于 <time>' + leaveMessage.createTime.split(".")[0] + '</time>\n';
        if (result.isUser == "true") {
            str += "<a class=\"am-fr\" href=\"javascript:deleteLeaveMessage('" + leaveMessage.id + "');\">删除</a>";
        }
        str += '</div>';
        str += '</header>';
        str += '<div class="am-comment-bd">';
        str += leaveMessage.comment;
        str += '</div>';
        if (result.isUser == "true") {
            str += '<blockquote>Email:' + leaveMessage.email + '</blockquote>';
        }
        str += '</div>';
        str += '</li>';
    }
    return str;
}

function getEmailCheckCode() {
    var leaveMessageName = $("#leave-message-name").val();
    var leaveMessageEmail = $("#leave-message-email").val();
    var leaveMessageComment = $("#leave-message-comment").val();
    if (leaveMessageName == null || leaveMessageName.length == 0 || leaveMessageName == "") {
        openAlert("请留下您的昵称");
    } else if (leaveMessageEmail == null || leaveMessageEmail.length == 0 || leaveMessageEmail == "") {
        openAlert("请留下您的邮箱，您的邮箱信息将不会被其他人看到，该邮箱仅用于对您提出的意见进行探讨");
    } else if (leaveMessageComment == null || leaveMessageComment.length == 0 || leaveMessageComment == "") {
        openAlert("说点什么呗~");
    } else if (leaveMessageName.length > 20) {
        openAlert("昵称长度不能大于20字符");
    } else if (leaveMessageEmail.length > 50) {
        openAlert("邮箱长度不能大于50字符");
    } else if (isEmail(leaveMessageEmail)) {
        if (leaveMessageComment.length > 300) {
            openAlert("评论内容不能超过300字符");
        } else {
            $('#leave-message-email-check-code-model').modal({
                relatedTarget: this,
                onConfirm: function(options) {
                    var leaveMessageEmailCheckCode = $("#leave-message-email-check-code").val();
                    if (leaveMessageEmailCheckCode == null || leaveMessageEmailCheckCode == "" || leaveMessageEmailCheckCode.length == 0) {
                        openAlert("请输入验证码！");
                    } else {
                        openLoadingModel("正在发表，请稍后...");
                        var obj = new Object();
                        obj.leaveMessageName = leaveMessageName;
                        obj.leaveMessageEmail = leaveMessageEmail;
                        obj.leaveMessageComment = leaveMessageComment;
                        obj.leaveMessageEmailCheckCode = leaveMessageEmailCheckCode;
                        $.ajax({
                            url: "/message/email/check_code/check_and_publish",
                            type: "POST",
                            cache: false,//设置不缓存
                            data: obj,
                            success: publishLeaveMessageSuccess,
                            error: function (XMLHttpRequest, textStatus, errorThrown) {
                                closeLoadingModel();
                                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
                            }
                        });
                    }
                },
                // closeOnConfirm: false,
                onCancel: function() {
                }
            });
            var obj = new Object();
            obj.leaveMessageName = leaveMessageName;
            obj.email = leaveMessageEmail;
            $.ajax({
                url: "/message/email/check_code/send",
                type: "POST",
                cache: false,//设置不缓存
                data: obj,
                success: function (data) {
                    console.log(data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    console.log("请求出错：" + XMLHttpRequest + textStatus + errorThrown);
                }
            });
        }
    } else {
        openAlert("您输入的邮箱格式不正确，您的邮箱信息将不会被其他人看到，该邮箱仅用于对您提出的意见进行探讨");
    }
}

function publishLeaveMessageSuccess(data) {
    var result = JSON.parse(data);
    if (result.status = "true") {
        window.location.reload();
    } else {
        closeLoadingModel();
        openAlert(result.content);
    }
}

function deleteLeaveMessage(leaveMessageId) {
    $('#delete-leave-message-confirm').modal({
        relatedTarget: this,
        onConfirm: function(options) {
            openLoadingModel("正在删除，请稍后...");
            var obj = new Object();
            obj.leaveMessageId = leaveMessageId;
            $.ajax({
                url: "/message/delete",
                type: "POST",
                cache: false,//设置不缓存
                data: obj,
                success: deleteLeaveMessageSuccess,
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    closeLoadingModel();
                    openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
                }
            });
        },
        // closeOnConfirm: false,
        onCancel: function() {
        }
    });
}

function deleteLeaveMessageSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        closeLoadingModel();
        openAlert(result.content);
    }
}

$(document).ready(function () {
    getMessage();
});