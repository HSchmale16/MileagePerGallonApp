package org.henryschmale.milespergallontracker;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.security.ConfirmationPrompt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class MileageIntervalAdapter extends RecyclerView.Adapter<MileageIntervalAdapter.MileageIntervalViewHolder> {
    private List<MileageInterval> mDataset;
    private Application context;

    public MileageIntervalAdapter(Application context, List<MileageInterval> mileageIntervals) {
        this.context = context;
        mDataset = mileageIntervals;
    }

    public void setDataset(List<MileageInterval> intervals) {
        mDataset = intervals;
        this.notifyDataSetChanged();
    }

    class MileageIntervalViewHolder extends RecyclerView.ViewHolder {
        TextView dateRangeText;
        TextView gallonsCostText;
        TextView mileageText;
        TextView mpgText;
        TextView mileCostText;
        MileageInterval interval;


        MileageIntervalViewHolder(View itemView) {
            super(itemView);

            dateRangeText = itemView.findViewById(R.id.date_range);
            gallonsCostText = itemView.findViewById(R.id.gallons);
            mileCostText = itemView.findViewById(R.id.mile_cost);
            mpgText = itemView.findViewById(R.id.mpg);
            mileageText = itemView.findViewById(R.id.mileage);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    MileageEvent event = interval.toMileageEvent();

                    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                CarRepository repo = new CarRepository(context);
                                repo.getCarDao().deleteMileageEvent(event);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder
                            .setMessage("Are you sure you want to remove the fuel stop on " + interval.when.toString() + "? You won't be able to recover it.")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });
        }

        public void setFields(MileageInterval interval) {
            this.interval = interval;

            Resources res = itemView.getResources();

            setDateRangeText(interval.lastFillup, interval.when);
            setGallonsCostText(interval.gallons, interval.costPerGallon);

            mileageText.setText(res.getString(R.string.format_miles, (float) interval.milesTraveled));
            mpgText.setText(res.getString(R.string.format_mpg, interval.mpg));
            mileCostText.setText(res.getString(R.string.format_cost, interval.costPerMile));
        }

        private void setDateRangeText(Date from, Date to) {
            java.text.DateFormat dtFormat = android.text.format.DateFormat.getMediumDateFormat(itemView.getContext());
            String s;
            if (from != null)
                s = itemView.getResources().getString(R.string.f_date_range, dtFormat.format(from), dtFormat.format(to));
            else
                s = "Started on " + dtFormat.format(to);
            dateRangeText.setText(s);
        }

        private void setGallonsCostText(double gals, double perGal) {
            String s = itemView.getResources().getString(R.string.f_gallon_text, gals, perGal);
            gallonsCostText.setText(s);
        }
    }

    @NonNull
    @Override
    public MileageIntervalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View mileageView = inflater.inflate(R.layout.item_mileage_interval, parent, false);

        return new MileageIntervalViewHolder(mileageView);
    }

    @Override
    public void onBindViewHolder(MileageIntervalViewHolder holder, int position) {
        MileageInterval interval = mDataset.get(position);
        holder.setFields(interval);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
