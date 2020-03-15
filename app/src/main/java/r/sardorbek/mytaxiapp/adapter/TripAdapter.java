package r.sardorbek.mytaxiapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import r.sardorbek.mytaxiapp.Data;
import r.sardorbek.mytaxiapp.R;


public class TripAdapter extends ArrayAdapter<Data> {


    public TripAdapter(Context context, int resource, ArrayList<Data> tripData) {
        super(context, resource, tripData);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        }

        Data currentData = getItem(position);

        TextView startingLocation = convertView.findViewById(R.id.startingAddress);
        TextView finalLocation = convertView.findViewById(R.id.endingAddress);
        TextView price = convertView.findViewById(R.id.price);

        if (currentData != null) {
            startingLocation.setText(currentData.getStartingLocations());
            finalLocation.setText(currentData.getFinalLocation());
            price.setText(currentData.getPrice());
        }

        return convertView;
    }
}
