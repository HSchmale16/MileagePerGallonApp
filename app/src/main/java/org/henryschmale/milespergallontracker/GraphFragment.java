package org.henryschmale.milespergallontracker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class GraphFragment extends Fragment {
    LineChart chart;

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View x = inflater.inflate(R.layout.fragment_graph, container, false);
        chart = x.findViewById(R.id.chart);
        return x;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedMpgViewModel viewModel = ViewModelProviders.of(getActivity()).get(SharedMpgViewModel.class);

        viewModel.getMileageIntervals().observe(this, listLiveData -> {
            listLiveData.removeObservers(GraphFragment.this);
            listLiveData.observe(GraphFragment.this, mileageIntervals -> {
                chart.clear();
                List<Entry> entries = new ArrayList<>();
                for (MileageInterval interval : mileageIntervals) {
                    entries.add(new Entry((float)interval.when.getTime(), (float)interval.mpg));
                }
                Collections.sort(entries, new EntryXComparator());
                LineDataSet dataSet = new LineDataSet(entries, "Label");
                chart.setData(new LineData(dataSet));

                XAxis xAxis = chart.getXAxis();
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        Date d = new Date((int)value);
                        DateFormat df = android.text.format.DateFormat.getDateFormat(getContext());
                        return df.format(d);
                    }
                });
                xAxis.setLabelRotationAngle(45);
                xAxis.setAvoidFirstLastClipping(true);
                chart.invalidate();
            });
        });
    }

}
