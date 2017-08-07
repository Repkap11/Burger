// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

//event is Event (in our case a DeltaSnapshot type event
//event.data is DeltaSnapshot

exports.daily_job = function daily_job(event, dayNumber){
    console.error("This job is ran in cron every hour!");
 }