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
import android.widget.Button;
import android.widget.EditText;

import imperiumnet.imperious.noteit.R;
import imperiumnet.imperious.noteit.callbacks.FolderAddListener;

/**
 * Created by Shadow on 6/30/2016.
 */
public class EditTextFragmentFolder extends DialogFragment {

    private FolderAddListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (FolderAddListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit_text_fragment, container, false);
        final Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm_frag);
        final EditText name = (EditText) view.findViewById(R.id.edit_text_frag);
        final Button btnCancel = (Button) view.findViewById(R.id.btn_cancel_frag);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.add(name.getText().toString());
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancelAdd();
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