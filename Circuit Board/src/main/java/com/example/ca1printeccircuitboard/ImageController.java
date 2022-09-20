package com.example.ca1printeccircuitboard;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.ResourceBundle;

public class ImageController implements Initializable {

    @FXML
    public ImageView PCBImage, blackAndWhite;

    @FXML
    public CheckBox numberscb;

    private  Color sampleColour;

    static Image image;
    private int[] imageArray;
    private int width;
    private int height;
    private WritableImage newBWImage;
    private WritableImage newPCBImage;

    //ArrayList<Integer> allRoots = new ArrayList<>();

    Hashtable<Integer, ArrayList<Integer>> theHashTable;


    public void open(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        // Set the initial directory for the displayed file dialog
        fc.setInitialDirectory(new File("C:\\Users"));//\willi\IdeaProjects\CA1 Printed Circuit  Board\src\main\java\com\example\ca1printeccircuitboard

        // Set the selected file or null if no file has been selected
        File file = fc.showOpenDialog(null); // Shows a new file open dialog.
        if (file != null) {

            // URI that represents this abstract pathname
            PCBImage.setImage(new Image(file.toURI().toString()));
            image = new Image(file.toURI().toString());   //imageDetails();
        } else {
            System.out.println("A file is invalid!");
        }


    }

    public void close(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public static int find(int[] a, int id) {
        if (a[id] == -111) return -111;
        while (a[id] != id) id = a[id];
        return id;
    }

    public static void union(int[] a, int p, int q) {
        a[find(a, q)] = find(a, p); //The root of q is made reference p
    }


    public void BlackAndWhiteImg(MouseEvent e) {
//        PCBImage.setOnMousePressed(e -> {
        double x = e.getX();
        double y = e.getY();


        ImageView view = (ImageView) e.getSource();
        width = (int) view.getImage().getWidth();
        height = (int) view.getImage().getHeight();

        imageArray = new int[(height * width)];

        Bounds bounds = view.getLayoutBounds();
        double xScale = bounds.getWidth() / view.getImage().getWidth();
        double yScale = bounds.getHeight() / view.getImage().getHeight();

        x /= xScale;
        y /= yScale;

        int xCord = (int) x;
        int yCord = (int) y;

        PixelReader pixelReader = PCBImage.getImage().getPixelReader();
        Color mcol = pixelReader.getColor(xCord, yCord);
        sampleColour = pixelReader.getColor(xCord,yCord);


        System.out.println("Hue : " + mcol.getHue());
        System.out.println("Sat : " + mcol.getSaturation());
        System.out.println("Bri : " + mcol.getBrightness());
//        System.out.println("red : " +mcol.getRed());
//        System.out.println("green : " +mcol.getGreen());
//        System.out.println("blue : " +mcol.getBlue());


        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {

                Color col = pixelReader.getColor(column, row);

                if ((col.getHue() < mcol.getHue() + 50) && (col.getHue() > mcol.getHue() - 50) &&     //sets the picture to black and white depending on its hue sat and bri
                        (col.getBrightness() < mcol.getBrightness() + 0.185) && (col.getBrightness() > mcol.getBrightness() - 0.185) &&
                        (col.getSaturation() < mcol.getSaturation() + 0.185) && (col.getSaturation() > mcol.getSaturation() - 0.185)
                ) {
                    pixelWriter.setColor(column, row, Color.BLACK);
                    imageArray[width * row + column] = width * row + column;
                } else {
                    pixelWriter.setColor(column, row, Color.WHITE);
                    imageArray[width * row + column] = -111;
                }
            }
        }
        joinPixelsInArray();
        createRect();
        blackAndWhite.setImage(writableImage);
        newBWImage = writableImage;

        //-------------------------------------------------------------------------


//      for(int i=0;i<imageArray.length;i++)
//            System.out.print(find(imageArray,i)+(((i+1)%width==0) ? "\n" : " "));

    }


    public void joinPixelsInArray() {
        for (int i = 0; i < imageArray.length; i++) {  //goes through the image

            if (imageArray[i] >= 0) {  // if the image length is greater or equal to 0
                if ((i + 1) < imageArray.length && imageArray[i + 1] >= 0 && (i + 1) % width != 0) {  //makes sure that i cant go above the image length, makes sure it cant go under 0 and makes sure that if the pixel is on the next line it doesnt union them together

                    union(imageArray, i, i + 1); // unions the pixels together
                }

                if ((i + width) < imageArray.length && imageArray[i + width] >= 0) {
                    union(imageArray, i, i + width);
                }
            }
        }
    }


    public void createRect() {
        theHashTable = new Hashtable<>();
//        for(int i = 0;i < imageArray.length;i+.+){
//            if (imageArray[i]!=-111 && !allRoots.contains(find(imageArray,i))) allRoots.add(find(imageArray,i));
//        }
        for (int i = 0; i < imageArray.length; i++) {
            if (imageArray[i] != -111) {
                int root = find(imageArray, i);
                if (theHashTable.containsKey(root)) {
                    ArrayList<Integer> elements = theHashTable.get(root);
                    elements.add(i);
                } else {
                    ArrayList<Integer> elements = new ArrayList<>();
                    elements.add(i);
                    theHashTable.put(root, elements);
                }

            }

        }
        createRect2();

    }

    public void createRect2() {
        int rectCount = 0;
        int bOrder[]=new int[theHashTable.size()];
        int max = Integer.MAX_VALUE;

        for(int i = 0; i < bOrder.length;i++){
            int maxRoot = 0;
            int maxCurrentVal = 0;
            for(int k : theHashTable.keySet()){
                if(theHashTable.get(k).size() >= maxCurrentVal && theHashTable.get(k).size() < max){
                    maxCurrentVal = theHashTable.get(k).size();
                    maxRoot = k;
                }
            }
            bOrder[i] = maxRoot;
            max = maxCurrentVal;

        }
        for (int theRoot : theHashTable.keySet()) {
            int top = Integer.MAX_VALUE, bottom = -Integer.MAX_VALUE, left = Integer.MAX_VALUE, right = -Integer.MAX_VALUE;
            for (int i : theHashTable.get(theRoot)) {
                //    if(find(imageArray,i)== theRoot ){
                if (i % width < left) left = i % width;
                if (i % width > right) right = i % width;
                if (i / width < top) top = i / width;
                if (i / width > bottom) bottom = i / width;
            }
            //}
//            Bounds bounds = PCBImage.getLayoutBounds();
//            double xScale = bounds.getWidth() / PCBImage.getImage().getWidth();
//            double yScale = bounds.getHeight() / PCBImage.getImage().getHeight();

//            left /= xScale;
//            right /= yScale;
//            top /= xScale;
//            bottom /= yScale;
            int size = theHashTable.get(theRoot).size();



            if (size > 27.5) {
                Rectangle rect = new Rectangle(left, top, right - left, bottom - top);
                rectCount = findIndex(bOrder,theRoot);
                rect.setTranslateX(PCBImage.getLayoutX());
                rect.setTranslateY(PCBImage.getLayoutY());
                rect.setStroke(Color.BLUE);
                rect.setFill(Color.TRANSPARENT);
                Tooltip.install(rect, new Tooltip("Number: " + rectCount + "\nSize : " + size));

                ((Pane) PCBImage.getParent()).getChildren().add(rect);
                if(numberscb.isSelected()) {
                    Text t = new Text(rect.getTranslateX() + left + 2, rect.getTranslateY() + bottom - 10, rectCount + "");
                    t.setFont(new Font(10));

                    ((Pane) PCBImage.getParent()).getChildren().add(t);
                }


            }
        }
        }

        public int findIndex(int[] b, int value){
        for(int i = 0; i < b.length; i++){
            if(b[i] == value)
                return i + 1;
        }
        return -1;
        }

        public void noiseReduction (ActionEvent actionEvent){
            WritableImage writableImage = newBWImage;
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            ((Pane) PCBImage.getParent()).getChildren().removeIf(r -> r instanceof Rectangle);
            ((Pane) PCBImage.getParent()).getChildren().removeIf(r -> r instanceof Text);

        /*for(int i = 0; i < imageArray.length; i++){
            if (imageArray[i]!=-111){
                if(theHashTable.keySet().size()<27.5){
                    for (int row = 0; row < height; row++) {
                        for (int column = 0; column < width; column++) {

                                pixelWriter.setColor(column, row, Color.WHITE);
                        }
                    }
                }
            }*/
            for (int root : theHashTable.keySet()) {
                ArrayList<Integer> values = theHashTable.get(root);

                if (values.size() < 27.5) {
                    for (int element : values) {
                        int column = element % width;
                        int row = element / width;
                        pixelWriter.setColor(column, row, Color.WHITE);
                    }
                }
            }
            blackAndWhite.setImage(writableImage);

            theHashTable.keySet().removeIf(k -> theHashTable.get(k).size() < 27.5);
            createRect2();

        }


        public void randomColours (ActionEvent actionEvent){
            int x = 0;
            int y = 0;
            WritableImage writableImage = new WritableImage(width, height);
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            for (int i = 0; i < imageArray.length; i++) {
                x = i % width;
                y = i / width;
                pixelWriter.setColor(x, y, Color.WHITE);
            }
            for (int theRoot : theHashTable.keySet()) {
                Color randC = randomColor();
                for (int val : theHashTable.get(theRoot)) {
                    x = val % width;
                    y = val / width;
                    pixelWriter.setColor(x, y, randC);
                }
            }
            blackAndWhite.setImage(writableImage);
        }

        public Color randomColor () {
            Random random = new Random();
            int r = random.nextInt(255);
            int g = random.nextInt(255);
            int b = random.nextInt(255);
            return Color.rgb(r, g, b);
        }

    public void sampleColours(ActionEvent actionEvent) {
        int x = 0;
        int y = 0;

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int i = 0; i < imageArray.length; i++) {
            x = i % width;
            y = i / width;
            pixelWriter.setColor(x, y, Color.WHITE);
        }
        for (int theRoot : theHashTable.keySet()) {
            Color randC = randomColor();
            for (int val : theHashTable.get(theRoot)) {
                x = val % width;
                y = val / width;
                pixelWriter.setColor(x, y, sampleColour);
            }
        }
        blackAndWhite.setImage(writableImage);
    }


}



