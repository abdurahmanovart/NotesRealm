package com.github.abdurahmanovart.notesrealm.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.abdurahmanovart.notesrealm.R;
import com.github.abdurahmanovart.notesrealm.manager.RealmManager;
import com.github.abdurahmanovart.notesrealm.model.Category;
import com.github.abdurahmanovart.notesrealm.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

/**
 * Activity to create and save note {@link Note}
 *
 * @author Abdurakhmanov
 **/

public class CreateNoteActivity extends AppCompatActivity {

    private static final int INPUT_MIN_LENGTH = 3;
    private static final String EXTRA_CATEGORY_NAME = "extra_category_name";

    private Toolbar mToolbar;
    private TextInputEditText mTitleEditText;
    private TextInputEditText mBodyEditText;
    private FloatingActionButton mCreateNoteButton;

    private Realm mRealm;
    private String mCategoryName;

    public static Intent createExplicitIntent(Context context, String categoryName) {
        Intent intent = new Intent(context, CreateNoteActivity.class);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        return intent;
    }

    //region Activity lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        initUI();
        mRealm = new RealmManager(this).getRealm();
        mCategoryName = getIntent().getExtras().getString(EXTRA_CATEGORY_NAME);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            closeActivity();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    //endregion

    //region private methods

    private void closeActivity() {
        if (inputDataEmpty()) {
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.close_activity)
                    .setMessage(R.string.lose_input_data)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    private boolean inputDataEmpty(){
        return mTitleEditText.getText().length()==0&&mBodyEditText.getText().length()==0;
    }

    private void initUI() {
        initToolbar();
        initInputs();
        initButton();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initInputs() {
        mTitleEditText = (TextInputEditText) findViewById(R.id.title_edit_text);
        mBodyEditText = (TextInputEditText) findViewById(R.id.body_edit_text);
    }

    private void initButton() {
        mCreateNoteButton = (FloatingActionButton) findViewById(R.id.quit_note_button);
        mCreateNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote(view);
            }
        });
    }

    private void saveNote(View view) {
        if (InputDataCorrect()) {
            Snackbar.make(view, getString(R.string.create_note_question), Snackbar.LENGTH_LONG)
                    .setAction(R.string.yes, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Note note = fillNoteData();
                            saveNoteToRealm(note);
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }).show();
        } else {
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        Toast.makeText(getApplicationContext(), "Поля должны содержать минимум " + INPUT_MIN_LENGTH + " символов", Toast.LENGTH_LONG).show();
    }

    @NonNull
    private Note fillNoteData() {
        Note note = new Note();
        note.setTitle(mTitleEditText.getText().toString());
        note.setBody(mBodyEditText.getText().toString());
        note.setId(new SimpleDateFormat("EEE, dd-MM-yyyy, HH:mm:ss", Locale.ENGLISH).format(new Date()));
        return note;
    }

    private boolean InputDataCorrect() {
        return mTitleEditText.getText().length() >= INPUT_MIN_LENGTH
                && mBodyEditText.getText().length() >= INPUT_MIN_LENGTH;
    }

    private void saveNoteToRealm(@NonNull final Note note) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Category category = realm.where(Category.class)
                        .equalTo("categoryName", mCategoryName).findFirst();
                category.getNotes().add(note);
            }
        });
    }

    //endregion

}