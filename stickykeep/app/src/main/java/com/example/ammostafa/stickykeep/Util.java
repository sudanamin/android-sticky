package com.example.ammostafa.stickykeep;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

//import android.support.design.widget.Snackbar;

/**
 * Created by Aminov on 2/23/2018.
 */
public final class Util {
    // Example Utility method

  //  FloatingActionMenu menuGreen;
    public static void deleteSticky(Context context, DocumentReference docReferece) {
        DocumentReference docRef = docReferece;

        docRef
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("deleting : ", "DocumentSnapshot successfully deleted!");
                       // Toast.makeText(context, "Item clicked at my doc id by amin is  " , Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deleting : ", "Error deleting document", e);
                    }
                });
    }

    public static void newSticky( CollectionReference collectionReference,int color) {

        Map<String, Object> newSticky = new HashMap<>();

        newSticky.put("sdata"," ");
        newSticky.put("top",10);
        newSticky.put("left",15);
        newSticky.put("Scolor","ffffff");


       // userData = mFirestore.collection("users").document(user.getUid()).collection("userData");
        collectionReference.add(newSticky)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("add new sticky", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("error to new stky", "Error adding document", e);
                    }
                });


    }


}
