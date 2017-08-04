package com.github.abdurahmanovart.notesrealm.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.abdurahmanovart.notesrealm.R;
import com.github.abdurahmanovart.notesrealm.manager.RealmManager;
import com.github.abdurahmanovart.notesrealm.model.Category;
import com.github.abdurahmanovart.notesrealm.model.Note;

import io.realm.Realm;

public class NoteDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_CATEGORY_NAME = "extra_category_name";
    private static final String EXTRA_NOTE_TITLE = "extra_note_title";
    private static final String EXTRA_NOTE_ID = "extra+note_id";
    private static final String EXTRA_NOTE_BODY = "extra_note_body";

    private String mCategoryName;
    private Toolbar mToolbar;
    private TextView mNoteTitleTextView;
    private TextView mNoteIdTextView;
    private TextView mNoteBodyTextView;
    private FloatingActionButton mEditNoteButton;
    private FloatingActionButton mDeleteNoteButton;

    private String mNoteTitle;
    private String mNoteId;
    private String mNoteBody;

    private Realm mRealm;

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
        setContentView(R.layout.activity_note_detail);
        mRealm = new RealmManager(this).getRealm();
        getDataFromIntent();
        initUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_note_button:
                Snackbar.make(v, getString(R.string.delete_note_question), Snackbar.LENGTH_LONG)
                        .setAction(R.string.yes, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteNote();
                            }
                        }).show();
                break;
            case R.id.edit_note_button:
                //TODO 4.08.2017 edit button;
                break;
        }
    }

    public void getDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        mCategoryName = bundle.getString(EXTRA_CATEGORY_NAME);
        mNoteTitle = bundle.getString(EXTRA_NOTE_TITLE);
        mNoteId = bundle.getString(EXTRA_NOTE_ID);
        mNoteBody = bundle.getString(EXTRA_NOTE_BODY);
    }

    private void initUI() {
        initToolbar();
        mNoteTitleTextView = (TextView) findViewById(R.id.note_title_text_view);
        mNoteIdTextView = (TextView) findViewById(R.id.notes_id_text_view);
        mNoteBodyTextView = (TextView) findViewById(R.id.note_body_text_view);

        mDeleteNoteButton = (FloatingActionButton) findViewById(R.id.delete_note_button);
        mEditNoteButton = (FloatingActionButton) findViewById(R.id.edit_note_button);
        mDeleteNoteButton.setOnClickListener(this);
        mEditNoteButton.setOnClickListener(this);

        mNoteTitleTextView.setText(mNoteTitle);
        mNoteIdTextView.setText(mNoteId);
        mNoteBodyTextView.setText(mNoteBody);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
