// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
exports.addMessage = functions.https.onRequest((req, res) => {
    // Grab the text parameter.
    const original = req.query.text;
    // Push the new message into the Realtime Database using the Firebase Admin SDK.
    admin.database().ref('/messages').push("Test value");
});

exports.group_users_with_locations = functions.database.ref('/users/{userId}/lunch_preference_1')
    .onWrite(event => {return group_users_with_locations(event, 1)});

function group_users_with_locations(event, pref_number){
    // Grab the current value of what was written to the Realtime Database.
    console.log('You changed ',event.params.userId, '\'s lunch pref ', event.data.ref.key, pref_number, ' lunch to ', event.data.val());
    //event.data.ref
    // You must return a Promise when performing asynchronous tasks inside a Functions such as
    // writing to the Firebase Realtime Database.
    // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
    //return event.data.ref.parent.child('uppercase').set(uppercase);
    return null;
}