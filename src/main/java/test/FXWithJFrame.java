package test;

import com.jogamp.newt.awt.NewtCanvasAWT;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import processing.core.PApplet;
import processing.opengl.PSurfaceJOGL;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public abstract class FXWithJFrame<T extends PApplet> {
    protected int frameWidth, frameHeight, fxWidth = 100, fxHeight = 100;
    protected T applet;
    protected NewtCanvasAWT canvas;
    protected JFXPanel fxPanel;
    protected JFrame frame;
    protected String title = "FX & Processing", className = "";

    private void initAndShowGUI() {
        // This method is invoked on the EDT thread
        frame = new JFrame(title);
        fxPanel = new JFXPanel();
        frame.setSize(frameWidth, frameHeight);
        fxPanel.setSize(fxWidth, fxHeight);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create your sketch
        PSurfaceJOGL ps = (PSurfaceJOGL) applet.getSurface();
        ps.initOffscreen(applet);
        ps.setLocation(0, 0);
        // add canvas to JFrame (used as a Component)
        canvas = (NewtCanvasAWT) ps.getComponent();
        initFrame();
        Platform.runLater(() -> {
            Scene scene = createScene();
            fxPanel.setScene(scene);
        });
        ps.startThread();
    }

    public void frameSize(int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    public void FXSize(int fxWidth, int fxHeight) {
        this.fxWidth = fxWidth;
        this.fxHeight = fxHeight;
    }

    public abstract void settings();

    public abstract void initFrame();

    public abstract Scene createScene();

    public void launch(String title, T applet) {
        this.title = title;
        this.applet = applet;
        settings();
        SwingUtilities.invokeLater(this::initAndShowGUI);
    }
}