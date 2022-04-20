package com.example.magicsquare;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    EditText[][] numberArrayET;
    TextView[] rowAnswersTV;
    TextView[] colAnswersTV;

    Button subm;
    Button con;
    Button newGame;

    int[] rowAnswersInt = new int[3];
    int[] colAnswersInt = new int[3];
    String[][] userInputString = new String[3][3];
    final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        subm = findViewById(R.id.submit);
        con = findViewById(R.id.cont);
        newGame = findViewById(R.id.newgame);

        numberArrayET = new EditText[3][3];
        rowAnswersTV = new TextView[3];
        colAnswersTV = new TextView[3];

        createGridLayout(findViewById(R.id.squareGrid));

        state = State.NEWGAME;
        System.out.println("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreState();
        displaySumRowsCols();
        System.out.println("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }

    protected void storeUserInput() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                userInputString[i][j] = numberArrayET[i][j].getText().toString();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle data) {
        super.onSaveInstanceState(data);
        data.putIntArray("rowAnswersInt", rowAnswersInt);
        data.putIntArray("colAnswersInt", colAnswersInt);
        // save state
        data.putSerializable("state", state);
        // save user input
        storeUserInput();
        data.putSerializable("userInputString", userInputString);
        System.out.println("onSaveInstanceState");
    }

    protected void restoreUserInput() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                numberArrayET[i][j].setText(userInputString[i][j]);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle data) {
        super.onRestoreInstanceState(data);

        rowAnswersInt = data.getIntArray("rowAnswersInt");
        colAnswersInt = data.getIntArray("colAnswersInt");
        userInputString = (String[][]) data.getSerializable("userInputString");
        restoreUserInput();
        state = (State) data.get("state");
        System.out.println("onRestoreInstanceState");
    }

    protected void restoreState() {
        switch (state) {
            case PLAY:
                setStatePlay();
                break;
            case FROZEN:
                setStateFrozen();
                break;
            case NEWGAME:
                setupNewGame();
                break;
            case WON:
                setStateWon();
                break;
        }
    }

    protected void formatAndAdd(TextView tv, GridLayout grid, String text, String hint) {
        GridLayout.LayoutParams myGLP = new GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, 1f)
        );
        myGLP.width = 0;
        myGLP.height = 0;
        tv.setLayoutParams(myGLP);
        tv.setText(text);
        tv.setHint(hint);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, getApplicationContext().getResources().getDimension(R.dimen.gridText));
        tv.setEms(1);
        tv.setMaxEms(1);
        tv.setMaxLines(1);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        grid.addView(tv);
    }


// textsize according to screen size

    protected void addEmptyTextView(GridLayout grid) {
        TextView tv = new TextView(this);
        tv.setAlpha(0);
        formatAndAdd(tv, grid, "", "");
    }

    protected void createGridLayout(GridLayout grid) {

        // over GridLayout rows
        for (int i = 0; i < 3; i++) {
            // over GridLayout cols
            for (int j = 0; j < 3; j++) {
                // user input
                EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setFilters(new InputFilter[]{
                        new InputFilter.LengthFilter(1)});
                formatAndAdd(et, grid, "", "?");
                numberArrayET[i][j] = et;
            }
            // =
            formatAndAdd(new TextView(this), grid, "=", "");
            // sum of row
            TextView tv = new TextView(this);
            formatAndAdd(tv, grid, "?", "");
            rowAnswersTV[i] = tv;
        }

        for (int i = 0; i < 3; i++) {
            formatAndAdd(new TextView(this), grid, "=", "");
        }
        addEmptyTextView(grid);
        addEmptyTextView(grid);

        for (int i = 0; i < 3; i++) {
            TextView tv = new TextView(this);
            formatAndAdd(tv, grid, "?", "");
            colAnswersTV[i] = tv;
        }
        addEmptyTextView(grid);
        addEmptyTextView(grid);
    }

    protected void prepareNumbers() {
        java.util.Collections.shuffle(list);
        ListIterator<Integer> it = list.listIterator();

        Arrays.fill(rowAnswersInt, 0);
        Arrays.fill(colAnswersInt, 0);

        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {
                int digit = it.next();
                System.out.println(digit);
                rowAnswersInt[i] += digit;
                colAnswersInt[j] += digit;
            }
        }

        for (int i = 0; i < 3; i++) {
            rowAnswersTV[i].setText(String.valueOf(rowAnswersInt[i]));
            colAnswersTV[i].setText(String.valueOf(colAnswersInt[i]));
        }

    }

    protected void displaySumRowsCols() {
        for (int i = 0; i < 3; i++) {
            rowAnswersTV[i].setText(String.valueOf(rowAnswersInt[i]));
            colAnswersTV[i].setText(String.valueOf(colAnswersInt[i]));
        }
    }

    protected void editTextInput(boolean editable) {
        for (EditText[] editTexts : numberArrayET) {
            for (int j = 0; j < numberArrayET[0].length; j++) {
                editTexts[j].setFocusableInTouchMode(true);
                editTexts[j].setFocusable(editable);
            }
        }
    }

    protected void clearTextInput() {
        for (EditText[] editTexts : numberArrayET) {
            for (int j = 0; j < numberArrayET[0].length; j++) {
                editTexts[j].setText("");
            }
        }
    }

    protected boolean checkAnswers() {
        Integer[] rowUserAnswers = new Integer[3];
        Integer[] colUserAnswers = new Integer[3];
        Arrays.fill(rowUserAnswers, 0);
        Arrays.fill(colUserAnswers, 0);

        // check the sum in rows and cols
        for (int i = 0; i < numberArrayET.length; i++) {
            for (int j = 0; j < numberArrayET[0].length; j++) {
                String value = numberArrayET[i][j].getText().toString();
                if (value.isEmpty()) {
                    return false;
                } else {
                    int userAns = Integer.parseInt(value);
                    if (userAns < 1 || userAns > 9) return false;
                    rowUserAnswers[i] += userAns;
                    colUserAnswers[j] += userAns;
                }
            }
        }

        for (int i = 0; i < rowUserAnswers.length; i++) {
            if (!rowUserAnswers[i].equals(rowAnswersInt[i]) || !colUserAnswers[i].equals(colAnswersInt[i])) {
                return false;
            }
        }

        return true;
    }

    protected void setStateFrozen() {
        editTextInput(false);
        newGame.setEnabled(true);
        subm.setEnabled(false);
        con.setEnabled(true);
        state = State.FROZEN;
    }

    protected void setStateWon() {
        editTextInput(false);
        newGame.setEnabled(true);
        subm.setEnabled(false);
        state = State.WON;
    }

    public void submit(View view) {

        String message;
        if (checkAnswers()) {
            message = "Good job!";
            setStateWon();
        } else {
            message = "Not correct!";
            setStateFrozen();
        }
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_LONG).show();
    }

    public void cont(View view) {
        setStatePlay();
    }

    public void setupNewGame() {
        clearTextInput();
        prepareNumbers();
        setStatePlay();
    }

    protected void setStatePlay() {
        editTextInput(true);
        newGame.setEnabled(false);
        con.setEnabled(false);
        subm.setEnabled(true);
        state = State.PLAY;
    }

    public void newgame(View view) {
        setupNewGame();
    }

    public void exitgame(View view) {
        finish();
    }

    enum State {
        NEWGAME,
        PLAY,
        FROZEN,
        WON
    }
}


