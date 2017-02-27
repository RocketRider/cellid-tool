package de.rrsoftware.cellid_tool.ui.locations;

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
import de.rrsoftware.cellid_tool.model.CellLocationManager;

class LocationListAdapter extends ArrayAdapter<Integer> {
    private final CellLocationManager lm;


    LocationListAdapter(final Context context, final Integer[] objects) {
        super(context, -1, objects);
        lm = CellLocationManager.getInstance(context);
    }


    @NonNull
    @Override
    public View getView(final int position, final View convertView, final @NonNull ViewGroup parent) {
        final int cellId = getItem(position);

        final LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.location_item, parent, false);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(getContext(), RegisterLocationActivity.class);
                intent.putExtra(RegisterLocationActivity.CELL_ID, cellId);
                getContext().startActivity(intent);
            }
        });

        final TextView cidView = (TextView) rowView.findViewById(R.id.cid);
        final TextView descView = (TextView) rowView.findViewById(R.id.desc);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        cidView.setText(String.valueOf(cellId));
        descView.setText(lm.getDescription(cellId));

        final File imageFile = new File(getContext().getFilesDir(), cellId + ".jpg");
        if (imageFile.exists()) {
            imageView.setImageDrawable(null);
            imageView.setImageURI(Uri.fromFile(imageFile));
        } else {
            imageView.setImageResource(R.drawable.ic_image_black_24dp);
        }

        return rowView;
    }

}
