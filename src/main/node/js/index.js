"use strict"; 

var camera, scene, renderer;
var canvasWidth, canvasHeight;
var windowScale;
var socket = io('ws://10.193.134.62:23810');


function IOUtils() {
	socket.on('xxx',async function(message){
		console.log('hello' + message)
	});
	socket.on('cube',async function(message){
		console.log('create cube:' + message)
		var cube = randomCube();
		scene.add(cube);
		objects.push(cube);
	});

	socket.on('changeCanvasSize', async function(message) {
		var canvasSize = JSON.parse(message);
		setCanvasSize(canvasSize.width, canvasSize.height, '#c0c0c0');
	});

	socket.on('receiveGeometry', async function(message){
		// clear scene
		while(scene.children.length > 0){ 
			scene.remove(scene.children[0]); 
		}

		console.log('scene cleared')
		setCanvasSize(controls.width, controls.height, controls.background);
		// get geometry
		parseGeometry(JSON.parse(message));
		
		renderer.render( scene, camera );

	});

}

function setCanvasSize(width, height, background) {

	camera = new THREE.OrthographicCamera( width / - 2, width / 2, height / 2, height / - 2, 0, 40 )
	var focus = new THREE.Vector3( width / 2, height / 2, 0 );
	camera.position.x = focus.x;
	camera.position.y = focus.y;
	camera.position.z = 10;
	camera.lookAt(focus);

	renderer.setSize(width, height);
	console.log(background);
	
	renderer.setClearColor(new THREE.Color(background));
	renderer.render( scene, camera );
}

function saveAsImage() {
	var imgData;

	try {
		var strMime = "image/jpeg";
		imgData = renderer.domElement.toDataURL(strMime);
		console.log(imgData);

		saveFile(imgData.replace(strMime, "image/octet-stream"), new Date().valueOf() + ".jpg");

	} catch (e) {
		console.log(e);
		return;
	}

}

var saveFile = function (strData, filename) {
	var link = document.createElement('a');
	if (typeof link.download === 'string') {
		document.body.appendChild(link); //Firefox requires the link to be in the body
		link.download = filename;
		link.href = strData;
		link.click();
		document.body.removeChild(link); //remove the link when done
	} else {
		location.replace(uri);
	}
}

function parseGeometry(geomJson) {
	var geo = new THREE.Geometry();
	var flag = true;
	for(var i = 0; i < geomJson.verts.length; ++ i) {
		var vt = geomJson.verts[i];
		geo.vertices.push(new THREE.Vector3(vt[0], vt[1], vt[2]));
		flag &= true;
	}
	
	for(var i = 0; i < geomJson.faces.length; ++ i) {
		var fs = geomJson.faces[i];
		geo.faces.push(new THREE.Face3(fs[0], fs[1], fs[2]));
		flag &= true;
	}
	var material = new THREE.MeshBasicMaterial( { color: controls.typeColor, side: THREE.DoubleSide } );

	if(flag) {
		var mesh = new THREE.Mesh( geo, material );
		scene.add( mesh )
	}

	for(var i = 0; i < geomJson.circles.length; ++ i) {
		var c = geomJson.circles[i];
		var circle = new THREE.CircleGeometry(c[3], 32);
		circle.translate(c[0], c[1], c[2]);
		var meshc = new THREE.Mesh(circle, material);
		scene.add( meshc );
	}

}

function loadLogo() {
	var textureLoader = new THREE.TextureLoader();
	var texture = textureLoader.load('textures/AAA.jpg');

	var geo = new THREE.PlaneGeometry(1000, 1000);
	geo.translate(500, 500, 0);

	var material = new THREE.MeshBasicMaterial({map:texture});

	var mesh = new THREE.Mesh(geo, material);
	scene.add( mesh );
}


function init() {

	IOUtils();

	// scene
	scene = new THREE.Scene();
	scene.autoUpdate = true;
	renderer = new THREE.WebGLRenderer({ antialias: true, preserveDrawingBuffer: true});

	
	setCanvasSize(window.innerWidth, window.innerHeight, '#c0c0c0');


	socket.emit('initCanvas', 'ww');
}

function addToDOM() {
	var container = document.getElementById('container');
	var canvas = container.getElementsByTagName('canvas');
	if (canvas.length>0) {
		container.removeChild(canvas[0]);
	}
	container.appendChild( renderer.domElement );
}


// Main body of the script
try {
	init();
	loadLogo();
	addToDOM();
} catch(e) {
	var errorReport = "Your program encountered an unrecoverable error, can not draw on canvas. Error was:<br/><br/>";
	$('#container').append(errorReport+e);
}
