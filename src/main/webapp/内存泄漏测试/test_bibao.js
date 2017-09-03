/**
 * Created by xiaoc on 2017/9/2.
 */
(function(){
    var a = 1;
    setA = function(val){
        a = val;
    };
    getA = function(){
        return a;
    };
})();
// console.log(a); //报错
console.log(getA()); //1
setA(2);   //上边定义的时候没写var 是全局变量，所以可以直接调
console.log(getA()); //2