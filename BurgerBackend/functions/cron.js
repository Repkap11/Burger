// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

//event is Event (in our case a DeltaSnapshot type event
//event.data is DeltaSnapshot

exports.add_user_to_locations_pref = function add_user_to_locations_pref(event){
}

var functions = require('firebase-functions');

exports.hourly_job =
  functions.pubsub.topic('hourly-tick').onPublish((event) => {
    console.log("This job is ran every hour!")
  });