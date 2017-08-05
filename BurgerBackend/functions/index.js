// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.add_user_to_locations_pref1 = functions.database.ref('/users/{userId}/lunch_preference_1')
    .onWrite(event => {return add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref2 = functions.database.ref('/users/{userId}/lunch_preference_2')
    .onWrite(event => {return add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref3 = functions.database.ref('/users/{userId}/lunch_preference_3')
    .onWrite(event => {return add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref4 = functions.database.ref('/users/{userId}/lunch_preference_4')
    .onWrite(event => {return add_user_to_locations_pref(event)});
exports.add_user_to_locations_pref5 = functions.database.ref('/users/{userId}/lunch_preference_5')
    .onWrite(event => {return add_user_to_locations_pref(event)});

function add_user_to_locations_pref(event){
    // Grab the current value of what was written to the Realtime Database.
    console.log('You changed ',event.params.userId, '\'s lunch pref ', event.data.ref.key, ' lunch to ', event.data.val());
    var lunch_location_ref = event.data.ref.parent.parent.parent.child("lunch_locations").child(event.data.val());
    lunch_location_ref.child(event.data.ref.key).child("users").push(event.params.userId);
    return null;
}

//    lunch_location_ref.once('value').then(function(dataSnapshot) {
//        // handle read data.
//        console.log('You changed lunch pref ', event.data.ref.key, pref_number, ' lunch to ', dataSnapshot.val().displayName);
//
//     });