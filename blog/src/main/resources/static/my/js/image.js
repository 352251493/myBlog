/**
 * Created by 郭欣光 on 2018/11/9.
 */

function openUploadImageModel() {
    $('#upload-image-model').modal({
        relatedTarget: this,
        onConfirm: function(e) {
            uploadImage();
        },
        onCancel: function(e) {
        }
    });
}

function uploadImage() {
    var file =  document.getElementById('uploadImage').files[0];
    var filePath = $('#uploadImage').val().toLowerCase().split(".");
    var fileType =  filePath[filePath.length - 1];
    var imageTitle = $("#imageTitle").val();
    if (imageTitle == null || imageTitle.length == 0 || imageTitle == "") {
        openAlert("图片标题不能为空！");
    } else if (imageTitle.length > 50) {
        openAlert("图片标题不能超过50字符！");
    } else if (file == null) {
        openAlert("请选择要上传的图片！");
    } else if (fileType != "bmf" && fileType != "png" && fileType != "gif" && fileType != "jpg" && fileType != "jpeg") {
        openAlert("上传的文件必须为图片类型！");
    } else if (file.size > 50 * 1024 * 1024) {
        openAlert("图片大小不能超过50MB！");
    } else {
        var formData = new FormData($('#upload-image-form')[0]);
        $("#upload-file-progress").html("0.00%");
        $("#upload-file-progress").css("width", "0.00%");
        $('#upload-file-progress-model').modal({
            relatedTarget: this,
            closeViaDimmer: false
        });
        var url = "/image/upload";
        // XMLHttpRequest 对象
        var xhr = new XMLHttpRequest();
        //true为异步处理
        xhr.open('post', url, true);
        //上传开始执行方法
        xhr.onloadstart = function() {
            console.log('开始上传');
        };
        xhr.upload.addEventListener('progress', uploadImageProgressFunction, false);
        xhr.addEventListener("load", uploadImageComplete, false);
        xhr.addEventListener("error", uploadImageFailed, false);
        xhr.send(formData);
    }
}

function uploadImageProgressFunction(evt) {
    // debugger;
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100)
            + '%';
        $("#upload-file-progress").html(completePercent);
        $("#upload-file-progress").css("width", completePercent);
    }
}

function uploadImageComplete(evt) {
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

function uploadImageFailed(evt) {
    $("#close-upload-file-process-model").click();
    console.log(evt.target.responseText);
    openAlert("上传失败！原因：" + evt.target.responseText);
}
