package byu.cs240.familymapclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import applogic.DataCache;
import model.Event;
import model.Person;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    public static final String FROM_EVENT_KEY = "Event passed by Event Activity";

    private final AppCompatActivity activity;
    private Event selectedEvent;
    private List<Polyline> mapLines;
    private GoogleMap map;
    private ImageView eventIcon;
    private TextView personName;
    private TextView eventDetails;
    private LinearLayout eventInfo;

    public MapFragment(AppCompatActivity activity) {
        this.activity = activity;
        mapLines = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (activity.getClass() == MainActivity.class) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventIcon = view.findViewById(R.id.event_icon);
        personName = view.findViewById(R.id.person_name);
        eventDetails = view.findViewById(R.id.event_details);
        eventInfo = view.findViewById(R.id.event_info);

        if (getArguments() != null) {
            String eventID = getArguments().getString(FROM_EVENT_KEY);
            selectedEvent = DataCache.getInstance().getEvents().get(eventID);
        }

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

                executeMarkerClickResponse(event);

                return true;
            }
        });

        // Have map center on selected event if being called from Event Activity
        if (activity.getClass() == EventActivity.class) {
            executeMarkerClickResponse(selectedEvent);
        }
    }

    private void executeMarkerClickResponse(Event event) {
        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(event.getLatitude(),
                event.getLongitude())));
        // Draw lines on map
        removeLines();
        drawSpouseLine(event);
        drawFamilyLines(event);
        drawLifeStoryLines(event);

        // Update event information
        Drawable genderIcon;
        if (DataCache.getInstance().getPeople().get(event.getPersonID()).getGender().equals("m")) {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                    .colorRes(R.color.male_icon).sizeDp(40);
        } else {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                    .colorRes(R.color.female_icon).sizeDp(40);
        }
        eventIcon.setImageDrawable(genderIcon);

        String firstName = DataCache.getInstance()
                .getPeople().get(event.getPersonID()).getFirstName();
        String lastName = DataCache.getInstance()
                .getPeople().get(event.getPersonID()).getLastName();
        personName.setText(firstName + " " + lastName);

        String eventType = event.getType().toUpperCase() + ":";
        String location = event.getCity() + ", " + event.getCountry();
        String year = "(" + event.getYear() + ")";
        eventDetails.setText(eventType + " " + location + " " + year);

        // Make event info clickable
        eventInfo.setClickable(true);
        eventInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PersonActivity.class);
                intent.putExtra(PersonActivity.PERSON_KEY, event.getPersonID());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapLoaded() {}

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.searchMenuItem);
        searchMenuItem.setIcon(new IconDrawable(activity, FontAwesomeIcons.fa_search)
                                                .colorRes(R.color.white)
                                                .actionBarSize());

        MenuItem settingsMenuItem = menu.findItem(R.id.settingsMenuItem);
        settingsMenuItem.setIcon(new IconDrawable(activity, FontAwesomeIcons.fa_gear)
                                                  .colorRes(R.color.white)
                                                  .actionBarSize());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.searchMenuItem) {
            Intent intent = new Intent(activity, SearchActivity.class);
            startActivity(intent);
        }

        return true;
    }

    private void drawSpouseLine(Event event) {
        // Identify the spouse
        Person person = DataCache.getInstance().getPeople().get(event.getPersonID());
        Person spouse = DataCache.getInstance().getPeople().get(person.getSpouseID());

        if (spouse != null) {
            // Get the earliest event of the spouse
            Event spouseEvent = DataCache.getInstance().getPersonEvents().get(spouse.getId()).peek();

            // Draw spouse line
            if (spouseEvent != null) {
                drawLine(event, spouseEvent, Color.MAGENTA, 15.0F);
            }
        }
    }

    private void drawFamilyLines(Event event) {
        drawParentLines(DataCache.getInstance().getPeople().get(event.getPersonID()), event, 15.0F);
    }

    private void drawParentLines(Person person, Event event, float width) {
        // Identify the current person's parents
        Person father = DataCache.getInstance().getPeople().get(person.getFatherID());
        Person mother = DataCache.getInstance().getPeople().get(person.getMotherID());

        if (father != null) {
            // Get the earliest event of the father
            Event fatherEvent = DataCache.getInstance().getPersonEvents().get(father.getId()).peek();

            // Draw father line
            if (fatherEvent != null) {
                drawLine(event, fatherEvent, Color.GREEN, width);
            }

            // Draw parent lines for the father
            drawParentLines(father, fatherEvent, width * 0.5F);
        }

        if (mother != null) {
            // Get the earliest event of the mother
            Event motherEvent = DataCache.getInstance().getPersonEvents().get(mother.getId()).peek();

            // Draw mother line
            if (motherEvent != null) {
                drawLine(event, motherEvent, Color.GREEN, width);
            }

            // Draw parent line for the mother
            drawParentLines(mother, motherEvent, width * 0.5F);
        }
    }

    private void drawLifeStoryLines(Event event) {
        // Identify person associated with selected event
        String personID = event.getPersonID();

        // Copy the PriorityQueue for the events of the given Person
        PriorityQueue<Event> lifeEvents = new PriorityQueue<Event>(DataCache.getInstance().getPersonEvents().get(personID));

        // Draw line from first event to second event
        if (lifeEvents.peek() != null) {
            drawStoryLine(lifeEvents.poll(), lifeEvents);
        }
    }

    private void drawStoryLine(Event startEvent, PriorityQueue<Event> lifeEvents) {
        if (lifeEvents.peek() != null) {
            // Get the next earliest remaining event
            Event endEvent = lifeEvents.poll();

            // Draw line between start and end events
            drawLine(startEvent, endEvent, Color.BLACK, 15.0F);

            // Draw line from end event to next earliest event
            drawStoryLine(endEvent, lifeEvents);
        }
    }

    private void drawLine(Event startEvent, Event endEvent, int lnColor, float width) {
        // Create start and end points for the line
        LatLng startPoint = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endPoint = new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

        // Add line to map by specifying its endpoints, color, and width
        PolylineOptions options = new PolylineOptions()
                .add(startPoint)
                .add(endPoint)
                .color(lnColor)
                .width(width);
        Polyline line = map.addPolyline(options);

        // Store line
        mapLines.add(line);
    }

    private void removeLines() {
        for (Polyline line : mapLines) {
            line.remove();
        }
        mapLines.clear();
    }
}