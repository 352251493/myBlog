/**
 * Created by 郭欣光 on 2018/10/30.
 */

function deleteArticle(articleId) {
    $('#delete-article-confirm').modal({
        relatedTarget: this,
        onConfirm: function(options) {
            openLoadingModel("正在删除，请稍后...");
            var obj = new Object();
            obj.articleId = articleId;
            $.ajax({
                url: "/article/delete",
                type: "POST",
                cache: false,//设置不缓存
                data: obj,
                success: deleteArticleSuccess,
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

function deleteArticleSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.href = result.content;
    } else {
        closeLoadingModel();
        openAlert(result.content);
    }
}

function openEditArticleModel() {
    $('#edit-article-model').modal();
}

function openChangeArticleImgModel() {
    $("#close-edit-article-model").click();
    $('#change-article-img-model').modal({
        relatedTarget: this,
        onConfirm: function(e) {
            changeArticleImg();
        },
        onCancel: function(e) {
        }
    });
}

function changeArticleImg() {
    var file =  document.getElementById('articleImg').files[0];
    var filePath = $('#articleImg').val().toLowerCase().split(".");
    var fileType =  filePath[filePath.length - 1];
    var articleId = $('#articleId').val();
    if (articleId == null || articleId == "" || articleId.length == 0) {
        openAlert("系统获取文章ID失败！");
    } else if (file == null) {
        openAlert("请选择要上传的图片！");
    } else if (fileType != "bmf" && fileType != "png" && fileType != "gif" && fileType != "jpg" && fileType != "jpeg") {
        openAlert("上传的文件必须为图片类型！");
    } else if (file.size > 50 * 1024 * 1024) {
        //file.size 以字节为单位
        openAlert("图片大小不能超过50MB！");
    } else {
        var formData = new FormData($('#change-article-img-form')[0]);
        $("#upload-file-progress").html("0.00%");
        $("#upload-file-progress").css("width", "0.00%");
        $('#upload-file-progress-model').modal({
            relatedTarget: this,
            closeViaDimmer: false
        });
        var url = "/article/edit/img";
        // XMLHttpRequest 对象
        var xhr = new XMLHttpRequest();
        //true为异步处理
        xhr.open('post', url, true);
        //上传开始执行方法
        xhr.onloadstart = function() {
            console.log('开始上传');
        };
        xhr.upload.addEventListener('progress', changeArticleImgProgressFunction, false);
        xhr.addEventListener("load", changeArticleImgComplete, false);
        xhr.addEventListener("error", changeArticleImgFailed, false);
        xhr.send(formData);
    }
}

function changeArticleImgProgressFunction(evt) {
    // debugger;
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)
            + '%';
        $("#upload-file-progress").html(completePercent);
        $("#upload-file-progress").css("width", completePercent);
    }
}

function changeArticleImgComplete(evt) {
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

function changeArticleImgFailed(evt) {
    $("#close-upload-file-process-model").click();
    console.log(evt.target.responseText);
    openAlert("上传失败！原因：" + evt.target.responseText);
}
