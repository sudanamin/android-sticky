package com.example.ammostafa.stickykeep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

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

   boolean typing =false;
    RecyclerView stickyList;

    private FloatingActionButton yellow;
    private FloatingActionButton blue;
    private FloatingActionButton green;
    private FloatingActionButton red;
    private FloatingActionButton orange;

    private int ColorYellow;
    private int ColorBlue;
    private int ColorGreen;
    private int ColorRed;
    private int ColorOrange;


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.yellow:
                    Util.newSticky(userData,ColorYellow);

                    break;
                case R.id.blue:
                    Util.newSticky(userData,ColorBlue);
                    break;
                case R.id.green:
                    Util.newSticky(userData,ColorGreen);
                    break;
                case R.id.red:
                    Util.newSticky(userData,ColorRed);
                    break;
                case R.id.orange:
                    Util.newSticky(userData,ColorOrange);
                    break;

                case R.id.fab:
                    Util.newSticky(userData,ColorOrange);
                    break;

            }
        }
    };

     public static Long lastTypingTime = Long.valueOf(0);
    CollectionReference userData;

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
        FloatingActionMenu fab =  findViewById(R.id.fab);

        yellow =  findViewById(R.id.yellow);
        blue =  findViewById(R.id.blue);
        green =  findViewById(R.id.green);
        red =  findViewById(R.id.red);
        orange =  findViewById(R.id.orange);

        yellow.setOnClickListener(clickListener);
        blue.setOnClickListener(clickListener);
        green.setOnClickListener(clickListener);
        red.setOnClickListener(clickListener);
        orange.setOnClickListener(clickListener);
orange.show(false);


fab.setOnClickListener(clickListener);
        // Firestore
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        /*
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
                    userData = mFirestore.collection("users").document(user.getUid()).collection("userData");

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



       FirestoreRecyclerOptions<StickyClass> response = new FirestoreRecyclerOptions.Builder<StickyClass>()
               .setQuery(query, StickyClass.class)
               .build();

       adapter = new FirestoreRecyclerAdapter<StickyClass, FireHolder>(response) {

           @Override
           public void onChildChanged(@NonNull ChangeEventType type,
                                      @NonNull DocumentSnapshot snapshot,
                                      int newIndex,
                                      int oldIndex) {
               switch (type) {
                   case ADDED:
                       notifyItemInserted(newIndex);
                       stickyList.scrollToPosition(newIndex);
                       break;
                   case CHANGED:
                     //  notifyItemChanged(newIndex);
                       break;
                   case REMOVED:
                       notifyItemRemoved(oldIndex);
                       break;
                   case MOVED:
                       notifyItemMoved(oldIndex, newIndex);
                       break;
                   default:
                       throw new IllegalStateException("Incomplete case statement");
               }
           }

           @Override
           public void onBindViewHolder(FireHolder holder, int position, StickyClass model) {
               progressBar.setVisibility(View.GONE);
               String ss = model.getsdata();
           //   if(holder.dataView.getText().toString().length() == 0) {
                   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                       holder.dataView.setText(Html.fromHtml(ss, Html.FROM_HTML_MODE_LEGACY));
                   } else {
                       holder.dataView.setText(Html.fromHtml(ss));
                   }
           //    }
     //    holder.dataView.setSelection(null);

               String docId = getSnapshots().getSnapshot(position).getId();

               Log.d("GETREFTEST", docId);
               System.out.println("modelllll. postion id "+position);


               Log.d("GETREFTEST", docId);
               System.out.println("modelllll.getididididididididid"+docId);

          }
           @Override
           public FireHolder onCreateViewHolder(ViewGroup group, int i) {
               View view = LayoutInflater.from(group.getContext())
                       .inflate(R.layout.sticky_ticket, group, false);

               FireHolder viewHolder = new FireHolder(view);

               viewHolder.setOnClickListener(new FireHolder.ClickListener() {

                   @Override
                   public void onItemClick(View view, int position,ClickEventType type) {
                       String docId = getSnapshots().getSnapshot(position).getId();
                       DocumentReference  docRef = mFirestore.collection("users").document(user.getUid()).collection("userData").document(docId);

                       switch (type) {
                           case ADD:
                               Util.newSticky(userData,121212);

                               Toast.makeText(view.getContext(), "Item was Added  " + docId, Toast.LENGTH_SHORT).show();
                               break;
                           case DELETE:
                               Util.deleteSticky(view.getContext(),docRef);

                               Toast.makeText(view.getContext(), "Item was deleted  " + docId, Toast.LENGTH_SHORT).show();
                               break;

                           default:
                               throw new IllegalStateException("Incomplete case statement");
                       }

                   }

                   @Override
                   public void onItemLongClick(View view, int position,ClickEventType type) {
                       Toast.makeText(view.getContext(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                   }
               });

               viewHolder.setOnTextChangeListener(new FireHolder.TextChangeListener() {
                                                      @Override
                                                      public void onTextChanged(CharSequence BDBDBD, int position) {
                                                       /*   */
                                                        //  lastTypingTime = Calendar.getInstance().getTimeInMillis();

                                                      }

                                                      @Override
                                                      public void afterTextChanged(Editable editable, int position) {
                                                         String docId = getSnapshots().getSnapshot(position).getId();
                                                         String stickyData = editable.toString();

                                                        String stickyDataHtml =  Html.toHtml(new SpannableString(stickyData));



                                                          DocumentReference docRef = mFirestore.collection("users").document(user.getUid()).collection("userData").document(docId);

                                                      /*    lastTypingTime = Calendar.getInstance().getTimeInMillis();


                                                               asynTask updateAsync = new asynTask(docRef, stickyData);
                                                               updateAsync.execute();*/





                                                          docRef.update("sdata", stickyDataHtml)
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
                                                  }
               );
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
                this.finish();
            }
        }
    }

}
