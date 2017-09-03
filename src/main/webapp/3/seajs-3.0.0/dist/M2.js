/**
 * Created by xiaoc on 2017/6/21.
 */
define(function (require,exports,module) {
    console.log("no exports : m2");
    var m1 = require("M1");
    exports.run= function () {
        m1.run();
        console.log("this is M2");
    }

});