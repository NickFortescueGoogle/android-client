package org.msf.records.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.msf.records.R;
import org.msf.records.model.ListItem;

import java.util.Arrays;

/**
 * Created by danieljulio on 11/10/2014.
 */
public class GridDialogFragment extends DialogFragment {

    private static final String TAG = GridDialogFragment.class.getName();
    private static final String ITEM_LIST_KEY = "ITEM_LIST_KEY";
    public static final int TYPE_PRIMARY = 0, TYPE_SECONDARY = 1;

    ArrayAdapterWithIcon mListAdapter;
    GridView mGridView;
    ListItem mData;
    Parcelable[] mParcelables;
    ListItem[] mIconListDialogs;
    OnItemClickListener mItemClickListener;

    abstract OnItemClickListener {
        public void onGridItemClick(int position, int type);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListAdapter = new ArrayAdapterWithIcon(getActivity());
        Bundle bundle = getArguments();
        mParcelables = bundle.getParcelableArray(ITEM_LIST_KEY);
        mIconListDialogs = Arrays.copyOf(mParcelables, mParcelables.length, ListItem[].class);
        mListAdapter.addAll(mIconListDialogs);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(getParentFragment() instanceof OnItemClickListener)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mItemClickListener = (OnItemClickListener) getParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGridView = new GridView(getActivity());

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mGridView.setLayoutParams(new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mGridView.setNumColumns(3);
        mGridView.setAdapter(mListAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mItemClickListener.onGridItemClick(position, TYPE_PRIMARY);
                getDialog().dismiss();
            }
        });

        return mGridView;
    }


    public class ArrayAdapterWithIcon extends ArrayAdapter<ListItem> {

        public ArrayAdapterWithIcon(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.gridview_cell_dialog_row, null);

            mData = getItem(position);
            ImageView image = (ImageView) convertView.findViewById(R.id.grid_dialog_image);
            TextView title = (TextView) convertView.findViewById(R.id.grid_dialog_text);
            image.setImageResource(mData.getIconId());
            title.setText(mData.getTitleId());

            return convertView;
        }
    }
}
