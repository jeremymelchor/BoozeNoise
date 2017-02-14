package com.example.melchor.boozenoise.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.example.melchor.boozenoise.Data;
import com.example.melchor.boozenoise.R;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class BarsFetchParamFragment extends BottomSheetDialogFragment implements DiscreteSeekBar.OnProgressChangeListener, CompoundButton.OnCheckedChangeListener {

    private final String TAG = BarsFetchParamFragment.class.getName();

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_param_bottom_sheet, null);
        dialog.setContentView(contentView);

        DiscreteSeekBar paramRadius = (DiscreteSeekBar) contentView.findViewById(R.id.param_radius_seekbar);
        paramRadius.setProgress(Data.getRadiusInMeters());
        paramRadius.setOnProgressChangeListener(this);

        Switch paramOpenNow = (Switch) contentView.findViewById(R.id.param_opennow_switch);
        paramOpenNow.setChecked(Data.isOpenNow());
        paramOpenNow.setOnCheckedChangeListener(this);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        /*View parent = (View) contentView.getParent();
        parent.setFitsSystemWindows(true);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        inflatedView.measure(0, 0);
        DisplayMetrics displaymetrics = new DisplayMetrics();        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        bottomSheetBehavior.setPeekHeight(screenHeight);

        if (params.getBehavior() instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior)params.getBehavior()).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        params.height = screenHeight;
        parent.setLayoutParams(params);*/
    }

    //==============================================================================================
    // Seekbars listeners
    //==============================================================================================

    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.param_radius_seekbar:
                Data.setRadiusInMeters(value);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }

    //==============================================================================================
    // Switch Listener
    //==============================================================================================

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Data.setOpenNow(isChecked);
    }
}
