package com.tech.arinzedroid.starchoiceadmin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.utils.Constants;


public class ConfirmDialogFragment extends DialogFragment {

    private OnButtonClickInterface OnButtonClickInterface;
    private static String POSITION = "POSITION";
    private int position;

    public ConfirmDialogFragment() {
        // Required empty public constructor
    }

    public static  ConfirmDialogFragment FragmentInstance(int position){
        ConfirmDialogFragment fragment = new ConfirmDialogFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION,position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            position = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_confirm_dialog, container, false);

        v.findViewById(R.id.delete_btn).setOnClickListener(view -> {
            OnButtonClickInterface.onDeleteClicked(position);
            dismiss();
        });
        v.findViewById(R.id.view_btn).setOnClickListener(view -> {
            OnButtonClickInterface.onViewClicked(position);
            dismiss();
        });
        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnButtonClickInterface) {
            OnButtonClickInterface = (OnButtonClickInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnButtonClickInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        OnButtonClickInterface = null;
    }

    public interface OnButtonClickInterface {
        void onDeleteClicked(int position);
        void onViewClicked(int position);
    }
}
