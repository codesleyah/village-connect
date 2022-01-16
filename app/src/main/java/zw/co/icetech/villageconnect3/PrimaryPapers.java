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
import com.google.firebase.database.FirebaseDatabase;

public class PrimaryPapers extends AppCompatActivity implements ppaperInterface {

    EditText primaryPapersSearchBar;
    //recyclerview variables
    RecyclerView primaryPaperRv;
    final String KEY_RECYCLER_STATE = "recycler_state";
    static Bundle recyclerviewState;
    ppaperAdapter paperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_papers);

        primaryPapersSearchBar = findViewById(R.id.primaryPapersSearchBar);

        //initializing the recyclerview
        primaryPaperRv = findViewById(R.id.primaryPapersRv);
        primaryPaperRv.setLayoutManager(new LinearLayoutManager(this));

        //connecting to firebase database
        FirebaseRecyclerOptions<pmodelPrimary> options =
                new FirebaseRecyclerOptions.Builder<pmodelPrimary>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PrimaryPapers"), pmodelPrimary.class)
                        .build();
        paperAdapter = new ppaperAdapter(options, this);
        primaryPaperRv.setAdapter(paperAdapter);


        //search database with edittext
        primaryPapersSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyWord = primaryPapersSearchBar.getText().toString().trim();
                if(keyWord!=null){
                    //connecting to firebase database
                    FirebaseRecyclerOptions<pmodelPrimary> options =
                            new FirebaseRecyclerOptions.Builder<pmodelPrimary>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("PrimaryPapers").orderByChild("title").startAt(keyWord).endAt(keyWord+"\uf8ff"), pmodelPrimary.class)
                                    .build();

                    paperAdapter = new ppaperAdapter(options, PrimaryPapers.this);
                    paperAdapter.startListening();
                    primaryPaperRv.setAdapter(paperAdapter);
                }else{
                    FirebaseRecyclerOptions<pmodelPrimary> options =
                            new FirebaseRecyclerOptions.Builder<pmodelPrimary>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("PrimaryPapers"), pmodelPrimary.class)
                                    .build();

                    paperAdapter = new ppaperAdapter(options, PrimaryPapers.this);
                    primaryPaperRv.setAdapter(paperAdapter);
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        paperAdapter.startListening();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //saving the recyclerview state
        recyclerviewState = new Bundle();
        Parcelable list_state = primaryPaperRv.getLayoutManager().onSaveInstanceState();
        recyclerviewState.putParcelable(KEY_RECYCLER_STATE,list_state);
    }
    protected void onPostResume() {
        super.onPostResume();
        // restore RecyclerView state
        if(recyclerviewState != null){
            Parcelable list_state = recyclerviewState.getParcelable(KEY_RECYCLER_STATE);
            primaryPaperRv.getLayoutManager().onRestoreInstanceState(list_state);
        }
    }
    //stop the music
    @Override
    protected void onStop() {
        super.onStop();
        paperAdapter.stopListening();
    }
    @Override
    public void openPaper( String paperUrl) {
        Intent intent = new Intent(PrimaryPapers.this, OpenPdf.class);
        intent.putExtra("pdfUrl",paperUrl);
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
        Toast.makeText(PrimaryPapers.this, "Download has started",Toast.LENGTH_LONG);
    }
}