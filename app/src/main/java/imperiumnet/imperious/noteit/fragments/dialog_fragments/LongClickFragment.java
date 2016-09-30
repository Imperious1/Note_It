package imperiumnet.imperious.noteit.fragments.dialog_fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import imperiumnet.imperious.noteit.R;
import imperiumnet.imperious.noteit.Utils.RealmUtils;
import imperiumnet.imperious.noteit.adapters.CustomArrayAdapter;
import imperiumnet.imperious.noteit.callbacks.LongClickListener;

/**
 * Created by Shadow on 6/28/2016.
 */
public class LongClickFragment extends DialogFragment {

    private LongClickListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (LongClickListener) getContext();
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.long_click_frag, container);
        final ListView listView = (ListView) view.findViewById(R.id.list_view_select_folder);
        final ArrayList<String> data = RealmUtils.getFoldersString();
        listView.setAdapter(new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, (getArguments().getStringArray("array"))));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    listener.delete((getArguments().getString("folder") != null) ? getArguments().getString("folder") : null);
                else if (position == 1)
                    if (getArguments().getStringArray("array")[1].contains("Move to")) {
                        listView.setAdapter(new CustomArrayAdapter(inflater.getContext(), data) {
                            @Override
                            public void onClick2(View v, int position) {
                                listener.moveToFolder(data.get(position));
                            }
                        });
                    } else if (getArguments().getStringArray("array")[1].contains("Remove"))
                        listener.removeFromFolder();
                    else listener.restore();
                    else if (position == 2)
                        listener.removeFromFolder();
            }
        });
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }
}