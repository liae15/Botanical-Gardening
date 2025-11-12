import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton; 
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;  

import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class Main extends Application 
{ 
    private ObservableList<String> plantsOwned = FXCollections.observableArrayList();
    private ObservableList<String> plantsWant = FXCollections.observableArrayList();
    String clickedOn = "";
    //private Image picture;
    private static final String API_KEY = System.getenv("TREFLE_API_KEY");
    private static final String BASE_URL =  "https://trefle.io/api/v1/plants?token=" + API_KEY + "&filter[common_name]=";

    private TextField tf;
    private Button searchButton;
    private Stage primaryStage;
    private Stage secondaryStage;
    Label scientific;
    Label nativeArea;
    Label plantGrowth;
    Label farmLength;
    Label yearFound;
    String imageLink;
    Image plantImage;
    ImageView displayImage;
    Button addToWantButton;
    Button addToOwnButton;
    
  @Override
  public void start(Stage primaryStage) {
    this.primaryStage = primaryStage;

    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10, 10, 10, 10));
    grid.setVgap(5);
    grid.setHgap(5);

    //Label display
    Label label = new Label("Enter Plant Name: ");
    GridPane.setConstraints(label, 0, 0);
    label.setStyle("-fx-font: normal 14px 'serif' ");
    //Button display
    Button seeListsButton = new Button("See lists");
    GridPane.setConstraints(seeListsButton, 0, 3, 3, 1);
    seeListsButton.setStyle("-fx-background-color: mediumseagreen; -fx-text-fill: white; -fx-font: normal 14px 'serif' ");
    addToWantButton= new Button("I want this plant");
    GridPane.setConstraints(addToWantButton, 0, 14);
    addToWantButton.setStyle("-fx-background-color: mediumseagreen; -fx-text-fill: white; -fx-font: normal 14px 'serif' ");
    addToOwnButton = new Button("I own this plant");
    GridPane.setConstraints(addToOwnButton, 1, 14);
    addToOwnButton.setStyle("-fx-background-color: mediumseagreen; -fx-text-fill: white; -fx-font: normal 14px 'serif' ");
    
    //Information labels being displayed
    scientific = new Label("Scientific Name: N/A");
    GridPane.setConstraints(scientific, 0, 7, 4, 1);
    scientific.setStyle("-fx-font: normal 14px 'serif' ");

    yearFound = new Label("Year Noted: N/A");
    GridPane.setConstraints(yearFound, 0, 9, 4, 1);
    yearFound.setStyle("-fx-font: normal 14px 'serif' ");

    displayImage = new ImageView();
    GridPane.setConstraints(displayImage, 1, 16);
      
    //categies taken off display and they required second link API search to "plant"
    nativeArea = new Label("Native To: N/A");
    GridPane.setConstraints(nativeArea, 0, 8, 4, 1);
    nativeArea.setStyle("-fx-font: normal 14px 'serif' ");
    
    farmLength = new Label("Time to Farm: N/A");
    GridPane.setConstraints(farmLength, 0, 10, 4, 1);
    farmLength.setStyle("-fx-font: normal 14px 'serif' ");
    
    plantGrowth = new Label("Growth Rate: N/A");
    GridPane.setConstraints(plantGrowth, 0, 12, 4, 1);
    plantGrowth.setStyle("-fx-font: normal 14px 'serif' ");


    //stage
    Scene mainScene;
    secondaryStage = new Stage();

    //textfield and search display
    tf = new TextField("");
    GridPane.setConstraints(tf, 1, 0);
    tf.setStyle("-fx-font: normal 13px 'serif' ");
    tf.setMaxWidth(200);  
    
    searchButton = new Button("Search"); 
    GridPane.setConstraints(searchButton, 2, 0);
    searchButton.setStyle("-fx-background-color: mediumseagreen; -fx-text-fill: white; -fx-font: normal 14px 'serif' ");

    //visibility
    addToWantButton.setVisible(false);
    addToOwnButton.setVisible(false);

    searchButton.setOnAction(e -> {
        //add display here of info
        addToWantButton.setVisible(true);
        addToOwnButton.setVisible(true);
        fetchInfo();
        //Need to link API thing here to display info below in labels
    });

    seeListsButton.setOnAction(e -> {
      primaryStage.hide();
      secondStage();
    });

    // Event handler for the want button
    addToWantButton.setOnAction(e -> {
        String plantName = tf.getText().trim();
        if ((!plantName.isEmpty()) && (plantsWant.indexOf(plantName) == -1)) {
            try {
                plantsWant.add(plantName);
            } catch (NullPointerException ex) {
                showAlert("Input Error", "Please enter a valid name for the plant.");
            }
        } else if (plantName.isEmpty()) {
            showAlert("Input Error", "Please fill in a plant name.");
        } else {
            showAlert("Input Error", "Please fill in a valid and unlisted plant name.");
        }
        tf.clear();
        addToWantButton.setVisible(false);
        addToOwnButton.setVisible(false);
    });

    // Event handler for the own button
    addToOwnButton.setOnAction(e -> {
        String plantName = tf.getText().trim();
        if ((!plantName.isEmpty()) && (plantsOwned.indexOf(plantName) == -1)) {
            try {
                plantsOwned.add(plantName);
            } catch (NullPointerException ex) {
                showAlert("Input Error", "Please enter a valid name for the plant.");
            }
        } else if (plantName.isEmpty()) {
            showAlert("Input Error", "Please fill in a plant name.");
        } else {
            showAlert("Input Error", "Please fill in a valid and unlisted plant name.");
        }
        tf.clear();
        addToWantButton.setVisible(false);
        addToOwnButton.setVisible(false);
    });


    grid.getChildren().addAll(label, tf, searchButton, scientific, yearFound, seeListsButton, addToWantButton, addToOwnButton, displayImage);
    mainScene = new Scene(grid, 500, 500);

    primaryStage.setTitle("Botan(app)le Gardening");
    primaryStage.setScene(mainScene);
    primaryStage.show();
  }

  public void secondStage() {
    secondaryStage = new Stage();
    Label choose = new Label("Which list would like to see?");
    choose.setStyle("-fx-font: normal 14px 'serif' ");
    Button want = new Button("Plants you want"); 
    want.setStyle("-fx-background-color: mediumseagreen; -fx-text-fill: white; -fx-font: normal 14px 'serif' ");
    Button own = new Button("Plants you own");
    own.setStyle("-fx-background-color: mediumseagreen; -fx-text-fill: white; -fx-font: normal 14px 'serif' ");
    Button goBack = new Button("Return to search");
    goBack.setStyle("-fx-background-color: mediumseagreen; -fx-text-fill: white; -fx-font: normal 14px 'serif' ");
    VBox listVbox;
    Scene listsScene;
    
    ListView<String> wantList = new ListView<>(plantsWant);
    wantList.setStyle("-fx-font: normal 14px 'serif' ");
    wantList.setPrefWidth(100);
    wantList.setPrefHeight(125);
    ListView<String> ownList = new ListView<>(plantsOwned);
    ownList.setPrefWidth(100);
    ownList.setPrefHeight(125);
    ownList.setStyle("-fx-font: normal 14px 'serif' ");
    wantList.setVisible(false);
    ownList.setVisible(false);
    
    listVbox = new VBox(choose, want, own, wantList, ownList, goBack);
    listVbox.setSpacing(20);
    listsScene = new Scene(listVbox, 500, 500);
    
    goBack.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override public void handle(ActionEvent e) {
        secondaryStage.hide();
        tf.setText("");
        primaryStage.show();
      }
    });

    want.setOnAction(e -> {
        wantList.setVisible(true);
        ownList.setVisible(false);
        ownList.managedProperty().bind(ownList.visibleProperty());
    });

    own.setOnAction(e -> {
        ownList.setVisible(true);
        wantList.setVisible(false);
        wantList.managedProperty().bind(wantList.visibleProperty());
    });

    ownList.setOnMouseClicked(event -> listClicks(event, ownList));
    wantList.setOnMouseClicked(event -> listClicks(event, wantList));

    secondaryStage.setTitle("Botan(app)le Gardening");
    secondaryStage.setScene(listsScene);
    secondaryStage.show();
  }

public void listClicks(MouseEvent event, ListView<String> listView) {
    if((event.getButton() == MouseButton.PRIMARY) && (event.getClickCount() == 1)) {
        String pickedPlant = listView.getSelectionModel().getSelectedItem();
        if(pickedPlant != null) {
            boolean search = showConfirm("Search?", "Would you like to get information about this plant?");
            if(search) {
                tf.setText(pickedPlant);
                secondaryStage.hide();
                primaryStage.show();
                searchButton.fire();
            } else {
                boolean delete = showConfirm("Delete?", "Would you like to remove this plant from the list?");
                if(delete) {
                    listView.getItems().remove(pickedPlant);
                } else {
                    showNeverMind("Oops!", "Don't worry, your list has been kept the same");
                    }
            }
        }
    }
}

private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }


private boolean showConfirm(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    return alert.showAndWait().filter(response -> response.getText().equals("OK")).isPresent();
    }

private void showNeverMind(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

private void resetView() {
    addToWantButton.setVisible(false);
    addToOwnButton.setVisible(false);
    scientific.setText("Scientific Name: N/A");
    yearFound.setText("Year Noted: N/A");
    displayImage.setVisible(false);
    tf.clear();
}
    
private void fetchInfo() {
    // Dummy data for demonstration
    String plant = tf.getText();
    String plantName = getPlantData(plant);
    System.out.println(plantName);
    if(plantName.equals("Error fetching plant data")) {
        showAlert("Error", "Could not fetch data for this plant.");
        return;
    }
    try {
        // JSON work
        JSONObject obj = new JSONObject(plantName);
        JSONArray data = obj.getJSONArray("data");
        if(data.length() == 0) {
            showAlert("Error", "No information found for this plant.");
            resetView();
            return;
        }
        
        JSONObject plantInfo = data.getJSONObject(0);
        //open links and create a new method to read through the plant link and use that info for more fetching
        String name = plantInfo.get("scientific_name").toString();
        String year = plantInfo.get("year").toString();
        imageLink = plantInfo.get("image_url").toString();
        plantImage = new Image(imageLink, 200, 0, true, false);
        displayImage.setImage(plantImage);
        
        String plantLink = "https://trefle.io" + plantInfo.getJSONObject("links").getString("plant");
        //fetchMoreInfo(plantLink);
        
        scientific.setText("Scientific Name: " + name);
        yearFound.setText("Year Discovered: " + year);
    }
    catch (JSONException e) {
        e.printStackTrace();
        showAlert("Error", "Data cannot be processed");
    }
}

public void fetchMoreInfo(String plantLink) {
    String moreInfo = getMoreData(plantLink);
    if (moreInfo.equals("Error fetching detailed plant data")) {
        showAlert("Error", "Could not fetch detailed data for this plant.");
        return;
    }
    returnMoreInfo(moreInfo);
}

private String getMoreData(String plantLink) {
    try {
        URL url = new URL(plantLink + "?token=" + API_KEY);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
            System.out.println(line);
        }
        reader.close();
        connection.disconnect();
        System.out.println(response.toString());
        return response.toString();
    } catch (IOException e) {
        e.printStackTrace();
        return "Error fetching plant data";
    }
}

private void returnMoreInfo(String moreInfo) {
    try {
        JSONObject obj = new JSONObject(moreInfo);
        JSONObject plantInfo = obj.getJSONObject("data");
        JSONObject mainPlant = plantInfo.getJSONObject("main_species");
        String growthRate = mainPlant.optString("growth_rate", "N/A");
        String daysToHarvest = mainPlant.optString("days_to_harvest", "N/A");
        
        JSONArray nativePlacesArray = mainPlant.optJSONArray("distribution");
        // end JSON work
        StringBuilder nativePlaces = new StringBuilder("Native To: ");
        if((nativePlacesArray != null) && (nativePlacesArray.length() > 0)) {
            for(int i = 0; i < nativePlacesArray.length(); i++) {
                nativePlaces.append(nativePlacesArray.getString(i));
                if(i < nativePlacesArray.length() -1) {
                    nativePlaces.append(", ");
                }
                if(i == (nativePlacesArray.length()-2)) {
                    nativePlaces.append("and ");
                }
            }
        } else {
            nativePlaces.append("N/A");
        }
        
    String growthSpeed = "Growth Rate: " + growthRate;
    String farmingLength = "Time to Farm: " + daysToHarvest;
    
    nativeArea.setText(nativePlaces.toString());
    plantGrowth.setText(growthSpeed);
    farmLength.setText(farmingLength);

    } catch (JSONException e) {
        e.printStackTrace();
        showAlert("Error", "Data cannot be processed");
    }
}

public static String getPlantData(String plant) {
    String searchPlant = "";
    for(int i = 0; i < plant.length(); i++) {
        if(plant.substring(i, i+1).equals(" ")) {
            searchPlant += "%20";
        } else {
            searchPlant += plant.substring(i, i+1);
        }
    }
    try {
        URL url = new URL(BASE_URL + searchPlant);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
            System.out.println(line);
        }
        reader.close();
        connection.disconnect();
        System.out.println(response.toString());
        return response.toString();
    } catch (IOException e) {
        e.printStackTrace();
        return "Error fetching plant data";
    }
}
    
    public static void main(String[] args) {
        launch(args);
    }
}
