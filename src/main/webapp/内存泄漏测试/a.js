/**
 * Created by xiaoc on 2017/8/20.
 */
function aa() {
    a =  new Array(1000000).join("*");
}
aa();

var a ;
function bb(i) {
    return function bb_1(){
        a = new Array(1000000).join(i);
    }
}
function cc() {
    console.log("11");
    for(var i=0;i<10;i++){
        bb(i)();
    }
}

cc();