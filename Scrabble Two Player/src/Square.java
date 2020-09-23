

/*
 * Team Name: El Cucharachas
 *
 * Students:
 * - Ahmed Jouda 	18329393
 * - Sean Mcdonnell 18391961
 * - Lleno Anya 	18357493
 *
 */

import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class Square extends Label {
    private boolean empty;
    private Tile tile;
    String value;

    Square() {
        empty = true;
        value = "  ";
        this.setPrefSize(UI.screenBounds.getHeight()/22.5,UI.screenBounds.getHeight()/22.5);
        this.setStyle("-fx-background-color: rgb(216, 226, 238); -fx-text-fill: black; -fx-border-color: black;");
        this.setAlignment(Pos.CENTER);
    }

    void setTile(Tile tile) {
        this.tile = tile;

        if(tile == null){
            this.setEmpty(true);
            this.setStyle("-fx-background-color: rgb(216, 226, 238); -fx-text-fill: black; -fx-border-color: black;");
            this.setValue(value);
        }
        else {
            this.setEmpty(false);
            this.setText(String.valueOf(tile.getLetter()));
            this.setStyle("-fx-background-color: rgb(244, 205, 175); -fx-text-fill: black; -fx-border-color: black;");
        }
    }


    Tile getTile() {
        return this.tile;
    }

    String getValue() {
        return this.value;
    }


    void setValue(String value) {
        this.value = value;
        this.setText(value);
        switch(this.value) {
            case "*":
            case "2W":
                this.setStyle("-fx-background-color: rgb(231, 40, 34); -fx-text-fill: white; -fx-border-color: black;");
                break;
            case "3W":
                this.setStyle("-fx-background-color: rgb(208, 74, 28); -fx-text-fill: white; -fx-border-color: black;");
                break;
            case "3L":
                this.setStyle("-fx-background-color: rgb(83, 144, 0); -fx-text-fill: white; -fx-border-color: black;");
                break;
            case "2L":
                this.setStyle("-fx-background-color: rgb(4, 110, 159); -fx-text-fill: white; -fx-border-color: black;");
                break;
            default:
                break;
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public String toString(){
        if(this.isEmpty()){
            return "|" + getValue() + "|";
        }
        else
            return "| " + tile.getLetter() + "|";
    }
    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

}