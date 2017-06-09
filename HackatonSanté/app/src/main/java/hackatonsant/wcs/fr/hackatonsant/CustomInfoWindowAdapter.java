package hackatonsant.wcs.fr.hackatonsant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View contentView;
    private final LayoutInflater inflater;

    public CustomInfoWindowAdapter(Context context){
        inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.widow_info, null);
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        TextView textViewTitle = (TextView) contentView.findViewById(R.id.textViewTitle);
        TextView textViewSnippet = (TextView) contentView.findViewById(R.id.textViewContent);

        textViewTitle.setText(marker.getTitle());
        textViewSnippet.setText(marker.getSnippet());

        return contentView;
    }
}
