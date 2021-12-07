package byu.cs240.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import applogic.DataCache;
import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {

    public static final String PERSON_KEY = "PersonID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        // Identify the person this Person Activity has been activated for
        Intent intent = getIntent();
        String personID = intent.getStringExtra(PERSON_KEY);
        Person person = DataCache.getInstance().getPeople().get(personID);

        // Set the views with his/her information
        TextView firstNameView = findViewById(R.id.paFirstName);
        firstNameView.setText(person.getFirstName());

        TextView lastNameView = findViewById(R.id.paLastName);
        lastNameView.setText(person.getLastName());

        TextView genderView = findViewById(R.id.paGender);
        genderView.setText(person.getGender().equals("m") ? getString(R.string.male) : getString(R.string.female));

        // Set the Expandable List View to hold the person's life event and family member data
        ExpandableListView eListView = findViewById(R.id.familyLifeLists);

        // Get person's family data
        PriorityQueue<Event> lifeEvents = new PriorityQueue<Event>(DataCache.getInstance().getPersonEvents().get(personID));
        List<Person> familyMembers = DataCache.getInstance().getFamilyMembers().get(personID);

        // Set the adapter
        eListView.setAdapter(new ExpandableListAdapter(lifeEvents, familyMembers, person));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return true;
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int LIFE_EVENT_GROUP_POSITION = 0;
        private static final int FAMILY_MEMBER_GROUP_POSITION = 1;

        private final List<Event> lifeEvents;
        private final List<Person> familyMembers;
        private final Person activityPerson;

        public ExpandableListAdapter(PriorityQueue<Event> lifeEvents, List<Person> familyMembers, Person activityPerson) {
            this.lifeEvents = new ArrayList<>();
            while (!lifeEvents.isEmpty()) {
                this.lifeEvents.add(lifeEvents.poll());
            }
            this.familyMembers = familyMembers;
            this.activityPerson = activityPerson;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENT_GROUP_POSITION:
                    return lifeEvents.size();
                case FAMILY_MEMBER_GROUP_POSITION:
                    return familyMembers.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENT_GROUP_POSITION:
                    return getString(R.string.life_events_title);
                case FAMILY_MEMBER_GROUP_POSITION:
                    return getString(R.string.family_members_title);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case LIFE_EVENT_GROUP_POSITION:
                    return lifeEvents.get(childPosition);
                case FAMILY_MEMBER_GROUP_POSITION:
                    return familyMembers.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case LIFE_EVENT_GROUP_POSITION:
                    titleView.setText(R.string.life_events_title);
                    break;
                case FAMILY_MEMBER_GROUP_POSITION:
                    titleView.setText(R.string.family_members_title);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);

            switch (groupPosition) {
                case LIFE_EVENT_GROUP_POSITION:
                    initializeLifeEventView(itemView, childPosition);
                    break;
                case FAMILY_MEMBER_GROUP_POSITION:
                    initializeFamilyMemberView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeLifeEventView(View lifeEventItemView, final int childPosition) {
            // Set the image as a map marker
            ImageView eventIcon = lifeEventItemView.findViewById(R.id.list_item_icon);
            eventIcon.setImageDrawable(new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker)
                                                        .colorRes(R.color.black).sizeDp(40));

            // Set the top text to the event and its location
            TextView eventInfo = lifeEventItemView.findViewById(R.id.list_item_text_top);
            Event event = lifeEvents.get(childPosition);
            String info = event.getType().toUpperCase() + ": " + event.getCity() + ", " +
                          event.getCountry() + " (" + event.getYear() + ")";
            eventInfo.setText(info);

            // Set the bottom text to the person's name
            TextView personName = lifeEventItemView.findViewById(R.id.list_item_text_bottom);
            String name = DataCache.getInstance().getPeople().get(event.getPersonID()).getFirstName() +
                          " " +
                          DataCache.getInstance().getPeople().get(event.getPersonID()).getLastName();
            personName.setText(name);

            // Enable a listener on life events that will open an event activity
            lifeEventItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra(EventActivity.EVENT_KEY, event.getId());
                    startActivity(intent);
                }
            });
        }

        private void initializeFamilyMemberView(View familyMemberItemView, final int childPosition) {
            Person person = familyMembers.get(childPosition);

            // Set the image as a male or female icon
            ImageView genderIcon = familyMemberItemView.findViewById(R.id.list_item_icon);
            Drawable icon;
            if (person.getGender().equals("m")) {
                icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male)
                        .colorRes(R.color.male_icon).sizeDp(40);
            } else {
                icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female)
                        .colorRes(R.color.female_icon).sizeDp(40);
            }
            genderIcon.setImageDrawable(icon);

            // Set the top text to the name of the person
            TextView personName = familyMemberItemView.findViewById(R.id.list_item_text_top);
            personName.setText(person.getFirstName() + " " + person.getLastName());

            // Set the bottom text to the relationship
            TextView relationshipView = familyMemberItemView.findViewById(R.id.list_item_text_bottom);
            String relationship = establishRelationship(person);
            relationshipView.setText(relationship);

            // Enable a listener on family members that will open their person activities
            familyMemberItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra(PersonActivity.PERSON_KEY, person.getId());
                    startActivity(intent);
                }
            });
        }

        private String establishRelationship(Person person) {
            String relationship = null;

            Person activityPersonFather = null;
            Person activityPersonMother = null;
            Person activityPersonSpouse = null;

            // Confirm the person associated with this activity has a father/mother/spouse
            if (activityPerson.getFatherID() != null) {
                activityPersonFather = DataCache.getInstance().getPeople().get(activityPerson.getFatherID());
                if (activityPersonFather.equals(person)) {
                    relationship = getString(R.string.father);
                }
            }
            if (activityPerson.getMotherID() != null) {
                activityPersonMother = DataCache.getInstance().getPeople().get(activityPerson.getMotherID());
                if (activityPersonMother.equals(person)) {
                    relationship = getString(R.string.mother);
                }
            }
            if (activityPerson.getSpouseID() != null) {
                activityPersonSpouse = DataCache.getInstance().getPeople().get(activityPerson.getSpouseID());
                if (activityPersonSpouse.equals(person)) {
                    relationship = getString(R.string.spouse);
                }
            }

            // If this person is not a father/mother/spouse, then they must be a child
            if (relationship == null) {
                relationship = getString(R.string.child);
            }

            return relationship;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}