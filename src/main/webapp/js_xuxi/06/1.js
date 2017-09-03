/**
 * Created by xiaoc on 2017/8/12.
 */

function per() {
    this.getName = function (name) {
        console.log(name);
    }
}

per.prototype.getAge = function (age) {
    console.log(age);
}
var a = {};
//如果per是空对象，a就完全继承了per
a.__proto__ = per.prototype;

a.getAge(2);
// a.getName("name");

//简单方法实现继承，js中无法实现多继承
var b = {};
b.__proto__ = new per();
b.__proto__.constructor = b;
b.getAge('b.age')
b.getName('b.name')