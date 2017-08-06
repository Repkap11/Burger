// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

//event is Event (in our case a DeltaSnapshot type event
//event.data is DeltaSnapshot

function add_user_to_locations_pref(event){
    //When a user changes their preference for any day, we get a message here
    //console.log('You changed ',event.params.userId, '\'s lunch pref ', event.data.ref.key, ' from ', event.data.previous.val(),' to ', event.data.current.val());
     //Figure out which lunch preference they are no longer picking for that day.

     var val_previous = event.data.previous.val();
     if (val_previous != null){
        //Remove the users id from that location's list of users for that day
        var lunch_location_previous_ref = event.data.ref.parent.parent.parent.child("lunch_locations").child(val_previous);
        lunch_location_previous_ref.child(event.data.ref.key).child('users').child(event.params.userId).remove();
     }
      var val_current = event.data.current.val();
     if (val_current != null){
        //Add the users id to that location's list of users for that day
        var lunch_location_current_ref = event.data.ref.parent.parent.parent.child("lunch_locations").child(val_current);
        lunch_location_current_ref. child(event.data.ref.key).child('users').child(event.params.userId).set('')
    }
    return null;
}