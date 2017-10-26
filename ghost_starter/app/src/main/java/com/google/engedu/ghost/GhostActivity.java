package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    TextView ghostTv, label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        //Link views using fv
        ghostTv = (TextView) findViewById(R.id.ghostText);
        label = (TextView) findViewById(R.id.gameStatus);

        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        // Do computer turn stuff then make it the user's turn again

        //fetch the existing word
        String word = ghostTv.getText().toString();

        //if the word in completed(valid word)
        if (dictionary.isWord(word) && word.length() >= 4) {
            Log.d("TAG", "computerTurn: true");
            label.setText("Computer won");
        }
        //else get the existing word and check for its prefix
        else {

            String longerWord = dictionary.getAnyWordStartingWith(word);

            //if the word with the existing word as prefix exists then add the
            //next character to the existing word
            if (longerWord != null) {
                char nextChar = longerWord.charAt(word.length());
                word += nextChar;
                ghostTv.setText(word);
                label.setText(USER_TURN);
            } else {

                //if no word exists with the entered word as a prefix then tell the user that he can't make up words which are not in dictionary
                label.setText("you can't bluff this , you lost");
            }
        }


        userTurn = true;
       // label.setText(USER_TURN);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        Log.d("TAG", "onKeyUp: "+keyCode+" "+ (char) event.getUnicodeChar());

        char keyPressed = (char) event.getUnicodeChar();

        if (Character.isLetter(keyPressed)) {
            String existingWord = ghostTv.getText().toString();
            existingWord += keyPressed;
            ghostTv.setText(existingWord);


            //call the computer turn
            computerTurn();
            //check for the validity
//            if (dictionary.isWord(existingWord)) {
//                label.setText("VALID WORD");
//            } else
//                label.setText("INVALID WORD");
            return true;
        } else

            return super.onKeyUp(keyCode, event);
    }

    public void challenge(View view) {

        //get the existing word
        String currentWord = ghostTv.getText().toString();

        //if the word is a valid word then the challenge is successful
        if (currentWord.length() > 3 && dictionary.isWord(currentWord)) {
            label.setText("you won");
        } else  {
            // if a word can be formed with the fragment as prefix, declare victory for the computer and display a possible word
            String anotherWord = dictionary.getAnyWordStartingWith(currentWord);
            if (anotherWord != null) {
                label.setText("computer won");
                ghostTv.setText(anotherWord);
            }
            //If a word cannot be formed with the fragment, declare victory for the user
            else {
                label.setText("you won, computer lost this game");
            }
        }


    }
}
