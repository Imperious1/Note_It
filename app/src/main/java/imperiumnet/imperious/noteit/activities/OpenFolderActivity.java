package imperiumnet.imperious.noteit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import imperiumnet.imperious.noteit.R;
import imperiumnet.imperious.noteit.Utils.RealmUtils;
import imperiumnet.imperious.noteit.Utils.Utils;
import imperiumnet.imperious.noteit.adapters.NoteRecyclerAdapter;
import imperiumnet.imperious.noteit.callbacks.LongClickListener;
import imperiumnet.imperious.noteit.fragments.dialog_fragments.LongClickFragment;
import imperiumnet.imperious.noteit.fragments.frags.NotesFragment;

public class OpenFolderActivity extends AppCompatActivity implements LongClickListener {

    private int clickedPosition;
    private LongClickFragment longClickFrag;
    public static NoteRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_folder_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle(getIntent().getStringExtra("folderName"));
        RecyclerView mRecycler = (RecyclerView) findViewById(R.id.notes_in_folder_recycler);
        assert mRecycler != null;
        mRecycler.setAdapter(mAdapter = new NoteRecyclerAdapter(this, getIntent().getStringExtra("folderName")) {
            @Override
            public void onClick2(View v, int position) {
                startActivity(new Intent(OpenFolderActivity.this, AddNote.class).putExtra("id", position).putExtra("folderName", getIntent().getStringExtra("folderName")));
            }

            @Override
            public void onLongClick2(View v, int position) {
                clickedPosition = position;
                if (longClickFrag == null) longClickFrag = new LongClickFragment();
                longClickFrag.setArguments(Utils.getBundle("array", (RealmUtils.getFolders().size() > 1) ? new String[]{"Move to trash", "Move to folder", "Remove from folder"} : new String[]{"Move to trash", "Remove from folder"}));
                longClickFrag.show(getSupportFragmentManager(), "long_click_frag");
            }
        });
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OpenFolderActivity.this, AddNote.class).putExtra("folderName", getIntent().getExtras().getString("folderName")));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void delete(@Nullable String folderName) {
        if (folderName == null) {
            RealmUtils.trashNote(clickedPosition);
        } else {
            RealmUtils.deleteFolder(folderName);
        }
        longClickFrag.dismiss();
        NotesFragment.mAdapter.updateNotesOnBack();
        if (NotesFragment.noteAdapter != null)
            NotesFragment.noteAdapter.updateNotesOnBack();
    }

    @Override
    public void moveToFolder(String folderName) {
        longClickFrag.dismiss();
        RealmUtils.addToFolder(RealmUtils.getNote(clickedPosition), folderName);
        OpenFolderActivity.mAdapter.updateNotesOnBack();
    }

    @Override
    public void removeFromFolder() {
        RealmUtils.removeFromFolder(clickedPosition);
        mAdapter.updateNotesOnBack();
        longClickFrag.dismiss();
    }

    @Override
    public void restore() {

    }
}