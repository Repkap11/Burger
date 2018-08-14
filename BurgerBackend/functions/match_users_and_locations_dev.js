// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require("firebase-functions");

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require("firebase-admin");

//event is Event (in our case a DeltaSnapshot type event
//event.data is DeltaSnapshot

exports.add_user_to_locations_pref = function add_user_to_locations_pref(event) {
  //When a user changes their preference for any day, we get a message here
  console.log("You changed ", event.params.userId, "'s lunch pref ", event.data.ref.key, " from ", event.data.previous.val(), " to ", event.data.current.val());
  //Figure out which lunch preference they are no longer picking for that day.

  var val_previous = event.data.previous.val();
  var allChanges = [];
  if (val_previous != null) {
    //Remove the users id from that location's list of users for that day
    var lunch_location_previous_ref = event.data.ref.parent.parent.parent.child("lunch_locations").child(val_previous);
    allChanges.push(
      lunch_location_previous_ref
        .child(event.data.ref.key)
        .child("users")
        .child(event.params.userId)
        .remove()
    );
  }
  var val_current = event.data.current.val();
  if (val_current != null) {
    //Add the users id to that location's list of users for that day
    var lunch_location_current_ref = event.data.ref.parent.parent.parent.child("lunch_locations").child(val_current);
    allChanges.push(
      lunch_location_current_ref
        .child(event.data.ref.key)
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
  var lunchGroup = event.data.previous.parent.parent;
  return lunchGroup.once("value").then(function(lunchGroupSnapshot) {
    //console.log('Read Lunch Group:'+ lunchGroupSnapshot.key+' val:'+ lunchGroupSnapshot.val());
    //var lunchGroupName = lunchGroupSnapshot.child('displayName').val();
    var allRemoves = [];
    lunchGroupSnapshot.child("users").forEach(function(userSnapshot) {
      //console.log('Read Location:'+ lunchLocationSnapshot.key+' val:'+ lunchLocationSnapshot.val());
      //var userName = userSnapshot.child('displayName').val();
      if (userSnapshot.child("lunch_preference_1").val() == event.params.locationId) {
        allRemoves.push(userSnapshot.child("lunch_preference_1").ref.remove());
      }
      if (userSnapshot.child("lunch_preference_2").val() == event.params.locationId) {
        allRemoves.push(userSnapshot.child("lunch_preference_2").ref.remove());
      }
      if (userSnapshot.child("lunch_preference_3").val() == event.params.locationId) {
        allRemoves.push(userSnapshot.child("lunch_preference_3").ref.remove());
      }
      if (userSnapshot.child("lunch_preference_4").val() == event.params.locationId) {
        allRemoves.push(userSnapshot.child("lunch_preference_4").ref.remove());
      }
      if (userSnapshot.child("lunch_preference_5").val() == event.params.locationId) {
        allRemoves.push(userSnapshot.child("lunch_preference_5").ref.remove());
      }
    });
    return Promise.all(allRemoves);
  });
};

exports.sync_users_and_locations = function(req, res, lunchGroupsKey) {
  var lunchGroups = admin.database().ref(lunchGroupsKey);
  return lunchGroups.once("value").then(function(lunchGroupsSnapshot) {
    //console.log("Clearing Lunch Groups:" + lunchGroupsSnapshot.key + " val:" + lunchGroupsSnapshot.val());
    //var lunchGroupName = lunchGroupSnapshot.child('displayName').val();
    var allRemoves = [];
    lunchGroupsSnapshot.forEach(function(lunchGroupSnapshot) {
      lunchGroupSnapshot.child("lunch_locations").forEach(function(lunchLocationSnapshot) {
        //console.log("Clearing Location:" + lunchLocationSnapshot.key + " val:" + lunchLocationSnapshot.val());
        //var locationName = lunchLocationSnapshot.child('displayName').val();
        for (i = 1; i < 6; i++) {
          allRemoves.push(lunchLocationSnapshot.child("lunch_preference_" + i).ref.remove());
        }
      });
      lunchGroupSnapshot.child("users").forEach(function(userSnapshot) {
        //console.log('Read Location:'+ lunchLocationSnapshot.key+' val:'+ lunchLocationSnapshot.val());
        //var userName = userSnapshot.child('displayName').val();
        for (i = 1; i < 6; i++) {
          var lunchPrefShapshot = userSnapshot.child("lunch_preference_" + i);
          var savedValue = lunchPrefShapshot.val();
          allRemoves.push(lunchPrefShapshot.ref.remove());
          allRemoves.push(lunchPrefShapshot.ref.set(savedValue));
        }
      });
      return Promise.all(allRemoves);
    });
  });
};
