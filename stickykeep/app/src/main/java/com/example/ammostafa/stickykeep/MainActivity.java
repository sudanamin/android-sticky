package com.example.ammostafa.stickykeep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    //@BindView(R.id.progress_bar)
    //ProgressBar progressBar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    FirebaseUser user;


    RecyclerView stickyList;

    //@BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    private void init(){
      //  linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
      //  stickyList.setLayoutManager(linearLayoutManager);
      //  db = FirebaseFirestore.getInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progress_bar);
        setSupportActionBar(toolbar);
        stickyList = findViewById(R.id.sticky_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Map<String, Object> newSticky = new HashMap<>();

                    newSticky.put("sdata"," ");
                    newSticky.put("top",10);
                    newSticky.put("left",15);
                    newSticky.put("Scolor","ffffff");



                CollectionReference userData = mFirestore.collection("users").document(user.getUid()).collection("userData");
                userData.add(newSticky)
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

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Firestore
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

    /*    mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, null);
                mMessagesDatabaseReference.push().setValue(friendlyMessage);

                // Clear input box
                mMessageEditText.setText("");
            }
        });*/


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.FacebookBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume(){

        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    //    mMessageAdapter.clear();
     //   detachDatabaseReadListener();
    }

    @Override
    protected void onStart(){
        super.onStart();


    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();

    }
    private void onSignedInInitialize(String username) {
        mUsername = username;
        attachDatabaseReadListener();
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
      //  mMessageAdapter.clear();
     //   detachDatabaseReadListener();
    }

   private void attachDatabaseReadListener() {


       DocumentReference  usersRef = mFirestore.collection("users").document(user.getUid());
     //  System.out.println("user is aaaaaaaaaaaaaaaaaaaaaaaaaaa "+user.getUid());
       //CollectionReference query;
       Query query = usersRef.collection("userData");
       //Query query = FirebaseFirestore.getInstance().collection("users/"+user.getUid()+"/userData");
    //   System.out.println("hhhhhhhhhhhh"+"hhhhhhhhhh");

            /*   query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                             @Override
                                             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                 if (task.isSuccessful()) {
                                                     for (DocumentSnapshot document : task.getResult()) {
                                                         StickyClass sc = document.toObject(StickyClass.class);
                                                        // StickyClass sc = new Gson().fromJson(document.getData().toString(), StickyClass.class);
                                                        // sc.getData();
                                                         System.out.println("sc get data ddddd "+ sc.getdata() + " => " + document.getData());
                                                     }
                                                 } else {
                                                     System.out.println("could errorrrrrr"+ "Error getting documents: "+ task.getException());
                                                 }
                                             }
                                         });*/


       FirestoreRecyclerOptions<StickyClass> response = new FirestoreRecyclerOptions.Builder<StickyClass>()
               .setQuery(query, StickyClass.class)
               .build();

       adapter = new FirestoreRecyclerAdapter<StickyClass, FireHolder>(response) {



           @Override
           public void onBindViewHolder(FireHolder holder, int position, StickyClass model) {
               progressBar.setVisibility(View.GONE);
               holder.dataView.setText(model.getsdata());
               String docId = getSnapshots().getSnapshot(position).getId();

               Log.d("GETREFTEST", docId);
               System.out.println("modelllll. postion id "+position);
           //    holder.stickyClose.setOnClickListener((new View.OnClickListener() {

            //       public void onClick(View v) {
                       //v.getId() will give you the image id
                    /*   String.valueOf(getAdapterPosition())
                       String docId = getSnapshots().getSnapshot(position).getId();
                       deleteSticky( docId);*/
           //          System.out.println("modelllll.view id"+v.getId());
           //          Toast.makeText(v.getContext(), "Sign in mmmmmm", Toast.LENGTH_SHORT).show();


           //      }
          //     }));
             //  String docId = getSnapshots().getSnapshot(position).getId();

               Log.d("GETREFTEST", docId);
               System.out.println("modelllll.getididididididididid"+docId);
               // holder.textTitle.setText(model.getTitle());
               // holder.textCompany.setText(model.getCompany());
               /* Glide.with(getApplicationContext())
                        .load(model.getImage())
                        .into(holder.imageView);
                holder.itemView.setOnClickListener(v -> {
                    Snackbar.make(friendList, model.getName()+", "+model.getTitle()+" at "+model.getCompany(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                });*/
          }
           @Override
           public FireHolder onCreateViewHolder(ViewGroup group, int i) {
               View view = LayoutInflater.from(group.getContext())
                       .inflate(R.layout.sticky_ticket, group, false);

               FireHolder viewHolder = new FireHolder(view);

               viewHolder.setOnClickListener(new FireHolder.ClickListener() {
                   @Override
                   public void onItemClick(View view, int position) {
                       String docId = getSnapshots().getSnapshot(position).getId();

                       Toast.makeText(view.getContext(), "Item clicked at my doc id by amin is  " + docId, Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onItemLongClick(View view, int position) {
                       Toast.makeText(view.getContext(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                   }
               });
               return  viewHolder;
           }
           @Override
           public void onError(FirebaseFirestoreException e) {
               Log.e("error", e.getMessage());
           }
       };
       adapter.startListening();
       adapter.notifyDataSetChanged();
       linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
       stickyList.setLayoutManager(linearLayoutManager);
       stickyList.setAdapter(adapter);
   }


  /*
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    mMessageAdapter.add(friendlyMessage);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }*/


  /*  private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void deleteSticky(View v )
    {
        Toast.makeText(this, "delete complete for doc id : ", Toast.LENGTH_SHORT).show();
       // Toast.makeText(v.getContext(), "Sign in canceled", Toast.LENGTH_SHORT).show();
  /*      db.collection("cities").document("DC")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
*/
    }
}
