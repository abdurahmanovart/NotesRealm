package com.github.abdurahmanovart.notesrealm.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.abdurahmanovart.notesrealm.R;
import com.github.abdurahmanovart.notesrealm.model.Note;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * @author Abdurakhmanov on 31.07.17
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final NoteClickListener mNoteClickListener;
    private RealmList<Note> mNoteList;

    public NoteAdapter(NoteClickListener noteClickListener,
                       RealmList<Note> noteList) {
        mNoteClickListener = noteClickListener;
        mNoteList = noteList;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        Note note = mNoteList.get(position);
        holder.idTextView.setText(note.getId());
        holder.titleTextView.setText(note.getTitle());
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title_text_view)
        TextView titleTextView;

        @BindView(R.id.id_text_view)
        TextView idTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mNoteClickListener.onClick(getLayoutPosition());
        }
    }

    public interface NoteClickListener {
        void onClick(int position);
    }

}
