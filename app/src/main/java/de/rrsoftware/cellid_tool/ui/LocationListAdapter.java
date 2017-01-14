package de.rrsoftware.cellid_tool.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import de.rrsoftware.cellid_tool.R;
import de.rrsoftware.cellid_tool.model.LocationManager;

public class LocationListAdapter extends ArrayAdapter<Integer> {
    private LocationManager lm;


    public LocationListAdapter(Context context, Integer[] objects) {
        super(context, -1, objects);
        lm = LocationManager.getInstance(context);
    }


    @Override
    public View getView(final int position, final View convertView, final @NonNull ViewGroup parent) {
        final int cellId = getItem(position);

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.location_item, parent, false);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RegisterLocationActivity.class);
                intent.putExtra(RegisterLocationActivity.CELL_ID, cellId);
                getContext().startActivity(intent);
            }
        });

        TextView cidView = (TextView) rowView.findViewById(R.id.cid);
        TextView descView = (TextView) rowView.findViewById(R.id.desc);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        cidView.setText(String.valueOf(cellId));
        descView.setText(lm.getDescription(cellId));

        File imageFile = new File(getContext().getExternalFilesDir(null), cellId + ".jpg");
        if (imageFile.exists()) {
            imageView.setImageDrawable(null);
            imageView.setImageURI(Uri.fromFile(imageFile));
        } else {
            imageView.setImageResource(R.drawable.ic_image_black_24dp);
        }

        return rowView;
    }

}
