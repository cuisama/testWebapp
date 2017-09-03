/**
 * Created by xiaoc on 2017/6/21.
 */
define(function (require, exports,module) {

    console.log("no exports : init");
    var m1 = require("M1");
    exports.init= function () {
        var m2 = require("M2");
        m2.run();
    }
});