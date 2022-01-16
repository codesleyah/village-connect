package zw.co.icetech.villageconnect3;

import static com.google.firebase.database.FirebaseDatabase.*;

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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BooksActivity extends AppCompatActivity implements BookInterface{

    //recyclerview variables
    RecyclerView recyclerview_bk;
    final String KEY_RECYCLER_STATE = "recycler_state";
    static Bundle recyclerviewState;
    BookAdapter bookAdapter;

    //database variables
    EditText searchBar;


    //variables for downloading the  podcast pdf file
    FirebaseStorage firebaseStorage;
    StorageReference storageReference,ref;
    TextView downBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        //initializing the recyclerview
        recyclerview_bk = findViewById(R.id.recyclerview_books);
        recyclerview_bk.setLayoutManager(new LinearLayoutManager(this));
        searchBar = findViewById(R.id.books_searchbar);

        //connecting to firebase database
        FirebaseRecyclerOptions<BookModel> options =
                new FirebaseRecyclerOptions.Builder<BookModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Books"), BookModel.class)
                        .build();

        bookAdapter = new BookAdapter(options, this);
        recyclerview_bk.setAdapter(bookAdapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyWord = searchBar.getText().toString().trim();
                if(keyWord!=null){
                    //connecting to firebase database
                    FirebaseRecyclerOptions<BookModel> options =
                            new FirebaseRecyclerOptions.Builder<BookModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Books").orderByChild("title").startAt(keyWord).endAt(keyWord+"\uf8ff"), BookModel.class)
                                    .build();

                    bookAdapter = new BookAdapter(options, BooksActivity.this);
                    bookAdapter.startListening();
                    recyclerview_bk.setAdapter(bookAdapter);
                }else{
                    FirebaseRecyclerOptions<BookModel> options =
                            new FirebaseRecyclerOptions.Builder<BookModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Books"), BookModel.class)
                                    .build();

                    bookAdapter = new BookAdapter(options, BooksActivity.this);
                    recyclerview_bk.setAdapter(bookAdapter);
                }
            }
        });


    }

    //start the music
    @Override
    protected void onStart() {
        super.onStart();
        bookAdapter.startListening();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //saving the recyclerview state
        recyclerviewState = new Bundle();
        Parcelable list_state = recyclerview_bk.getLayoutManager().onSaveInstanceState();
        recyclerviewState.putParcelable(KEY_RECYCLER_STATE,list_state);
    }
    protected void onPostResume() {
        super.onPostResume();
        // restore RecyclerView state
        if(recyclerviewState != null){
            Parcelable list_state = recyclerviewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerview_bk.getLayoutManager().onRestoreInstanceState(list_state);
        }
    }

    //stop the music
    @Override
    protected void onStop() {
        super.onStop();
        bookAdapter.stopListening();
    }

    @Override
    public void openBook(String book, String book_url) {

          Intent intent = new Intent(BooksActivity.this, OpenPdf.class);
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
        Toast.makeText(BooksActivity.this, "Download has started",Toast.LENGTH_LONG);
    }
}