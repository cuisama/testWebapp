/**
 * Created by yxcui on 2017/9/1.
 */
$().ready(function () {

    /**
     * 规范要求，只有在同一个元素上相继触发 mousedown 和 mouseup 事件，才会触发 click 事件；如果 mousedown 或 mouseup 中的一个被取消，就不会触发 click 事件。

     　　这句话也很好理解，有时候我们在浏览网页时，鼠标在一个按钮或者链接上按下了，但是突然却又改了主意，此时我们一般会移开鼠标，在另一个空白处松开鼠标哈哈~
     *
     * 鼠标按下和鼠标抬起两个事件都有，才会触发点击事件，当断点打在mousedown事件中时，浏览器检测到鼠标按下事件后，因断点的缘故
     * 鼠标抬起事件就检测不到了，所以浏览器认为并没有click事件发生，就触发不了click函数
     */
    $("#div01").bind("mousedown",function () {
        console.log("mousedown");
    });
    $("#div01").bind("click",function () {
        console.log("click");
    });
});