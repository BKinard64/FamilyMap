package byu.cs240.familymapclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

import applogic.DataCache;
import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private static final int EVENT_ITEM_VIEW_TYPE = 0;
    private static final int PERSON_ITEM_VIEW_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Create the RecyclerView to hold the search results
        RecyclerView searchResults = findViewById(R.id.search_results);
        searchResults.setLayoutManager(new LinearLayoutManager(this));

        // Create the SearchView for user's to enter queries
        SearchView searchBar = findViewById(R.id.search_bar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               return findEventsAndPeople(query, searchResults);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return findEventsAndPeople(newText, searchResults);
            }
        });
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

    private boolean findEventsAndPeople(String criterion, RecyclerView searchResults) {
        // Create List Objects to pass to Adapter
        List<Event> searchMatchEvents = DataCache.getInstance().findEventsMatchingSearch(criterion);
        List<Person> searchMatchPeople = DataCache.getInstance().findPeopleMatchingSearch(criterion);

        // Create the adapter for the Search Activity
        FamilyMapSearchAdapter adapter = new FamilyMapSearchAdapter(searchMatchEvents, searchMatchPeople);
        searchResults.setAdapter(adapter);

        return true;
    }

    private class FamilyMapSearchAdapter extends RecyclerView.Adapter<FamilyMapSearchViewHolder> {
        private final List<Event> eventList;
        private final List<Person> personList;

        public FamilyMapSearchAdapter(List<Event> eventList, List<Person> personList) {
            this.eventList = eventList;
            this.personList = personList;
        }

        @Override
        public int getItemViewType(int position) {
            return position < eventList.size() ? EVENT_ITEM_VIEW_TYPE : PERSON_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public FamilyMapSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            return new FamilyMapSearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull FamilyMapSearchViewHolder holder, int position) {
            if (position < eventList.size()) {
                holder.bind(eventList.get(position));
            } else {
                holder.bind(personList.get(position - eventList.size()));
            }
        }

        @Override
        public int getItemCount() {
            return eventList.size() + personList.size();
        }
    }

    private class FamilyMapSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView icon;
        private final TextView topText;
        private final TextView bottomText;

        private final int viewType;
        private Event event;
        private Person person;

        public FamilyMapSearchViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            this.icon = itemView.findViewById(R.id.list_item_icon);
            this.topText = itemView.findViewById(R.id.list_item_text_top);
            this.bottomText = itemView.findViewById(R.id.list_item_text_bottom);

            itemView.setOnClickListener(this);
        }

        private void bind(Event event) {
            this.event = event;
            // Set the icon as a map marker
            icon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker)
                                                    .colorRes(R.color.black).sizeDp(40));
            // Set topText as event info
            String eventInfo = event.getType().toUpperCase() + ": " + event.getCity() + ", " +
                    event.getCountry() + " (" + event.getYear() + ")";
            topText.setText(eventInfo);

            // Set bottomText as person's name
            String personName = DataCache.getInstance().getPeople().get(event.getPersonID()).getFirstName() +
                    " " +
                    DataCache.getInstance().getPeople().get(event.getPersonID()).getLastName();
            bottomText.setText(personName);
        }

        private void bind(Person person) {
            this.person = person;
            // Set the icon as a male/female
            Drawable genderIcon;
            if (person.getGender().equals("m")) {
                genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male)
                        .colorRes(R.color.male_icon).sizeDp(40);
            } else {
                genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female)
                        .colorRes(R.color.female_icon).sizeDp(40);
            }
            icon.setImageDrawable(genderIcon);

            // Set topText as person's name
            topText.setText(person.getFirstName() + " " + person.getLastName());
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            if (viewType == EVENT_ITEM_VIEW_TYPE) {
                intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra(EventActivity.EVENT_KEY, event.getId());
            } else {
                intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra(PersonActivity.PERSON_KEY, person.getId());
            }
            startActivity(intent);
        }
    }
}