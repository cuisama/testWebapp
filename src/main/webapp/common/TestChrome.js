/**
 * Created by yxcui on 2017/8/21.
 */
$().ready(function () {

    $("#btn01").click(function (e) {
        $("#01").append("<span>this is span! </span>");
    });

    $("#btn02").click(function (e) {
        setTimeout(function (s) {
            console.log("11");
        }),1000;
    });
    var s = confirm("adf");
    console.log(s);
});