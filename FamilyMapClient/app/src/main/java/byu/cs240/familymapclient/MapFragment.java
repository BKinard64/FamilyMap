package byu.cs240.familymapclient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
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
    private TextView personName;
    private TextView eventDetails;
    private LinearLayout eventInfo;

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

        personName = view.findViewById(R.id.person_name);
        eventDetails = view.findViewById(R.id.event_details);
        eventInfo = view.findViewById(R.id.event_info);

        // TO-DO: Add search and setting menu icons

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);

        // Add markers for each event
        for (Event event : DataCache.getInstance().getEvents().values()) {
            // Assign marker its unique color based on eventType
            float mrkrColor = DataCache.getInstance().getEventColors().get(event.getType().toUpperCase());

            // Add marker to the map by specifying its location and color
            Marker marker = map.addMarker(new MarkerOptions()
                                    .position(new LatLng(event.getLatitude(), event.getLongitude()))
                                    .icon(BitmapDescriptorFactory.defaultMarker(mrkrColor)));

            // Set the marker's tag to be the model event object for the marker
            marker.setTag(event);
        }

        // Set a listener for when marker is clicked
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Event event = (Event)marker.getTag();
                map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(event.getLatitude(),
                                                                           event.getLongitude())));

                // TO-DO: Draw lines on map

                // Update event information
                // TO-DO: UPDATE ICON

                String firstName = DataCache.getInstance()
                                    .getPeople().get(event.getPersonID()).getFirstName();
                String lastName = DataCache.getInstance()
                                    .getPeople().get(event.getPersonID()).getLastName();
                personName.setText(firstName + " " + lastName);

                String eventType = event.getType().toUpperCase() + ":";
                String location = event.getCity() + ", " + event.getCountry();
                String year = "(" + event.getYear() + ")";
                eventDetails.setText(eventType + " " + location + " " + year);

                // Make event details clickable
                eventInfo.setClickable(true);
                // TO-DO: Set onClickListener

                return true;
            }
        });
    }

    @Override
    public void onMapLoaded() {}
}