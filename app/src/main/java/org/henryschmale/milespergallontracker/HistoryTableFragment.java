package org.henryschmale.milespergallontracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class HistoryTableFragment extends Fragment {
    TableView<MileageEvent> view;
    View emptyDataIndicator;

    public HistoryTableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View x = inflater.inflate(R.layout.fragment_history_table, container, false);
        view = x.findViewById(R.id.history_table);
        emptyDataIndicator = x.findViewById(R.id.tbl_msg_no_data);

        return x;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedMpgViewModel viewModel = ViewModelProviders.of(getActivity()).get(SharedMpgViewModel.class);

        final String[] HEADERS = {"Date", "Mileage", "Per Gallon", "Gallons"};
        SimpleTableHeaderAdapter adapter = new SimpleTableHeaderAdapter(HistoryTableFragment.this.getActivity(), HEADERS);
        view.setHeaderAdapter(adapter);

        view.setEmptyDataIndicatorView(emptyDataIndicator);

        viewModel.getMileageEvents().observe(this, new Observer<LiveData<List<MileageEvent>>>() {
            @Override
            public void onChanged(LiveData<List<MileageEvent>> listLiveData) {
                listLiveData.removeObservers(HistoryTableFragment.this);
                listLiveData.observe(HistoryTableFragment.this, new Observer<List<MileageEvent>>() {
                    @Override
                    public void onChanged(List<MileageEvent> mileageEvents) {
                        view.setDataAdapter(new MileageEventTableAdapter(
                                HistoryTableFragment.this.getActivity(), mileageEvents
                        ));
                    }
                });
            }
        });
    }

}