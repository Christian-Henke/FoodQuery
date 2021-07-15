/**
 * Filename:   BPTree.java
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set. 
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 * @author A-team 31
 * 
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree (the highest node)
    private Node root;
    
    // Branching factor is the number of children nodes 
    // for internal nodes of the tree
    private int branchingFactor;
    
    
    /**
     * Public constructor
     * 
     * @param branchingFactor 
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
               "Illegal branching factor: " + branchingFactor);
        }
        // set up the branching factor
        this.branchingFactor = branchingFactor;
        // create blank node for root
        root = new LeafNode();
    }
    
    
    /**
     * Inserts the key and value in the appropriate nodes in the tree
     * 
     * Note: key-value pairs with duplicate keys can be inserted into the tree.
     * 
     * @param key
     * @param value
     */
    @Override
    public void insert(K key, V value) {
         // begin inserting at the root
         root.insert(key, value);
    }
    
    /**
     * Gets the values that satisfy the given range 
     * search arguments.
     * 
     * Value of comparator can be one of these: 
     * "<=", "==", ">="
     * 
     * Example:
     *     If given key = 2.5 and comparator = ">=":
     *         return all the values with the corresponding 
     *      keys >= 2.5
     *      
     * If key is null, return empty list.
     * If comparator is null, empty, or not according
     * to required form, return empty list.
     * 
     * @param key to be searched
     * @param comparator is a string
     * @return list of values that are the result of the 
     * range search; if nothing found, return empty list
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        // if the key or comparator are null
        if (key==null || comparator==null)
            return new ArrayList<V>();  // return empty list

        // if the comparator is not in the required form
        if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();  // return empty list
        // begin searching at the root
        return root.rangeSearch(key, comparator);
    }
    
    /**
     * Returns a string representation for the tree
     * This method is provided to students in the implementation.
     * 
     * Student edit:
     * A node shows its predecessor as [node]*[predecessor]
     * 
     * @return a string representation
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) { 
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString() + "*");
                    // point to a prev in the form: node*prev
                    if (node instanceof BPTree.LeafNode && ((BPTree.LeafNode) node).previous!=null)
                        sb.append(((LeafNode) node).previous.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }
    
    
    /**
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     * 
     * @author sapan
     */
    private abstract class Node {
        
        // List of keys
        List<K> keys;
        
        /**
         * Package constructor
         */
        Node() {
            this.keys = new ArrayList<K>();
        }
        
        /**
         * Inserts key and value in the appropriate leaf node 
         * and balances the tree if required by splitting
         *  
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();
        
        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();
        
        /*
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * 
         * @return boolean
         */
        abstract boolean isOverflow();
        
        public String toString() {
            return keys.toString();
        }
    
    } // End of abstract class Node
    
    
    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     * 
     * @author sapan
     * @author A-team 31
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;
        
        /**
         * Package constructor
         */
        InternalNode() {
            super();  // this.keys = new ArrayList<K>();
            this.children = new ArrayList<Node>();
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            // return the first key of the first child
            return children.get(0).getFirstLeafKey();
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            // definition of overflow for internal node:
            // have more children than branching factor
            return children.size()>branchingFactor;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {
            // recurse down the tree towards the leaf where we insert
            int loc = Collections.binarySearch(keys, key);  
            int childIndex = loc >= 0 ? loc + 1 : -loc - 1;  
            Node child = children.get(childIndex);
            child.insert(key, value);
            
            // if the child is overflowing
            if (child.isOverflow()) {
                // split the child into the child and a new sibling
                Node sibling = child.split();
                
                // add the sibling into the correct location in the keys list
                int siblingLoc = Collections.binarySearch(keys, key);
                // if the key is already in the keys list
                if (siblingLoc>=0) {
                    children.set(siblingLoc+1, sibling);
                // if the key is not in the keys list yet
                } else {
                    keys.add((-siblingLoc-1), sibling.getFirstLeafKey());
                    children.add((-siblingLoc-1)+1,  sibling);
                }
            }
            // exceptional case for if this node, as the root, overflows
            if (root.isOverflow()) {
                // split as usually, except we split this node
                // and update the root
                Node sibling = split();
                InternalNode newRoot = new InternalNode();
                newRoot.keys.add(sibling.getFirstLeafKey());
                newRoot.children.add(this);
                newRoot.children.add(sibling);
                root = newRoot;
            }
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            // take half of the keys & children
            int from = keys.size() / 2 + 1;
            int to = keys.size();
            // and put them into a new sibling internal node
            InternalNode sibling = new InternalNode();
            sibling.keys.addAll(keys.subList(from, to));
            sibling.children.addAll(children.subList(from, to + 1));

            // remove the keys and children from this node
            keys.subList(from - 1, to).clear();
            children.subList(from, to + 1).clear();

            // return the new sibling node
            return sibling;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
            for (int i = 0; i < keys.size(); i++) {
                // if a key is larger than the given key, 
                if (keys.get(i).compareTo(key) > 0) {
                    // do the search on that key's left child
                    return children.get(i).rangeSearch(key, comparator);
                }
            }

            // if the key is never larger than the given key, 
            // recurse down the last node anyways.
            return children.get(children.size() - 1).rangeSearch(key, comparator);
        }
    } // End of class InternalNode
    
    
    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     * 
     * @author sapan
     * @author A-team 31
     */
    private class LeafNode extends Node {
        // List of values
        List<ArrayList<V>> values;
        
        // Reference to the next leaf node
        LeafNode next;
        
        // Reference to the previous leaf node
        LeafNode previous;
        
        /**
         * Package constructor
         */
        LeafNode() {
            super();  // this.keys = new ArrayList<K>();
            values = new ArrayList<ArrayList<V>>();
        }
        
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            // the first key in this leaf node
            return keys.get(0);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            // definition of overflow for a leaf node:
            // number of keys reaches or surpasses branching factor
            return keys.size() >= branchingFactor;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
            // search for if the key is in the node already
            int loc = Collections.binarySearch(keys, key);
            
            // if the key is in the leaf already
            if (loc >= 0) {
                // add the value to the value list corresponding to that key
                values.get(loc).add(value);
            // if the key is not in the leaf yet
            } else {
                // the binarySearch() method gives the location
                // for a new (key,value) pair as (-returned-1) 
                
                // add a new key there
                keys.add((-loc-1), key);
                // and a corresponding value list 
                ArrayList<V> newValues = new ArrayList<V>();
                // with its first value
                newValues.add(value);
                values.add((-loc-1), newValues);
            }
            
            // exceptional case for if this node, as the root, overflows
            if (root.isOverflow()) {
                // split as usually, except we split this node
                // and update the root
                Node sibling = split();
                InternalNode newRoot = new InternalNode();
                newRoot.keys.add(sibling.getFirstLeafKey());
                newRoot.children.add(this);
                newRoot.children.add(sibling);
                root = newRoot;
            }
            
            
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            // create a new sibling of this node
            LeafNode sibling = new LeafNode();
            
            // take the last half of this nodes keys and value lists
            int from = keys.size()/2; 
            int to = keys.size();
            // and add them to the sibling
            sibling.keys.addAll(keys.subList(from, to));
            sibling.values.addAll(values.subList(from, to));
            // and remove them from this node
            keys.subList(from, to).clear();
            values.subList(from, to).clear();

            // update linked list pointers
            if (this.next!=null)
                this.next.previous = sibling;
            sibling.next = next;
            sibling.previous = this;
            next = sibling;
             // return the new sibling
            return sibling;
        }
        
        /**
         * Returns a List of all the values in the this leaf and 
         * values in leaves to the right of this one
         * 
         * @return List of all values in this leaf and right leaves
         */
        private void grabRightLeaves(List<V> vList) {
            for (List<V> v : values) {
                vList.addAll(v);
            }

            // recursively add all the values in the next nodes
            if (next != null)
                next.grabRightLeaves(vList);
        }

        /**
         * Returns a List of all the values in this leaf and 
         * values in leaves to the left of this one
         * 
         * @return List of all values in this leaf and left leaves
         */
        private void grabLeftLeaves(List<V> vList) {

            // add all the values in this node
            for (List<V> v : values) {
                vList.addAll(v);
            }

            // recursively add all the values in the previous nodes
            if (previous != null)
                previous.grabLeftLeaves(vList);
        }
        
        /**
         * (non-Javadoc)
         * 
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
           // create a new value list
           List<V> vList = new ArrayList<V>();
           // iterate through all the keys
           for (int i = 0; i < keys.size(); i++) {
               // add all values for keys equal to our key
               if (keys.get(i).equals(key))
                   vList.addAll(values.get(i));
               // add all values for keys<=our key if that's the comparator
               if (keys.get(i).compareTo(key) > 0 && comparator.equals(">="))
                   vList.addAll(values.get(i));
               // add all values for keys>=our key if that's the comparator
               if (keys.get(i).compareTo(key) < 0 && comparator.equals("<="))
                   vList.addAll(values.get(i));
           }
            // after going through the list, get the rest of the values in the tree based on the
            // comparator
            if (comparator.equals(">=") && next != null) {
                next.grabRightLeaves(vList);
            }
            if (comparator.equals("<=") && previous != null) {
                previous.grabLeftLeaves(vList);
            }

           // return an unsorted values list
           return vList;
        }
        
    } // End of class LeafNode
    
    
    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create empty BPTree with branching factor of 3
        BPTree<Integer, Integer> bpTree = new BPTree<>(3);

        // create a pseudo random number generator
        Random rnd1 = new Random();
        
        // build an ArrayList of random values and add to BPTree also
        // allows for comparing the contents of the ArrayList 
        // against the contents and functionality of the BPTree
        // does not ensure BPTree is implemented correctly
        // just that it functions as a data structure with
        // insert, rangeSearch, and toString() working.
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            // random integer to insert
            Integer j = rnd1.nextInt(100);
            list.add(j);
            bpTree.insert(j,j);
            
            // output the insert and result
            System.out.println("\n\n\ninserting: " + (j));
            System.out.println("List structure:\n" + list.toString());
            System.out.println("Tree structure:\n" + bpTree.toString());
        }
        
        // output some <= filters 
        System.out.println("\n\n\n");
        List<Integer> filteredValues1 = bpTree.rangeSearch(10, "<=");
        List<Integer> filteredValues2 = bpTree.rangeSearch(30, "<=");
        List<Integer> filteredValues3 = bpTree.rangeSearch(40, "<=");
        List<Integer> filteredValues4 = bpTree.rangeSearch(50, "<=");
        List<Integer> filteredValues5 = bpTree.rangeSearch(70, "<=");
        Collections.sort(filteredValues1);
        Collections.sort(filteredValues2);
        Collections.sort(filteredValues3);
        Collections.sort(filteredValues4);
        Collections.sort(filteredValues5);
        System.out.println("Filtered values: " + filteredValues1.toString());
        System.out.println("Filtered values: " + filteredValues2.toString());
        System.out.println("Filtered values: " + filteredValues3.toString());
        System.out.println("Filtered values: " + filteredValues4.toString());
        System.out.println("Filtered values: " + filteredValues5.toString());

        
        
        // output some >= filters 
        System.out.println("\n\n\n");
        List<Integer> filteredValues1g = bpTree.rangeSearch(10, ">=");
        List<Integer> filteredValues2g = bpTree.rangeSearch(30, ">=");
        List<Integer> filteredValues3g = bpTree.rangeSearch(40, ">=");
        List<Integer> filteredValues4g = bpTree.rangeSearch(50, ">=");
        List<Integer> filteredValues5g = bpTree.rangeSearch(70, ">=");
        Collections.sort(filteredValues1g);
        Collections.sort(filteredValues2g);
        Collections.sort(filteredValues3g);
        Collections.sort(filteredValues4g);
        Collections.sort(filteredValues5g);
        System.out.println("Filtered values: " + filteredValues1g.toString());
        System.out.println("Filtered values: " + filteredValues2g.toString());
        System.out.println("Filtered values: " + filteredValues3g.toString());
        System.out.println("Filtered values: " + filteredValues4g.toString());
        System.out.println("Filtered values: " + filteredValues5g.toString());

    }

} // End of class BPTree
