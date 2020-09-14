"use strict";
import * as THREE from 'three'
import socket from '@/socket'
import * as gui from '@/assets/js/gui'
var OrbitControls = require('three-orbit-controls')(THREE)

var offsetHeight = 190;
var renderer;

function IOUtils() {
    socket.on('receiveGeometry', async function (message) {
        // clear scene
        while (scene.children.length > 0) {
            scene.remove(scene.children[0]);
        }

        console.log('scene cleared')
        let w = gui.controls.width, h = gui.controls.height;
        if (gui.controls.sidSta % 2 == 1) {
            let temp = w;
            w = h;
            h = temp;
        }
        // get geometry
        parseGeometry(JSON.parse(message));

        renderer.render(scene, camera);

    });

}

function parseGeometry(geomJson) {

    let flag = true;
    const geometry = new THREE.Geometry();
    const vertices = geomJson.verts;
    for (let i = 0; i < vertices.length; i++) {
        geometry.vertices.push(new THREE.Vector3(vertices[i][0], vertices[i][1], vertices[i][2]));
        flag &= true;
    }
    const faces = geomJson.faces;
    const colors_faces = geomJson.colors_faces;
    let mats = [];
    for (let i = 0; i < faces.length; i++) {
        geometry.faces.push(new THREE.Face3(faces[i][0], faces[i][1], faces[i][2]));
        let material = new THREE.MeshBasicMaterial({color: colors_faces[i], side: THREE.DoubleSide});
        geometry.faces[i].materialIndex = i;
        mats.push(material);
        flag &= true;
    }
    if (flag) {
        let mesh = new THREE.Mesh(geometry, mats);
        scene.add(mesh);
    }

    for (let i = 0; i < geomJson.wires.length; i++) {
        let pts = [];
        let lineGeometry = new THREE.BufferGeometry();
        let indexes = geomJson.wires[i];
        for (let j = 0; j < indexes.length; j++) {
            let p = vertices[indexes[j]]
            pts.push(p[0], p[1], p[2]);
        }
        let p = vertices[indexes[0]];
        pts.push(p[0], p[1], p[2]);
        lineGeometry.setAttribute('position', new THREE.Float32BufferAttribute(pts, 3));
        const lineMaterial = new THREE.LineBasicMaterial({color: geomJson.colors_wires[i], linewidth: 40});
        let line = new THREE.Line(lineGeometry, lineMaterial);
        scene.add(line);
    }

    for (let i = 0; i < geomJson.circles.length; ++i) {
        const c = geomJson.circles[i];
        const circle = new THREE.CircleGeometry(c[3], 32);
        circle.translate(c[0], c[1], c[2]);
        let material = new THREE.MeshBasicMaterial({
            color: geomJson.colors_circles[i],
            side: THREE.DoubleSide,
            wireframe: true
        });
        const meshc = new THREE.Mesh(circle, material);
        scene.add(meshc);
    }
}



function initRender() {
    renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setSize(window.innerWidth, window.innerHeight - offsetHeight);
    addToDOM();
}

var camera;
function initCamera() {
    camera = new THREE.PerspectiveCamera(45, window.innerWidth / (window.innerHeight - offsetHeight), 1, 10000);
    camera.position.set(100, -150, 100);
    camera.up = new THREE.Vector3(0, 0, 1)
}

var scene;
function initScene() {
    scene = new THREE.Scene();
    scene.background = new THREE.Color( 0xffffff );
    var axesHelper = new THREE.AxesHelper( 5000 );
    scene.add( axesHelper );
}

var light;
function initLight() {
    scene.add(new THREE.AmbientLight(0x404040));

    light = new THREE.DirectionalLight(0xffffff, 1);
    light.position.set(10, -20, 15);
    scene.add(light);
}

//用户交互插件 鼠标左键按住旋转，右键按住平移，滚轮缩放
var controls;
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

function render() {
    renderer.render(scene, camera);
}

//窗口变动触发的函数
function onWindowResize() {
    camera.aspect = window.innerWidth / (window.innerHeight - offsetHeight);
    camera.updateProjectionMatrix();
    render();
    renderer.setSize(window.innerWidth, window.innerHeight - offsetHeight);

}

function animate() {
    //更新控制器
    controls.update();
    render();

    requestAnimationFrame(animate);

}
function init() {
    IOUtils();
    initRender();
    initScene();
    initCamera();
    initLight();
    initControls();

    animate();
    window.onresize = onWindowResize;

}

function addToDOM() {
    var container = document.getElementById('container');
    var canvas = container.getElementsByTagName('canvas');
    if (canvas.length > 0) {
        container.removeChild(canvas[0]);
    }
    container.appendChild(renderer.domElement);
}


function main() {
    init();
}

export {
    main
}