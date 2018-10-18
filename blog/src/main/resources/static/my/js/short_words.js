/**
 * Created by 郭欣光 on 2018/10/18.
 */

function openAddShortWordsModel() {
    $("#add-short-words-model").modal({
        relatedTarget: this,
        onConfirm: function(e) {
            addShortWords();
        },
        onCancel: function(e) {
        }
    });
}

function addShortWords() {
    var shortWordsWord = $("#short-words-word").val();
    if (shortWordsWord == null || shortWordsWord.length == 0 || shortWordsWord == "") {
        openAlert("输入的毒鸡汤不能为空！");
    } else if (shortWordsWord.length > 100) {
        openAlert("毒鸡汤长度不能超过100字符！");
    } else {
        var obj = new Object();
        obj.word = shortWordsWord;
        $.ajax({
            url: "/short_words/add",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: addShortWordsSuccess,
            error: openAjaxErrorAlert
        });
    }
}

function addShortWordsSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        openAlert(result.content);
    }
}

function openDeleteShortWordsModel() {
    $("#delete-short-words-model").modal({
        relatedTarget: this,
        onConfirm: function(e) {
            deleteShortWords();
        },
        onCancel: function(e) {
        }
    });
}

function deleteShortWords() {
    var deleteOption = null;
    try {
        deleteOption = $("#delete-short-words-select option:selected");
    } catch (e) {
        console.log(e);
    }
    if (deleteOption == null) {
        openAlert("还没有毒鸡汤可供选择哦~~~");
    } else {
        var shortWordsId = deleteOption.val();
        if (shortWordsId == null || shortWordsId.length == 0 || shortWordsId == "") {
            openAlert("请选择要删除的毒鸡汤！");
        } else {
            var obj = new Object();
            obj.id = shortWordsId;
            $.ajax({
                url: "/short_words/delete",
                type: "POST",
                cache: false,//设置不缓存
                data: obj,
                success: deleteShortWordsSuccess,
                error: openAjaxErrorAlert
            });
        }
    }
}

function deleteShortWordsSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        openAlert(result.content);
    }
}