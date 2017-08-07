// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

exports.send_notification_for_date = function send_notification_for_date(req, res){
    console.error('You called this function');
    var lunchGroups = admin.database().ref('lunch_groups');
    lunchGroups.once('value').then(function(lunchGroupsSnapshot){

        lunchGroupsSnapshot.forEach(function(lunchGroupSnapshot){

           console.error('Read Lunch Group:'+ lunchGroupSnapshot.key+' val:'+ lunchGroupSnapshot.val());
           var lunchGroupName = lunchGroupSnapshot.child('displayName').val();
           lunchGroupSnapshot.child('lunch_locations').forEach(function(lunchLocationSnapshot) {

                console.error('Read Location:'+ lunchLocationSnapshot.key+' val:'+ lunchLocationSnapshot.val());
                var locationName = lunchLocationSnapshot.child('displayName').val();
                lunchLocationSnapshot.child('lunch_preference_1').child('users').forEach(function(userKeySnapshot){

                    console.error('Read UserKey:'+ userKeySnapshot.key+' val:'+ userKeySnapshot.val());
                    var userSnapshot = lunchGroupSnapshot.child('users').child(userKeySnapshot.key);

                    console.error('Read ReadUser:'+ userSnapshot.key+' val:'+ userSnapshot.val());
                    var userName = userSnapshot.child('displayName').val();
                    userSnapshot.child('devices').forEach(function(userDeviceSnapshot){
                        console.error('Will notify device:'+ userDeviceSnapshot.key);
                        var title = 'Lunch Reminder (' +lunchGroupName+')';
                        var body = userName+', you have lunch at '+locationName+' today'
                         const payload = {
                              data: {
                                title: title,
                                body: body,
                              }
                        }
                        console.error('Notifying:'+userName+' of lunch at '+ locationName);
                        admin.messaging().sendToDevice(userDeviceSnapshot.key, payload);
                    });
                });


            });
        });
     });
    res.status(200).send("Triggering notifications");
}