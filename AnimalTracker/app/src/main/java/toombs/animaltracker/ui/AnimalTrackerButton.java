package toombs.animaltracker.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import toombs.animaltracker.R;

public class AnimalTrackerButton extends AppCompatButton {

    public AnimalTrackerButton(Context context) {
        super(context);
    }

    public AnimalTrackerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimalTrackerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AnimalTrackerButton(Context context, String text) {
        super(context);
        this.setText(text);
        this.setBackground(getResources().getDrawable(R.drawable.button_background_selector,context.getTheme()));
        this.setHighlightColor(getResources().getColor(R.color.colorAccent, context.getTheme()));
        this.setTextColor(getResources().getColor(R.color.colorPrimary, context.getTheme()));
    }


}
