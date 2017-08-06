package com.github.abdurahmanovart.notesrealm.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.github.abdurahmanovart.notesrealm.R;
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

public class CreateNoteActivity extends BaseNoteActivity {

    public static Intent createExplicitIntent(Context context,
                                              String categoryName) {
        Intent intent = new Intent(context, CreateNoteActivity.class);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        return intent;
    }

    //region protected methods

    @Override
    protected int getNoteIdVisibility() {
        return View.INVISIBLE;
    }

    @Override
    protected void handleActivityClosing() {
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

    @Override
    protected void getDataFromIntent() {
        mCategoryName = getIntent().getExtras().getString(EXTRA_CATEGORY_NAME);
    }

    @Override
    protected void onMainButtonClick(View view) {
        saveNote(view);
    }

    //endregion

    //region private methods

    private boolean inputDataEmpty() {
        return mTitleEditText.getText().length() == 0 &&
                mBodyEditText.getText().length() == 0;
    }

    private void saveNote(View view) {
        if (InputDataCorrect()) {
            Snackbar.make(view, R.string.create_note_question, Snackbar.LENGTH_LONG)
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

    private boolean InputDataCorrect() {
        return mTitleEditText.getText().length() >= INPUT_MIN_LENGTH
                && mBodyEditText.getText().length() >= INPUT_MIN_LENGTH;
    }

    private Note fillNoteData() {
        Note note = new Note();
        note.setTitle(mTitleEditText.getText().toString());
        note.setBody(mBodyEditText.getText().toString());
        note.setId(new SimpleDateFormat("EEE, dd-MM-yyyy, HH:mm:ss", Locale.ENGLISH).format(new Date()));
        return note;
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