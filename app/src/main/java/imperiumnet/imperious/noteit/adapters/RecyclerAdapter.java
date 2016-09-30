package imperiumnet.imperious.noteit.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import imperiumnet.imperious.noteit.R;
import imperiumnet.imperious.noteit.Singleton.SingleyBoo;
import imperiumnet.imperious.noteit.Utils.RealmUtils;
import imperiumnet.imperious.noteit.Utils.Utils;
import imperiumnet.imperious.noteit.models.FolderModel;
import imperiumnet.imperious.noteit.models.NoteModel;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by blaze on 8/9/2016.
 */
public abstract class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int T_FOLDER = 1;
    private static final int T_NOTE = 2;
    private static final int T_SEPERATOR_FOLD = 3;
    private static final int T_SEPERATOR_NOTE = 4;
    private ArrayList<FolderModel> mFolders = new ArrayList<>();
    private ArrayList<NoteModel> mNotes = new ArrayList<>();
    private Context context;

    public RecyclerAdapter(Context context) {
        this.context = context;
        addAll();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == T_FOLDER)
            return new FolderHolder(View.inflate(parent.getContext(), R.layout.folder_row, null));
        else if (viewType == T_NOTE) return new NoteHolder(View.inflate(parent.getContext(),
                (!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("mini_note", false)) ?
                        R.layout.note_row : R.layout.mini_note_row, null));
        else if (viewType == T_SEPERATOR_FOLD)
            return new SeperatorOne(View.inflate(context, R.layout.seperator_row_one, null));
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        if (position < mFolders.size()) return T_FOLDER;
        else return T_NOTE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NoteHolder) {
            ((NoteHolder) holder).title.setText(mNotes.get((position - mFolders.size())).getTitle());
            ((NoteHolder) holder).note.setText(mNotes.get((position - mFolders.size())).getNote());
            ((NoteHolder) holder).note.setMaxLines(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("note_preview_count", "3")));
            ((NoteHolder) holder).note.setText(mNotes.get((position - mFolders.size())).getNote());
            if (mNotes.get((position - mFolders.size())).isEdited())
                ((NoteHolder) holder).noteDate.setText((getTimeSinceCreation(mNotes.get((position - mFolders.size()))) != null) ? "Last updated: " + getTimeSinceCreation(mNotes.get((position - mFolders.size()))) : "null");
            else
                ((NoteHolder) holder).noteDate.setText((getTimeSinceCreation(mNotes.get((position - mFolders.size()))) != null) ? getTimeSinceCreation(mNotes.get((position - mFolders.size()))) : "null");
        } else if (holder instanceof FolderHolder) {
            ((FolderHolder) holder).title.setText(mFolders.get(position).getName());
            ((FolderHolder) holder).date.setText(Utils.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return mFolders.size() + mNotes.size();
    }

    public void updateNotesOnBack() {
        mNotes.clear();
        mFolders.clear();
        addAll();
        notifyDataSetChanged();
    }

    public ArrayList<NoteModel> getNotes() {
        return mNotes;
    }

    private void addAll() {
        for (FolderModel m : RealmUtils.getFolders()) {
            if (m.getFolder() == null)
                mFolders.add(m);
        }
        for (NoteModel m : chooseOrderNormal(PreferenceManager.getDefaultSharedPreferences(context).getString("sort_order", "1"))) {
            if (m.getFolder() == null && !m.isTrash()) {
                mNotes.add(m);
            }
        }
    }

    private void clearAdapterContent() {
        mNotes.clear();
        mFolders.clear();
        notifyDataSetChanged();
    }

    public void search(String query) {
        clearAdapterContent();
        for (NoteModel m : RealmUtils.searchNote(query)) {
            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("search_trash", false))
                mNotes.add(m);
            else if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean("search_trash", false) && m.isTrash());
        }
        notifyDataSetChanged();
    }

    public static String getTimeSinceCreation(NoteModel model) {
        long timePassed = TimeUnit.MILLISECONDS.toMillis(new Date().getTime() - model.getCreationDate().getTime());
        if (timePassed < 1000) {
            return "NOW";
        } else if (timePassed < 60000) {
            return TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - model.getCreationDate().getTime()) + " second(s) ago";
        } else if (timePassed >= 60000 && timePassed < 3600000) {
            return TimeUnit.MILLISECONDS.toMinutes(new Date().getTime() - model.getCreationDate().getTime()) + " minute(s) ago";
        } else if (timePassed > 3600000 && timePassed < 86400000) {
            return TimeUnit.MILLISECONDS.toHours(new Date().getTime() - model.getCreationDate().getTime()) + " hour(s) ago";
        } else if (timePassed > 86400000 && timePassed < 2592000000L) {
            return TimeUnit.MILLISECONDS.toDays(new Date().getTime() - model.getCreationDate().getTime()) + " day(s) ago";
        } else if (timePassed > 2592000000L) {
            return Utils.getDate();
        } else {
            return null;
        }
    }

    public static RealmResults<NoteModel> chooseOrderNormal(String number) {
        switch (number) {
            case "1":
                return SingleyBoo.getRealm().where(NoteModel.class).findAllSorted("creationDate", Sort.DESCENDING);
            case "2":
                return SingleyBoo.getRealm().where(NoteModel.class).findAllSorted("creationDate", Sort.ASCENDING);
            case "3":
                return SingleyBoo.getRealm().where(NoteModel.class).findAllSorted("title", Sort.ASCENDING);
            case "4":
                return SingleyBoo.getRealm().where(NoteModel.class).findAllSorted("title", Sort.DESCENDING);
            default:
                return SingleyBoo.getRealm().where(NoteModel.class).findAllSorted("creationDate", Sort.DESCENDING);
        }
    }

    public abstract void onClick2(View v, int position);

    public abstract void onLongClick2(View v, int position);

    public abstract void onClick2(View v, String folderName);

    public abstract void onLongClick2(View v, String folderName);

    class FolderHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date;
        RelativeLayout folder;

        public FolderHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.folder_title);
            date = (TextView) itemView.findViewById(R.id.folder_date);
            folder = (RelativeLayout) itemView.findViewById(R.id.folder);
            folder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick2(v, mFolders.get(getAdapterPosition()).getName());
                }
            });
            folder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongClick2(v, mFolders.get(getAdapterPosition()).getName());
                    return true;
                }
            });
        }
    }

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
                    onClick2(v, mNotes.get((getAdapterPosition() - mFolders.size())).getId());
                }
            });
            noteItself.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongClick2(v, mNotes.get((getAdapterPosition() - mFolders.size())).getId());
                    return true;
                }
            });
            noteDate = (TextView) itemView.findViewById(R.id.note_date);
        }
    }

    class SeperatorOne extends RecyclerView.ViewHolder {

        public SeperatorOne(View itemView) {
            super(itemView);
        }
    }
}