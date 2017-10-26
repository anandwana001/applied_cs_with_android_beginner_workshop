package com.google.engedu.ghost;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {

        //get the root node in this object
        TrieNode currentNode = this;
        String key;

        //loop through all the chars in the word
        for (int i = 0; i < s.length(); i++) {
            //fetch individual characters in the word and check if that char exists as a key
            key = "" + s.charAt(i);
            if (!currentNode.children.containsKey(key)) {
                //if it doesn't exist then add it as a new key with empty node as child
                currentNode.children.put(key, new TrieNode());
            }
            //go to the next child in that node( with next char as key)
            currentNode = currentNode.children.get(key);
        }

        //make the boolean true after the whole word is added
        currentNode.isWord = true;

    }


    public boolean isWord(String s) {

        //get the root node of this object
        TrieNode currentNode = this;
        String key;

        //maintain a boolean to check for end of word
        boolean isFound = false;

        //loop through all the chars
        for (int i = 0; i < s.length(); i++) {
            key = "" + s.charAt(i);
            //if the current char is present as a key , then go to its child node and further so on
            if (currentNode.children.containsKey(key)) {
                currentNode = currentNode.children.get(key);
            }
            //if any char is not found , then break out of the loop
            else {
                isFound = false;
                return isFound;
            }
        }

        //if the loop was completed with all chars present in the node, then make the boolean true and return it.
        if (currentNode.isWord) {
            isFound = true;
        }
        return isFound;

    }

    public String getAnyWordStartingWith(String s) {
        //fetch the current node of that object
        TrieNode currentNode = this;

        //a set to fetch all the keys of the last node( char)
        Set keyList;

        String key;
        String word = "";


        if (!s.isEmpty()) {

            //loop through the characters of the prefix s
            for (int i = 0; i < s.length(); i++) {

                key = "" + s.charAt(i);
                //add the char to the word to be returned
                word += key;
                //check if that char is present as a key, if not then that word is not a valid word so break out and return
                if (currentNode.children.containsKey(key)) {
                    currentNode = currentNode.children.get(key);
                } else {
                    return null;
                }

            }
        }

        //after you have obtained the last node in the prefix word
        //then get all the next keys possible and select a random next key till that word is completed( i.e check for the boolean isWord)
        while (!currentNode.isWord) {
            keyList = currentNode.children.keySet();
            key = keyList.toArray()[new Random().nextInt(keyList.size())].toString();
            word += key;
            currentNode = currentNode.children.get(key);
        }

        return word;

    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
