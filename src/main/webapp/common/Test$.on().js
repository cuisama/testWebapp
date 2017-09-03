/**
 * Created by yxcui on 2017/8/21.
 */
$().ready(function () {
    //$(document).off();不会将01、02、03的点击事件off掉
    // $("#01").on("click",function () {
    //    console.log("01_最大的");
    // });
    // $("#02").on("click",function (e) {
    //    console.log("02_中间的");
    //    e.preventDefault();
    // });
    // $("#03").on("click",function () {
    //    console.log("03_最小的");
    // });


    $("#04").on("click",function () {
        console.log("04_off_document");
        $(document).off();
    });
    
    //$(document).off();会将01、02、03的点击事件off掉
    /**
     * 上边：只能为页面现有的元素绑定点击事件，如果是动态生成的新的元素，是没有事件的
     * 下边：将指定的事件绑定在document上，而新产生的元素如果符合指定的元素，那就触发此事件
     */
    $(document).on("click","#01",function (e) {
        console.log("01_最大的");
    });
    $(document).on("click","#02",function (e) {
        console.log("02_中间的");
        /**
         * 该方法将通知 Web 浏览器不要执行与事件关联的默认动作（如果存在这样的动作）。
         * 例如，如果 type 属性是 "submit"，在事件传播的任意阶段可以调用任意的事件句柄，通过调用该方法，可以阻止提交表单。
         * 注意，如果 Event 对象的 cancelable 属性是 fasle，那么就没有默认动作，或者不能阻止默认动作。
         * 无论哪种情况，调用该方法都没有作用。
         */
        e.preventDefault();
        return false;//会阻止事件冒泡
    });
    $(document).on("click","#03",function (e) {
        console.log("03_最小的");
    });



});
