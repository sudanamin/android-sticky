package com.example.ammostafa.stickykeep;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;

import static com.example.ammostafa.stickykeep.MainActivity.lastTypingTime;

/**
 * Created by Aminov on 2/23/2018.
 */

public class asynTask extends AsyncTask<String, Void,Void>
{
    String data;
    DocumentReference docRef;


    asynTask(DocumentReference docRef , String data) {
        // list all the parameters like in normal class define
       this.docRef = docRef;
       this.data = data;
    }




    @Override
    protected Void doInBackground(String... strings) {

        try {
            Thread.sleep(5000);
            Long typingTime =  Calendar.getInstance().getTimeInMillis();
            long timeDiff = typingTime  - lastTypingTime;

            if (timeDiff >= 5000) {
                System.out.println("hi from data : "+this.data+" time diff = "+timeDiff +" last time : "+lastTypingTime+ " typing time : "+typingTime);
                this.docRef.update("sdata", this.data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("update sdata", "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("update sdata", "Error updating document", e);
                            }
                        });
            }
        }catch (InterruptedException e) {
        }
        return null;
    }
}

