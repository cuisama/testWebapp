/**
 * Created by yxcui on 2017/5/22.
 */
var $ = require('jquery');
var request = require("request");

var pageModel = "create";//新建模式  edit 编辑模式
var chapterModel = undefined;
var coursewareSubmitType = undefined;
var chapterListIndex = 0;
var sequenceCode="";//没啥用了
/*"524C1B6785464E34E050007F01002743":{
 contentCode:1
 contentId:"524C1B6785464E34E050007F01002743"
 coursewareName:""
 coursewareOutLine:""
 coursewareType:"1"
 isLeaf:1
 knowledgePoint:""
 parentId:"root"
 resourceId:undefined
 title:"11"
 }*///db结构 课程目录主键：课程实体
var db = {};
//初始化数据据
var initDb = {
    course : null,
    contents : [],
    attachments : [],
};

var currentEditContentId = undefined;
var dbCourseId = undefined;
var chapterItemResourceId = undefined;
var attachments = [];
var thumbnailId = undefined;//略缩图

exports.initTask = function(trans){
    pageModel = "edit";
    request.ajax({
        urlkey:"course_getCourse",
        type:"post",
        data:{
            courseId:trans.courseId
        }
    },{
        success:function(res){
            var data = res.data;
            initDb = data;
            //初始化课程
            var course = data.course;
            dbCourseId = course.courseId;
            thumbnailId = course.thumbnailId;
            $("#courseName").val(course.courseName);
            $("#courseOutline").val(course.summary);
            $("#courseHour").val(course.classHour);
            $("#courseCredit").val(course.credits);
            $("#lecturer").val(course.teachersId);
            $("#courseTime").val(course.duration);
            $("#courseCredit").val(course.score);
            $("#isOpenClass").val(course.isPublic);
            //$("#courseType").val(course.courseType);//下拉列表还没有被获取
            $("#certificateTemplate").val(course.certificate);
            //$("#forPost").val();
            //初始化附件
            var html = "";
            for(var i = data.attachments.length-1;i>=0;i--){
                attachments.push(data.attachments[i].resourceId);
                html = "<tr id = "+data.attachments[i].resourceId+">" +
                    "<td>"+data.attachments[i].fileName+"</td>" +
                    "<td><a>删除</a></td>" +
                    "</tr>"+html;
            }
            $("#referenceTable tbody").html(html);
            $("#referenceTable tbody tr td:last a").click(function(){
                var resourceId = $(this).parents("tr").attr("id");
                request.ajax({
                    urlkey:"resource_delete",
                    type:"post",
                    data:{
                        resourceId:resourceId,
                        deleteUserId:"111111",
                    }
                },{
                    success:function(res){
                        console.log(res);
                        if(attachments.includes(resourceId)){
                            attachments.splice(attachments.indexOf(resourceId),1);
                        }
                        $("#"+resourceId).remove();
                    }
                });
            });

            $("#referenceTable").css("display","table");

            //初始化课程目录数据
            var contents = data.contents;
            var t = ["chapter","section","lesson"];
            for(var i=0, len = contents.length;i<len; i++){
                db[contents[i].contentId] = contents[i];
                coursewareSubmitType = t[contents[i].isLeaf-1];
                chapterListIndex = db[contents[i].parentId]&&db[contents[i].parentId].contentCode-1||0;
                renderingChapterItem({
                    contentCode:contents[i].contentCode,
                    contentId:contents[i].contentId,
                    coursewareName:"coursewareName",//contents[i].,
                    coursewareOutLine:contents[i].courseOutLine,
                    coursewareType:contents[i].courseType,
                    isLeaf:contents[i].isLeaf,
                    knowledgePoint:contents[i].knowledgePointId,
                    parentId:contents[i].parentId,
                    resourceId:contents[i].resourceId,
                    title:contents[i].contentName,
                });
            }
        }
    });
};
exports.run = function(delay_process) {
    if (delay_process) {
        // 初期显示页面(编辑用)事件
        if (delay_process.additionExes) {
            if ($.isArray(delay_process.additionExes)) {
                $.each(delay_process.additionExes, function(index, exe_func){
                    if ($.isFunction(exe_func)) {
                        exe_func();
                    }
                });
            }
        }
    }
};
$().ready(function(){
    chapterModel = $("#chapterList").children("div");
    $("#chapterList").html("");
    request.ajax({
            urlkey:"dictionary_listCourseType",
            type:"post",
        },{
            success:function (data) {
                var list = data.data;
                var html="";
                for(var i = list.length-1;i>=0;i--){
                    html = "<option value='"+list[i].id+"'>"+list[i].name+"</option>"+html;
                }
                $("#courseType").append(html);
            }
        }
    );
});

//目录视图
$("#catalogView").click(function(){
    $(".chapterItem").children(":not(.chapter)").css("display","none");
});
//摘要视图
$("#summaryView").click(function(){
    $(".chapterItem").children(":not(.chapter)").css("display","block");
});

//新增章节按钮
$("#addChapter").click(function() {
    if(!dbCourseId){
        if (saveCourse()) {
            return;
        }
    }
    $("#coursewareAdd").modal();
    initCoursewareData();
    coursewareSubmitType = "chapter";
});
//保存
$("#save").click(function(){
    if(checkCourse())return true;
    var t = [];
    $("#referenceTable tbody tr").each(function(){
        t.push($(this).attr("id"));
    });
    t = t.slice(attachments.length, t.length-attachments.length);
    request.ajax({
        urlkey:"course_save",
        type:"post",
        data:{
            courseId:dbCourseId,
            courseName:$("#courseName").val(),
            summary:$("#courseOutline").val(),
            classHour:$("#courseHour").val(),
            credits:$("#courseCredit").val(),
            label:$("#courseType").val(),
            publishStatus:0,//未发布状态
            publishDate:new Date().getTime(),
            teachersId:$("#lecturer").val(),
            thumbnailId:thumbnailId,
            duration:$("#courseTime").val(),
            lang:1,//简体中文//$("").val(),
            score:$("#courseCredit").val(),//$("").val(),
            isPublic:$("#isOpenClass").val(),
//            isBBS:$("").val(),
//            isOnline:$("").val(),
//            isEvaluation:$("").val(),
//            isChat:$("").val(),
//            isCertificate:$("#certificateTemplate").val(),
//            isBeforeTest:$("").val(),
//            isAfterTest:$("").val(),
//            deleteFlag:$("").val(),
//            deleteDateTime:$("").val(),
//            deleteUserId:$("").val(),

            insertUserId:"insertUserId",
            insertDateTime:new Date().getTime(),
//            updateUserId:$("").val(),
//            updateDateTime:$("").val(),
            courseType:$("#courseType").val(),
            certificate:$("#certificateTemplate").val(),
            attachmentResourceIds:t.join(","),
        }
    },{
        success:function(data){
            attachments.push(t);
        }
    });
});
//发布
$("#publish").click(function(){
    request.ajax({
        urlkey:"course_publish",
        type:"post",
        data:{
            courseId:dbCourseId,
            entityId:$("#forPost").val(),
            publishStatus:1,//发布的时候 变为    已发布
            insertUserId:"111111",
            insertDateTime:new Date().getTime(),
        }
    },{
        success:function(data){

        }
    });
});
//取消
$("#cancel").click(function(){
    if(pageModel == "create"){
        request.ajax({
            urlkey:"course_cancel",
            type:"post",
            data:{
                courseId:dbCourseId
            }
        },{
            success:function(data){

            }
        });
    }eles;{//编辑模式  如果pageModel为空了
        request.ajax({
            urlkey:"course_cancelDraft",
            type:"post",
            data:{
                courseId:dbCourseId
            }
        },{
            success:function(data){

            }
        });
    }

});

/**
 * 初始化modal数据
 * @param data
 */
function initCoursewareData(data){
    chapterItemResourceId =undefined;
    data = data||{};
    var $1 = $("#coursewareAdd #sectionName");
    var $2 = $("#coursewareAdd #knowledgePoint");
    var $3 = $("#coursewareAdd #coursewareOutLine");
    var $4 = $("#coursewareAdd #coursewareType");
    var $5 = $("#coursewareAdd #courseware");

    $1.val(data.title);
    $2.val(data.knowledgePoint);
    $3.val(data.coursewareOutLine);
    $4.val(data.coursewareType||1);//默认设置为1
    $5.val(data.courseware);

    if(data.contentId&&!data.knowledgePoint){
        $2.attr("disabled","true");
        $3.attr("disabled","true");
        $4.attr("disabled","true");
        $5.attr("disabled","true");
    }else{
        $2.removeAttr("disabled");
        $3.removeAttr("disabled");
        $4.removeAttr("disabled");
        $5.removeAttr("disabled");
    }
    /*	data={
     sectionName:$("#sectionName").val(),
     knowledgePoint:$("#knowledgePoint").val(),
     coursewareOutLine:$("#coursewareOutLine").val(),
     coursewareType:$("#coursewareType").val(),
     courseware:$("#courseware").val(),
     }*/
}

//上传文件
function uploadFiles(fileElementIds,success){
    $.ajaxFileUpload({
        url:"http://localhost:8080/restCmatcService/rest/resourceResource/insertResource",
        fileElementIds:fileElementIds,
        dataType:"json",
        data:{
            insertUserId:"insertUserId",
            insertDateTime:new Date().getTime(),
        },
        success:success||function(res){
            alert("上传成功");
        }
    });
}

//封面
$('#thumbnail').change(function(event){
    previewImg($(this), $("#preImage"));
    uploadFiles("thumbnail",function(res){
        thumbnailId= res.data;
    });
});

//课程 参考资料
$('#referenceData').change(listener);

//上传参考资料监听
function listener(event) {
    uploadFiles("referenceData",function(res){
        var html = "<tr id = "+res.result+"><td>"+$('#referenceData').val()+"</td><td><a>删除</a></td></tr>";
        $("#referenceTable tbody").append(html);
        $("#referenceTable tbody tr:last td:last a").click(function(){
            var resourceId = $(this).parents("tr").attr("id");
            request.ajax({
                urlkey:"resource_delete",
                type:"post",
                data:{
                    resourceId:resourceId,
                    deleteUserId:"111111",
                }
            },{
                success:function(res){
                    console.log(res);
                    if(attachments.includes(resourceId)){
                        attachments.splice(attachments.indexOf(resourceId),1);
                    }
                    $("#"+resourceId).remove();
                }
            });
        });
        $("#referenceTable").css("display","table");
        var file = $("#referenceData");
        file.after(file.clone(true).val(""));
        file.remove();
        $("#referenceData").change(listener);
    });
}

//上传资源按钮 课程目录画面
$("#coursewareAdd #uploadCourseware").click(function(){
    chapterItemResourceId = undefined;
    uploadFiles("courseware",function(res){
        chapterItemResourceId = res.result;
        alert("上传成功");
    });
});

function previewImg (fileControl, viewImg){
    viewImg.empty();
    var img = document.createElement('img');
    $(img).attr('class', 'preImage');
    if (window.FileReader
        && window.File
        && window.Blob) {
        var file = fileControl[0].files[0];
        var reader = new FileReader();
        if(!/image\/\w+/.test(file.type)){
            return false;
        }
        // onload是异步操作
        reader.onload = function(e){
            $(img).attr('src', e.target.result);
            // 图片size改变
            $(img).click(exchangeImgSize);
            $(img).appendTo(viewImg);
            viewImg.show();
        };
        reader.readAsDataURL(file);
    } else if(fileControl[0].files){
        var objUrl = this.getObjectURL(fileControl[0].files[0]);
        $(img).attr('src', objUrl);
        // 图片size改变
        $(img).click(exchangeImgSize);
        $(img).appendTo(viewImg);
    } else {
//		fileControl.select();
//		var imageSrc = document.selection.createRange().text;
        var imageSrc = $(fileControl).val();
        document.selection.empty();
        viewImg.css({
            'filter' : 'progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)',
//			'filter' : 'progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=\'scale\',src=\''+imageSrc+'\')',
            'class' : 'preImage',
            'margin-left':'10px'
        });
//		viewImg[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imageSrc;
        $(viewImg).html("当前浏览器不支持预览");
        // 图片size改变
        viewImg.click(exchangeImgSize);
    }
}
function exchangeImgSize(e){
    var target = e.target, cssStatus = target.className;
    var heightTo = 200, widthTo = 400;
    if (target.tagName.toUpperCase() == 'IMG'){
        if (cssStatus.indexOf('preImage') > -1) {
            target.className = '';
            target.style.position = 'absolute';
            target.style.left = '100px';
            target.style.top = '-460px';
            target.style.width = Math.min(target.naturalWidth, widthTo) + 'px';
            target.style.height = Math.min(target.naturalHeight, heightTo) + 'px';
        } else {
            target.style.cssText = '';
            target.className = 'preImage';
        }
    }
}

//添加课程画面 （课程目录表） 的提交 按钮
$("#coursewareAdd #submit").click(function(){
    addChapter({
        title:$("#coursewareAdd #sectionName").val(),
        knowledgePoint:$("#coursewareAdd #knowledgePoint").val(),
        coursewareOutLine:$("#coursewareAdd #coursewareOutLine").val(),
        coursewareType:$("#coursewareAdd #coursewareType").val(),
        coursewareName:$("#coursewareAdd #courseware").val(),
        resourceId:chapterItemResourceId,
    });
});
function addChapter(data){
    if(currentEditContentId){//更新
        request.ajax({
                urlkey:"courseContent_update",
                type:"post",
                data:{
                    courseContentId:currentEditContentId,
//	        	courseId:"",
//	        	contentCode:sequenceCode,
                    contentName:data.title,
                    courseOutLine:data.coursewareOutLine,
                    knowledgePointId:data.knowledgePoint,
                    courseType: data.coursewareType,
                    resourceId:data.resourceId,
//	        	isLeaf:sequenceCode.split('.').length,
//	        	parentId:"parentId",
//	        	referenceId:"0",
                    maxTimeAllowed:"45",
                    updateUserId:"insertUserId",
                    updateDateTime:Date.parse(new Date()),
                }
            },{
                success:function (res) {
                    db[currentEditContentId].title = data.title;
                    db[currentEditContentId].knowledgePoint = data.knowledgePoint;
                    db[currentEditContentId].coursewareOutLine = data.coursewareOutLine;
                    db[currentEditContentId].coursewareType = data.coursewareType;
                    db[currentEditContentId].coursewareName = data.coursewareName;
                    db[currentEditContentId].resourceId = data.resourceId;
//	                updateChapterItem(data);
                    var n = $("#"+currentEditContentId);
                    currentEditContentId = undefined;
                    if(n.hasClass("chapter")){
                        n.find(".chapterTitle").html(data.title);
                    }else if(n.hasClass("section")){
                        n.find(".sectionTitle").html(data.title);
                    }else if(n.hasClass("lesson")){
                        n.find(".lessonTitle").html(data.title);
                    }
                    if(n.next().hasClass("contentItem")){
                        n.next().find("span:eq(0)").html(data.coursewareName);
                        n.next().find("span:eq(1)").html(data.knowledgePoint);
                        n.next().find("span:eq(2)").html(data.coursewareOutLine);
                    }
                }
            }
        );
    }else{
        var isLeaf = -1;
        var parentId = undefined;
        switch (coursewareSubmitType) {
            case "chapter":
                sequenceCode = $(".chapterItem").length+1;
                isLeaf=1;
                parentId = "root";
                break;
            case "section":
                sequenceCode = $(".addSection:eq("+chapterListIndex+")").parents(".chapterItem").find(".section").length+1;
                isLeaf=2;
                parentId = $(".addSection:eq("+chapterListIndex+")").parents(".chapterItem").find(".chapter").attr("id");
                break;
            case "lesson":
                var id = ".addLesson:eq("+chapterListIndex+")";
                var chapterItem = $(id).parents(".section");
                parentId = chapterItem.attr("id");
                var x=1;
                while(1){
                    if(chapterItem.next().hasClass("lesson")){   //找完lesson的section 再找section的最后一个lesson
                        chapterItem = chapterItem.next().next();//认为lesson后必以后contentItem
                        x++;
                    }else{
                        break;
                    }
                }
                sequenceCode = x;
                isLeaf=3;
                break;
        }
        data.contentCode = sequenceCode;
        data.isLeaf = isLeaf;
        data.parentId = parentId;
        data.resourceId = chapterItemResourceId;
        //新追加
        request.ajax({
                urlkey:"courseContent_insert",
                type:"post",
                data:{
//	    		ContentId:,
                    courseId:dbCourseId,
                    contentCode:data.contentCode,
                    contentName:data.title,
                    courseOutLine:data.coursewareOutLine,
                    knowledgePointId:data.knowledgePoint,
                    courseType: data.coursewareType,
                    resourceId:data.resourceId,//$("").val(),
                    isLeaf:isLeaf,//sequenceCode.split('.').length,//没有点（章）：1 有一个点（节）：2  两个点（课）：3
                    parentId:parentId,//$("").val(),
                    referenceId:"0",
                    maxTimeAllowed:"45",
                    insertUserId:"insertUserId",
                    insertDateTime:Date.parse(new Date()),
                }
            },{
                success:function (res) {
                    data.contentId = res.data;
                    db[data.contentId] = data;
                    renderingChapterItem(data);
                }
            }
        );
    }
}

$("#coursewareAdd #cancel").click(function(){
    $("#coursewareAdd").modal("close");
});
/**
 * 更新目录
 */
function updateChapterItem(data){

}

/**
 * 添加数据库成功后 为画面添加节点
 */
function renderingChapterItem(data){
    var chapterItem = undefined;

    switch (coursewareSubmitType) {
        case "chapter":
            var index = $("#chapterList").children().length+1;
            chapterItem = $("<div class='chapterItem'></div>");
            chapterItem.append(setChapter(index, data));
            $("#chapterList").append(chapterItem);
            break;
        case "section":
            var id = ".addSection:eq("+chapterListIndex+")";//addSection按钮本身在chapter里
//			chapterItem =$(id).parents(".chapter");
//			chapterItem.after(setSection(data));
            chapterItem =$(id).parents(".chapterItem");
            sequenceCode=(chapterListIndex+1)+"."+(chapterItem.find(".section").length+1);//（章。节）
            var contentCode=(chapterItem.find(".section").length+1);//只显示节号
            chapterItem.append(setSection(contentCode,data));
            break;
        case "lesson":
            var id = ".addLesson:eq("+chapterListIndex+")";
            chapterItem =$(id).parents(".section");   //先找lesson的section
            sequenceCode = chapterItem.find(".sectionCode").html()+".";//（章。节。课）
            var contentCode="";//  只显示课号
            var x=1;
            while(1){
                if(chapterItem.next().hasClass("lesson")){   //找完lesson的section 再找section的最后一个lesson
                    chapterItem = chapterItem.next().next();//认为lesson后必以后contentItem
                    x++;
                }else{
                    break;
                }
            }
            contentCode+=x;
            sequenceCode+=x;
            chapterItem.after(setLesson(contentCode,data));//chapterItem是当前lesson的上一个lesson
            break;
        default:
            break;
    }
    registerEvent(chapterItem);
}

/**
 * 为按钮注册事件
 * @param chapterItem
 */
function registerEvent(chapterItem) {
    switch (coursewareSubmitType) {
        case "chapter":
            //添加二级菜单按钮
            $(".addSection").each(function(index,node){
                $(this).unbind("click");
                $(this).click(function(){
                    coursewareSubmitType = "section";
                    chapterListIndex = index;//$("#chapterList .addSection").index($(this));
                    $("#coursewareAdd").modal();
                    initCoursewareData();
                });
            });
            //编辑按钮 //todo
            chapterItem.find(".l1.edit").click(function(){
                $("#coursewareAdd").modal();
                /*				var data ={
                 sectionName:chapterItem.find("span.chapterTitle").html(),
                 }
                 if(!chapterItem.find(".addSection")){
                 data.knowledgePoint = $(".chapterItem .contentItem span:eq(1)").val();
                 data.coursewareOutLine = $(".chapterItem .contentItem span:eq(2)").val();
                 //					data.coursewareType = $(".chapterItem .contentItem span:eq(2)").val();
                 //					data.courseware =
                 }*/
                currentEditContentId = chapterItem.find(".chapter").attr("id");
                initCoursewareData(db[currentEditContentId]);
            });
            //删除按钮
            chapterItem.find(".l1.delete").click(function(){
                var delIds = [];
                chapterItem.find("div[id]").each(function(index){
                    delIds.push($(this).attr("id"));
                });
                var delId = chapterItem.find(".chapter").attr("id");
                request.ajax({
                    urlkey:"courseContent_deleteByIds",
                    type:"post",
                    data:{
                        courseContentIds:JSON.stringify(delIds),
                        courseId:dbCourseId,
                        contentCode:db[delId].contentCode,
                        isLeaf:db[delId].isLeaf,
                        updateUserId:"updateUserId",
                        updateDateTime:new Date().getTime()
                    }
                },{
                    success:function(){
                        for(var i = delIds.length;i--;i>=0){//i的初始值是length-1
                            delete db[delIds[i]];
                        }
                        //要删除的章节的章号
                        var delCode = $("#"+delId).parent(".chapterItem").find(".chapter .chapterCode").html().substr(1,1);
                        //更新后续的章号
                        /*						$(".chapterItem").each(function() {
                         var n = $(this).find(".chapter .chapterCode");
                         var itCode =n.html().substr(1,1);
                         if(itCode>delCode){
                         n.html("第"+(itCode-1)+"章");
                         }
                         });*/
                        $("#"+delId).parent(".chapterItem").nextAll(function(){
                            var n = $(this).find(".chapter .chapterCode");
                            n.html("第"+(n.html().substr(1,1)-1)+"章");
                        });
                        $("#"+delId).parent(".chapterItem").remove();
                    }
                });
            });
            //上移按钮
            chapterItem.find(".l1.up").click(function(){
                if($(this).parents(".chapterItem").prev()){
                    var prev = $(this).parents(".chapterItem").prev();
                    var cur = $(this).parents(".chapterItem");

                    var curId = cur.find(".chapter").attr("id");
                    var prevId= prev.find(".chapter").attr("id");

                    //交换两者的contentId
                    request.ajax({
                        urlkey:"courseContent_exchangeContentCode",
                        type:"post",
                        data:{
                            lastContentId:prevId,
                            nextContentId:curId,
                            lastContentCode:db[curId].contentCode,
                            nextContentCode:db[prevId].contentCode,
                            updateUserId:"updateUserId",
                            updateDateTime:new Date().getTime()
                        }
                    },{
                        success:function(){
                            var t =db[prevId].contentCode;
                            db[prevId].contentCode = db[curId].contentCode;
                            db[curId].contentCode = t;
                            var curChapterCode = cur.find(".chapterCode").html();
                            var prevChapterCode = prev.find(".chapterCode").html();
                            //先把章号换一下
                            cur.find(".chapterCode").html(prevChapterCode);
                            prev.find(".chapterCode").html(curChapterCode);

                            //再交换内容
                            cur.insertBefore(prev);
                        }
                    });
                }
            });
            //下移按钮
            chapterItem.find(".l1.dn").click(function(){
                if($(this).parents(".chapterItem").next()){
                    var next = $(this).parents(".chapterItem").next();
                    var cur = $(this).parents(".chapterItem");

                    var curId = cur.find(".chapter").attr("id");
                    var nextId= next.find(".chapter").attr("id");

                    //交换两者的contentId
                    request.ajax({
                        urlkey:"courseContent_exchangeContentCode",
                        type:"post",
                        data:{
                            lastContentId:curId,
                            nextContentId:nextId,
                            lastContentCode:db[nextId].contentCode,
                            nextContentCode:db[curId].contentCode,
                            updateUserId:"updateUserId",
                            updateDateTime:new Date().getTime()
                        }
                    },{
                        success:function(){
                            var t =db[nextId].contentCode;
                            db[nextId].contentCode = db[curId].contentCode;
                            db[curId].contentCode = t;
                            var curChapterCode = cur.find(".chapterCode").html();
                            var nextChapterCode = next.find(".chapterCode").html();

                            cur.find(".chapterCode").html(nextChapterCode);
                            next.find(".chapterCode").html(curChapterCode);

                            cur.insertAfter(next);
                        }
                    });
                }
            });
            break;
        case "section":
            //添加三级菜单按钮
            $(".addLesson").each(function(index,node){
                $(this).unbind("click");
                $(this).click(function(){
                    coursewareSubmitType = "lesson";
                    chapterListIndex = index;
                    $("#coursewareAdd").modal();
                    initCoursewareData();
                });
            });
            //编辑按钮
            chapterItem.find(".l2.edit").unbind("click");
            chapterItem.find(".l2.edit").click(function(){
                $("#coursewareAdd").modal();
                currentEditContentId = $(this).parents(".section").attr("id");
                initCoursewareData(db[currentEditContentId]);
            });
            //删除按钮
            chapterItem.find(".l2.delete").unbind("click");
            chapterItem.find(".l2.delete").click(function(){
                var cur = $(this).parents(".section");
                var delIds = [];
                delIds.push(cur.attr("id"));
                var delId = delIds[0];
                //寻找要删除节点的子节点
                var children = $(this).parents(".section").next();
                while (1) {
                    if(children.hasClass("contentItem")){
                        children = children.next();
                    }else if(children.hasClass("lesson")){
                        delIds.push(children.attr("id"));
                        children = children.next();
                    }else{//section //chapter
                        break;
                    }
                }

                //删除数据库数据
                request.ajax({
                    urlkey:"courseContent_deleteByIds",
                    type:"post",
                    data:{
                        courseContentIds:JSON.stringify(delIds),
                        courseId:dbCourseId,
                        contentCode:db[delId].contentCode,
                        isLeaf:db[delId].isLeaf,
                        updateUserId:"updateUserId",
                        updateDateTime:new Date().getTime()
                    }
                },{
                    success:function(){
                        //更新后续节点的节号
                        $("#"+delId).nextAll(".section").each(function(){
                            var n = $(this).find(".sectionCode");
                            n.html(n.html()-1);
                        });
                        //删除子节点及其内容
                        for(var i = delIds.length;i--;i>=0){//i的初始值是length-1
                            delete db[delIds[i]];

                            if($("#"+delIds[i]).next().hasClass("contentItem")){
                                $("#"+delIds[i]).next().remove();
                            }
                            $("#"+delIds[i]).remove();
                        }
                    }
                });
            });
            //上移按钮
            chapterItem.find(".l2.up").unbind("click");//chapterItem 下的所有二级按钮
            chapterItem.find(".l2.up").click(function(){
                if($(this).parents(".section").prev().hasClass("chapter")){
                    return;
                }

                var mvToAf = $(this).parents(".section").prev();
                while (1) {
                    if(mvToAf.hasClass("contentItem")){
                        mvToAf = mvToAf.prev();
                    }else if(mvToAf.hasClass("chapter")){//到顶了 首行已经check 不会执行到这
                        mvToAf = undefined;
                        break;
                    }else if(mvToAf.hasClass("lesson")){
                        mvToAf = mvToAf.prev();
                    }else{//只能是section了
                        break;
                    }
                }
                if(mvToAf){
                    var contentItem = undefined;
                    var cur = $(this).parents(".section");

                    if(cur.next().hasClass("contentItem")){//下边有内容
                        contentItem = cur.next();
                    }else if(cur.next().hasClass("lesson")){//下边有lesson节点
                        contentItem = [];
                        var t = cur;
                        while (1) {
                            if(t.next().hasClass("contentItem")){//一进来必没有contentItem
                                contentItem.unshift(t.next());
                                t = t.next();
                            }else if(t.next().hasClass("chapter")){//到底了 到下一章了
                                break;
                            }else if(t.next().hasClass("lesson")){
                                contentItem.unshift(t.next());
                                t = t.next();
                            }else {//section 或者最后一章的底
                                break;
                            }
                        }
                    }

                    var prevId = mvToAf.attr("id");
                    var	curId = cur.attr("id");
                    //操作数据库
                    request.ajax({
                        urlkey:"courseContent_exchangeContentCode",
                        type:"post",
                        data:{
                            lastContentId:prevId,
                            nextContentId:curId,
                            lastContentCode:db[curId].contentCode,
                            nextContentCode:db[prevId].contentCode,
                            updateUserId:"updateUserId",
                            updateDateTime:new Date().getTime()
                        }
                    },{
                        success:function(){
                            var t =db[prevId].contentCode;
                            db[prevId].contentCode = db[curId].contentCode;
                            db[curId].contentCode = t;

                            cur.insertBefore(mvToAf);

                            //交换节号
                            var curCode = cur.find(".sectionCode").html();
                            var mvToAfCode = mvToAf.find(".sectionCode").html();
                            cur.find(".sectionCode").html(mvToAfCode);
                            mvToAf.find(".sectionCode").html(curCode);

                            if(contentItem){//移动内容节点
                                if(contentItem instanceof Array){
                                    contentItem.forEach(function(node,index){
                                        node.insertAfter(cur);
                                    });
                                }
                                contentItem.insertAfter(cur);
                            }
                        }
                    });
                }
            });
            //下移按钮
            /*			chapterItem.find(".l2.dn").unbind("click");//chapterItem 下的所有二级按钮
             chapterItem.find(".l2.dn").click(function(){
             if($(this).next().hasClass("chapter")){
             return;
             }
             if($(this).next().hasClass("content")&&$(this).next().hasClass("chapter")){

             }
             });*/
            break;
        case "lesson":
            //chapterItem是目标lesson的上一个节点 可能是[节]点 可能是上一节的课contentItem点
            //编辑按钮
            chapterItem.next().find(".l3.edit").click(function(){
                $("#coursewareAdd").modal();
                currentEditContentId = $(this).parents(".lesson").attr("id");
                initCoursewareData(db[currentEditContentId]);
            });
            //删除按钮
            chapterItem.next().find(".l3.delete").click(function(){
                var cur = $(this).parents(".lesson");
                var delIds = [];
                delIds.push(cur.attr("id"));
                var delId = delIds[0];

                //删除数据库数据
                request.ajax({
                    urlkey:"courseContent_deleteByIds",
                    type:"post",
                    data:{
                        courseContentIds:JSON.stringify(delIds),
                        courseId:dbCourseId,
                        contentCode:db[delId].contentCode,
                        isLeaf:db[delId].isLeaf,
                        updateUserId:"updateUserId",
                        updateDateTime:new Date().getTime()
                    }
                },{
                    success:function(){
                        delete db[delId];
                        //更新后续课节点的课号
                        var t = $("#"+delId).next().next();
                        while (1) {
                            if(t.hasClass("contentItem")){
                                t = t.next();
                            }else if(t.hasClass("lesson")){
                                var n = t.find(".lessonCode");
                                n.html(n.html()-1);
                                t = t.next();
                            }else {//section chapter
                                break;
                            }
                        }

                        $("#"+delId).next().remove();
                        $("#"+delId).remove();
                    }
                });
            });
            //上移按钮
            //chapterItem.next().find(".l3.up").unbind("click");//应该是唯一的  不需要解绑
            chapterItem.next().find(".l3.up").click(function(){
                var cur = $(this).parents(".lesson");
                if(cur.prev().hasClass("section")){
                    return;
                }

                var mvToAf = cur.prev().prev();//只要上一个不是节  在lesson后必有section的前提下   课上边必不可能直接是章 prev().prev()则一定是兄弟节

                var prevId = mvToAf.attr("id");
                var curId = cur.attr("id");
                //操作数据库
                request.ajax({
                    urlkey:"courseContent_exchangeContentCode",
                    type:"post",
                    data:{
                        lastContentId:prevId,
                        nextContentId:curId,
                        lastContentCode:db[curId].contentCode,
                        nextContentCode:db[prevId].contentCode,
                        updateUserId:"updateUserId",
                        updateDateTime:new Date().getTime()
                    }
                },{
                    success:function(){
                        var t =db[prevId].contentCode;
                        db[prevId].contentCode = db[curId].contentCode;
                        db[curId].contentCode = t;

                        var contentItem = cur.next();

                        cur.insertBefore(mvToAf);
                        contentItem.insertAfter(cur);

                        //交换节号
                        var curCode = cur.find(".lessonCode").html();
                        var mvToAfCode = mvToAf.find(".lessonCode").html();
                        cur.find(".lessonCode").html(mvToAfCode);
                        mvToAf.find(".lessonCode").html(curCode);
                    }
                });
            });
            break;
        default:
            break;
    }
}

function setChapter(index,data){
    var s = chapterModel.children("div:eq(0)").prop("outerHTML");
    s = s.replace("{$chapterCode}",index)
        .replace("{$chapterTitle}",data.title)
        .replace("{$chapterId}",data.contentId);
    if(data.coursewareOutLine){
        s = s.replace("<a class=\"addSection btn btn-xs btn-success\">新增二级目录</a>","");
        s += setContentItem(data);
    }
    return s;
}
function setSection(contentCode,data) {
    var s = chapterModel.children("div:eq(1)").prop("outerHTML");
    s = s.replace("{$sectionCode}",contentCode)
        .replace("{$sectionTitle}",data.title)
        .replace("{$sectionId}",data.contentId);
    if(data.coursewareOutLine){
        s = s.replace("<a class=\"col-md-offset-2 addLesson btn btn-xs btn-success\">新增三级目录</a>","");
        s += setContentItem(data);
    }
    return s;
}
function setLesson(contentCode,data) {
    var s = chapterModel.children("div:eq(2)").prop("outerHTML");
    s = s.replace("{$lessonCode}",contentCode)
        .replace("{$lessonTitle}",data.title)
        .replace("{$lessonId}",data.contentId);
    s += setContentItem(data);//content必不能为空
    return s;
}
function setContentItem(data) {
//	if(data.coursewareOutLine){
    var s = chapterModel.children("div:eq(3)").prop("outerHTML");
    s = s.replace("{$knowledgePoint}",data.knowledgePoint)
        .replace("{$coursewareOutLine}",data.coursewareOutLine)
        .replace("{$coursewareName}",data.coursewareName);
    return s;
//	}
//	return "";
}

//检查界面数据
/*function checkCourse(){
 var checkIds = ["#courseName","#courseOutline","#courseHour","#courseCredit","#courseType",
 "#lecturer","#courseTime","#courseCredit","#isOpenClass"];
 var warning = false;
 checkIds.some(function(id,index){
 if(!$(id).val()){
 $("#alertWarning").css("display","block");
 console.log("xxx");
 warning = true;
 return true;
 }
 });
 return warning;
 }*/

function checkCourse(){
    var ids = ["#courseName", "#courseOutline", "#courseHour", "#courseCredit",
        "#lecturer", "#courseTime", "#courseCredit", "#isOpenClass",
        "#courseType", "#certificateTemplate", "#forPost"];
    var html = "";
    for(var i=ids.length-1;i>=0;i--){
        if(!$(ids[i]).val()){
            html+=ids[i]+", ";
        }
    }
    if(html){
        alert(html.substring(0, html.length-2)+"不能为空");
        return true;
    }
    return false;
}

function alerts(msg){
    $("#generalDialog").find(".modal-body H4").html(msg);
    $("#generalDialog").modal();
}

//添加章节时自动保存课程
function saveCourse(publishStatus){
    if(checkCourse())return true;
    attachments = [];
    $("#referenceTable tbody tr").each(function(){
        attachments.push($(this).attr("id"));
    });
    request.ajax({
            urlkey:"course_insert",
            type:"post",
            data:{
//            courseId:$("").val(),
                courseName:$("#courseName").val(),
                summary:$("#courseOutline").val(),
                classHour:$("#courseHour").val(),
                credits:$("#courseCredit").val(),
                label:$("#courseType").val(),
                publishStatus:publishStatus||2,//草稿状态
                publishDate:new Date().getTime(),
                teachersId:$("#lecturer").val(),
                thumbnailId:thumbnailId,//$("").val(),
                duration:$("#courseTime").val(),
                lang:1,//简体中文//$("").val(),
                score:$("#courseCredit").val(),//$("").val(),
                isPublic:$("#isOpenClass").val(),
//            isBBS:$("").val(),
//            isOnline:$("").val(),
//            isEvaluation:$("").val(),
//            isChat:$("").val(),
//            isCertificate:$("#certificateTemplate").val(),
//            isBeforeTest:$("").val(),
//            isAfterTest:$("").val(),
//            deleteFlag:$("").val(),
//            deleteDateTime:$("").val(),
//            deleteUserId:$("").val(),
                insertUserId:"insertUserId",
                insertDateTime:new Date().getTime(),
//            updateUserId:$("").val(),
//            updateDateTime:$("").val(),
                courseType:$("#courseType").val(),
                certificate:$("#certificateTemplate").val(),
                attachmentResourceIds:attachments.join(","),
            }
        },{
            success:function (res) {
                dbCourseId = res.data;
                console.log("课程数据已存入数据库");
            },
            fail:function (data) {
                console.log(data);
            }
        }
    );
}
