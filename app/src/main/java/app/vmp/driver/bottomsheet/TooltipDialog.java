package app.vmp.driver.bottomsheet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import app.vmp.driver.R;

public class TooltipDialog extends BottomSheetDialogFragment {

    private Context mContext;

    public TooltipDialog(){

    }

    public TooltipDialog(Context context,String id){
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.btmsht_tooltip, null);
        dialog.setContentView(contentView);
    }
}

