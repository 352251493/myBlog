/**
 * Created by 郭欣光 on 2018/10/16.
 */

function openAddLabelModel() {
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
    } else if (name.length > 30) {
        openAlert("标签名称长度不能超过30字符！");
    } else {
        var obj = new Object();
        obj.name = name;
        $.ajax({
            url: "/label/add",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: addLabelSuccess,
            error: openAjaxErrorAlert
        });
    }
}

function addLabelSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        openAlert(result.content);
    }
}

function openDeleteLabelModel() {
    $('#delete-label-model').modal({
        relatedTarget: this,
        onConfirm: function(e) {
            deleteLabel();
        },
        onCancel: function(e) {
        }
    });
}

function deleteLabel() {
    var deleteLabelCheckbox = null;
    try {
        deleteLabelCheckbox = document.getElementsByClassName("delete-label-checkbox");
    } catch (err){
        console.log(err);
    }
    if (deleteLabelCheckbox == null) {
        openAlert("还没有标签可供选择哦~~~");
    } else {
        var isSelect = false;
        var deleteLabelList = new Array();
        var n = 0;
        for (var i = 0; i < deleteLabelCheckbox.length; i++) {
            var deleteLabel = deleteLabelCheckbox[i];
            if (deleteLabel.checked) {
                isSelect = true;
                deleteLabelList[n] = deleteLabel.value;
                n++;
            }
        }
        if (isSelect) {
            $('#delete-label-confirm').modal({
                relatedTarget: this,
                onConfirm: function(options) {
                    var obj = new Object();
                    obj.labelIdList = deleteLabelList + "";
                    $.ajax({
                        url: "/label/delete",
                        type: "POST",
                        cache: false,//设置不缓存
                        data: obj,
                        success: deleteLabelSuccess,
                        error: openAjaxErrorAlert
                    });
                },
                // closeOnConfirm: false,
                onCancel: function() {
                }
            });
        } else {
            openAlert("请选择要删除的标签！");
        }
    }
}

function deleteLabelSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        openAlert(result.content);
    }
}