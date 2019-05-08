// burger-1af72
// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require("firebase-admin");
//admin.initializeApp(functions.config().firebase);
admin.initializeApp({
    databaseURL: 'https://burger-1af72.firebaseio.com',
    projectId:  'burger-1af72'
});

// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require("firebase-functions");

var modes = [
    { dev_start: "dev_", dev_end: "_dev" }, //
    { dev_start: "", dev_end: "" }
];

for (var j in modes) {
    const dev_start = modes[j].dev_start;
    const dev_end = modes[j].dev_end;
    const match_users_and_locations = require("./match_users_and_locations" + dev_end);
    const notify_users_of_location = require("./notify_users_of_location" + dev_end);
    const notify_weird_beer = require("./notify_weird_beer" + dev_end);

    for (i = 1; i < 6; i++) {
        exports["add_user_to_locations_pref" + i + dev_end] = functions.database.ref("/lunch_groups" + dev_end + "/{groupId}/users/{userId}/lunch_preference_" + i)
            .onWrite((change, event) => {
                return match_users_and_locations.add_user_to_locations_pref(change, event);
            });
        exports[dev_start + "lunch_tick_" + i] = functions.pubsub.topic(dev_start + "lunch-tick-" + i).onPublish(event => {
            return notify_users_of_location.send_notification_for_date(event, i, "lunch_groups" + dev_end);
        });
    }
    exports.remove_pref_when_location_deleted = functions.database.ref("/lunch_groups" + dev_end + "/{groupID}/lunch_locations/{locationId}")
        .onDelete((change, event) => {
            return match_users_and_locations.remove_pref_when_location_deleted(change, event);
        });
    exports[dev_start + "notify_users_of_driver_to_location"] = functions.database
        .ref("/lunch_groups" + dev_end + "/{groupId}/lunch_locations/{locationId}/{whichLunchPreference}/pending_drivers/{driverId}")
        .onWrite((change, event) => {
            return notify_users_of_location.send_notification_of_driver_to_location(change, event);
        });
    exports[dev_start + "weird_beer"] = functions.pubsub.topic(dev_start + "weird-beer")
        .onPublish(event => {
            return notify_weird_beer.send_notification_for_beer(event, "lunch_groups" + dev_end);
        });
    exports["sync_users_and_locations" + dev_end] = functions.https
        .onRequest((req, res) => {
            var result = match_users_and_locations.sync_users_and_locations(req, res, "/lunch_groups" + dev_end);
            res.status(200).send("Done" + dev_end + "\n");
            return result;
        });
}
