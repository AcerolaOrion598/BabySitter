package com.djaphar.babysitter.SupportClasses.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.djaphar.babysitter.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class MainDialog extends AppCompatDialogFragment {

    private EditText fieldChangeEd;
    private String title, editableContent;
    private View calledView;
    private MainDialogListener listener;

    public MainDialog(String title, String editableContent, View calledView) {
        this.title = title;
        this.editableContent = editableContent;
        this.calledView = calledView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = View.inflate(requireActivity(), R.layout.main_dialog, null);
        fieldChangeEd = view.findViewById(R.id.field_change_ed);
        fieldChangeEd.setHint(title);
        if (calledView.getId() == R.id.kid_age_container) {
            fieldChangeEd.setInputType(InputType.TYPE_CLASS_PHONE);
            if (editableContent.equals("null")) {
                editableContent = "";
            }
        }
        fieldChangeEd.setText(editableContent);
        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .setTitle(title)
                .setNegativeButton(R.string.cancel_button, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.ok_button, (dialogInterface, i) -> {
                    String value = fieldChangeEd.getText().toString();
                    if (value.equals("")) {
                        Toast.makeText(requireActivity(), R.string.empty_input, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listener.returnFieldValue(value, calledView);
                })
                .create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (MainDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface MainDialogListener {
        void returnFieldValue(String fieldValue, View calledView);
    }
}
