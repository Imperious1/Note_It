package imperiumnet.imperious.noteit.fragments.frags;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import imperiumnet.imperious.noteit.R;
import imperiumnet.imperious.noteit.Utils.RealmUtils;
import imperiumnet.imperious.noteit.activities.MainActivity;
import imperiumnet.imperious.noteit.callbacks.PrefFragClickOptionClickListener;
import imperiumnet.imperious.noteit.callbacks.WarningListener;
import imperiumnet.imperious.noteit.models.FolderModel;
import imperiumnet.imperious.noteit.models.NoteModel;

import static imperiumnet.imperious.noteit.Utils.Utils.convertFM;
import static imperiumnet.imperious.noteit.Utils.Utils.convertNM;

public class PrefFragment extends PreferenceFragment {

    private PrefFragClickOptionClickListener listener;
    private WarningListener warningListener;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (PrefFragClickOptionClickListener) getActivity();
        warningListener = (WarningListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.preference_frag, container, false);
        final Preference export = getPreferenceManager().findPreference("note_export");
        final Preference prefImport = getPreferenceManager().findPreference("note_import");
        final EditTextPreference pinPass = (EditTextPreference) getPreferenceManager().findPreference("pin_code");
        final Preference trashAll = getPreferenceManager().findPreference("trash_all");
        final SwitchPreference miniNotes = (SwitchPreference) getPreferenceManager().findPreference("mini_note");
        final ListPreference noteLayout = (ListPreference) getPreferenceManager().findPreference("layout_type");
        final ListPreference sortOrder = (ListPreference) getPreferenceManager().findPreference("sort_order");
        sortOrder.setSummary(chooseOrderName(getPreferenceManager().getSharedPreferences().getString("sort_order", "1")));
        noteLayout.setSummary(chooseLayoutName(getPreferenceManager().getSharedPreferences().getString("layout_type", "1")));
        final ListPreference noteLines = (ListPreference) getPreferenceManager().findPreference("note_preview_count");
        if (!getPreferenceManager().getSharedPreferences().getString("note_preview_count", "3").equals("999")) {
            noteLines.setSummary(getPreferenceManager().getSharedPreferences().getString("note_preview_count", "3") + " lines");
        } else
            noteLines.setSummary("Limitless lines");
        noteLines.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().equals("999"))
                    noteLines.setSummary("Limitless" + " lines");
                else noteLines.setSummary(newValue + " lines");
                return true;
            }
        });
        noteLayout.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!newValue.toString().equals(getPreferenceManager().getSharedPreferences().getString("layout_type", "1"))) {
                    noteLayout.setSummary(chooseLayoutName(newValue.toString()));
                    NotesFragment.mRecycler.setLayoutManager(
                            (RecyclerView.LayoutManager) NotesFragment.selectLayout(preference.getContext(),
                                    Integer.parseInt(newValue.toString())));
                }
                return true;
            }
        });
        sortOrder.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                sortOrder.setSummary(chooseOrderName(newValue.toString()));
                NotesFragment.mAdapter.updateNotesOnBack();
                return true;
            }
        });
        export.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                listener.importExport(false);
                return true;
            }
        });
        prefImport.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                listener.importExport(true);
                return true;
            }
        });
        trashAll.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ((MainActivity) getActivity()).deleteAll();
                return true;
            }
        });
        miniNotes.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                NotesFragment.mAdapter.updateNotesOnBack();
                return false;
            }
        });
        return view;
    }

    private String chooseLayoutName(String number) {
        switch (number) {
            case "1":
                return "Single-column";
            case "2":
                return "Multi-column";
            case "3":
                return "Multi-column Staggered";
            default:
                return "1";
        }
    }

    private String chooseOrderName(String number) {
        switch (number) {
            case "1":
                return "Newest first (default)";
            case "2":
                return "Oldest first";
            case "3":
                return "Alphabetical";
            case "4":
                return "Reverse alphabetical";
            default:
                return "Newest first (default)";
        }
    }


    public static ArrayList<Object> importNotes(Context context) {
        ArrayList<Object> list = new ArrayList<>();
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir(), "noteit_exported.data")));
            Object object;
            while ((object = ois.readObject()) != null) {
                list.add(object);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            try {
                if (ois != null)
                    ois.close();
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
        RealmUtils.clearAllNotes();
        RealmUtils.clearAllFolders();
        return list;
    }

    public static void exportNotes(Context context, ArrayList<NoteModel> modelArrayList, ArrayList<FolderModel> modelArrayList2) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir(), "noteit_exported.data")));
            for (FolderModel m : modelArrayList2) {
                oos.writeObject(convertFM(m));
            }
            for (NoteModel m : modelArrayList) {
                oos.writeObject(convertNM(m));
            }
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}