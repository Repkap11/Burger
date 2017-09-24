root_key_lunch_groups = 'lunch_groups_dev'

// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

exports.send_notification_for_beer = function send_notification_for_beer(event){
    console.error('You called send_notification_for_beer');
    var lunchGroups = admin.database().ref(root_key_lunch_groups);
    lunchGroups.once('value').then(function(lunchGroupsSnapshot){

        lunchGroupsSnapshot.forEach(function(lunchGroupSnapshot){

            var groupName = lunchGroupSnapshot.child('displayName').val();
            var groupHasWeirdBeer = lunchGroupSnapshot.child('hasWeirdBeer').val();
            //console.error('Read Lunch Group:'+ groupName+' hasWeirdBeer:'+groupHasWeirdBeer);
            if (groupHasWeirdBeer){
                lunchGroupSnapshot.child('users').forEach(function(userSnapshot){
                    userSnapshot.child('devices').forEach(function(userDeviceSnapshot){
                    var userName = userSnapshot.child('displayName').val();
                    var title = 'Weird Beer Reminder (' +groupName+')';
                    var body = 'Weird Beer is starting soon. Don\'t miss out!';
                     const payload = {
                          data: {
                            title: title,
                            body: body,
                          }
                    }
                    console.log('Notifying:'+userName+' of weird beer');
                    admin.messaging().sendToDevice(userDeviceSnapshot.key, payload);
                });
            });
            }
       });
    });
}