package main;

import controlP5.ColorWheel;
import controlP5.ControlP5;
import controlP5.DropdownList;
import processing.core.PApplet;

import java.io.File;

/**
 * @classname: processing-code
 * @description:
 * @author: amomorning
 * @date: 2020/08/23
 */
public class Controller extends PApplet {
    ControlP5 cp5;
    Controls controls;
    ColorWheel bkg, type;
    DropdownList list;

    public void settings() {
        size(250, 800);

        // 需要手动对controls初始化
        controls = new Controls();
        controls.width = 1000;
        controls.height = 1000;
        controls.sizPoi_min = 0.5f;
        controls.sizPoi_max = 0.5f;
        controls.angPoi = 0.0f;
        controls.LinNum_X = 4;
        controls.LinNum_Y = 4;
        controls.shiL_Y = false;
        controls.shiL_X = false;
        controls.shiP_X = false;
        controls.shiP_Y = false;
        controls.seed = 233;
        controls.selPoi = 1.0f;
    }


    public void setup() {
        background(0);
        cp5 = new ControlP5(this);

        // 控制gui中控件位置的参数
        // 使用 += 方便用相对位置进行定位
        int onX = 10;
        int onY = 10;

        // 每个控件名称为Control中的变量名，且需要用plugTo绑定到对应的controls对象
        // 且需要用 setValue 方法赋值为controls中对应的变量值
        // 滑动条
        cp5.addSlider("width", 0, 5000).setValue(controls.width).setPosition(onX, onY).plugTo(controls);
        // 滑动条
        cp5.addSlider("height",0, 5000).setValue(controls.height).setPosition(onX, onY+=20).plugTo(controls);
        // 色轮
        bkg = cp5.addColorWheel("setBackground").setPosition(onX, onY+=20);
        // 滑动条
        cp5.addSlider("sizPoi_max", 0, 1.5f).setValue(controls.sizPoi_max).setPosition(onX, onY += 230).plugTo(controls);
        // 滑动条
        cp5.addSlider("sizPoi_min", 0, 1).setValue(controls.sizPoi_min).setPosition(onX, onY += 20).plugTo(controls);
        // 下拉列表
        // 注意下拉列表三角朝上才能使用下面那个滑动条，可能是cp5的bug
        list = cp5.addDropdownList("setShaPoi").setPosition(onX, onY += 20);
        list.addItem("square", 0).setLabel("square");
        list.addItem("triangle", 1).setLabel("triangle");
        list.addItem("circle", 2).setLabel("circle");
        // 滑动条
        cp5.addSlider("angPoi", 0, 360).setValue(controls.angPoi).setPosition(onX, onY += 60).plugTo(controls);
        //色轮
        type = cp5.addColorWheel("setTypeColor").setPosition(onX, onY += 20);
        // 滑动条
        cp5.addSlider("LinNum_X", 1, 20).setValue(controls.LinNum_X).setPosition(onX, onY += 230).plugTo(controls);
        // 滑动条
        cp5.addSlider("LinNum_Y", 1, 20).setValue(controls.LinNum_Y).setPosition(onX, onY += 20).plugTo(controls);
        // 布尔值
        cp5.addToggle("shiL_X").setPosition(onX, onY += 20).plugTo(controls);
        // 布尔值
        cp5.addToggle("shiL_Y").setPosition(onX + 50, onY).plugTo(controls);
        // 布尔值
        cp5.addToggle("shiP_X").setPosition(onX + 100, onY).plugTo(controls);
        // 布尔值
        cp5.addToggle("shiP_Y").setPosition(onX + 150, onY).plugTo(controls);
        // 滑动条
        cp5.addSlider("seed", 0, 30000).setValue(controls.seed).setPosition(onX, onY += 40).plugTo(controls);
        // 滑动条
        cp5.addSlider("selPoi", 0, 1.0f).setValue(controls.selPoi).setPosition(onX, onY += 20).plugTo(controls);
        // 按钮，按钮上绑定同名方法
        cp5.addButton("updateAll").setPosition(onX, onY += 20);
    }

    public void draw() {

    }


    /**
     * 与 button 同名函数，按下时执行
     * 如果需要测试是否对controls中变量赋值可以在这里打印
     */
    public void updateAll() {

        // 颜色需要转为16进制代码
        controls.background = rgb2Hex(new int[] {bkg.r(), bkg.g(), bkg.b()});
//        System.out.println(controls.background);
        controls.typeColor = rgb2Hex(new int[] {type.r(), type.g(), type.b()});
//        System.out.println(controls.typeColor);

        // 下拉框
        controls.shaPoi = list.getLabel();
//        System.out.println(list.getLabel());

        // cp5 的 toggle 迷之没有绑定，所以手动绑定一下
        // cp5 通用的 getValue() 方法返回浮点数 这里 1.0f 为 true，0.0f 为 false
        controls.shiL_X = cp5.get("shiL_X").getValue() > 0;
        controls.shiL_Y = cp5.get("shiL_Y").getValue() > 0;
        controls.shiP_X = cp5.get("shiP_X").getValue() > 0;
        controls.shiP_Y = cp5.get("shiP_Y").getValue() > 0;
//        System.out.println(controls.shiL_X);


        // 对Show中静态变量controls赋值并执行静态方法parseGeometry()
        Show.controls = controls;
        Show.parseGeometry(controls);
    }


    public String rgb2Hex(int[] rgb) {
        String hex = "#";
        for(int i = 0; i < 3; ++ i) {
            // 格式化为16进制且补零对齐
            hex += String.format("%02x", rgb[i]);
        }
        return hex;
    }
}
