/**
 * Created by 郭欣光 on 2018/10/25.
 */

function openLoadingModel(content) {
    $("#loading-content").html(content);
    $('#loading-model').modal();
}

function closeLoadingModel() {
    $("#close-loading-model").click();
}
