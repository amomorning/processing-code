package test;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.awt.*;


/**
 * @program: Sample3D
 * @description:
 * @author: Naturalpowder
 * @create: 2021-03-15 16:41
 **/
public class Test_FXPExample extends FXWithJFrame<Test_Processing3DWithJFrame> {

    @Override
    public void initFrame() {
        frame.add(fxPanel, BorderLayout.WEST);
        frame.add(canvas, BorderLayout.CENTER);
    }

    @Override
    public void settings() {
        frameSize(1000, 600);
    }

    @Override
    public Scene createScene() {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 300, 200);
        javafx.scene.control.TextArea text = new javafx.scene.control.TextArea();
        text.setPromptText("Welcome JavaFX!");
        root.setCenter(text);

        Button button = new Button("Add Point");
        root.setBottom(button);

        button.setOnMouseClicked(event -> {
            applet.update();
            text.setText(applet.getInfo());
        });

        Menu menu = new Menu("文件");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        MenuItem menuItem = new MenuItem("打开");
        menu.getItems().add(menuItem);
        root.setTop(menuBar);
        menuItem.setOnAction(event -> {
            System.out.println("你好");
        });
        return scene;
    }

    public static void main(String[] args) {
        Test_FXPExample test_fxpExample = new Test_FXPExample();
        test_fxpExample.launch("Title", new Test_Processing3DWithJFrame());
    }
}