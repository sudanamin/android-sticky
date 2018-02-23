package com.example.ammostafa.stickykeep;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

/**
 * Created by Aminov on 2/23/2018.
 */
public final class Util {
    // Example Utility method
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
}
