package com.example.sjhapp_note;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Dialog_new extends DialogFragment {
    EditText dialogEdit;
    String beginText="";
//    InputMethodManager imm;


    public Dialog_new(String beginText) {
        this.beginText = beginText;
    }

    public Dialog_new() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_dialog, container);
        dialogEdit = view.findViewById(R.id.id_dialog_edit);
        dialogEdit.setText(beginText);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window dialogWindow = getDialog().getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.CENTER;
            dialogWindow.setAttributes(layoutParams);
            this.setCancelable(false);

//            imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        }


    }


}

