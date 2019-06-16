package org.henryschmale.milespergallontracker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import org.henryschmale.milespergallontracker.R;

public class StatCard extends CardView {
    private TextView statText;
    private TextView descriptionText;
    private ImageView iconView;
    private double value = 0;
    private int formatStringId;

    public StatCard(@NonNull Context context) {
        super(context);
        // init();
    }

    public StatCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StatCard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.StatCard
        );

        inflate(getContext(), R.layout.card_stat, this);

        this.formatStringId = a.getResourceId(
                R.styleable.StatCard_formatStatId,
                R.string.def_statcard_stat_format
        );

        this.statText = findViewById(R.id.stat);
        this.descriptionText = findViewById(R.id.description);
        this.iconView = findViewById(R.id.icon);

        this.iconView.setImageDrawable(
                getResources().getDrawable(a.getResourceId(
                        R.styleable.StatCard_iconDrawable,
                        R.drawable.ic_launcher_background))
        );

        this.descriptionText.setText(
                getResources().getText(a.getResourceId(
                        R.styleable.StatCard_description,
                        R.string.def_statcard_description_lbl))
        );

        setValue(value);
        a.recycle();
    }

    public void setValue(double value) {
        this.value = value;
        String text = getResources().getString(this.formatStringId, value);
        this.statText.setText(text);
    }
}
