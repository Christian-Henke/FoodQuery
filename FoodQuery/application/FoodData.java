/**
 * Filename:   FoodData.java
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 * @author A-Team 31
 */
public class FoodData implements FoodDataADT<FoodItem> {

    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    // Scanner object for reading and writing to files
    //private Scanner scnr;
    
    // Array of nutrients store info in indexes. Used for performing common 
    // operations on each nutrient.
    // In order that appear in file pass in for adding food items and
    // saving food items to file
    private static final String[] NUTRIENTS = {"calories","fat","carbohydrate","fiber","protein"};
    
    // Types of comparators that can be used in BPTree
    private static final String[] COMPARATORS = {"<=", ">=", "=="};
    
    /**
     * Public constructor
     */
    public FoodData() {
        foodItemList = new ArrayList<FoodItem>();
        indexes = new HashMap<String, BPTree<Double, FoodItem>>();
        // Create BPTree for each nutrient to store data on each FoodItem
        for (String nutrient : NUTRIENTS) {
          indexes.put(nutrient, new BPTree<Double, FoodItem>(3));
        }        
        
    }
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
     */
    @Override
    public void loadFoodItems(String filePath) {
        try (Scanner scnr = new Scanner(new File(filePath))) {
          while (scnr.hasNextLine()) {
              String foodItemData = scnr.nextLine().trim();
              // If doesn't contain all data needed, skip this line
              // Input always string as reading from file
              
              if (foodItemData.split(",").length == 12) { 
                String[] foodItemDataArr = foodItemData.split(",");                
                // If valid format add, else skip without exception
                // TODO: need to check if id not in tree already? Will add to complexity?
                if (foodItemDataArr[0].trim().matches("[A-Za-z0-9]+") // ID must be alphanumeric
                    && !foodItemDataArr[0].trim().isEmpty() // non-empty ID and name
                    && !foodItemDataArr[1].trim().isEmpty()
                	&& isAlphabeticString(foodItemDataArr[2])
                    && foodItemDataArr[2].trim().toLowerCase().equals("calories") // case insensitive
                    && isNonNegativeDouble(foodItemDataArr[3])
                    
                    && isAlphabeticString(foodItemDataArr[4])
                    && foodItemDataArr[4].trim().toLowerCase().equals("fat")
                    && isNonNegativeDouble(foodItemDataArr[5])
                    
                    && isAlphabeticString(foodItemDataArr[6])
                    && foodItemDataArr[6].trim().toLowerCase().equals("carbohydrate")
                    && isNonNegativeDouble(foodItemDataArr[7])
                    
                    && isAlphabeticString(foodItemDataArr[8])
                    && foodItemDataArr[8].trim().toLowerCase().equals("fiber")
                    && isNonNegativeDouble(foodItemDataArr[9])
                    
                    && isAlphabeticString(foodItemDataArr[10])
                    && foodItemDataArr[10].trim().toLowerCase().equals("protein")
                    && isNonNegativeDouble(foodItemDataArr[11])) {
                  FoodItem foodItem = new FoodItem(foodItemDataArr[0].trim(), foodItemDataArr[1].trim());
                  foodItem.addNutrient("calories", Double.parseDouble(foodItemDataArr[3].trim()));
                  foodItem.addNutrient("fat", Double.parseDouble(foodItemDataArr[5].trim()));
                  foodItem.addNutrient("carbohydrate", Double.parseDouble(foodItemDataArr[7].trim()));
                  foodItem.addNutrient("fiber", Double.parseDouble(foodItemDataArr[9].trim()));
                  foodItem.addNutrient("protein", Double.parseDouble(foodItemDataArr[11].trim()));
                  
                  // For BPTree corresponding with each nutrient, add this foodItem and it's 
                  // nutritional value for that nutrient
                  this.addFoodItem(foodItem);
                  
                }
                
              }            
          }
          
          
        } catch(IOException e) {
          // If invalid filePath, don't modify fields and exit without 
          // throwing exception
        }         
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
        List<FoodItem> filteredFoodItems = new ArrayList<FoodItem>();
                
        // If non-alphanumeric search, return without saving 
        if (!substring.matches("[A-Za-z0-9]+")) {
        	return filteredFoodItems;
        }
        
        // Case insensitive search
        substring = substring.toLowerCase();
        
        for (FoodItem foodItem : foodItemList) {
        	if (foodItem.getName().toLowerCase().contains(substring)) {
        		filteredFoodItems.add(foodItem);
        	}	
        }
        
        return filteredFoodItems;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
      ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
      // If no filters applied, return entire foodItemList
      if (rules.isEmpty()) return foodItemList; 
      
      int ruleNum = 0;
      for (String rule : rules) {
        String[] ruleArr = rule.split(" ");
        // If valid nutrient, comparator, and value, else skip
        if (ruleArr.length == 3
        	&& isAlphabeticString(ruleArr[0])
            && Arrays.asList(NUTRIENTS).contains(ruleArr[0].toLowerCase()) // valid nutrient name
            && Arrays.asList(COMPARATORS).contains(ruleArr[1]) // valid comparator
            && isNonNegativeDouble(ruleArr[2])
            ) {
            // Init variables used to perform comparison for clarity
            String nutrient = ruleArr[0].toLowerCase();
            String comparator = ruleArr[1];
            Double key = Double.parseDouble(ruleArr[2]);
          
            // If first rule, add everything that meets requirement
            // since start with empty list
            if (ruleNum == 0) {
              foodItems.addAll(indexes.get(nutrient).rangeSearch(key, comparator));
            } else { // Remove any item that doesn't meet rule in current list
              foodItems.retainAll(indexes.get(nutrient).rangeSearch(key, comparator));
            } 
        }
        ruleNum ++;
      }
      
      return foodItems;
      
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    /**
     * Food item should have calorie, fat, carbohydrate, fiber, and protein 
     * value set to non-zero value or will not be added
     **/
    @Override
    public void addFoodItem(FoodItem foodItem) {	
    	// For BPTree corresponding with each nutrient, add this foodItem and it's 
        // nutritional value for that nutrient
        for (String nutrient : NUTRIENTS) {
          indexes.get(nutrient).insert(foodItem.getNutrientValue(nutrient), foodItem);
        }
        
        foodItemList.add(foodItem);
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }


    /* (non-Javadoc)
     * @see src.application.FoodDataADT#saveFoodItems(java.lang.String)
     */
    @Override
    public void saveFoodItems(String filename) {
      try (FileWriter fileWriter = new FileWriter(filename)) {
    	  // Sort items alphabetically ignoring case
    	  Collections.sort(foodItemList, (FoodItem a, FoodItem b) -> a.getName().compareToIgnoreCase((b.getName())));
    	 
    	  //Create String to store all information to write 
    	  String fileContent = "";
    	  
    	  // Add each food to the file with newline after each
    	  for (FoodItem foodItem : foodItemList) {
    		  fileContent += foodItem.getID() +  ",";
    		  fileContent += foodItem.getName() + ",";
    		  
    		  // foreach loop preserves order so should be formatted correctly
    		  for (String nutrient : NUTRIENTS) {
    			  fileContent += nutrient + ",";
    			  fileContent += foodItem.getNutrientValue(nutrient) + ",";
    		  }
    		  
    		  fileContent += System.lineSeparator();    		  
    	  }  
    	  
    	  fileWriter.write(fileContent);
    	  fileWriter.close();
    	  
      } catch (IOException e) {
    	  // If fail to write to file, simply exit method without returning anything
    	  // or throwing exception
      }
    }
    
    /** 
     * Determines if String i represents a non-negative double
     * @param i String to analyze 
     * @return true if i represents a double, else false
     */
    private boolean isNonNegativeDouble(String i) {
      try {
    	i = i.trim();
        if(Double.parseDouble(i) < 0) return false;
      } catch (Exception e) {
        return false;
      } 
      return true;
    }
    
    /** 
     * Determines if String s consists entirely of alphabetic characters
     * @param s
     * @return true if s is alphabetic
     */
    private boolean isAlphabeticString(String s) {
    	s = s.trim();
    	return s != null && !s.equals("") && s.matches("^[a-zA-Z]*$");
    }

}
