// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

exports.send_notification_for_date = function send_notification_for_date(req, res){
    console.error('You called this function');
    var lunchGroup = admin.database().ref('lunch_groups').child('-KqqZZp2_qIcTsvDusBx');
    var groupsLocations = lunchGroup.child('lunch_locations');
    groupsLocations.once('value').then(function(lunchLocationsSnapshot) {

        //console.error('Read Lunch Locations:', lunchLocationsSnapshot.key,' val:', lunchLocationsSnapshot.val());
        lunchLocationsSnapshot.forEach(function(locationSnapshot) {

            //console.error('Read Location:', locationSnapshot.key,' val:', locationSnapshot.val());
            var locationName = locationSnapshot.child('displayName').val();
            locationSnapshot.child('lunch_preference_1').child('users').forEach(function(userKeySnapshot){

                //console.error('Read UserKey:', userKeySnapshot.key,' val:', userKeySnapshot.val());
                lunchGroup.child('users').child(userKeySnapshot.key).once('value').then(function(userSnapshot){

                    //console.error('Read ReadUser:', userSnapshot.key,' val:', userSnapshot.val());
                    var userName = userSnapshot.child('displayName').val();
                    userSnapshot.child('devices').forEach(function(userDeviceSnapshot){
                        console.error('Will notify device:'+ userDeviceSnapshot.key,' val:'+ userDeviceSnapshot.val());
                        var title = 'Lunch Reminder';
                        var body = userName+', you have lunch at '+locationName+' today'
                         const payload = {
                              notification: {
                                title: title,
                                body: body
                              }
                        }
                        console.error('Notifying:',userName,' of lunch at ', locationName);
                        admin.messaging().sendToDevice(userDeviceSnapshot.key, payload);
                    });
                });
            });
        });
    });
    res.status(200).send("Triggering notifications");
}