package org.henryschmale.milespergallontracker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.henryschmale.milespergallontracker.view.StatCard;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class DashboardFragment extends Fragment {
    public static final String TAG = "DashboardFragment";
    private StatCard mpg;
    private StatCard cost;
    private StatCard interMiles;
    private StatCard totalMiles;

    public DashboardFragment() {
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
        View x = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mpg = x.findViewById(R.id.avg_mpg);
        cost = x.findViewById(R.id.avg_mile_cost);
        interMiles = x.findViewById(R.id.avg_miles_between_fillups);
        totalMiles = x.findViewById(R.id.total_miles);

        return x;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedMpgViewModel viewModel = ViewModelProviders.of(getActivity()).get(SharedMpgViewModel.class);

        viewModel.getMileageIntervals().observe(this, new Observer<LiveData<List<MileageInterval>>>() {
            @Override
            public void onChanged(LiveData<List<MileageInterval>> listLiveData) {
                listLiveData.removeObservers(DashboardFragment.this);
                listLiveData.observe(DashboardFragment.this, new Observer<List<MileageInterval>>() {
                    @Override
                    public void onChanged(List<MileageInterval> mileageIntervals) {
                        Log.d(TAG, "MileageIntervals were updated, new length = " + mileageIntervals.size());
                        Optional<MileageInterval> data = mileageIntervals.stream()
                                .reduce(MileageIntervalBinaryOp::computeTotalMileage);

                        if(data.isPresent()) {
                            MileageInterval s = data.get();
                            Log.d(TAG, "Updating labels");
                            float times = (float)mileageIntervals.size();
                            totalMiles.setValue(s.milesTraveled);
                            interMiles.setValue(s.milesTraveled / times);
                            mpg.setValue(s.mpg / times);
                            cost.setValue(s.costPerMile / times);
                        } else {
                            totalMiles.setValue(0);
                            interMiles.setValue(0);
                            mpg.setValue(0);
                            cost.setValue(0);
                        }
                    }
                });
            }
        });
    }

}
