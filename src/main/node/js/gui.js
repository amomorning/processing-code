var controls = new function () {
    this.ssss = 0.5;
    this.test = 4;
}

var gui = new dat.GUI();
gui.add(controls, 'ssss', 0, 2);
gui.add(controls, 'test', 2, 13 );
