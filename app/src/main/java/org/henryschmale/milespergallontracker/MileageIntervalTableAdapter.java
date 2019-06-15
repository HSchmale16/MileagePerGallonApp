package org.henryschmale.milespergallontracker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.codecrafters.tableview.TableDataAdapter;

public class MileageIntervalTableAdapter extends TableDataAdapter<MileageInterval> {

    public MileageIntervalTableAdapter(Context context, List<MileageInterval> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        MileageInterval event = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderDate(event.lastFillup);
                break;
            case 1:
                renderedView = renderDate(event.when);
                break;
            case 2:
                renderedView = renderMileage(event.milesTraveled);
                break;
            case 3:
                renderedView = renderMileage((long)event.mpg);
                break;
            case 4:
                renderedView = renderCost(event.costPerMile);
                break;
        }

        return renderedView;
    }

    private TextView renderCost(double cost) {
        TextView v = new TextView(getContext());
        v.setText(String.format(Locale.US, "$%.2f", cost));
        v.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        return v;
    }

    private TextView renderMileage(long m) {
        TextView v = new TextView(getContext());
        v.setText(String.format(Locale.US, "%,d", m));
        v.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        return v;
    }

    private TextView renderDate(Date d) {
        TextView v = new TextView(getContext());
        v.setText(d.toString());
        return v;
    }

    private TextView renderGallons(double gal) {
        TextView v = new TextView(getContext());
        v.setText(String.format(Locale.US, "%.3f", gal));
        v.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        return v;
    }
}
