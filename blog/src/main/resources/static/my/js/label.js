/**
 * Created by 郭欣光 on 2018/10/16.
 */

function openAddLabelModel(isClear) {
    if (isClear) {
        $("#label-name").val("");
    }
    $('#add-label-model').modal({
        relatedTarget: this,
        onConfirm: function(e) {
            addLabel();
        },
        onCancel: function(e) {
        }
    });
}

function addLabel() {
    var name = $("#label-name").val();
    if (name == null || name == "" || name.length == 0) {
        openAlert("标签名称不能为空！");
        openAddLabelModel(false);
    } else if (name.length > 30) {
        openAlert("标签名称长度不能超过30字符！");
        openAddLabelModel(false);
    } else {
        var obj = new Object();
        obj.name = name;
        $.ajax({
            url: "/label/add",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: addLabelSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                if (XMLHttpRequest.status >= 400 && XMLHttpRequest.status < 500) {
                    openAlert("客户端请求出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
                    openAddLabelModel(false);
                } else if (XMLHttpRequest.status >= 500 || XMLHttpRequest.status <= 600) {
                    openAlert("服务器出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
                    openAddLabelModel(false);
                } else if (XMLHttpRequest.status >= 300 || XMLHttpRequest.status < 400) {
                    openAlert("重定向问题！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
                    openAddLabelModel(false);
                } else {
                    openAlert("抱歉，系统好像出现一些异常！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
                    openAddLabelModel(false);
                }
            }
        });
    }
}

function addLabelSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        openAlert(result.content);
        openAddLabelModel(false);
    }
}
