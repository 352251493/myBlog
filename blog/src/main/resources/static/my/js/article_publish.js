/**
 * Created by 郭欣光 on 2018/10/25.
 */
function publishArticle() {
    var articleTitle = $("#articleTitle").val();
    var articleAbstract = $("#articleAbstract").val();
    var file =  document.getElementById('articleImg').files[0];
    var filePath = $('#articleImg').val().toLowerCase().split(".");
    var fileType =  filePath[filePath.length - 1];
    var articleContent = editor.txt.html();
    if (articleTitle == null || articleTitle == "" || articleTitle.length == 0) {
        openAlert("标题不能为空！");
    } else if (articleAbstract == null || articleAbstract == "" || articleAbstract.length == 0) {
        openAlert("摘要不能为空！");
    } else if (file == null) {
        openAlert("请上传封面图片！");
    } else if (fileType != "bmf" && fileType != "png" && fileType != "gif" && fileType != "jpg" && fileType != "jpeg") {
        openAlert("封面图片必须是图片类型！");
    } else if (file.size > 50 * 1024 * 1024) {
        openAlert("图片大小不能超过50MB！");
    } else if (articleContent == null || articleContent == "" || articleContent.length == 0) {
        openAlert("请输入正文！");
    } else if (articleTitle.length > 100) {
        openAlert("标题不能超过100字符！");
    } else if (articleAbstract.length > 200) {
        openAlert("摘要不能超过200字符！");
    } else {
        var articleLabelDom = document.getElementsByName("articleLabel");
        var isSelect = false;
        for (var i = 0; i < articleLabelDom.length; i++) {
            if (articleLabelDom[i].checked) {
                isSelect = true;
                break;
            }
        }
        if (isSelect) {
            var formData = new FormData($('#article-form')[0]);
            formData.append("articleContent", articleContent);
            openLoadingModel("正在发表，请稍后...");
            $.ajax({
                type: "POST",
                url: "/article/publish",
                // async: false,//true表示同步，false表示异步
                cache: false,//设置不缓存
                data: formData,
                contentType: false,//必须设置false才会自动加上正确的Content-Tyoe
                processData: false,//必须设置false才避开jquery对formdata的默认处理，XMLHttpRequest会对formdata进行正确的处理
                success: publishArticleSuccess,
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    closeLoadingModel();
                    openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
                }
            });
        } else {
            openAlert("请选择类别！");
        }
    }
}

function publishArticleSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        closeLoadingModel();
        openAlert("发表成功！");
    } else {
        closeLoadingModel();
        openAlert(result.content);
    }
}

function getUser() {
    $.ajax({
        type: "POST",
        url: "/user/get_user",
        // async: false,//true表示同步，false表示异步
        cache: false,//设置不缓存
        success: function (data) {
            console.log(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest + textStatus + errorThrown);
        }
    });
}

$(document).ready(function () {
    var E = window.wangEditor;
    editor = new E('#article-content');
    // 下面两个配置，使用其中一个即可显示“上传图片”的tab。但是两者不要同时使用！！！
    editor.customConfig.uploadImgShowBase64 = true;   // 使用 base64 保存图片
    // editor.customConfig.uploadImgServer = '/upload_experimental_img';  // 上传图片到服务器
    // editor.customConfig.uploadImgMaxSize = 10 * 1024 * 1024;//将图片大小限制为10M，默认大小是5M
    editor.customConfig.uploadImgMaxLength = 1;//限制一次最多上传1张图片
    // editor.customConfig.uploadFileName = 'experimentalImage';
    // editor.customConfig.uploadImgHeaders = {
    //     'Accept': 'text/x-json'
    // };
    editor.customConfig.pasteTextHandle = function(content) {
        var reTag = /<img[^>]*>/gi;
        if(content.match(reTag) != null) {
            alert("复制的内容含有图片，图片内容无法复制，请您在相应的地方手动插入图片");
            return content.replace(reTag, '');
        } else {
            return content;
        }
    }
    editor.customConfig.zIndex = 100;
    // 自定义菜单配置
    editor.customConfig.menus = [
        'head',  // 标题
        'bold',  // 粗体
        'italic',  // 斜体
        'underline',  // 下划线
        'foreColor',  // 文字颜色
        'backColor',  // 背景颜色
        'link',  // 插入链接
        'list',  // 列表
        'justify',  // 对齐方式
        'quote',  // 引用
        'emoticon',  // 表情
        'image',  // 插入图片
        'table',  // 表格
        'code',  // 插入代码
        'undo',  // 撤销
        'redo'  // 重复
    ];
    editor.create();
    setInterval(getUser, 3 * 60 * 1000);
});
