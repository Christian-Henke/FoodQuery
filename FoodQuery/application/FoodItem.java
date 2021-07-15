/**
 * Filename:   FoodItem.java
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
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a food item with all its properties.
 * 
 * @author A-Team 31
 */
public class FoodItem {
	
    // The name of the food item.
    private String name;

    // The id of the food item.
    private String id;

    // Map of nutrients and value.
    private HashMap<String, Double> nutrients;
    
    /**
     * Constructor
     * @param name name of the food item
     * @param id unique id of the food item 
     */
    public FoodItem(String id, String name) {
        this.id = id;
        this.name = name;
        nutrients = new HashMap<String, Double>();
    }
    
    /**
     * Gets the name of the food item
     * 
     * @return name of the food item
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the unique id of the food item
     * 
     * @return id of the food item
     */
    public String getID() {
        return id;
    }
    
    /**
     * Gets the nutrients of the food item
     * 
     * @return nutrients of the food item
     */
    public HashMap<String, Double> getNutrients() {
        return nutrients;
    }

    /**
     * Adds a nutrient and its value to this food. 
     * If nutrient already exists, updates its value.
     * 
     * @param name The name of the nutrient
     * @param value The amount of nutrient this item has
     */
    public void addNutrient(String name, double value) {
        nutrients.put(name, value);
        this.name = this.name + name + String.valueOf(value);
    }

    /**
     * Returns the value of the given nutrient for this food item. 
     * If not present, then returns 0.
     * 
     * @param name The name of the nutrient to lookup
     */
    public double getNutrientValue(String name) {
        return nutrients.get(name)==null ? 0 : nutrients.get(name);
    }
    
}
