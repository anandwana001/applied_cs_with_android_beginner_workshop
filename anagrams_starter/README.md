# ANAGRAMS

## Preparation

We will be using a few data structures in the workshop activity, so please look over your course (or online) notes on [ArrayLists](https://www.youtube.com/watch?v=OI0jOKxoDO4), [HashSets](https://www.youtube.com/watch?v=Wd4jfE-iNnE), and [HashMaps](https://docs.oracle.com/javase/7/docs/api/java/util/HashMap.html). You should be able to confidently insert, delete, access and check existence for elements using these data structures in Java.

<a href="http://www.youtube.com/watch?feature=player_embedded&v=eMymKAFYaCs
" target="_blank"><img src="http://img.youtube.com/vi/eMymKAFYaCs/0.jpg" 
alt="Applied CS ArrayList" width="560" height="315" /></a>

<a href="http://www.youtube.com/watch?feature=player_embedded&v=O-zTuD8JRbE
" target="_blank"><img src="http://img.youtube.com/vi/O-zTuD8JRbE/0.jpg" 
alt="Applied CS ArrayList" width="560" height="315" /></a>

As an example activity using HashMaps, create a program (not necessarily an Android app - command-line is fine) that will take in a three-letter country code (see ISO-3166) and will return the full name of the country to which it belongs. For example:

Input | Output
----- | ---------------------------------------------
GBR  | United Kingdom of Great Britain and Northern Ireland
IDN  | Indonesia
IND  | India
   
As an extension, if the input is greater than 3 letters, consider it as the name of a country, and return the three-letter code for it. Write a helpful error message if the input is neither a valid code nor a country name.


## Workshop
#### Objectives
By the end of this workshop you will:
* Be familiar with how dictionaries can be used to store data (in this case words)
* Have used a hash maps to store groupings of words which are anagrams
* Be able to explain the limitations that some data structures face when working with large data sets 

#### Aim
For this workshop, you will be creating an Android app for a simple anagram game.
You'll notice that the workshop has 3 distinct milestones to complete the activity.

#### Timing
<span style="background-color: #E0F7FA"> In total, the tasks in this workshop should take you around 3-4 hours to complete. However, it's broken down into milestones which can be completed in around an hour each.</span>

#### Scenario
An anagram is a word formed by rearranging the letters of another word. For example, cinema is an anagram of iceman.

<a href="http://www.youtube.com/watch?feature=player_embedded&v=_C33CdeHgrc
" target="_blank"><img src="http://img.youtube.com/vi/_C33CdeHgrc/0.jpg" 
alt="Applied CS ArrayList" width="560" height="315" /></a>

The mechanics of the game are as follows:

The game provides the user with a word from the dictionary.
The user tries to create as many words as possible that contain all the letters of the given word plus one additional letter. Note that adding the extra letter at the beginning or the end without reordering the other letters is not valid. For example, if the game picks the word 'ore' as a starter, the user might guess 'rose' or 'zero' but not 'sore'.
The user can give up and see the words that they did not guess.

#### Difficulty
<span style="background-color: #E0F7FA">In order to ensure that the game is not too difficult, the computer will only propose words that have at least 5 possible valid anagrams.</span>

#### Starter Code
We have provided you with some [starter code](https://cswithandroid.withgoogle.com/content/assets/img/anagrams_starter.zip) that contains a 10,000-word dictionary and handles the UI portions of this game and you will be responsible for writing the AnagramDictionary class that handles all word manipulations.

#### Tour of the Code
The starter code is composed of two java classes:

* AnagramsActivity: In Android development an Activity is a single, focused thing that the user can do. Most of our apps in this class will have a single activity but often apps are made up of multiple activities (e.g. login, settings, etc.). 
The starter code implements several methods:

	* 	onCreate: this method gets called by the system when the app is launched. It is made up of some boilerplate code plus code that opens the word list to initialize the dictionary and code to connect the text box to the processWord helper.
	* 	processWord: A helper that adds words to the UI and colors them.
	* 	onCreateOptionsMenu: boilerplate
	* 	onOptionsItemSelected: boilerplate
	* 	defaultAction: This is the handler that is called when the floating button is clicked. Depending on the game mode, it either starts the game or shows the missing answer to the previous game.
* AnagramDictionary: This class will store the valid words from the text file and handle selecting and checking words for the game. This is where your code will among the following methods:
	* 	AnagramDictionary: The constructor. It should store the words in the appropriate data structures (details below).
	* 	isGoodWord: Asserts that the given word is in the dictionary and isn't formed by adding a letter to the start or end of the base word.
	* 	getAnagrams: Creates a list of all possible anagrams of a given word.
	* 	getAnagramsWithOneMoreLetter: Creates a list of all possible words that can be formed by adding one letter to the given word.
	* 	pickGoodStarterWord: Randomly selects a word with at least the desired number of anagrams.

#### Milestone 1: Essentials
The first milestone focuses on creating a very simply working program. You’ll be implementing the foundations which will in turn be built on in Milestones 2 and 3.

##### AnagramDictionary
We will start by implementing a simplified version of the game that has the user guess anagrams of the given word.
To do so, your first task will be to advance the implementation of the AnagramDictionary's constructor. Each word that is read from the dictionary file should be stored in an ArrayList (called wordList).
We will store duplicates of our words in some other convenient data structures later but wordList will do for now.

##### getAnagrams
Implement getAnagrams which takes a string and finds all the anagrams of that string in our input. Our strategy for now will be straight-forward: just compare each string in wordList to the input word to determine if they are anagrams. But how shall we do that?
There are different strategies that you could employ to determine whether two strings are anagrams of each other (like counting the number of occurrences of each letter) but for our purpose you will create a helper function (call it sortLetters) that takes a String and returns another String with the same letters in alphabetical order (e.g. "post" -> "opst"). Determining whether two strings are anagrams is then a simple matter of checking that they are the same length (for the sake of speed) and checking that the sorted versions of their letters are equal.

##### Stop and Check
<span style="background-color: #FBE9E7">At this point, you should have a working app so try running it on your device and verify that it works. You can change the hard-coded return value of pickGoodStarterWord to try out your code with different words (e.g. "skate").</span>

##### wordSet and lettersToWord
Unfortunately, the straight-forward strategy will be too slow for us to implement the rest of this game. So we will need to revisit our constructor and find some data structures that store the words in ways that are convenient for our purposes. We will create two new data structures (in addition to wordList):

* A HashSet (called wordSet) that will allow us to rapidly (in O(1)) verify whether a word is valid.
* A HashMap (called lettersToWord) that will allow us to group anagrams together. We will do this by using the sortLetters version of a string as the key and storing an ArrayList of the words that correspond to that key as our value. For example, we may have an entry of the form: key: "opst" value: ["post", "spot", "pots", "tops", ...].

As you process the input words, call sortLetters on each of them then check whether lettersToWord already contains an entry for that key. If it does, add the current word to ArrayList at that key. Otherwise, create a new ArrayList, add the word to it and store in the HashMap with the corresponding key.

Once you have completed this, you have reached the end of Milestone 1! You’re now ready to move on the second milestone, where you’ll be adding more complexity to your program.

#### Milestone 2: Adding quality
Milestone 2 is all about ensuring that the words picked are suitable for the anagram game. Just as the previous milestone, this one is split up into three sections.

##### isGoodWord
Your next task is to implement isGoodWord which checks:
the provided word is a valid dictionary word (i.e., in wordSet), and
the word does not contain the base word as a substring.
Example

With the base word 'post':

Input | Output
----- | ---------------------------------------------
isGoodWord("nonstop")	| true
isGoodWord("poster")		| false
isGoodWord("lamp post")	| 	false
isGoodWord("spots")	| 	true
isGoodWord("apostrophe")	| 	false

Checking whether a word is a valid dictionary word can be accomplished by looking at wordSet to see if it contains the word. Checking that the word does not contain the base word as a substring is left as a challenge!

##### getAnagramsWithOneMoreLetter

Finally, implement getAnagramsWithOneMoreLetter which takes a string and finds all anagrams that can be formed by adding one letter to that word.
Be sure to instantiate a new ArrayList as your return value then check the given word + each letter of the alphabet one by one against the entries in lettersToWord.
Also update defaultAction in AnagramActivity to invoke getAnagramsWithOneMoreLetter instead of getAnagrams.

##### Stop and Check

<span style="background-color: #FBE9E7">At this point, you should have a working app so try running your app on your device and verify that it works although the game will be a bit boring since it will always use the same starter word.</span>

##### pickGoodStarterWord

If your game is working, proceed to implement pickGoodStarterWord to make the game more interesting. Pick a random starting point in the wordList array and check each word in the array until you find one that has at least MIN_NUM_ANAGRAMS anagrams. Be sure to handle wrapping around to the start of the array if needed.

##### Stop and Check

<span style="background-color: #FBE9E7">Run your app again to make sure it's working.
Two thirds of the way through! Just one milestone and the extension before you’re done.</span>

#### Milestone 3: Refactoring
At this point, the game is functional but can be quite hard to play if you start off with a long base word. To avoid this, let's refactor AnagramDictionary to give words of increasing length.

This refactor starts in the constructor where, in addition to populating wordList, you should also store each word in a HashMap (let's call it sizeToWords) that maps word length to an ArrayList of all words of that length. This means, for example, you should be able to get all four-letter words in the dictionary by calling sizeToWords.get(4).
You should also create a new member variable called wordLength and default it to DEFAULT_WORD_LENGTH. Then in pickGoodStarterWord, restrict your search to the words of length wordLength, and once you're done, increment wordLength (unless it's already at MAX_WORD_LENGTH) so that the next invocation will return a larger word.

#### Extensions
This activity (like all future activities) contains some optional extensions. If time permits, you should attempt at least one extension, either from the list below or one that you have invented yourself.

* Two-letter mode: switch to allowing the user to add two letters to form anagrams
* Optimize word selection by removing words that do not have enough anagrams from the pool of possible starter words. Note that those words should still remain in wordSet since they can still be used as anagrams to other words.
* Two-word mode: Allow the user to add one letter to a pair of words to form two new valid words.
* Your idea

In what other ways could you extend the program?