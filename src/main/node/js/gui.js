"use strict";
var controls = new function () {
    this.sizPoi = 0.5;
    this.sizRnd = false;
    this.shaPoi = 'square';
    this.angPoi = 0.0;
    this.typeColor = '#000000';

    this.LinNum_X = 4;
    this.LinNum_Y = 4;
    this.shiL_X = false;
    this.shiL_Y = false;
    this.shiP_X = false;
    this.shiP_Y = false;
    this.seed = 233;
    this.selPoi = 1;
    

    this.update = function() {
        socket.emit('parametersExchange', JSON.stringify(this));
    };
    this.save = function() {
        saveAsImage();
    }
}

var gui = new dat.GUI();
var element = gui.addFolder('Element Parameters');
element.add(controls, 'sizPoi', 0, 1);
element.add(controls, 'sizRnd');
element.add(controls, 'shaPoi', [ 'square', 'circle', 'triangle' ] );
element.add(controls, 'angPoi', 0, 360).step(1);
element.addColor(controls, 'typeColor');
element.open();

var space = gui.addFolder('Space Parameters');
space.add(controls, 'LinNum_X', 1, 15).step(1);
space.add(controls, 'LinNum_Y', 1, 15).step(1);
space.add(controls, 'shiL_X');
space.add(controls, 'shiL_Y');
space.add(controls, 'shiP_X');
space.add(controls, 'shiP_Y');
space.add(controls, 'seed', 0, 10000).step(1);
space.add(controls, 'selPoi', 0, 1);

space.open();


var util = gui.addFolder('Utils');
util.add(controls, 'update');
util.add(controls, 'save');
util.open();
