package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Earthquake earthquake = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_view_item, parent, false);
        }

        TextView primaryLocation = (TextView) convertView.findViewById(R.id.pri_location);
        TextView secondaryLocation = (TextView) convertView.findViewById(R.id.sec_location);
        TextView magnitude = (TextView) convertView.findViewById(R.id.magnitude);
        TextView time = (TextView) convertView.findViewById(R.id.the_time);
        TextView dateView = (TextView) convertView.findViewById(R.id.date);

        primaryLocation.setText(earthquake.getPrimaryText());
        secondaryLocation.setText(earthquake.getSecondaryText());
        magnitude.setText(formatMagnitude(earthquake.getMagnitude()));


        //Format the date and time
        Date date = new Date(earthquake.getTime());

        //Get the date
        SimpleDateFormat formatDate = new SimpleDateFormat("MMM d, yyyy");
        SimpleDateFormat formatTime = new SimpleDateFormat("h:mm a");

        time.setText(formatTime.format(date));
        dateView.setText(formatDate.format(date));

        //Set the color of the magnitude shape
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
        magnitudeCircle.setColor(getMagnitudeColor(earthquake.getMagnitude()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage(earthquake);
            }
        });
        return convertView;
    }

    /**
     * Open the url for the earthquake in order to see more details.
     *
     * @param earthquake {@link Earthquake}
     */
    private void openWebPage(Earthquake earthquake) {
        Uri webPageUri = Uri.parse(earthquake.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW,webPageUri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null){
            getContext().startActivity(intent);
        }
    }

    /**
     *
     * @param magnitude int
     * @return int
     */
    private int getMagnitudeColor(double magnitude) {
        int magnitudeFloor = (int) Math.floor(magnitude);
        int magnitudeColorId;

        switch (magnitudeFloor) {
            case 0:

            case 1:
                magnitudeColorId = ContextCompat.getColor(getContext(), R.color.magnitude1);
                break;
            case 2:
                magnitudeColorId = ContextCompat.getColor(getContext(), R.color.magnitude2);
                break;
            case 3:
                magnitudeColorId = ContextCompat.getColor(getContext(), R.color.magnitude3);
                break;
            case 4:
                magnitudeColorId = ContextCompat.getColor(getContext(), R.color.magnitude4);
                break;
            case 5:
                magnitudeColorId = ContextCompat.getColor(getContext(), R.color.magnitude5);
                break;
            case 6:
                magnitudeColorId = ContextCompat.getColor(getContext(), R.color.magnitude6);
                break;
            case 7:
                magnitudeColorId = ContextCompat.getColor(getContext(), R.color.magnitude7);
                break;
            case 8:
                magnitudeColorId = ContextCompat.getColor(getContext(), R.color.magnitude8);
                break;
            case 9:
                magnitudeColorId = ContextCompat.getColor(getContext(), R.color.magnitude9);
                break;
            default:
                magnitudeColorId = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
                break;
        }
        return magnitudeColorId;
    }

    /**
     * Make value have one decimal place.
     *
     * @param magnitude double
     * @return String
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(magnitude);
    }
}
