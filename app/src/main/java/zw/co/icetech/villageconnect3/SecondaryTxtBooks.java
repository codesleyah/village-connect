package zw.co.icetech.villageconnect3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SecondaryTxtBooks extends AppCompatActivity implements SecondaryBookInterface{

    EditText SecondaryBooksSearch;

    private RecyclerView recyclerView;
    final String KEY_RECYCLER_STATE = "recycler_state";
    static Bundle recyclerviewState;
    SecondaryBookAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_txt_books);

        SecondaryBooksSearch =findViewById(R.id.SecondaryBooksSearch);

        // Create a instance of the database and get
        // its reference
        mbase = FirebaseDatabase.getInstance().getReference().child("SeconderyBooks");
        recyclerView = findViewById(R.id.SecondaryBooksRecyclerview);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<SecondaryBooksModel> options
                = new FirebaseRecyclerOptions.Builder<SecondaryBooksModel>()
                .setQuery(mbase, SecondaryBooksModel.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new SecondaryBookAdapter(options, this);
        recyclerView.setAdapter(adapter);


        //query database
        SecondaryBooksSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyWord = SecondaryBooksSearch.getText().toString().trim();
                if(keyWord!=null){
                    //connecting to firebase database
                    FirebaseRecyclerOptions<SecondaryBooksModel> options =
                            new FirebaseRecyclerOptions.Builder<SecondaryBooksModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("SeconderyBooks").orderByChild("title").startAt(keyWord).endAt(keyWord+"\uf8ff"), SecondaryBooksModel.class)
                                    .build();

                    adapter = new SecondaryBookAdapter(options, SecondaryTxtBooks.this);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);
                }else{
                    FirebaseRecyclerOptions<SecondaryBooksModel> options =
                            new FirebaseRecyclerOptions.Builder<SecondaryBooksModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Books"), SecondaryBooksModel.class)
                                    .build();

                    adapter = new SecondaryBookAdapter(options, SecondaryTxtBooks.this);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }
    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //saving the recyclerview state
        recyclerviewState = new Bundle();
        Parcelable list_state = recyclerView.getLayoutManager().onSaveInstanceState();
        recyclerviewState.putParcelable(KEY_RECYCLER_STATE,list_state);
    }
    protected void onPostResume() {
        super.onPostResume();
        // restore RecyclerView state
        if(recyclerviewState != null){
            Parcelable list_state = recyclerviewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(list_state);
        }
    }
    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void openBook(String book_url) {
        Intent intent = new Intent(SecondaryTxtBooks.this, OpenPdf.class);
        intent.putExtra("pdfUrl",book_url);
        startActivity(intent);
    }

    @Override
    public void downlaodPdf(Context context, String fileName, String fileExtension, String destinationDir, String url) {
        DownloadManager downloadManager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDir, fileName+fileExtension);
        downloadManager.enqueue(request);
        Toast.makeText(SecondaryTxtBooks.this, "Download has started",Toast.LENGTH_LONG);
    }
}