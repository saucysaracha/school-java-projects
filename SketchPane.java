
// Description: The SketchPane class instantiates all the instance
// variables, sets up the layout of the BorderPane, and makes it so that
// the user can draw shapes of different sizes and stroke widths
// depending on which buttons they press, and they can undo/erase
// their work.

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;


public class SketchPane extends BorderPane {

    //Task 1: Declare all instance variables listed in UML diagram
    ArrayList<Shape> shapeList;
    ArrayList<Shape> tempList;
    Button undoButton;
    Button eraseButton;
    Label fillColorLabel;
    Label strokeColorLabel;
    Label strokeWidthLabel;
    ComboBox<String> fillColorCombo;
    ComboBox<String> strokeWidthCombo;
    ComboBox<String> strokeColorCombo;
    RadioButton radioButtonLine;
    RadioButton radioButtonRectangle;
    RadioButton radioButtonCircle;
    Pane sketchCanvas;
    Color[] colors;
    String[] strokeWidth;
    String[] colorLabels;
    Color currentStrokeColor;
    Color currentFillColor;
    int currentStrokeWidth;
    Line line;
    Circle circle;
    Rectangle rectangle;
    double x1;
    double y1;


    //Task 2: Implement the constructor
    public SketchPane() {
        // Colors, labels, and stroke widths that are available to the user
        colors = new Color[] {Color.BLACK, Color.GREY, Color.YELLOW, Color.GOLD, Color.ORANGE, Color.DARKRED, Color.PURPLE, Color.HOTPINK, Color.TEAL, Color.DEEPSKYBLUE, Color.LIME} ;
        colorLabels = new String[] {"black", "grey", "yellow", "gold", "orange", "dark red", "purple", "hot pink", "teal", "deep sky blue", "lime"};
        fillColorLabel = new Label("Fill Color:");
        strokeColorLabel = new Label("Stroke Color:");
        strokeWidthLabel = new Label("Stroke Width:");
        strokeWidth = new String[] {"1", "3", "5", "7", "9", "11", "13"};

        //Implement the SketchPane constructor. The constructor must instantiate:
        //Two Array Lists to hold shapes (shapeList) and remember shapes (tempList)
        shapeList = new ArrayList<Shape>();
        tempList = new ArrayList<Shape>();

        // Various Controls
        //  3 Combo Boxes with Labels to select the Fill Color, Stroke Width, and Fill Color
        fillColorCombo = new ComboBox<String>();
        fillColorCombo.getItems().addAll(colorLabels);
        fillColorCombo.getSelectionModel().selectFirst();
        fillColorCombo.setOnAction(new ColorHandler());

        strokeWidthCombo = new ComboBox<String>();
        strokeWidthCombo.getItems().addAll(strokeWidth);
        strokeWidthCombo.getSelectionModel().selectFirst();
        strokeWidthCombo.setOnAction(new WidthHandler());

        strokeColorCombo = new ComboBox<String>();
        strokeColorCombo.getItems().addAll(colorLabels);
        strokeColorCombo.getSelectionModel().selectFirst();
        strokeColorCombo.setOnAction(new ColorHandler());

        // 3 Radio Buttons to select Line, Rectangle, Circle, arranged in a Toggle Group
        radioButtonLine = new RadioButton("Line");
        radioButtonRectangle = new RadioButton("Rectangle");
        radioButtonCircle = new RadioButton("Circle");
        //bind them to their event handler/arrange in a toggle group
        ToggleGroup radioButtonGroup = new ToggleGroup();
        radioButtonLine.setToggleGroup(radioButtonGroup);
        radioButtonRectangle.setToggleGroup(radioButtonGroup);
        radioButtonCircle.setToggleGroup(radioButtonGroup);
        radioButtonLine.setSelected(true);

        //  2 Buttons (Undo and Erase)
        undoButton = new Button("Undo");
        eraseButton = new Button("Erase");
        // bind them to their event handlers
        undoButton.setOnAction(new ButtonHandler());
        eraseButton.setOnAction(new ButtonHandler());

        //Instantiate sketchCanvas and set its background color to WHITE
        sketchCanvas = new Pane(); //add children to this once code is implemented

        sketchCanvas.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        //Finally, register the sketchCanvas Pane with the MouseHandler and set the default values for
        //the remaining instance variables (x1 and y2 should be zero, current stroke and fill color should
        //be black, current stroke width should be 1).
        sketchCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new MouseHandler());
        sketchCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new MouseHandler());
        sketchCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, new MouseHandler());
        x1 = 0;
        y1 = 0;
        currentStrokeColor = Color.BLACK;
        currentFillColor = Color.BLACK;
        currentStrokeWidth = 1;

        //set up BorderPane layout
        // Instantiate an HBox to hold the ComboBoxes. To achieve the given layout, instantiate the HBox
        //with size 20 and set the minimum size to (20,40). Use pos.CENTER for alignment and set the
        //background color of the HBox to LIGHTGREY. Then, add the Labels and ComboBoxes
        HBox topBox = new HBox(fillColorLabel, fillColorCombo, strokeWidthLabel, strokeWidthCombo, strokeColorLabel, strokeColorCombo);
        topBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
        topBox.setSpacing(20);
        topBox.setAlignment(Pos.CENTER);
        topBox.setMinSize(20, 40);

        //Instantiate an HBox to hold the RadioButtons and Buttons. To achieve the given layout,
        //instantiate the HBox with size 20 and set the minimum size to (20,40). Use pos.CENTER for
        //alignment and set the background color of the HBox to LIGHTGREY. Then, add the 3
        //Radiobuttons and 2 Buttons.
        HBox bottomBox = new HBox(radioButtonLine, radioButtonRectangle, radioButtonCircle, undoButton, eraseButton);
        bottomBox.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
        bottomBox.setMinSize(20, 40);
        bottomBox.setSpacing(20);
        bottomBox.setAlignment(Pos.CENTER);
        //declare Center first so that items drawn in sketchCanvas do not overlap
        // into either of the HBoxes
        this.setCenter(sketchCanvas);
        this.setTop(topBox);
        //this.setCenter(sketchCanvas);
        this.setBottom(bottomBox);

    }

    private class MouseHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            // TASK 3: Implement the mouse handler for Circle and Line
            // Rectange Example given!
            if (radioButtonRectangle.isSelected()) {
                //Mouse is pressed
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    x1 = event.getX();
                    y1 = event.getY();
                    rectangle = new Rectangle();
                    rectangle.setX(x1);
                    rectangle.setY(y1);
                    shapeList.add(rectangle);
                    rectangle.setFill(Color.WHITE);
                    rectangle.setStroke(Color.BLACK);
                    sketchCanvas.getChildren().add(rectangle);
                }
                //Mouse is dragged
                else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    rectangle.setWidth(Math.abs(event.getX() - x1));
                    rectangle.setHeight(Math.abs(event.getY() - y1));

                }
                //Mouse is released
                else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    rectangle.setFill(currentFillColor);
                    rectangle.setStroke(currentStrokeColor);
                    rectangle.setStrokeWidth(currentStrokeWidth);
                }
            }

                // TASK 3: Implement the mouse handler for Circle and Line
                // Circle
                if (radioButtonCircle.isSelected()) {
                    //Mouse is pressed
                    if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                        x1 = event.getX();
                        y1 = event.getY();
                        circle = new Circle();
                        circle.setCenterX(x1);
                        circle.setCenterY(y1);
                        shapeList.add(circle);
                        circle.setFill(Color.WHITE);
                        circle.setStroke(Color.BLACK);
                        sketchCanvas.getChildren().add(circle);
                    }
                    //Mouse is dragged
                    else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                        /*double x2 = event.getX();
                        double y2 = event.getY();
                        circle.setRadius(getDistance(x1, y1, x2, y2)); */
                        circle.setRadius(getDistance(x1, y1, event.getX(), event.getY()));

                    }
                    //Mouse is released
                    else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                        circle.setFill(currentFillColor);
                        circle.setStroke(currentStrokeColor);
                        circle.setStrokeWidth(currentStrokeWidth);
                    }
                }
            // Line
            if (radioButtonLine.isSelected()) {
                //Mouse is pressed
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    x1 = event.getX();
                    y1 = event.getY();
                    line = new Line();
                    line.setStartX(x1);
                    line.setStartY(y1);
                    line.setEndX(x1);
                    line.setEndY(y1);
                    shapeList.add(line);
                    line.setFill(Color.WHITE);
                    line.setStroke(Color.BLACK);
                    sketchCanvas.getChildren().add(line);
                }
                //Mouse is dragged
                else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    line.setEndX(event.getX());
                    line.setEndY(event.getY());
                }
                //Mouse is released
                else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                    line.setFill(currentFillColor);
                    line.setStroke(currentStrokeColor);
                    line.setStrokeWidth(currentStrokeWidth);
                }
            }
        }
    }

    private class ButtonHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // TASK 4: Implement the button handler
            //If the source of the event is the Undo Button and the array list with the shapes is not empty,
            //remove the last element from the array list and also remove the last element that was drawn on
            //sketchCanvas.
            if(event.getSource() == undoButton)
            {
                if(shapeList.isEmpty() == false)
                {
                    //remove last shape in the array
                    sketchCanvas.getChildren().remove(shapeList.get(shapeList.size() - 1));
                //    sketchCanvas.getChildren().remove(shapeList.lastIndexOf(shapeList));
                    //remove last element from the arraylist, (size of array - 1)
                    shapeList.remove(shapeList.size() - 1);
                   // shapeList.remove(shapeList.lastIndexOf(shapeList));
                }
                //• Else (if there are currently no shapes in the array list) it is possible that the Erase Button was
                //pushed previously, which deleted all the shapes. Copy the elements from your temporary array
                //list into the array list with the shapes, clear the temporary array list, and add all the shapes from
                //the array list to sketchCanvas. This ensures that all shapes reappear if the user wants to undo an
                //erase event.
                else
                {
                    //copy items from tempList into shapeList
                 /*   for(int i = 0; i < tempList.size(); i++)
                    {
                        shapeList.set(i, tempList.get(i));
                    }*/
                    shapeList.addAll(tempList);
                    tempList.clear();
                    //add all shapes from the array list to sketchCanvas
                    sketchCanvas.getChildren().addAll(shapeList);
                }
            }
            //• If the source of the event is the Erase Button and the array list with the shapes is not empty,
            //clear the temporary array list, add all elements from the shape array list to the temporary array
            //list (as backup), clear the array list with the shapes, and clear sketchCanvas.
            if((event.getSource() == eraseButton) && (shapeList.isEmpty() == false))
            {
                tempList.clear();
                //put all of the items from shapeList into tempList
               /* for(int i = 0; i < shapeList.size(); i++)
                {
                    tempList.set(i, shapeList.get(i));
                } */
                tempList.addAll(shapeList);
                shapeList.clear();
                //clear sketchCanvas
                sketchCanvas.getChildren().clear();
            }

        }
    }

    private class ColorHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            // TASK 5: Implement the color handler
            // In this method, the color selected by the user for shape filling and stroke is assigned as color to be used
            //for drawing. This listener is registered with your ComboBoxes. Your ComboBoxes contain Strings that
            //represent colors; however, you cannot use a String as a color to fill a shape. Solution: Determine the
            //index i of the selected color in the fillColorCombo (you can use .getSelectionModel().getSelectedIndex())
            //and assign the Color that is at index i in the colors[] array to the variable currentFillColor. Do the same
            //for the strokeColorCombo and currentStrokeColor
            currentFillColor = colors[(fillColorCombo.getSelectionModel().getSelectedIndex())];
            currentStrokeColor = colors[(strokeColorCombo.getSelectionModel().getSelectedIndex())];
        }
    }

    private class WidthHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event){
            // TASK 6: Implement the stroke width handler
            currentStrokeWidth = Integer.parseInt(strokeWidthCombo.getValue());
        }
    }

    // Get the Euclidean distance between (x1,y1) and (x2,y2)
    private double getDistance(double x1, double y1, double x2, double y2)  {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

}