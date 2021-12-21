# FamilyMap
FamilyMap is an Android application that stores and displays a registered user's family history information on an interactive Google Map. After logging in or registering,
a user can view their own family history events as well as those of their immediate family and ancestors on the map. Upon clicking a marker, the user has the option to
click on the associated event details to launch an activity showing that person's personal info, life events, and family members. There are also settings and search activities
for users to use within the application.

## FamilyMap Server
The server used to store user's family history data was written in Java and contains methods to add and retrieve various user, people, event, and authtoken tables in a SQLite database.
In addition to being able to accept HTTP Requests according to a specified API, the server can also render a webpage that can be used to easily interact with the server.

## FamiyMap Application
The application was programmed in Android and allows users to log-in or register and view their family history events. The **Map Activity**, which displays markers on a Google Map representing
family life events, is created after successfully exiting the **Login Activity**. The **Person Activity**, where a person's personal and family information is displayed, is activated when a user
clicks on the event details that appear in response to a map marker being clicked. The **Event Activity**, which resembles the Map Activity, displays the Google Map centered on the event selected
by the user from the Person or Search Activities. The **Search Activity** allows users to search for people and events stored in the database that belong to their family history. Finally, the
**Settings Activity** provides filters for the user to manipulate to adjust what kind of information is displayed in the various other activities, including options to filter male/female events and
maternal/paternal ancestors.
