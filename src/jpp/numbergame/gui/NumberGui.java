package jpp.numbergame.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import jpp.numbergame.Coordinate2D;
import jpp.numbergame.Direction;
import jpp.numbergame.NumberGame;
import jpp.numbergame.Tile;

import java.util.ArrayList;
import java.util.List;


public class NumberGui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        int width = 4;
        int height = 4;
        NumberGame game = new NumberGame(width,height,2);
        Pane root = new Pane();
        root.setPrefSize(300,500);
        List<MyTile> tiles = new ArrayList<>();
        for(int i = 0 ; i < width * height ; i++){
            int x = game.grid[i % 4][i / 4].getValue();
            if(x != 0)
                tiles.add(new MyTile(x+""));
            else
                tiles.add(new MyTile(""));
        }

        for(int i = 0 ; i < tiles.size() ; i++){
            MyTile tile = tiles.get(i);
            tile.setTranslateX(50 * (i % 4));
            tile.setTranslateY(50 * (i / 4));
            root.getChildren().add(tile);
        }
        Label points = new Label();
        points.setText("Points : " + game.getPoint());
        points.setTranslateX(0);
        points.setTranslateY(220);
        root.getChildren().add(points);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    game.move(Direction.UP) ; break;
                    case DOWN:  game.move(Direction.DOWN); break;
                    case LEFT:  game.move(Direction.LEFT); break;
                    case RIGHT: game.move(Direction.RIGHT); break;
                }
                tiles.clear();

                for(int i = 0 ; i < width * height ; i++){
                    int x = game.grid[i % 4][i / 4].getValue();
                    if(x != 0)
                        tiles.add(new MyTile(x+""));
                    else
                        tiles.add(new MyTile(""));

                }
                for(int i = 0 ; i < tiles.size() ; i++){
                    MyTile tile = tiles.get(i);
                    root.getChildren().remove(0);
                    tile.setTranslateX(50 * (i % 4));
                    tile.setTranslateY(50 * (i / 4));
                    root.getChildren().add(tile);
                }

                points.setText("Points : " + game.getPoint());
                root.getChildren().remove(0);
                root.getChildren().add(points);

                if(game.isFull() && !game.canMove()){
                    System.out.println("GameOver and your point is :" + game.getPoint());

                }

            }

        });



        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private class MyTile extends StackPane{
        public MyTile(String value) {
            Rectangle rec = new Rectangle(50,50);
            rec.setFill(null);
            rec.setStroke(Color.BLACK);

            Text text = new Text(value);
            text.setFont(Font.font(30));
            setAlignment(Pos.CENTER);
            getChildren().addAll(rec,text);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
