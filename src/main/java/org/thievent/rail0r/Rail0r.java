package org.thievent.rail0r;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.inputmap.InputMap;
import com.sun.javafx.scene.control.inputmap.KeyBinding;


public class Rail0r extends Application {
    private final BorderPane root = new BorderPane();
    private final Scene scene = new Scene(root, 300, 250);

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sample Canvas");

        root.setTop( new TextField());
        MyControl control = new MyControl();
        root.setCenter(control);
        stage.setScene(scene);
        stage.sizeToScene();
        Platform.runLater( control::requestFocus );
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    class MyControl extends Control {

        private final StringBuilder buffer = new StringBuilder();

        public MyControl(){
            setPrefSize(400, 400 );
            setFocusTraversable(true);
            setOnMouseClicked(ev-> requestFocus());
            requestFocus();
            setOnKeyTyped(ev-> { if ( !ev.isShortcutDown() ){
                addTextToBuffer( ev.getCharacter() );
            }});
        }
        public void addTextToBuffer( String str ){
            buffer.append( str );
            ((MyControlSkin)getSkin()).paintCanvas();
        }

        public String getText(){ return buffer.toString(); }

        @Override protected Skin<?> createDefaultSkin() {
            return new MyControlSkin(this);
        }
    }

    class MyControlSkin extends SkinBase<MyControl> {

        final BorderPane borderPane = new BorderPane();
        final ScrollBar rightScroll = new ScrollBar();
        final Canvas canvas = new Canvas();
        final MyControlBehavior behavior;

        public MyControlSkin(MyControl control) {
            super(control);
            behavior = new MyControlBehavior( control );
            rightScroll.setOrientation(Orientation.VERTICAL);
            borderPane.setRight( rightScroll );
            borderPane.setCenter(canvas);
            canvas.setWidth( 150);
            canvas.setHeight( 150 );
            getChildren().add( borderPane );
            paintCanvas();
        }

        public void paintCanvas(){
            GraphicsContext gr = canvas.getGraphicsContext2D();
            gr.clearRect( 0,0, canvas.getWidth(), canvas.getHeight());
            gr.setFill( Color.BLACK);
            gr.fillText( "Buff:" + getSkinnable().getText(), 30, 20 );
        }

        @Override
        public void dispose() {
            super.dispose();
            behavior.dispose();
            getChildren().removeAll();
        }
    }

    class MyControlBehavior extends BehaviorBase<MyControl> {

        final InputMap<MyControl> inputMap;

        public MyControlBehavior(MyControl control) {
            super(control);
            this.inputMap = createInputMap();
            addDefaultMapping( inputMap, new InputMap.KeyMapping(new KeyBinding(KeyCode.C).shortcut().ctrl(), e-> copy() ) );
        }

        public void copy(){
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString( getNode().getText() );
            clipboard.setContent(content);
        }

        @Override
        public InputMap<MyControl> getInputMap() {
            return inputMap;
        }
    }

}
