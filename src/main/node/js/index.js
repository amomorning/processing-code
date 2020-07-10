"use strict"; // good practice - see https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Strict_mode
////////////////////////////////////////////////////////////////////////////////
// Picking: pick object and change its color and place a sphere
////////////////////////////////////////////////////////////////////////////////

/*global THREE, document, window*/

// Mostly grabbed from http://mrdoob.github.com/three.js/examples/canvas_interactive_cubes.html
// Author unknown.

var controls;
var camera, scene, projector, renderer;
var sphereMaterial;

var objects = [];
var headlight;

var sphereGeom;

var canvasWidth;
var canvasHeight;
var clock = new THREE.Clock();

var stats;

init();
animate();

function init() {
	canvasWidth = window.innerWidth;
	canvasHeight = window.innerHeight;
	var canvasRatio = canvasWidth / canvasHeight;

	camera = new THREE.PerspectiveCamera( 70, canvasRatio, 1, 10000 );
	camera.position.set( 0, 300, 500 );

	initScene();

	initLight();

	var geometry = new THREE.CubeGeometry( 100, 100, 100 );

	for ( var i = 0; i < 10; i ++ ) {

		var object = new THREE.Mesh( geometry,
			new THREE.MeshLambertMaterial(
				{ color: Math.random() * 0xFFFFFF } )
				//, opacity: 0.5, transparent: true } ) );
			);

		// add a random box between -400 to +400
		object.position.x = Math.random() * 800 - 400;
		object.position.y = Math.random() * 800 - 400;
		object.position.z = Math.random() * 800 - 400;

		// make box randomly scale by 1 to 3x
		object.scale.x = Math.random() * 2 + 1;
		object.scale.y = Math.random() * 2 + 1;
		object.scale.z = Math.random() * 2 + 1;

		// probably not uniformly distributed rotations, but that's OK
		// See Arvo, "Graphics Gems 3", p. 117 for the right method.
		object.rotation.x = Math.random() * 2 * Math.PI;
		object.rotation.y = Math.random() * 2 * Math.PI;
		object.rotation.z = Math.random() * 2 * Math.PI;

		scene.add( object );

		objects.push( object );

	}

	sphereMaterial = new THREE.MeshBasicMaterial( { color: 0xD0D0D0 } );


	renderer = new THREE.WebGLRenderer( { antialias: true } );
	renderer.setSize(canvasWidth, canvasHeight);
	renderer.setClearColor(new THREE.Color(0xFFFFFF));
	document.body.appendChild(renderer.domElement);

	var container = document.getElementById('container');
	container.appendChild( renderer.domElement );

	sphereGeom = new THREE.SphereGeometry( 6, 12, 6 );

	document.addEventListener( 'mousedown', onDocumentMouseDown, false );


	initStats();
	initGUI();
	initControls();


	window.addEventListener('resize', onResize, false);

}


function initScene() {
	scene = new THREE.Scene();
    var axesHelper = new THREE.AxesHelper( 5000 );
    scene.add( axesHelper );
}

function initLight() {
	headlight = new THREE.PointLight( 0xFFFFFF, 0.3 );

	scene.add( headlight );

	var light = new THREE.DirectionalLight( 0xFFFFFF, 0.6 );
	light.position.set( 200, 500, 500 );

	scene.add( light );

	light = new THREE.DirectionalLight( 0xFFFFFF, 0.6 );
	light.position.set( 0, 200, -300 );

	scene.add( light );

	light = new THREE.DirectionalLight( 0xFFFFFF, 0.4 );
	light.position.set( -400, -300, 200 );

	scene.add( light );

}


function initGUI() {
	var controls = new function () {
		this.ssss = 0.5;
		this.test = 4;
	}

	var gui = new dat.GUI();
	gui.add(controls, 'ssss', 0, 1);
	gui.add(controls, 'test', 2, 13 );
}

function onResize() {
	camera.aspect = window.innerWidth / window.innerHeight;
	camera.updateProjectionMatrix();
	renderer.setSize(window.innerWidth, window.innerHeight);
}

function onDocumentMouseDown( event ) {

	var raycaster = new THREE.Raycaster(); // create once
	var mouse = new THREE.Vector2(); // create once
	mouse.x = ( event.clientX / renderer.domElement.clientWidth ) * 2 - 1;
	mouse.y = - ( event.clientY / renderer.domElement.clientHeight ) * 2 + 1;
	
	raycaster.setFromCamera( mouse, camera );
	
	var intersects = raycaster.intersectObjects( objects, false );
		


	if ( intersects.length > 0 ) {

		intersects[0].object.material.color.setRGB( Math.random(), Math.random(), Math.random() );

		var sphere = new THREE.Mesh( sphereGeom, sphereMaterial );

		console.log(intersects)
		sphere.position.x = intersects[0].point.x;
		sphere.position.y = intersects[0].point.y;
		sphere.position.z = intersects[0].point.z;
		scene.add( sphere );
	}


}
function initControls() {

    controls = new OrbitControls(camera, renderer.domElement);

    // 使动画循环使用时阻尼或自转 意思是否有惯性
    controls.enableDamping = true;
    //动态阻尼系数 就是鼠标拖拽旋转灵敏度
    controls.dampingFactor = 0.25;

    controls.minDistance = 20;
    controls.maxDistance = 6000;
    controls.enablePan = false;
    controls.enableZoom = true;
}


function initStats() {
	stats = new Stats();
	document.body.appendChild(stats.dom);
}

//

function animate() {
	stats.update();
	window.requestAnimationFrame(animate);
	renderer.render( scene, camera );
}
