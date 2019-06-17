package org.henryschmale.milespergallontracker;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryListFragment extends Fragment {

    RecyclerView view;

    public HistoryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View x = inflater.inflate(R.layout.fragment_history_list, container, false);

        view = x.findViewById(R.id.list_view);
        return x;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedMpgViewModel viewModel = ViewModelProviders.of(getActivity()).get(SharedMpgViewModel.class);

        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        view.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        MileageIntervalAdapter adapter = new MileageIntervalAdapter(getActivity().getApplication(), new ArrayList<>());
        view.setAdapter(adapter);

        viewModel.getMileageIntervals().observe(this, (LiveData<List<MileageInterval>> listLiveData) -> {
            listLiveData.removeObservers(HistoryListFragment.this);
            listLiveData.observe(HistoryListFragment.this, mileageIntervals -> adapter.setDataset(mileageIntervals));
        });
    }
}
