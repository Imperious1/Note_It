package imperiumnet.imperious.noteit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;

import imperiumnet.imperious.noteit.R;
import imperiumnet.imperious.noteit.Singleton.SingleyBoo;
import imperiumnet.imperious.noteit.Utils.RealmUtils;
import imperiumnet.imperious.noteit.Utils.Utils;
import imperiumnet.imperious.noteit.callbacks.FolderAddListener;
import imperiumnet.imperious.noteit.callbacks.ImportExportListener;
import imperiumnet.imperious.noteit.callbacks.LongClickListener;
import imperiumnet.imperious.noteit.callbacks.PrefFragClickOptionClickListener;
import imperiumnet.imperious.noteit.callbacks.WarningListener;
import imperiumnet.imperious.noteit.fragments.dialog_fragments.EditTextFragmentFolder;
import imperiumnet.imperious.noteit.fragments.dialog_fragments.ImportExportFragment;
import imperiumnet.imperious.noteit.fragments.dialog_fragments.WarningFragment;
import imperiumnet.imperious.noteit.fragments.frags.NotesFragment;
import imperiumnet.imperious.noteit.fragments.frags.PrefFragment;
import imperiumnet.imperious.noteit.models.FolderModel;
import imperiumnet.imperious.noteit.models.FolderModelSerializable;
import imperiumnet.imperious.noteit.models.NoteModelSerializable;

import static imperiumnet.imperious.noteit.fragments.frags.PrefFragment.exportNotes;
import static imperiumnet.imperious.noteit.fragments.frags.PrefFragment.importNotes;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WarningListener, LongClickListener, PrefFragClickOptionClickListener, ImportExportListener, FolderAddListener {

    private WarningFragment warningFragment;
    private ImportExportFragment importExportFragment;
    private EditTextFragmentFolder frag;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SingleyBoo.getInstance(this);
        if (getSupportFragmentManager().getFragments() == null && getFragmentManager().findFragmentByTag("setty_mc_set_face") == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.note_coordinator, new NotesFragment(), "fraggy").commit();
            setTitle("Notes");
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddNote.class));
            }
        });
        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_folder) {
            if (frag == null) frag = new EditTextFragmentFolder();
            if (!frag.isVisible())
                frag.show(getSupportFragmentManager(), "folder_frag");
        } else if (item.getItemId() == R.id.menu_trash_all) {
            RealmUtils.clearTrash();
            NotesFragment.noteAdapter.updateNotesOnBack();
        }
        return super.onOptionsItemSelected(item);
    }

    public void pushFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        clearFragment();
        if (fm.getFragments() != null) {
            for (Fragment f : fm.getFragments())
                if (f != null) ft.hide(f);
            if (fm.findFragmentByTag(tag) != null)
                ft.show(fm.findFragmentByTag(tag));
            else ft.add(R.id.note_coordinator, fragment, tag);
        } else ft.add(R.id.note_coordinator, fragment, tag);
        ft.commit();
    }

    private void clearFragment() {
        android.app.FragmentManager fm = getFragmentManager();
        while (fm.popBackStackImmediate()) ;
    }

    public void pushFragment(android.app.Fragment fragment, String tag) {
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        clearSupport();
        if (fm.findFragmentByTag(tag) != null) {
            ft.show(fm.findFragmentByTag(tag));
        } else {
            ft.addToBackStack(tag).add(R.id.note_coordinator, fragment, tag).commit();
        }
    }

    private void clearSupport() {
        if (getSupportFragmentManager().getFragments() != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : getSupportFragmentManager().getFragments())
                if (f != null) ft.hide(f);
            ft.commit();
        }
    }

    public void deleteAll() {
        if (warningFragment == null) warningFragment = new WarningFragment();
        warningFragment.setArguments(Utils.getBundle("isMain", true));
        warningFragment.show(getSupportFragmentManager(), "warning_frag_main");
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.notes_drawer) {
            pushFragment(new NotesFragment(), "note_frag");
            if (!fab.isShown()) fab.show();
            setTitle("Notes");
            fab.setImageResource(R.drawable.ic_create);
        } else if (item.getItemId() == R.id.settings_drawer) {
            fab.hide();
            pushFragment(new PrefFragment(), "pref_frag");
            setTitle("Settings");
        } else if (item.getItemId() == R.id.trash_drawer) {
            fab.setImageResource(R.drawable.ic_delete);
            NotesFragment frag = new NotesFragment();
            Bundle b = new Bundle();
            b.putString("trash", "ya");
            frag.setArguments(b);
            pushFragment(frag, "trash_frag");
            setTitle("Trash");
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void OnBtnCancelClick() {
        warningFragment.dismiss();
    }

    @Override
    public void OnBtnConfirmClick() {
        RealmUtils.clearAllNotes();
        RealmUtils.clearAllFolders();
        NotesFragment.mAdapter.updateNotesOnBack();
        warningFragment.dismiss();
    }

    @Override
    public void OnBtnSaveExitClick() {

    }

    @Override
    public void delete(@Nullable String folderName) {
        if (folderName == null) {
            RealmUtils.trashNote(NotesFragment.clickedPosition);
        } else {
            RealmUtils.deleteFolder(folderName);
        }
        NotesFragment.longClickFrag.dismiss();
        NotesFragment.mAdapter.updateNotesOnBack();
        if (NotesFragment.noteAdapter != null)
            NotesFragment.noteAdapter.updateNotesOnBack();
    }

    @Override
    public void moveToFolder(String folderName) {
        NotesFragment.longClickFrag.dismiss();
        RealmUtils.addToFolder(RealmUtils.getNote(NotesFragment.clickedPosition), folderName);
        NotesFragment.mAdapter.updateNotesOnBack();
    }

    @Override
    public void removeFromFolder() {

    }

    @Override
    public void restore() {
        RealmUtils.restoreNote(NotesFragment.clickedPosition);
        NotesFragment.noteAdapter.updateNotesOnBack();
        NotesFragment.mAdapter.updateNotesOnBack();
        NotesFragment.longClickFrag.dismiss();
    }

    @Override
    public void importExport(boolean isImport) {
        if (importExportFragment == null) importExportFragment = new ImportExportFragment();
        if (importExportFragment.getArguments() == null && isImport)
            importExportFragment.setArguments(Utils.getBundle("isImport", true));
        if (!importExportFragment.isVisible())
            importExportFragment.show(getFragmentManager(), "imp_exp_frag");
        else importExportFragment.dismiss();
    }

    @Override
    public void chooseOutcome(boolean value) {
        if (value) {
            for (Object o : importNotes(this)) {
                if (o instanceof FolderModelSerializable)
                    RealmUtils.addFolder(Utils.convertFMS((FolderModelSerializable) o));
                else if (o instanceof NoteModelSerializable)
                    RealmUtils.addNote(Utils.convertNMS((NoteModelSerializable) o));
            }
            importExportFragment.dismiss();
            NotesFragment.mAdapter.updateNotesOnBack();
            Snackbar.make(findViewById(R.id.note_coordinator), "Notes imported successfully!", Snackbar.LENGTH_LONG).show();
        } else {
            exportNotes(this, RealmUtils.getNotes(), RealmUtils.getFolders());
            importExportFragment.dismiss();
            Snackbar.make(findViewById(R.id.note_coordinator), "Notes exported successfully!", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void add(String name) {
        if (!RealmUtils.checkIfFolderExists(name))
            RealmUtils.addFolder(new FolderModel().setName(name).setCreationDate(Calendar.getInstance().getTime()));
        else
            Snackbar.make(findViewById(R.id.note_coordinator), "Folder already exists", Snackbar.LENGTH_LONG).show();
        NotesFragment.mAdapter.updateNotesOnBack();
        frag.dismiss();
    }

    @Override
    public void cancelAdd() {
        frag.dismiss();
    }

    @Override
    public void cancel() {
        importExportFragment.dismiss();
    }
}