// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


var match_users_and_locations = require('./match_users_and_locations');

exports.add_user_to_locations_pref1 = functions.database.ref('/lunch_groups/{groupID}/users/{userId}/lunch_preference_1')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref2 = functions.database.ref('/lunch_groups/{groupID}/users/{userId}/lunch_preference_2')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref3 = functions.database.ref('/lunch_groups/{groupID}/users/{userId}/lunch_preference_3')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref4 = functions.database.ref('/lunch_groups/{groupID}/users/{userId}/lunch_preference_4')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref5 = functions.database.ref('/lunch_groups/{groupID}/users/{userId}/lunch_preference_5')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});

var notify_users_of_location = require('./notify_users_of_location');
exports.test = functions.https.onRequest((req, res) => {
    return notify_users_of_location.send_notification_for_date(req, res);
});
