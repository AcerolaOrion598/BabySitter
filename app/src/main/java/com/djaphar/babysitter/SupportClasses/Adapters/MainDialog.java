package com.djaphar.babysitter.SupportClasses.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.djaphar.babysitter.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class MainDialog extends AppCompatDialogFragment {

    private EditText fieldChangeEd;
    private String title, editableContent;
    private TextView targetTv;

    public MainDialog(String title, String editableContent, TextView targetTv) {
        this.title = title;
        this.editableContent = editableContent;
        this.targetTv = targetTv;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = View.inflate(requireActivity(), R.layout.main_dialog, null);
        fieldChangeEd = view.findViewById(R.id.field_change_ed);
        fieldChangeEd.setHint(title);
        fieldChangeEd.setText(editableContent);
        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setTitle(title)
                .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> targetTv.setText(fieldChangeEd.getText().toString()))
                .create();
    }
}
