root_key_lunch_groups = 'lunch_groups_dev'

// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

exports.send_notification_for_beer = function send_notification_for_beer(event){
    console.error('You called send_notification_for_beer');
}