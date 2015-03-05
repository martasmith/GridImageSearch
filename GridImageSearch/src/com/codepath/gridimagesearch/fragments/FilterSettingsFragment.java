package com.codepath.gridimagesearch.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.gridimagesearch.R;

public class FilterSettingsFragment extends DialogFragment {

    private Spinner sprImageSize, sprColorFilter, sprImageType;
    private TextView tvSiteFilter;
    private ArrayAdapter<CharSequence> aSprImageSize, aSprColorFilter, aSprImageType;
    private String imgSizeVal, colorFilterVal, imgTypeVal, siteFilterVal;
    int spinnerPos;
    private Button saveBtn, resetBtn;

    private OnFilterSettingsFragmentListener fragmentListener;

    public FilterSettingsFragment() {
        // required empty constructor
    }

    public interface OnFilterSettingsFragmentListener {
        public void OnFilterSet(String imgSize, String colorFilter, String imgType, String siteFilter);
    }

    public static FilterSettingsFragment newInstance(String imgSizeVal, String colorFilterVal, String imgTypeVal, String siteFilterVal) {
        FilterSettingsFragment frag = new FilterSettingsFragment();
        Bundle args = new Bundle();
        args.putString("imgSizeVal", imgSizeVal);
        args.putString("colorFilterVal", colorFilterVal);
        args.putString("imgTypeVal", imgTypeVal);
        args.putString("siteFilterVal", siteFilterVal);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ImgSearchDialogStyle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_settings, container);
        getDialog().setTitle("Advanced search options");
        getFragmentArgs();
        setupViews(view);
        populateSpinners();
        setupSaveBtnListener();
        setupResetBtnListener();

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    private void getFragmentArgs() {
        // get data from arguments passed to the fragment
        imgSizeVal = getArguments().getString("imgSizeVal","");
        colorFilterVal = getArguments().getString("colorFilterVal","");
        imgTypeVal = getArguments().getString("imgTypeVal","");
        siteFilterVal = getArguments().getString("siteFilterVal","");
    }

    private void setupViews(View view) {
        saveBtn = (Button) view.findViewById(R.id.btnSave);
        resetBtn = (Button) view.findViewById(R.id.btnReset);
        sprImageSize = (Spinner) view.findViewById(R.id.sprImageSize);
        sprColorFilter = (Spinner) view.findViewById(R.id.sprColorFilter);
        sprImageType = (Spinner) view.findViewById(R.id.sprImageType);
        tvSiteFilter = (TextView) view.findViewById(R.id.etSiteFilter);
    }

    private void populateSpinners() {
        // create data source adapters
        aSprImageSize = ArrayAdapter.createFromResource(getActivity(), R.array.imageSizeArr,
                R.layout.item_custom_textview);
        aSprColorFilter = ArrayAdapter.createFromResource(getActivity(), R.array.colorFilterArr,
                R.layout.item_custom_textview);
        aSprImageType = ArrayAdapter.createFromResource(getActivity(), R.array.imageTypeArr,
                R.layout.item_custom_textview);

        // add data source to adapter
        sprImageSize.setAdapter(aSprImageSize);
        sprColorFilter.setAdapter(aSprColorFilter);
        sprImageType.setAdapter(aSprImageType);

        // populate view data
        if (imgSizeVal != "") {
            sprImageSize.setSelection(aSprImageSize.getPosition(imgSizeVal));
        }
        if (colorFilterVal!= "") {
            sprColorFilter.setSelection(aSprColorFilter.getPosition(colorFilterVal));
        }
        if (imgTypeVal != "") {
            sprImageType.setSelection(aSprImageType.getPosition(imgTypeVal));
        }
        if (siteFilterVal != "") {
            tvSiteFilter.setText(siteFilterVal);
        }
    }

    @Override
    public void onAttach(Activity searchActivity) {
        super.onAttach(searchActivity);
        //make sure SearchActivity implemented required interface
        try {
            fragmentListener = (OnFilterSettingsFragmentListener) searchActivity;
        } catch (ClassCastException e) {
            throw new ClassCastException(searchActivity.toString()
                    + " must implement OnFilterSettingsFragmentListener");
        }
    }

    public void onSave() {
        // get the currently selected data from the spinners and the EditText
        imgSizeVal = sprImageSize.getSelectedItem().toString();
        colorFilterVal = sprColorFilter.getSelectedItem().toString();
        imgTypeVal = sprImageType.getSelectedItem().toString();
        siteFilterVal = tvSiteFilter.getText().toString();
        //set values
        if (fragmentListener != null) {
            fragmentListener.OnFilterSet(imgSizeVal, colorFilterVal, imgTypeVal, siteFilterVal);
        }
    }

    private void onReset() {
        sprImageSize.setSelection(0);
        sprColorFilter.setSelection(0);
        sprImageType.setSelection(0);
        tvSiteFilter.setText("");
    }

    public void onDetatch() {
        super.onDetach();
        //reset fragment listener to null
        fragmentListener = null;
    }

    private void setupSaveBtnListener() {
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onSave();
                dismiss();
            }
        });
    }

    private void setupResetBtnListener() {
        resetBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onReset();
            }
        });
    }
}
