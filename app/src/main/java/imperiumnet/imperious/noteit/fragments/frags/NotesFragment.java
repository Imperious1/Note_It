package imperiumnet.imperious.noteit.fragments.frags;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import imperiumnet.imperious.noteit.R;
import imperiumnet.imperious.noteit.Utils.RealmUtils;
import imperiumnet.imperious.noteit.Utils.Utils;
import imperiumnet.imperious.noteit.activities.AddNote;
import imperiumnet.imperious.noteit.activities.OpenFolderActivity;
import imperiumnet.imperious.noteit.adapters.NoteRecyclerAdapter;
import imperiumnet.imperious.noteit.adapters.RecyclerAdapter;
import imperiumnet.imperious.noteit.fragments.dialog_fragments.LongClickFragment;

/**
 * Created by Shadow on 7/1/2016.
 */
public class NotesFragment extends Fragment {

    public static RecyclerAdapter mAdapter;
    public static NoteRecyclerAdapter noteAdapter;
    public static int clickedPosition;
    public static LongClickFragment longClickFrag;
    public static RecyclerView mRecycler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.recycler_main);
        mRecycler.setLayoutManager((RecyclerView.LayoutManager) selectLayout(view.getContext(), Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(view.getContext()).getString("layout_type", "1"))));
        if (getArguments() == null) {
            mAdapter = new RecyclerAdapter(view.getContext()) {
                @Override
                public void onClick2(View v, int position) {
                    startActivity(new Intent(v.getContext(), AddNote.class).putExtra("id", position));
                }

                @Override
                public void onLongClick2(View v, int position) {
                    clickedPosition = position;
                    if (longClickFrag == null) longClickFrag = new LongClickFragment();
                    longClickFrag.setArguments(Utils.getBundle("array", (RealmUtils.getFolders().size() != 0) ? new String[]{"Move to trash", "Move to folder"} : new String[]{"Move to trash"}));
                    longClickFrag.show(getFragmentManager(), "long_click_frag");
                }

                @Override
                public void onClick2(View v, String folderName) {
                    startActivity(new Intent(v.getContext(), OpenFolderActivity.class).putExtra("folderName", folderName));
                }

                @Override
                public void onLongClick2(View v, String folderName) {
                    if (longClickFrag == null) longClickFrag = new LongClickFragment();
                    Bundle b = new Bundle();
                    b.putString("folder", folderName);
                    b.putStringArray("array", new String[]{"Delete"});
                    longClickFrag.setArguments(b);
                    longClickFrag.show(getActivity().getSupportFragmentManager(), "long_click_frag");
                }
            };
        } else {
            noteAdapter = new NoteRecyclerAdapter(view.getContext(), null) {
                @Override
                public void onClick2(View v, int position) {
                    startActivity(new Intent(v.getContext(), AddNote.class).putExtra("id", position));
                }

                @Override
                public void onLongClick2(View v, int position) {
                    clickedPosition = position;
                    if (longClickFrag == null) longClickFrag = new LongClickFragment();
                    longClickFrag.setArguments(Utils.getBundle("array", new String[]{"Delete", "Restore note"}));
                    longClickFrag.show(getFragmentManager(), "long_click_frag");
                }
            };
        }
        mRecycler.setAdapter((getArguments() == null) ? mAdapter : noteAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getArguments() == null) {
            inflater.inflate(R.menu.menu_search, menu);
            SearchView mSearch = (SearchView) menu.findItem(R.id.action_search).getActionView();
            mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!newText.isEmpty())
                        mAdapter.search(newText);
                    else mAdapter.updateNotesOnBack();
                    return false;
                }
            });

            inflater.inflate(R.menu.main, menu);
        } else {
            if(noteAdapter.getNoteCount() != 0)
            inflater.inflate(R.menu.menu_trash, menu);
        }
    }

    public static Object selectLayout(Context context, int id) {
        switch (id) {
            case 1:
                return new LinearLayoutManager(context);
            case 2:
                return new GridLayoutManager(context, 2);
            case 3:
                return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            default:
                return new LinearLayoutManager(context);
        }
    }
}