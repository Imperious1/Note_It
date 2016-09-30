package imperiumnet.imperious.noteit.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import imperiumnet.imperious.noteit.R;
import imperiumnet.imperious.noteit.models.NoteModel;

import static imperiumnet.imperious.noteit.adapters.RecyclerAdapter.chooseOrderNormal;
import static imperiumnet.imperious.noteit.adapters.RecyclerAdapter.getTimeSinceCreation;

/**
 * Created by blaze on 8/9/2016.
 */
public abstract class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteHolder> {

    private ArrayList<NoteModel> mNotes = new ArrayList<>();
    private Context context;
    private String folderName;

    public NoteRecyclerAdapter(Context context, @Nullable String folderName) {
        this.context = context;
        this.folderName = folderName;
        addNotes();
    }

    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteRecyclerAdapter.NoteHolder(View.inflate(parent.getContext(), (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("mini_note", false)) ?
                R.layout.note_row : R.layout.mini_note_row, null));
    }

    @Override
    public void onBindViewHolder(NoteRecyclerAdapter.NoteHolder holder, int position) {
        holder.title.setText(mNotes.get((position)).getTitle());
        holder.note.setText(mNotes.get(position).getNote());
        holder.note.setMaxLines(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("note_preview_count", "3")));
        holder.note.setText(mNotes.get(position).getNote());
        if (mNotes.get((position)).isEdited())
            holder.noteDate.setText((getTimeSinceCreation(mNotes.get((position))) != null) ? "Last updated: " + getTimeSinceCreation(mNotes.get((position))) : "null");
        else
            holder.noteDate.setText((getTimeSinceCreation(mNotes.get((position))) != null) ? getTimeSinceCreation(mNotes.get((position))) : "null");
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void updateNotesOnBack() {
        mNotes.clear();
        addNotes();
        notifyDataSetChanged();
    }

    public int getNoteCount() {
        return mNotes.size();
    }

    private void addNotes() {
        for (NoteModel m : chooseOrderNormal(PreferenceManager.getDefaultSharedPreferences(context).getString("sort_order", "1"))) {
            if (folderName != null) {
                if (m.getFolder() != null && m.getFolder().equals(folderName) && !m.isTrash()) {
                    mNotes.add(m);
                }
            } else {
                if (m.isTrash()) {
                    mNotes.add(m);
                }
            }
        }
    }

    public abstract void onClick2(View v, int position);

    public abstract void onLongClick2(View v, int position);

    class NoteHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView note;
        TextView noteDate;
        CardView noteItself;

        public NoteHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text_title_note);
            note = (TextView) itemView.findViewById(R.id.text_content_note);
            noteItself = (CardView) itemView.findViewById(R.id.cardView);
            noteItself.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick2(v, mNotes.get((getAdapterPosition())).getId());
                }
            });
            noteItself.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongClick2(v, mNotes.get((getAdapterPosition())).getId());
                    return true;
                }
            });
            noteDate = (TextView) itemView.findViewById(R.id.note_date);
        }
    }

}
