package com.tech.arinzedroid.starchoiceadmin.fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tech.arinzedroid.starchoiceadmin.R;
import com.tech.arinzedroid.starchoiceadmin.model.TransactionsModel;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTransactionDialogFragment extends DialogFragment {

    private static String TRANS_DATA = "TRANS_DATA";
    private TransactionsModel transactionsModel;
    private OnButtonClickedInterface onButtonClickedInterface;

    @BindView(R.id.amt)
    EditText amtET;
    @BindView(R.id.save_btn)
    Button saveBtn;
    @BindView(R.id.delete_btn)
    Button deleteBtn;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    public EditTransactionDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    public static EditTransactionDialogFragment instance(TransactionsModel transactionsModel){
        EditTransactionDialogFragment dialogFragment = new EditTransactionDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(TRANS_DATA, Parcels.wrap(transactionsModel));
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            transactionsModel = Parcels.unwrap(getArguments().getParcelable(TRANS_DATA));
        }
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof OnButtonClickedInterface){
            onButtonClickedInterface = (OnButtonClickedInterface) context;
        }else{
            throw new RuntimeException(context.toString() + "Must implement interface on host activity");
        }

    }

    @Override
    public void onDetach(){
        onButtonClickedInterface = null;
        super.onDetach();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_transaction, container, false);
        ButterKnife.bind(this,v);
        if(transactionsModel != null){
            amtET.setText(String.valueOf(transactionsModel.getAmount()));
            saveBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
        }else{
            Toast.makeText(getActivity(), "Trans data is not valid", Toast.LENGTH_SHORT).show();
        }

        saveBtn.setOnClickListener(view -> {
            if(!TextUtils.isEmpty(amtET.getText())){
                String amt = amtET.getText().toString();
                double _amt = Double.parseDouble(amt);
                transactionsModel.setAmount(_amt);
                onButtonClickedInterface.onSaveClicked(transactionsModel);
                dismiss();
            }else{
                amtET.setError("Field must not be empty");
            }

        });

        deleteBtn.setOnClickListener(view -> {
            onButtonClickedInterface.onDeleteClicked(transactionsModel);
            dismiss();
        });

        return v;
    }


    public interface OnButtonClickedInterface {
        void onDeleteClicked(TransactionsModel transactionsModel);
        void onSaveClicked(TransactionsModel transactionsModel);
    }

}
