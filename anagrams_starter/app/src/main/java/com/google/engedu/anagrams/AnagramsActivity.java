package com.google.engedu.anagrams;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class AnagramsActivity extends AppCompatActivity {

    public static final String START_MESSAGE = "Find as many words as possible that can be formed by adding one letter to <big>%s</big> (but that do not contain the substring %s).";
    private AnagramDictionary dictionary;
    private String currentWord;
    private ArrayList<String> anagrams;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anagrams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new AnagramDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        // Set up the EditText box to process the content of the box when the user hits 'enter'
        final EditText editText = (EditText) findViewById(R.id.editText);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setImeOptions(EditorInfo.IME_ACTION_GO);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    processWord(editText);
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void processWord(EditText editText) {
        TextView resultView = (TextView) findViewById(R.id.resultView);
        String word = editText.getText().toString().trim().toLowerCase();
        if (word.length() == 0) {
            return;
        }
        String color = "#cc0029";
        if (dictionary.isGoodWord(word, currentWord) && anagrams.contains(word)) {
            anagrams.remove(word);
            color = "#00aa29";
        } else {
            word = "X " + word;
        }
        resultView.append(Html.fromHtml(String.format("<font color=%s>%s</font><BR>", color, word)));
        editText.setText("");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_anagrams, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean defaultAction(View view) {
        TextView gameStatus = (TextView) findViewById(R.id.gameStatusView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        EditText editText = (EditText) findViewById(R.id.editText);
        TextView resultView = (TextView) findViewById(R.id.resultView);
        if (currentWord == null) {
            currentWord = dictionary.pickGoodStarterWord();
            anagrams = dictionary.getAnagramsWithOneMoreLetter(currentWord);
            gameStatus.setText(Html.fromHtml(String.format(START_MESSAGE, currentWord.toUpperCase(), currentWord)));
            fab.setImageResource(android.R.drawable.ic_menu_help);
            fab.hide();
            resultView.setText("");
            editText.setText("");
            editText.setEnabled(true);
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        } else {
            editText.setText(currentWord);
            editText.setEnabled(false);
            fab.setImageResource(android.R.drawable.ic_media_play);
            currentWord = null;
            resultView.append(TextUtils.join("\n", anagrams));
            gameStatus.append(" Hit 'Play' to start again");
        }
        return true;
    }
}
