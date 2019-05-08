// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require("firebase-admin");

//event is Event (in our case a DeltaSnapshot type event
//change.after is DeltaSnapshot

exports.add_user_to_locations_pref = function add_user_to_locations_pref(change, event) {
    //When a user changes their preference for any day, we get a message here
    console.log("You changed ", event.params.userId, "'s lunch pref ", change.after.ref.key, " from ", change.before.val(), " to ", change.after.val());
    //Figure out which lunch preference they are no longer picking for that day.

    var val_previous = change.before.val();
    var allChanges = [];
    if (val_previous !== null) {
    //Remove the users id from that location's list of users for that day
        var lunch_location_previous_ref = change.before.ref.parent.parent.parent.child("lunch_locations").child(val_previous);
        allChanges.push(
            lunch_location_previous_ref
                .child(change.before.ref.key)
                .child("users")
                .child(event.params.userId)
                .remove()
        );
    }
    var val_current = change.after.val();
    if (val_current !== null) {
    //Add the users id to that location's list of users for that day
        var lunch_location_current_ref = change.before.ref.parent.parent.parent.child("lunch_locations").child(val_current);
        allChanges.push(
            lunch_location_current_ref
                .child(change.before.ref.key)
                .child("users")
                .child(event.params.userId)
                .set("")
        );
    }
    return Promise.all(allChanges);
};

exports.remove_pref_when_location_deleted = function remove_pref_when_location_deleted(event) {
    //When a location is deleted, delete that location from any users perference
    console.log("You deleted location" + event.params.locationId + " from group " + event.params.groupID);

    //var lunchGroup = admin.database().ref(root_key_lunch_groups).child(event.params.groupID);
    var lunchGroup = change.before.parent.parent;
    return lunchGroup.once("value").then(lunchGroupSnapshot => {
    //console.log('Read Lunch Group:'+ lunchGroupSnapshot.key+' val:'+ lunchGroupSnapshot.val());
    //var lunchGroupName = lunchGroupSnapshot.child('displayName').val();
        var allRemoves = [];
        lunchGroupSnapshot.child("users").forEach(userSnapshot => {
            //console.log('Read Location:'+ lunchLocationSnapshot.key+' val:'+ lunchLocationSnapshot.val());
            //var userName = userSnapshot.child('displayName').val();
            if (userSnapshot.child("lunch_preference_1").val() === event.params.locationId) {
                allRemoves.push(userSnapshot.child("lunch_preference_1").ref.remove());
            }
            if (userSnapshot.child("lunch_preference_2").val() === event.params.locationId) {
                allRemoves.push(userSnapshot.child("lunch_preference_2").ref.remove());
            }
            if (userSnapshot.child("lunch_preference_3").val() === event.params.locationId) {
                allRemoves.push(userSnapshot.child("lunch_preference_3").ref.remove());
            }
            if (userSnapshot.child("lunch_preference_4").val() === event.params.locationId) {
                allRemoves.push(userSnapshot.child("lunch_preference_4").ref.remove());
            }
            if (userSnapshot.child("lunch_preference_5").val() === event.params.locationId) {
                allRemoves.push(userSnapshot.child("lunch_preference_5").ref.remove());
            }
        });
        return Promise.all(allRemoves);
    });
};

exports.sync_users_and_locations = function(req, res, lunchGroupsKey) {
    var lunchGroups = admin.database().ref(lunchGroupsKey);
    return lunchGroups.once("value").then(lunchGroupsSnapshot => {
    //console.log("Clearing Lunch Groups:" + lunchGroupsSnapshot.key + " val:" + lunchGroupsSnapshot.val());
    //var lunchGroupName = lunchGroupSnapshot.child('displayName').val();
        var allRemoves = [];
        lunchGroupsSnapshot.forEach(lunchGroupSnapshot => {
            var allLocationRemoves = [];
            lunchGroupSnapshot.child("lunch_locations").forEach(lunchLocationSnapshot => {
                //console.log("Clearing Location:" + lunchLocationSnapshot.key + " val:" + lunchLocationSnapshot.val());
                //var locationName = lunchLocationSnapshot.child('displayName').val();
                for (var i = 1; i < 6; i++) {
                    allLocationRemoves.push(lunchLocationSnapshot.child("lunch_preference_" + i).ref.remove());
                }
            });

            allRemoves.push(
                Promise.all(allLocationRemoves).then(() => {
                    console.log("All removes done");
                    allUserRemoves = [];
                    lunchGroupSnapshot.child("users").forEach(userSnapshot => {
                        //console.log('Read Location:'+ lunchLocationSnapshot.key+' val:'+ lunchLocationSnapshot.val());
                        //var userName = userSnapshot.child('displayName').val();
                        for (var i = 1; i < 6; i++) {
                            const lunchPrefShapshot = userSnapshot.child("lunch_preference_" + i);
                            const savedValue = lunchPrefShapshot.val();
                            if (savedValue !== null) {
                                console.log("Removing a value:" + savedValue);
                                allUserRemoves.push(
                                    lunchPrefShapshot.ref.remove().then(() => {
                                        console.log("Adding back a value:" + savedValue);
                                        return lunchPrefShapshot.ref.set(savedValue);
                                    })
                                );
                            }
                        }
                    });
                    console.error("All add backs done");
                    return Promise.all(allUserRemoves);
                })
            );
        });
        return Promise.all(allRemoves);
    });
};
