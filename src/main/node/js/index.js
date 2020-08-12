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
		setCanvasSize(canvasSize.width, canvasSize.height);
	});

}

function setCanvasSize(width, height) {
	var canvasRatio = width / height;
	windowScale = 12;
	var windowWidth = windowScale * canvasRatio;
	var windowHeight = windowScale;

	camera = new THREE.OrthographicCamera( windowWidth / - 2, windowWidth / 2, windowHeight / 2, windowHeight / - 2, 0, 40 )
	var focus = new THREE.Vector3( 5,5,0 );
	camera.position.x = focus.x;
	camera.position.y = focus.y;
	camera.position.z = 10;
	camera.lookAt(focus);

	renderer.setSize(width, height);
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


function PolygonGeometry(sides, location, radius) {
	var geo = new THREE.Geometry();

	// generate vertices
	for ( var pt = 0 ; pt < sides; pt++ )
	{
		// Add 90 degrees so we start at +Y axis, rotate counterclockwise around
		var angle = (Math.PI/2) + (pt / sides) * 2 * Math.PI;

		var x = radius * Math.cos(angle) + location.x;
		var y = radius * Math.sin(angle) + location.y;

		// Save the vertex location
		geo.vertices.push( new THREE.Vector3( x, y, 0.0 ) );
	}

	// generate faces
	for ( var face = 0 ; face < sides-2; face++ )
	{
		// this makes a triangle fan, from the first +Y point around
		geo.faces.push( new THREE.Face3( 0, face+1, face+2 ) );
	}
	// done: return it.
	return geo;
}

function init() {

	IOUtils();

	// scene
	scene = new THREE.Scene();
	renderer = new THREE.WebGLRenderer({ antialias: false, preserveDrawingBuffer: true});

	
	setCanvasSize(window.innerWidth, window.innerHeight);

	renderer.gammaInput = true;
	renderer.gammaOutput = true;
	renderer.setClearColor(new THREE.Color(0xFFFFFF));

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
	var geo = PolygonGeometry(9, new THREE.Vector3( 5, 5, 0 ), 4);
	var material = new THREE.MeshBasicMaterial( { color: 0xff0000, side: THREE.FrontSide } );
	var mesh = new THREE.Mesh( geo, material );
	scene.add( mesh );
	addToDOM();
} catch(e) {
	var errorReport = "Your program encountered an unrecoverable error, can not draw on canvas. Error was:<br/><br/>";
	$('#container').append(errorReport+e);
}
