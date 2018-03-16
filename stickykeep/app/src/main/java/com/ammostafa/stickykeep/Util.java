package com.ammostafa.stickykeep;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

//import android.support.design.widget.Snackbar;

/**
 * Created by Aminov on 2/23/2018.
 */
public final class Util {
    // Example Utility method


    public static  int statusCode ;
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

    public static void newSticky( CollectionReference collectionReference,String color) {

        Map<String, Object> newSticky = new HashMap<>();

        newSticky.put("sdata"," ");
        newSticky.put("top",10);
        newSticky.put("left",15);

        newSticky.put("Scolor",color);
        newSticky.put("timestamp", FieldValue.serverTimestamp());


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
public static Boolean  internetConnectivity(Context context ) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = cm.getActiveNetworkInfo();
    return  (info != null && info.isConnected());

}



    public static String   RGBtoString(String rgb)
    {

        String[] parts = rgb.split(",");
        System.out.println("parts one is : "+ parts[1]);
        switch ( parts[0]){
            case "rgb(218": return "#DA55C6";
            //break;

            case "rgb(146":  return "#92FF8A";
            // break;

            case "rgb(101":  return "#6596F7";
            //  break;

            case "rgb(255": if (parts[1].contains("255")) return  "#ffff8a"; else return "#ff8534";


        }
        return "#ffffff";

    }
}
