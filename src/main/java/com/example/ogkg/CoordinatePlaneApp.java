package com.example.ogkg;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CoordinatePlaneApp extends Application {

  private Pane coordinatePlane;
  private double planeWidth = 800;
  private double planeHeight = 800;
  private double axisOffsetX;
  private double axisOffsetY;
  private List<Circle> points;
  private Circle solutionPoint;
  private double minX = -planeWidth / 2;
  private double maxX = planeWidth / 2;
  private double minY = -planeHeight / 2;
  private double maxY = planeHeight / 2;

  public CoordinatePlaneApp() {
    points = new ArrayList<>();
  }

  @Override
  public void start(Stage primaryStage) {
    coordinatePlane = new Pane();
    coordinatePlane.setPrefSize(planeWidth, planeHeight);
    coordinatePlane.setStyle("-fx-background-color: white;");

    Button generateButton = new Button("Generate Points");
    generateButton.setOnAction(e -> generatePoints());

    Button solveButton = new Button("Solve");
    solveButton.setOnAction(e -> findMaximizationPoint());

    Button addPointButton = new Button("Add Point");
    addPointButton.setOnAction(e -> enablePointAdding());

    Button clearButton = new Button("Clear Points");
    clearButton.setOnAction(e -> clearPoints());
    Label minXLabel = new Label("Min X:");
    TextField minXField = new TextField();
    Label maxXLabel = new Label("Max X:");
    TextField maxXField = new TextField();
    Label minYLabel = new Label("Min Y:");
    TextField minYField = new TextField();
    Label maxYLabel = new Label("Max Y:");
    TextField maxYField = new TextField();
    Button setBoundsButton = new Button("Set Bounds");
    setBoundsButton.setOnAction(e -> setBounds(minXField.getText(), maxXField.getText(), minYField.getText(), maxYField.getText()));

    HBox buttonBox = new HBox(10, generateButton, solveButton, addPointButton, clearButton,minXLabel, minXField, maxXLabel, maxXField, minYLabel, minYField, maxYLabel, maxYField, setBoundsButton);
    buttonBox.setPadding(new Insets(10));

    BorderPane root = new BorderPane();
    root.setCenter(coordinatePlane);
    root.setBottom(buttonBox);

    Scene scene = new Scene(root, 900, 900);
    primaryStage.setTitle("Coordinate Plane");
    primaryStage.setScene(scene);
    primaryStage.show();

    axisOffsetX = planeWidth / 2;
    axisOffsetY = planeHeight / 2;

    drawAxes();
  }

  private void drawAxes() {
    Line xAxis = new Line(0, axisOffsetY, planeWidth, axisOffsetY);
    Line yAxis = new Line(axisOffsetX, 0, axisOffsetX, planeHeight);

    coordinatePlane.getChildren().addAll(xAxis, yAxis);
  }

  private void generatePoints() {
    Random random = new Random();
    for (int i = 0; i < 100; i++) {
      double x = minX + random.nextDouble() * (maxX - minX);
      double y = minY + random.nextDouble() * (maxY - minY);
      addPoint(x, y);
      System.out.println("("+x+","+y+")");
    }
  }

  private void addPoint(double x, double y) {
    double translatedX = x + axisOffsetX;
    double translatedY = axisOffsetY - y;

    Circle point = new Circle(translatedX, translatedY, 2, Color.RED);
    points.add(point);
    coordinatePlane.getChildren().add(point);
  }

  private void findMaximizationPoint() {
    int n = points.size();

    // Обчислення центру мас
    double sumX = 0.0;
    double sumY = 0.0;
    for (Circle point : points) {
      sumX += point.getCenterX();
      sumY += point.getCenterY();
    }
    double centerX = sumX / n;
    double centerY = sumY / n;

    // Знаходження точки, для якої сума відстаней є максимальною
    double maxDistanceSum = 0.0;
    Circle maximizationPoint = null;
    for (Circle point : points) {
      double distance = Math.sqrt(Math.pow(point.getCenterX() - centerX, 2) + Math.pow(point.getCenterY() - centerY, 2));
      maxDistanceSum += distance;
      if (maximizationPoint == null || maxDistanceSum > maxDistanceSum) {
        maximizationPoint = new Circle(point.getCenterX(), point.getCenterY(), 5, Color.GREEN);
      }
    }
    coordinatePlane.getChildren().add(maximizationPoint);

    System.out.println("Solution Point: (" + (-axisOffsetX + maximizationPoint.getCenterX()) + ", " + (axisOffsetY - maximizationPoint.getCenterY()) + ")");
  }

  private void enablePointAdding() {
    coordinatePlane.setOnMouseClicked(this::handlePointAdding);
  }

  private void handlePointAdding(javafx.scene.input.MouseEvent event) {
    points = new ArrayList<>();
    if (event.getButton() == MouseButton.PRIMARY) {
      double x = -axisOffsetX + event.getX();
      double y = axisOffsetY - event.getY();
      addPoint(x, y);
      System.out.println(x + " " + y);
      // Disable point adding after one point is added
      coordinatePlane.setOnMouseClicked(null); // Disable point adding after one point is added
    }
  }

  private void clearPoints() {
    coordinatePlane.getChildren().removeAll(coordinatePlane.getChildren().filtered(child -> child instanceof Circle));
    points.clear();
    minX = -planeWidth / 2;
    maxX = planeWidth / 2;
    minY = -planeHeight / 2;
    maxY = planeHeight / 2;
  }
  private void setBounds(String minXStr, String maxXStr, String minYStr, String maxYStr) {
    try {
      minX = Double.parseDouble(minXStr);
      maxX = Double.parseDouble(maxXStr);
      minY = Double.parseDouble(minYStr);
      maxY = Double.parseDouble(maxYStr);
    } catch (NumberFormatException e) {
      // Введені обмеження некоректні
    }
  }
  public static void main(String[] args) {
    launch(args);
  }
}