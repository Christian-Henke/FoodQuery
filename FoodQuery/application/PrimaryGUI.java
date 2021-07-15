/**
 * Filename: PrimaryGUI.java 
 * Project: Milestone 3
 * Authors: A-team 31
 *
 * Semester: Fall 2018 Course: CS400
 * 
 * Due Date: 12/12/18 10pm
 * 
 * Credits: none
 * 
 * Bugs: no known bugs
 */

package application;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Primary GUI that displays all the objects that should appear on the screen.
 * 
 * @author A-team 31
 */
public class PrimaryGUI {

    private BorderPane root; // Borderpane used in the GUI
    private Stage primaryStage; // Primary stage of the GUI
    private HBox addLoadFood; // Hbox used for adding food
    private FoodData foodData; // Main structure that stores foodItems
    private FoodListView foodListView; // GUI structure used to display the food items
    private FoodListView mealListView; // GUI structure used to display the meal items
    private Button addToMeal;
    private Button filterBoth;
    private TextField searchTextField;

    /**
     * Constructor for the Primary GUI.
     * 
     * @param root, root/borderpane for the overall display
     * @param scene, main scene of the main page
     * @param primaryStage, primary stage for the display
     */
    public PrimaryGUI(BorderPane root, Scene scene, Stage primaryStage) {
        this.root = root;
        this.primaryStage = primaryStage;
        this.foodData = new FoodData();
        this.foodListView = new FoodListView();
        this.mealListView = new FoodListView();
        this.addToMeal = new Button("Add to Meal");
        this.filterBoth = new Button("Apply Selected Nutrient Filters & Search");
        this.searchTextField = new TextField();
        foodListView.getView().setMaxHeight(150);
        mealListView.getView().setMinHeight(150);

        
        
        root.setPadding(new Insets(15, 12, 15, 12));
    }

    /**
     * Displays all the objects on screen by calling the various show Methods
     */
    public void display() {
        /** Add Buttons **/
        showLoadAddNutrients();

        /** Food List **/
        showFoodList();

        /** Meal List **/
        showMealList();
    }

    /**
     * Method to show all of the load and add buttons as well as all the nutrient filters and
     * nutrient filter list.
     */
    private void showLoadAddNutrients() {
        /** Load/Add Food Button, Nutrient filter **/
        VBox loadAndFilter = new VBox();
        // Load/Add Food button
        addLoadFood = new HBox();
       
        // Button that, when pressed, loads food into the internal FoodData structure from an
        // external File
        Button loadFood = new Button("Load Food from csv");
        loadFood.setOnMouseClicked(event -> {

            // open a file chooser for the user to select a file

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Load Food from file");
            FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                String filePath = selectedFile.getAbsolutePath();

                // load food into foodData
                foodData = new FoodData();
                foodData.loadFoodItems(filePath);
                foodListView.reset();
                foodListView.addAll(foodData.getAllFoodItems());
            }
        });


        // Button that, when pressed, prompts the user to enter a manual food item into the foodData Structure
        Button addFood = new Button("Add Food");

        // Popup for adding a food item
        addFood.setOnMouseClicked(e -> {
            // Set up a pop up window
            Stage addFoodPopUp = new Stage();
            addFoodPopUp.setMinHeight(400);
            addFoodPopUp.initModality(Modality.APPLICATION_MODAL);
            addFoodPopUp.initOwner(primaryStage);
            BorderPane addFoodPane = new BorderPane();
            addFoodPane.setPadding(new Insets(15, 12, 15, 12));
            Scene addFoodScene = new Scene(addFoodPane, 320, 310);

            // Add parts to the pop up window
            VBox addFoodDialogBox = new VBox();
            Label addFoodLabel = new Label("Please enter all food data before adding food");
            Button addFoodFinishedButton = new Button("Add food");
            TextField idData = new TextField();
            TextField foodNameData = new TextField();
            TextField calorieData = new TextField();
            TextField fatData = new TextField();
            TextField carbData = new TextField();
            TextField fiberData = new TextField();
            TextField proteinData = new TextField();
            Label idPrompt = new Label("ID:                    ");
            Label foodNamePrompt = new Label("Food Name:     ");
            Label caloriePrompt = new Label("Calories:           ");
            Label fatPrompt = new Label("Fat:                    ");
            Label carbPrompt = new Label("Carbohydrates: ");
            Label fiberPrompt = new Label("Fiber:                 ");
            Label proteinPrompt = new Label("Protein:             ");
            HBox idBox = new HBox();
            HBox foodNameBox = new HBox();
            HBox calorieBox = new HBox();
            HBox fatBox = new HBox();
            HBox carbBox = new HBox();
            HBox fiberBox = new HBox();
            HBox proteinBox = new HBox();

            // Add all the parts to their respective HBoxes
            idBox.getChildren().addAll(idPrompt, idData);
            foodNameBox.getChildren().addAll(foodNamePrompt, foodNameData);
            calorieBox.getChildren().addAll(caloriePrompt, calorieData);
            fatBox.getChildren().addAll(fatPrompt, fatData);
            carbBox.getChildren().addAll(carbPrompt, carbData);
            fiberBox.getChildren().addAll(fiberPrompt, fiberData);
            proteinBox.getChildren().addAll(proteinPrompt, proteinData);
            VBox foodData = new VBox();
            foodData.getChildren().addAll(idBox, foodNameBox, calorieBox, fatBox, carbBox, fiberBox,
                proteinBox);

            addFoodDialogBox.getChildren().addAll(addFoodLabel, foodData, addFoodFinishedButton);
            addFoodDialogBox.setSpacing(20);
            addFoodPane.setCenter(addFoodDialogBox);

            // Show the pop up window
            addFoodPopUp.setScene(addFoodScene);
            addFoodPopUp.show();

            // Close the window when the user clicks finished
            addFoodFinishedButton.setOnMouseClicked(c -> {


                // Get raw input from user
                String ID = idData.getText();
                String name = foodNameData.getText();
                String newCalorie = calorieData.getText();
                String newFat = fatData.getText();
                String newCarb = carbData.getText();
                String newFiber = fiberData.getText();
                String newProtein = proteinData.getText();
                // If in correct format, add to foodData and foodList
                if (isNonNegativeDouble(newCalorie) && isNonNegativeDouble(newFat)
                    && isNonNegativeDouble(newCarb) && isNonNegativeDouble(newFiber)
                    && isNonNegativeDouble(newProtein)) {

                    FoodItem foodItem = new FoodItem(ID, name);

                    Double calories = Double.parseDouble(newCalorie);
                    Double fat = Double.parseDouble(newFat);
                    Double carb = Double.parseDouble(newCarb);
                    Double fiber = Double.parseDouble(newFiber);
                    Double protein = Double.parseDouble(newProtein);

                    foodItem.addNutrient("calories", calories);
                    foodItem.addNutrient("fat", fat);
                    foodItem.addNutrient("carbohydrate", carb);
                    foodItem.addNutrient("fiber", fiber);
                    foodItem.addNutrient("protein", protein);
                    this.foodData.addFoodItem(foodItem);
                    this.foodListView.addFoodItem(foodItem);
                }
                addFoodPopUp.close();
            });
        });

        Button writeFile = new Button("Write food to a file");

        // When writeFile is clicked, save the currently displayed list to a file of the users
        // choice
        writeFile.setOnMouseClicked(event -> {

            FileChooser fileWriter = new FileChooser();
            fileWriter.setTitle("Write to File");
            FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileWriter.getExtensionFilters().add(extFilter);
            fileWriter.setInitialFileName("FoodList");

            File selectedFile = fileWriter.showSaveDialog(null);
            if (selectedFile != null) {
                String filePath = selectedFile.getAbsolutePath();
                if (filePath.contains(".csv")) {
                    foodData.saveFoodItems(filePath);
                }

            }
        });

        //display the the IO buttons
        
        addLoadFood.getChildren().addAll(loadFood, addFood, writeFile);
        addLoadFood.setPadding(new Insets(0, 12, 15, 0));
        addLoadFood.setSpacing(15);

        //display the Nutrient filter;
        showNutrientFilter(loadAndFilter);
    }

    /**
     * Creates the view for the filter section (including the nutrient table)
     * 
     * @param loadAndFilter, overall VBox for the right hand side of the filter section
     */
    private void showNutrientFilter(VBox loadAndFilter) {

        HBox filterSection = new HBox();

        ////////////////////////////////////
        ///// Add nutrient filter HBox /////
        ////////////////////////////////////
        VBox nutrientFilter = new VBox();
        HBox nutrientAdder = new HBox();
        ObservableList<String> nutrientsToFilter = FXCollections.observableArrayList("Calories",
            "Protein", "Fiber", "Fat", "Carbohydrate");

        // Nutrient filter dropdowns for comparators
        ObservableList<String> typeOfComparators =
            FXCollections.observableArrayList("<=", ">=", "==");
        Label filterText = new Label("Nutrient Filter");
        filterText.setFont(new Font(30));

        // Nutrient Filter description
        Label nutrFilterDescrip =
            new Label("\nFilter the food list by adding nutrient constraints below.\n "
                + "Note: all items excluding calories are in grams.\n");
        nutrientFilter.getChildren().addAll(filterText, nutrFilterDescrip);

        Button addNutrientFilter = new Button("+");
        ComboBox<String> nutrientComboBox = new ComboBox<String>(nutrientsToFilter);
        nutrientComboBox.setValue("Calories");
        ComboBox<String> comparator = new ComboBox<String>(typeOfComparators);
        comparator.setValue("<=");
        TextField filterValue = new TextField();
        filterValue.setText("100");
        filterValue.setPrefWidth(100);

        nutrientAdder.getChildren().addAll(addNutrientFilter, nutrientComboBox, comparator,
            filterValue);
        nutrientAdder.setSpacing(2);

        nutrientFilter.getChildren().addAll(nutrientAdder);



        /////////////////////////////////
        ///// Table of Filters HBox /////
        /////////////////////////////////
        VBox totalFilterList = new VBox();
        totalFilterList.setPadding(new Insets(0, 12, 0, 50));

        Label filterList = new Label("Filter List:");
        filterList.setFont(new Font(30));;

        HBox buttonMagic = new HBox();
        Button removeSelectedFilters = new Button("Remove Selected Filters");
        Button applyFilters = new Button("Apply Selected Nutrient Filters");

        buttonMagic.getChildren().addAll(removeSelectedFilters, applyFilters);
        buttonMagic.setPadding(new Insets(12, 0, 15, 0));
        buttonMagic.setSpacing(15);

        HBox filterTable = new HBox();
        filterTable.setPadding(new Insets(0, 0, 0, 0));
        FoodListView filters = new FoodListView(false); // don't allow duplicate filters

        filters.getView().setMaxHeight(50);
        filters.getView().setPrefWidth(1200);
        
        totalFilterList.getChildren().addAll(filterList, buttonMagic, filters.getView());
        filterSection.getChildren().addAll(nutrientFilter, totalFilterList);


        loadAndFilter.getChildren().addAll(addLoadFood, filterSection);
        loadAndFilter.setPadding(new Insets(0, 0, 20, 0));
        root.setTop(loadAndFilter);


        // Handle all actions related to Nutritional Filtering
        addNutrientFilter.setOnAction(e -> {
        	String nutrientValue = nutrientComboBox.getValue().trim();
        	String comparatorValue = comparator.getValue().trim();
        	String filterVal = filterValue.getText().trim();
        	if (!nutrientValue.isEmpty() && !comparatorValue.isEmpty() && !filterVal.isEmpty()
        		&& isNonNegativeDouble(filterVal)) {
        		filters.addFoodItem(new FoodItemComparator(nutrientValue + " " + comparatorValue + " " + filterVal));
        	}
        });

        removeSelectedFilters.setOnAction(e -> {
            filters.removeSelected();
        });

        applyFilters.setOnAction(e -> {
        	// Number of rules to apply likely will not be incredibly large
            // so time complexity of this will not be an issue. 
        	ArrayList<String> rules = new ArrayList<String>();
        	for (FoodItem foodItemFilter : filters.getSelection()) {
        		rules.add(foodItemFilter.getName());
        	}
        	
        	// Make sure any filtering done by search bar still maintained
        	List<FoodItem> filteredList = foodData.filterByNutrients(rules);
        	
		    foodListView.reset();
		    foodListView.addAll(filteredList);
        });
        
        filterBoth.setOnAction(e ->{ 	
        	// First filter by Nutrient Filter rules 
        	ArrayList<String> rules = new ArrayList<String>();
        	for (FoodItem foodItemFilter : filters.getSelection()) {
        		rules.add(foodItemFilter.getName());
        	}
        	
        	List<FoodItem> filteredByNutrients = foodData.filterByNutrients(rules);
        	
        	// Next filter by name
        	FoodData tempFilterByBoth = new FoodData();
        	for (FoodItem foodItem : filteredByNutrients) {
        		tempFilterByBoth.addFoodItem(foodItem);
        	}
        	
        	
        	foodListView.reset();
        	foodListView.addAll(tempFilterByBoth.filterByName(searchTextField.getText().trim()));
        });
        
    }

    /**
     * Creates the view for the food list.
     */
    private void showFoodList() {
    	
    	/** Food List Label **/
		VBox foodList = new VBox();
		// Top level label
		Label foodListText = new Label("Food List");
		foodListText.setFont(new Font(30));
		
		// Search Bar and Analyze Button
		HBox searchFood = new HBox();
		HBox searchAndAdd = new HBox();
		
		Label searchLabel = new Label("Search for: ");
		
		searchTextField.setOnKeyReleased(e -> {
		    
		    String searchText = searchTextField.getText();
		    if (searchText.equals("")) {
		        foodListView.reset();
		        foodListView.addAll(foodData.getAllFoodItems());
		    } else {
			    List<FoodItem> filteredList = foodData.filterByName(searchText.trim());
			    foodListView.reset();
			    foodListView.addAll(filteredList);
		    }
		    
		});
		searchFood.getChildren().addAll(searchLabel, searchTextField);
		searchAndAdd.getChildren().addAll(searchFood, addToMeal, filterBoth);
		searchAndAdd.setPadding(new Insets(15, 12, 15, 0));

        searchAndAdd.setSpacing(15);
        // Add to pane
        foodList.getChildren().addAll(foodListText, searchAndAdd, foodListView.getView());
        root.setCenter(foodList);
    }

    /**
     * Creates the view for the Meal List.
     */
    private void showMealList() {

        /** Meal List Label **/
        VBox meal = new VBox();
        // Top level label
        Label mealList = new Label("Meal");
        mealList.setFont(new Font(30));
        // Analyze Button
        Button analyze = new Button("Analyze All");
        Button removeButton = new Button("Remove Selected");

        // Information
        // Label analyzeClarification = new Label("Analyze Meal button applies to selected and
        // unselected items.");
        Label calories = new Label("Calories");
        Label protein = new Label("Protein");
        Label fiber = new Label("Fiber");
        Label fats = new Label("Fats");
        Label carbs = new Label("Carbohydrates");

        Label calorie_data = new Label("___");
        Label protein_data = new Label("___");
        Label fiber_data = new Label("___");
        Label fats_data = new Label("___");
        Label carb_data = new Label("___");


        /****************************************
         * SAMPLE HARD-CODED FOOD FOR TABLE *
         ****************************************/

        /** Nutrient Info **/
        HBox analyzeHBox = new HBox();
        HBox meal_info = new HBox();
        HBox analyzeAndMealInfo = new HBox();

        analyzeHBox.getChildren().addAll(analyze, removeButton);
        analyzeHBox.setPadding(new Insets(15, 12, 15, 0));
        analyzeHBox.setSpacing(15);

        analyze.setOnMouseClicked(event -> {
            List<FoodItem> mealFoodItems = mealListView.getFoodItemList();
            double calorieCnt = 0;
            double proteinCnt = 0;
            double fiberCnt = 0;
            double fatCnt = 0;
            double carbCnt = 0;
            for (FoodItem mealFoodItem : mealFoodItems) {
                HashMap<String, Double> nutrients = mealFoodItem.getNutrients();
                calorieCnt += nutrients.get("calories");
                proteinCnt += nutrients.get("protein");
                fiberCnt += nutrients.get("fiber");
                fatCnt += nutrients.get("fat");
                carbCnt += nutrients.get("carbohydrate");
            }
            calorie_data.setText("" + calorieCnt);
            protein_data.setText("" + proteinCnt  + " g");
            fiber_data.setText("" + fiberCnt + " g");
            fats_data.setText("" + fatCnt + " g");
            carb_data.setText("" + carbCnt + " g");


        });

        meal_info.setSpacing(15);
        meal_info.getChildren().addAll(calories, calorie_data, protein, protein_data, fiber,
            fiber_data, fats, fats_data, carbs, carb_data);
        meal_info.setPadding(new Insets(25, 12, 15, 0));
        meal_info.setSpacing(15);
        analyzeAndMealInfo.getChildren().addAll(analyzeHBox, meal_info);


        // Add to Meal List Functionality
        addToMeal.setOnAction(e -> {
            mealListView.addAll(foodListView.getSelection());
        });

        // Remove from Meal List Functionality
        removeButton.setOnAction(e -> {
            mealListView.removeSelected();
        });

        // Add to pane
        meal.getChildren().addAll(mealList, analyzeAndMealInfo, mealListView.getView());
        // meal.setMinHeight(1200);
        // only add pane when analyze button clicked
        root.setBottom(meal);
    }

    /**
     * Determines if String i represents a non-negative double
     * 
     * @param i String to analyze
     * @return true if i represents a non-negative double, else false
     */
    private boolean isNonNegativeDouble(String i) {
        try {
            if (Double.parseDouble(i) < 0)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
