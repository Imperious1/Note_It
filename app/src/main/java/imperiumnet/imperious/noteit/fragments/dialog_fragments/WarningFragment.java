package imperiumnet.imperious.noteit.fragments.dialog_fragments;

/**
 * Created by blaze on 8/9/2016.
 */

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
import android.widget.TextView;

import java.util.ArrayList;

import imperiumnet.imperious.noteit.R;
import imperiumnet.imperious.noteit.callbacks.WarningListener;

/**
 * Created by Shadow on 6/26/2016.
 */
public class WarningFragment extends DialogFragment {

    private WarningListener listener;

    public WarningFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (WarningListener) activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.warning_exit_frag, container);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_title_frag);
        TextView txtContent = (TextView) view.findViewById(R.id.edit_text_frag);
        Button btnConfirm = (Button) view.findViewById(R.id.btn_confirm_frag);
        ArrayList<String> mName;
        if (getArguments() != null) {
            mName = getArguments().getStringArrayList("list");
            if (mName != null) {
                if (mName.get(0).equals("Add a note") && mName.get(1).isEmpty() && mName.get(2).isEmpty()) {
                    view.findViewById(R.id.btn_save_exit).setVisibility(View.INVISIBLE);
                }
            } else if (getArguments().getBoolean("isMain")) {
                view.findViewById(R.id.btn_save_exit).setVisibility(View.INVISIBLE);
                btnConfirm.setText("DELETE");
                txtTitle.setText("Delete all notes?");
                txtContent.setText("Are you sure you want to delete all notes?");
            }
        }
        view.findViewById(R.id.btn_cancel_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnBtnCancelClick();
            }
        });
        view.findViewById(R.id.btn_confirm_frag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnBtnConfirmClick();
            }
        });
        view.findViewById(R.id.btn_save_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnBtnSaveExitClick();
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