/**
 * Created by 郭欣光 on 2018/11/9.
 */

function deleteImage(imageId) {
    $('#delete-image-confirm').modal({
        relatedTarget: this,
        onConfirm: function(options) {
            openLoadingModel("正在删除，请稍后...");
            var obj = new Object();
            obj.imageId = imageId;
            $.ajax({
                url: "/image/delete",
                type: "POST",
                cache: false,//设置不缓存
                data: obj,
                success: deleteImageSuccess,
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

function deleteImageSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        closeLoadingModel();
        openAlert(result.content);
    }
}