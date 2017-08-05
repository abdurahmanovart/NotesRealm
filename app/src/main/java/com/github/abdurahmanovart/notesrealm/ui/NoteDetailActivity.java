package com.github.abdurahmanovart.notesrealm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.github.abdurahmanovart.notesrealm.R;
import com.github.abdurahmanovart.notesrealm.model.Category;
import com.github.abdurahmanovart.notesrealm.model.Note;

import io.realm.Realm;

/**
 * Activity to show note details
 *
 * @author Abdurakhmanov 06.08.2017
 */

public class NoteDetailActivity extends BaseNoteActivity {

    private boolean mNoteEdited;

    public static Intent createExplicitIntent(Context context,
                                              String categoryName,
                                              String noteTitle,
                                              String noteId,
                                              String noteBody) {
        Intent intent = new Intent(context, NoteDetailActivity.class);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_NOTE_TITLE, noteTitle);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        intent.putExtra(EXTRA_NOTE_BODY, noteBody);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillUIData();
    }

    @Override
    protected void onMainButtonClick(View view) {
        Snackbar.make(view, getString(R.string.edit_note_question), Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editNote();
                    }
                }).show();
    }

    @Override
    protected void onSecondButtonClick(View view) {
        Snackbar.make(view, getString(R.string.delete_note_question), Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteNote();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                mTitleEditText.setText(data.getStringExtra(EXTRA_NOTE_TITLE));
                mBodyEditText.setText(data.getStringExtra(EXTRA_NOTE_BODY));
                mNoteEdited = true;
            }
        }
    }

    @Override
    protected int getMainButtonImageResource() {
        return android.R.drawable.ic_menu_edit;
    }

    @Override
    protected int getSecondButtonVisibility() {
        return View.VISIBLE;
    }

    @Override
    protected boolean isEditTextEnabled() {
        return false;
    }

    protected void handleActivityClosing() {
        if (mNoteEdited) {
            finishWithResultOk();
        } else {
            finish();
        }
    }

    protected void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        mCategoryName = bundle.getString(EXTRA_CATEGORY_NAME);
        mNoteTitle = bundle.getString(EXTRA_NOTE_TITLE);
        mNoteId = bundle.getString(EXTRA_NOTE_ID);
        mNoteBody = bundle.getString(EXTRA_NOTE_BODY);
    }

    //region private methods

    private void fillUIData() {
        mTitleEditText.setText(mNoteTitle);
        mBodyEditText.setText(mNoteBody);
    }

    private void deleteNote() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Category category = mRealm.where(Category.class)
                        .equalTo("categoryName", mCategoryName)
                        .findFirst();
                Note note = mRealm.where(Note.class)
                        .equalTo("id", mNoteId)
                        .findFirst();
                category.getNotes().remove(note);
            }
        });
        finishWithResultOk();
    }

    private void finishWithResultOk() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void editNote() {
        startActivityForResult(EditNoteActivity.createExplicitIntent(getApplicationContext(),
                mNoteTitle,
                mNoteId,
                mNoteBody), 2);
    }

    //endregion
}
