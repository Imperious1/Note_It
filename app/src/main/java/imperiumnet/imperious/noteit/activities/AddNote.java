package imperiumnet.imperious.noteit.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

import imperiumnet.imperious.noteit.R;
import imperiumnet.imperious.noteit.Singleton.SingleyBoo;
import imperiumnet.imperious.noteit.Utils.RealmUtils;
import imperiumnet.imperious.noteit.Utils.Utils;
import imperiumnet.imperious.noteit.callbacks.WarningListener;
import imperiumnet.imperious.noteit.fragments.frags.NotesFragment;
import imperiumnet.imperious.noteit.fragments.dialog_fragments.WarningFragment;
import imperiumnet.imperious.noteit.models.NoteModel;

public class AddNote extends AppCompatActivity implements WarningListener {

    private EditText title;
    private EditText content;
    private boolean trigger;
    private WarningFragment frag;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_two);
        setSupportActionBar(toolbar);
        title = (EditText) findViewById(R.id.note_title);
        content = (EditText) findViewById(R.id.note_content);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getExtras() != null && getIntent().getExtras().getInt("id") != 0) {
            getSupportActionBar().setSubtitle("Edit a note");
            title.setText(RealmUtils.getNote(getIntent().getExtras().getInt("id")).getTitle());
            content.setText(RealmUtils.getNote(getIntent().getExtras().getInt("id")).getNote());
        } else
            getSupportActionBar().setSubtitle("Add a note");
        primaryBtnListeners();
        textWatchers();
    }

    @Override
    public void finish() {
        if (getIntent().getStringExtra("folderName") == null)
            NotesFragment.mAdapter.updateNotesOnBack();
        else OpenFolderActivity.mAdapter.updateNotesOnBack();
        super.finish();
    }

    private void primaryBtnListeners() {
        final Button btnSave = (Button) findViewById(R.id.btn_save);
        assert btnSave != null;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getExtras() == null && !content.getText().toString().isEmpty() && !title.getText().toString().isEmpty()) {
                    handleAddClick();
                } else if (getIntent().getExtras() != null && getIntent().getExtras().getInt("id") == 0 &&
                        getIntent().getExtras().getString("folderName") != null && !content.getText().toString().isEmpty() && !title.getText().toString().isEmpty()) {
                    handleAddClick();
                }
                if (getIntent().getExtras() != null && getIntent().getExtras().getInt("id") != 0 && !content.getText().toString().isEmpty() && !title.getText().toString().isEmpty()) {
                    handleUpdateClick();
                }
                if (content.getText().toString().isEmpty() && title.getText().toString().isEmpty())
                    Snackbar.make(mCoordinatorLayout, "Your note or title is empty", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void handleAddClick() {
        add();
        finish();
    }

    private void handleUpdateClick() {
        update(getIntent().getExtras().getInt("id"));
        finish();
    }

    private void textWatchers() {
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                trigger = !s.toString().isEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                trigger = !s.toString().isEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void add() {
        RealmUtils.addNote(new NoteModel().setId(getNoteId())
                .setNote(content.getText().toString())
                .setTitle(title.getText().toString())
                .setEdited(false)
                .setCreationDate(Calendar.getInstance().getTime())
                .setSynced(false)
                .setFolder((getIntent().getExtras() != null) ? getIntent().getExtras().getString("folderName") : null));
    }

    private void update(int position) {
        RealmUtils.updateNote(new NoteModel().setId(position)
                .setSynced(false)
                .setCreationDate(Calendar.getInstance().getTime())
                .setEdited(true)
                .setNote(content.getText().toString())
                .setTitle(title.getText().toString())
                .setFolder((getIntent().getExtras() != null) ? getIntent().getExtras().getString("folderName") : null));

    }

    private int getNoteId() {
        return SingleyBoo.getRealm().where(NoteModel.class).findAll().size() + 1;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                handleHome();
                break;
            case R.id.menu_delete_note:
                handleDeletion();
                break;
            case R.id.menu_reset_note:
                handleReset();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleHome() {
        if (!trigger) {
            this.finish();
        } else {
            if (frag == null) frag = new WarningFragment();
            ArrayList<String> test = new ArrayList<>();
            assert getSupportActionBar() != null;
            test.add(getSupportActionBar().getSubtitle().toString());
            test.add(title.getText().toString());
            test.add(content.getText().toString());
            frag.setArguments(Utils.getBundle("list", test));
            frag.show(getSupportFragmentManager(), "warn_frag");
        }
    }

    private void handleDeletion() {
        if (getIntent().getExtras() != null) {
            RealmUtils.deleteNote(getIntent().getExtras().getInt("id"));
            Snackbar.make(mCoordinatorLayout, "Note deleted", Snackbar.LENGTH_SHORT).show();
            this.finish();
        }
    }

    private void handleReset() {
        if (!title.getText().toString().isEmpty() || !content.getText().toString().isEmpty()) {
            title.setText("");
            content.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        if (!trigger) {
            super.onBackPressed();
        } else {
            if (frag == null) frag = new WarningFragment();
            frag.show(getSupportFragmentManager(), "warn_frag");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().getInt("id") != 0) {
                getMenuInflater().inflate(R.menu.menu_exit, menu);
            } else {
                getMenuInflater().inflate(R.menu.menu_add, menu);
            }
        return true;
    }

    @Override
    public void OnBtnCancelClick() {
        frag.dismiss();
    }

    @Override
    public void OnBtnConfirmClick() {
        finish();
    }

    @Override
    public void OnBtnSaveExitClick() {
        if (getIntent().getExtras() != null) {
            update(getIntent().getExtras().getInt("id"));
            finish();
        } else {
            add();
            finish();
        }
    }
}