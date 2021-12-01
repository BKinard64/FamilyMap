package byu.cs240.familymapclient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import applogic.DataCache;
import model.Event;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;

    public MapFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);

        // Add markers for each event
        for (Event event : DataCache.getInstance().getEvents().values()) {
            // Assign marker its unique color based on eventType
            float mrkrColor = DataCache.getInstance().getEventColors().get(event.getType());

            // Add marker to the map by specifying its location and color
            Marker marker = map.addMarker(new MarkerOptions()
                                    .position(new LatLng(event.getLatitude(), event.getLongitude()))
                                    .icon(BitmapDescriptorFactory.defaultMarker(mrkrColor)));

            // Set the marker's tag to be the model event object for the marker
            marker.setTag(event);
        }
    }

    @Override
    public void onMapLoaded() {}
}