/**
 * Filename:   FoodItemView.java
 * Project:    Milestone 3
 * Authors:    A-team 31
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   12/12/18 10pm
 * Version:    1.0
 * 
 * Credits:    none
 * 
 * Bugs:       no known bugs
 */

package application;

import java.util.HashMap;

import javafx.scene.control.CheckBox;

public class FoodItemView {	
	
	private CheckBox foodView;
	private FoodItem foodItem;
	
	public FoodItemView(FoodItem foodItem, HashMap<String, FoodItem> selectedFoodItems) {
		foodView = new CheckBox(foodItem.getName());
		this.foodItem = foodItem;
		// Set CheckBox ID to that of the FoodItem
		foodView.setId(foodItem.getID());
		
		// If CheckBox selected or unselected, update selectedFoodItems HashMap
		foodView.setOnAction(event -> {
			if (foodView.isSelected()) {
				selectedFoodItems.put(foodView.getId(), this.foodItem);
			} else {
				selectedFoodItems.remove(foodView.getId());
			}
		}	
		);
	}
	
	public CheckBox getView() {
		return foodView;
	}

	public FoodItem getFoodItem() {
		return foodItem;
	}
}
