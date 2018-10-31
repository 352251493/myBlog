/**
 * Created by 郭欣光 on 2018/10/31.
 */
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

function editArticle() {
    var articleTitle = $("#articleTitle").val();
    var articleAbstract = $("#articleAbstract").val();
    var articleContent = editor.txt.html();
    var articleId = $("#articleId").val();
    if (articleId == null || articleId == "" || articleId.length == 0) {
        openAlert("系统获取文章ID出错！");
    } else if (articleTitle == null || articleTitle == "" || articleTitle.length == 0) {
        openAlert("标题不能为空！");
    } else if (articleAbstract == null || articleAbstract == "" || articleAbstract.length == 0) {
        openAlert("摘要不能为空！");
    } else if (articleContent == null || articleContent == "" || articleContent.length == 0) {
        openAlert("请输入正文！");
    } else if (articleTitle.length > 100) {
        openAlert("标题不能超过100字符！");
    } else if (articleAbstract.length > 200) {
        openAlert("摘要不能超过200字符！");
    } else {
        var articleLabelDom = document.getElementsByName("articleLabel");
        var isSelect = false;
        var articleLabel;
        for (var i = 0; i < articleLabelDom.length; i++) {
            if (articleLabelDom[i].checked) {
                isSelect = true;
                articleLabel = articleLabelDom[i].value;
                break;
            }
        }
        if (isSelect) {
            openLoadingModel("正在处理，请稍后...");
            var obj = new Object();
            obj.articleId = articleId;
            obj.articleTitle = articleTitle;
            obj.articleAbstract = articleAbstract;
            obj.articleLabel = articleLabel;
            obj.articleContent = articleContent;
            $.ajax({
                url: "/article/edit",
                type: "POST",
                cache: false,//设置不缓存
                data: obj,
                success: editArticleSuccess,
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

function editArticleSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.href = result.content;
    } else {
        closeLoadingModel();
        openAlert(result.content);
    }
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
    };
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