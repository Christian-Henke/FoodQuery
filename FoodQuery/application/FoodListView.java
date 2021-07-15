/**
 * Filename:   FoodListView.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

public class FoodListView {

	public ListView<CheckBox> foodListView; // List View of viewable items
	public List<FoodItem> foodItemList; // List of items being displayed 
	private HashMap<String, FoodItem> selectedFoodItems; // HashMap of selected food items
														 // continuously updated by FoodItemView
														 // ID, FoodItem
	private boolean allowDuplicateNames = true; // allow duplicates by default
	
	public FoodListView(Boolean allowDuplicates) {
		this();
		this.allowDuplicateNames = allowDuplicates;
	}
	
	/**
	 * 
	 */
	public FoodListView() {
		this.foodListView = new ListView<CheckBox>(); // Contains all items want to view
												// at certain instance
		this.foodItemList = new ArrayList<FoodItem>(); // Contains all items
		this.selectedFoodItems = new HashMap<String, FoodItem>();
		this.foodListView.setMinHeight(100);
	}
	
	/**
	 * Returns selected food items
	 * @return
	 */
	public List<FoodItem> getSelection() {
		ArrayList<FoodItem> selected = new ArrayList<FoodItem>();
		selected.addAll(selectedFoodItems.values());
		return selected;
	}
	
	/**
	 * Add foodItem to foodListView and foodItemList
	 * in alphabetical order 
	 */
	public void addFoodItem(FoodItem foodItem) {
		int insertIndex = Collections.binarySearch(foodItemList, foodItem,  (a, b) -> {return a.getName().compareToIgnoreCase(b.getName());});
		
		// If don't allow duplicates and index greater than 
		// or equal to 0 (indicates item already in array), don't add
		if (!allowDuplicateNames && insertIndex >= 0) {return;}
		
		// If item name not in list already, index to insert is complement of index
		if (insertIndex < 0) insertIndex = ~insertIndex;
		foodItemList.add(insertIndex, foodItem);
		FoodItemView foodItemView  = new FoodItemView(foodItem, selectedFoodItems);
		foodListView.getItems().add(insertIndex, foodItemView.getView());
	}
	
	
	/** 
	 * Adds list of foodItems to foodListView and FoodItemList 
	 * in alphabetical order
	 * @param foodItems
	 */
	public void addAll(List<FoodItem> foodItems) {	
		for (FoodItem foodItem : foodItems) {
			addFoodItem(foodItem);
		}
	}
	
	
	/**
	 * Removes selected items from foodListView, foodItemList, and 
	 * selectedFoodItems. 
	 * 
	 *   TODO: may want to change remove from foodListView not to rely on indexs
	 * 
	 */
	public void removeSelected() {
		for (FoodItem foodItem : this.getSelection()) {
			foodListView.getItems().remove(foodItemList.indexOf(foodItem));
			foodItemList.remove(foodItem);
			selectedFoodItems.remove(foodItem.getID());
		}
	}
	
	
	/**
	 * Deletes all items the in foodListView and foodItemList
	 */
	public void reset() {
	    foodListView.getItems().clear();
		foodItemList = new ArrayList<FoodItem>();
		selectedFoodItems = new HashMap<String, FoodItem>();
	}
	
	/**
	 * Returns list view of food items
	 * @return foodListView listview of food items
	 */
	public ListView<CheckBox> getView() {
		return foodListView;
	}
	
	/**
	 * Returns list of food items being displayed 
	 * @return
	 */
	public List<FoodItem> getFoodItemList() {
		return foodItemList;
	}
	
}
