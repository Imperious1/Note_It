package imperiumnet.imperious.noteit.fragments.dialog_fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import imperiumnet.imperious.noteit.R;
import imperiumnet.imperious.noteit.callbacks.ImportExportListener;

/**
 * Created by Shadow on 6/28/2016.
 */
public class ImportExportFragment extends DialogFragment {

    private ImportExportListener listener;

    public ImportExportFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (ImportExportListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.import_export_fragment, container, false);
        final TextView title = (TextView) view.findViewById(R.id.txt_title_frag);
        final TextView summary = (TextView) view.findViewById(R.id.edit_text_frag);
        final Button btnExportImport = (Button) view.findViewById(R.id.btn_confirm_frag);
        final Button btnCancel = (Button) view.findViewById(R.id.btn_cancel_frag);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancel();
            }
        });
        if (getArguments() == null) {
            title.setText("Export notes?");
            summary.setText("Previous exports will be replaced, are you sure?");
            btnExportImport.setText("EXPORT");
            btnExportImport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.chooseOutcome(false);
                }
            });
        } else {
            title.setText("Import notes?");
            summary.setText("Existing notes will be replaced, are you sure?");
            btnExportImport.setText("IMPORT");
            btnExportImport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.chooseOutcome(true);
                }
            });
        }
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