package org.henryschmale.milespergallontracker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.codecrafters.tableview.TableDataAdapter;

public class MileageEventTableAdapter extends TableDataAdapter<MileageEvent> {
    public MileageEventTableAdapter(Context context, List<MileageEvent> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        MileageEvent event = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderDate(event.when);
                break;
            case 1:
                renderedView = renderMileage(event.mileage);
                break;
            case 2:
                renderedView = renderCost(event.costPerGallon);
                break;
            case 3:
                renderedView = renderGallons(event.gallons);
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
