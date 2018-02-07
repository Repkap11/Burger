// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

exports.send_notification_for_date = function send_notification_for_date(event, dayOfWeek, root_key_lunch_groups){
    //console.error('You called this function');
    var lunchGroups = admin.database().ref(root_key_lunch_groups);
    return lunchGroups.once('value').then(function(lunchGroupsSnapshot){

        lunchGroupsSnapshot.forEach(function(lunchGroupSnapshot){

           //console.error('Read Lunch Group:'+ lunchGroupSnapshot.key+' val:'+ lunchGroupSnapshot.val());
           var lunchGroupName = lunchGroupSnapshot.child('displayName').val();
           lunchGroupSnapshot.child('lunch_locations').forEach(function(lunchLocationSnapshot) {

                //console.error('Read Location:'+ lunchLocationSnapshot.key+' val:'+ lunchLocationSnapshot.val());
                var locationName = lunchLocationSnapshot.child('displayName').val();
                lunchLocationSnapshot.child('lunch_preference_'+dayOfWeek).child('users').forEach(function(userKeySnapshot){

                    //console.error('Read UserKey:'+ userKeySnapshot.key+' val:'+ userKeySnapshot.val());
                    var userSnapshot = lunchGroupSnapshot.child('users').child(userKeySnapshot.key);

                    //console.error('Read ReadUser:'+ userSnapshot.key+' val:'+ userSnapshot.val());
                    var userName = userSnapshot.child('displayName').val();
                    userSnapshot.child('devices').forEach(function(userDeviceSnapshot){
                        //console.error('Will notify device:'+ userDeviceSnapshot.key);
                        var title = 'Lunch Reminder (' +lunchGroupName+')';
                        var body = userName+', you have lunch at '+locationName+' today'
                         const payload = {
                              data: {
                                title: title,
                                body: body,
                              }
                        }
                        console.log('Notifying:'+userName+' of lunch at '+ locationName);
                        admin.messaging().sendToDevice(userDeviceSnapshot.key, payload);
                    });
                });
            });
        });
    });
}

exports.send_notification_of_driver_to_location = function send_notification_of_driver_to_location(event){
    //console.error('You called this function send_notification_of_driver_to_location' + event.data.current.val());
    if (event.data.current.val() != null){
        //Notify the group of users that one of their members is driving

        //Find information about the location
        var location = event.data.ref.parent.parent.parent.child("displayName");
        return location.once('value').then(function(locationNameSnapshot){
            var locationName = locationNameSnapshot.val();

            //console.error('Seeing location:'+locationName);

            //Find information about the driver
            var driver = event.data.ref.parent.parent.parent.parent.parent.child("users").child(event.data.current.key);
            return driver.once('value').then(function(driverSnapshot){
                var driverName = driverSnapshot.child('displayName').val();
                var driverCarSize = driverSnapshot.child('carSizeNum').val();
                var numGuests = driverCarSize - 1;
                if (numGuests > 0){
                    //console.error('Driver is:'+ driverName +' with car size:'+driverCarSize);
                    //This is the group of users which that member is driving for
                    var lunch_pref_group = event.data.ref.parent.parent.child("users");
                    //console.error("Searching users:"+lunch_pref_group.key + ":"+lunch_pref_group);
                    return lunch_pref_group.once('value').then(function(lunchPrefUsersSnapshot){

                        var numUsersNotified = lunchPrefUsersSnapshot.numChildren() - 1;
                        //console.log('numChildren:'+lunchPrefUsersSnapshot.numChildren()+' numUsersNotified:'+numUsersNotified);
                        var allUserPrefsPromise = [];
                        lunchPrefUsersSnapshot.forEach(function(lunchPrefUserSnapshot){
                            //console.error('I will be notifying:'+lunchPrefUserSnapshot.key);
                            //Get the list of all users.
                            var user_to_be_notified = event.data.ref.parent.parent.parent.parent.parent.child("users").child(lunchPrefUserSnapshot.key);
                            allUserPrefsPromise.push(user_to_be_notified.once('value').then(function(userSnapshot){

                                var allNotificationsPromise = [];
                                var userName = userSnapshot.child('displayName').val();
                                if (driver.key == userSnapshot.key){
                                    //console.error('Not notifying '+userName+' since they are the driver.');
                                } else {
                                    userSnapshot.child('devices').forEach(function(userDeviceSnapshot){
                                        //console.error('Will notify device:'+ userDeviceSnapshot.key);
                                        var title = 'Lunch at ' +locationName;
                                        var body = driverName+' is leaving and can take '+ numGuests +' extra people.'
                                         const payload = {
                                              data: {
                                                title: title,
                                                body: body,
                                              }
                                        }
                                        allNotificationsPromise.push(admin.messaging().sendToDevice(userDeviceSnapshot.key, payload));
                                    });
                                }
                            }));
                        });
                        console.error('Notifying '+driverName+' that there are '+ numUsersNotified +' people who might want to go.');
                        var allDevicesPromise = [];
                        driverSnapshot.child('devices').forEach(function(userDeviceSnapshot){
                            console.error('Will notify device:'+ userDeviceSnapshot.key);
                            var title = 'Lunch at ' +locationName;
                            var peopleString = numUsersNotified == 1 ? 'person' : 'people';
                            var body = 'Notifying '+numUsersNotified+' '+peopleString +' you\'re driving to '+locationName;
                             const payload = {
                                  data: {
                                    title: title,
                                    body: body,
                                  }
                            }
                            allDevicesPromise.push(admin.messaging().sendToDevice(userDeviceSnapshot.key, payload));
                            //Remove this pending driver
                            allDevicesPromise.push(event.data.ref.remove());
                        });
                        return Promise.all([Promise.all(allDevicesPromise),Promise.all(allUserPrefsPromise)]);
                    });
                } else {
                    console.error('Driver is:'+ driverName +' with car size:'+driverCarSize+', so theres no room for other people');
                    var allDriverPromises = [];
                    driverSnapshot.child('devices').forEach(function(userDeviceSnapshot){
                        console.error('Will notify device:'+ userDeviceSnapshot.key);
                        var title = 'Lunch at ' +locationName;
                        var body = 'Not notifying anyone. Get a bigger car!';
                         const payload = {
                              data: {
                                title: title,
                                body: body,
                              }
                        }
                        allDriverPromises.push(admin.messaging().sendToDevice(userDeviceSnapshot.key, payload));
                        //Remove this pending driver
                        allDriverPromises.push(event.data.ref.remove());
                    });
                    return Promise.all(allDriverPromises);
                }
            });
        });
    }
}


