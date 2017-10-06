// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


var match_users_and_locations = require('./match_users_and_locations');
exports.add_user_to_locations_pref1 = functions.database.ref('/lunch_groups/{groupId}/users/{userId}/lunch_preference_1')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref2 = functions.database.ref('/lunch_groups/{groupId}/users/{userId}/lunch_preference_2')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref3 = functions.database.ref('/lunch_groups/{groupId}/users/{userId}/lunch_preference_3')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref4 = functions.database.ref('/lunch_groups/{groupId}/users/{userId}/lunch_preference_4')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref5 = functions.database.ref('/lunch_groups/{groupId}/users/{userId}/lunch_preference_5')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});

exports.remove_pref_when_location_deleted = functions.database.ref('/lunch_groups/{groupID}/lunch_locations/{locationId}')
    .onDelete(event => {return match_users_and_locations.remove_pref_when_location_deleted(event)});

var notify_users_of_location = require('./notify_users_of_location');
exports.lunch_tick_1 = functions.pubsub.topic('lunch-tick-1')
    .onPublish((event) => {return notify_users_of_location.send_notification_for_date(event, 1, 'lunch_groups')});
exports.lunch_tick_2 = functions.pubsub.topic('lunch-tick-2')
    .onPublish((event) => {return notify_users_of_location.send_notification_for_date(event, 2, 'lunch_groups')});
exports.lunch_tick_3 = functions.pubsub.topic('lunch-tick-3')
    .onPublish((event) => {return notify_users_of_location.send_notification_for_date(event, 3, 'lunch_groups')});
exports.lunch_tick_4 = functions.pubsub.topic('lunch-tick-4')
    .onPublish((event) => {return notify_users_of_location.send_notification_for_date(event, 4, 'lunch_groups')});
exports.lunch_tick_5 = functions.pubsub.topic('lunch-tick-5')
    .onPublish((event) => {return notify_users_of_location.send_notification_for_date(event, 5, 'lunch_groups')});

exports.notify_users_of_driver_to_location = functions.database.ref('/lunch_groups/{groupId}/lunch_locations/{locationId}/{whichLunchPreference}/pending_drivers/{driverId}')
    .onWrite((event) => {return notify_users_of_location.send_notification_of_driver_to_location(event)});

var notify_weird_beer = require('./notify_weird_beer');
exports.weird_beer = functions.pubsub.topic('weird-beer')
    .onPublish((event) => {return notify_weird_beer.send_notification_for_beer(event, 'lunch_groups')});




//Start DEV

var match_users_and_locations = require('./match_users_and_locations_dev');
exports.add_user_to_locations_pref1_dev = functions.database.ref('/lunch_groups_dev/{groupId}/users/{userId}/lunch_preference_1')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref2_dev = functions.database.ref('/lunch_groups_dev/{groupId}/users/{userId}/lunch_preference_2')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref3_dev = functions.database.ref('/lunch_groups_dev/{groupId}/users/{userId}/lunch_preference_3')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref4_dev = functions.database.ref('/lunch_groups_dev/{groupId}/users/{userId}/lunch_preference_4')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref5_dev = functions.database.ref('/lunch_groups_dev/{groupId}/users/{userId}/lunch_preference_5')
    .onWrite(event => {return match_users_and_locations.add_user_to_locations_pref(event)});

exports.remove_pref_when_location_deleted = functions.database.ref('/lunch_groups_dev/{groupID}/lunch_locations/{locationId}')
    .onDelete(event => {return match_users_and_locations.remove_pref_when_location_deleted(event)});

var notify_users_of_location = require('./notify_users_of_location_dev');
exports.dev_lunch_tick_1 = functions.pubsub.topic('dev_lunch-tick-1')
    .onPublish((event) => {return notify_users_of_location.send_notification_for_date(event, 1, 'lunch_groups_dev')});
exports.dev_lunch_tick_2 = functions.pubsub.topic('dev_lunch-tick-2')
    .onPublish((event) => {return notify_users_of_location.send_notification_for_date(event, 2, 'lunch_groups_dev')});
exports.dev_lunch_tick_3 = functions.pubsub.topic('dev_lunch-tick-3')
    .onPublish((event) => {return notify_users_of_location.send_notification_for_date(event, 3, 'lunch_groups_dev')});
exports.dev_lunch_tick_4 = functions.pubsub.topic('dev_lunch-tick-4')
    .onPublish((event) => {return notify_users_of_location.send_notification_for_date(event, 4, 'lunch_groups_dev')});
exports.dev_lunch_tick_5 = functions.pubsub.topic('dev_lunch-tick-5')
    .onPublish((event) => {return notify_users_of_location.send_notification_for_date(event, 5, 'lunch_groups_dev')});

exports.dev_notify_users_of_driver_to_location = functions.database.ref('/lunch_groups_dev/{groupId}/lunch_locations/{locationId}/{whichLunchPreference}/pending_drivers/{driverId}')
    .onWrite((event) => {return notify_users_of_location.send_notification_of_driver_to_location(event)});

var notify_weird_beer = require('./notify_weird_beer_dev');
exports.dev_weird_beer = functions.pubsub.topic('dev_weird-beer')
    .onPublish((event) => {return notify_weird_beer.send_notification_for_beer(event, 'lunch_groups_dev')});