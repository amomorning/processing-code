"use strict";
var controls = new function () {
    this.LinNumX = 3;
    this.LinNumY = 4;
    this.add = function() {
        var object = randomCube();
        scene.add(object);
        objects.push(object);
    };
    this.hello = function() {
        socket.emit('event', 'hello from gui');
    };
}

var gui = new dat.GUI();
gui.add(controls, 'LinNumX', 2, 13).step(1);
gui.add(controls, 'LinNumY', 2, 13).step(1);
gui.add(controls, 'add');
gui.add(controls, 'hello');
