/**
 * Created by 郭欣光 on 2018/10/30.
 */

var articleCommentPage = 0;

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

function getArticleComment() {
    if (articleCommentPage > 0) {
        $("#load-more-article-comment-button").attr("disabled","disabled");
        var str = '<i class="am-icon-spinner am-icon-spin"></i>正在加载...';
        $("#load-more-article-comment-button").html(str);
    }
    var articleId = $("#articleId").val();
    if (articleId == null || articleId == "" || articleId.length == 0) {
        console.log("获取文章ID出错！");
        if (articleCommentPage > 0) {
            $("#article-comment-list").append(str);
            $("#load-more-article-comment-button").html("点击加载更多");
            $("#load-more-article-comment-button").removeAttr("disabled");
        }
    } else {
        articleCommentPage = articleCommentPage + 1;
        var obj = new Object();
        obj.articleCommentPage = articleCommentPage;
        $.ajax({
            url: "/article/comment/article_id/" + articleId,
            type: "GET",
            cache: false,//设置不缓存
            data: obj,
            success: getArticleCommentSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                console.log("请求出错：" + XMLHttpRequest + textStatus + errorThrown);
                articleCommentPage = articleCommentPage -1;
                if (articleCommentPage > 0) {
                    $("#article-comment-list").append(str);
                    $("#load-more-article-comment-button").html("点击加载更多");
                    $("#load-more-article-comment-button").removeAttr("disabled");
                }
            }
        });
    }
}

function getArticleCommentSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        if (articleCommentPage == 1) {
            if (result.isHasComment == "true") {
                var str = "";
                for (var i = 0; i < result.content.length; i++) {
                    var articleComment = result.content[i];
                    str += '<hr/>';
                    str += '<div class="am-u-sm-3 am-u-md-3 am-u-lg-2">';
                    str += '<img src="' + articleComment.headImg + '" alt="" class="blog-author-img am-circle">';
                    str += '</div>';
                    str += '<div class="am-u-sm-9 am-u-md-9 am-u-lg-10">';
                    str += '<h3 class="am-cf">';
                    str += '<span>' + articleComment.name + ' &nbsp;: &nbsp;</span>';
                    if (result.isUser == "true") {
                        str += '<span class="blog-color">' + articleComment.email + ' &nbsp;: &nbsp;</span>';
                    }
                    str += '<span class="blog-color">' + articleComment.createTime.split(".")[0] + '</span>';
                    if (result.isUser == "true") {
                        str += '<a class="am-btn am-btn-link am-fr" style="color: red" href="javascript:deleteArticleComment("' + articleComment.id + '");"><i class="am-icon-trash am-icon-fw"></i></a>';
                    }
                    str += '</h3>';
                    str += '<p>' + articleComment.comment + '</p>';
                    str += '</div>';
                }
                $("#article-comment-list").html(str);
                str = '<button type="button" id="load-more-article-comment-button" class="am-btn am-btn-default am-btn-block" onclick="getArticleComment();">点击加载更多</button>';
                $("#show-load-more-article-comment").html(str);
            } else {
                console.log(result.content);
            }
        } else {
            if (result.isHasComment == "true") {
                var str = "";
                for (var i = 0; i < result.content.length; i++) {
                    var articleComment = result.content[i];
                    str += '<hr/>';
                    str += '<div class="am-u-sm-3 am-u-md-3 am-u-lg-2">';
                    str += '<img src="' + articleComment.headImg + '" alt="" class="blog-author-img am-circle">';
                    str += '</div>';
                    str += '<div class="am-u-sm-9 am-u-md-9 am-u-lg-10">';
                    str += '<h3 class="am-cf">';
                    str += '<span>' + articleComment.name + ' &nbsp;: &nbsp;</span>';
                    if (result.isUser == "true") {
                        str += '<span class="blog-color">' + articleComment.email + ' &nbsp;: &nbsp;</span>';
                    }
                    str += '<span class="blog-color">' + articleComment.createTime.split(".")[0] + '</span>';
                    if (result.isUser == "true") {
                        str += '<a class="am-btn am-btn-link am-fr" style="color: red" href="javascript:deleteArticleComment("' + articleComment.id + '");"><i class="am-icon-trash am-icon-fw"></i></a>';
                    }
                    str += '</h3>';
                    str += '<p>' + articleComment.comment + '</p>';
                    str += '</div>';
                }
                $("#article-comment-list").append(str);
                $("#load-more-article-comment-button").html("点击加载更多");
                $("#load-more-article-comment-button").removeAttr("disabled");
            } else {
                $("#load-more-article-comment-button").html("没有更多啦~");
            }
        }
    } else {
        console.log(result.content);
        articleCommentPage = articleCommentPage - 1;
        if (articleCommentPage > 0) {
            $("#article-comment-list").append(str);
            $("#load-more-article-comment-button").html("点击加载更多");
            $("#load-more-article-comment-button").removeAttr("disabled");
        }
    }
}

$(document).ready(function () {
    getArticleComment();
});
