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

public class SecondaryPapers extends AppCompatActivity implements SecondaryPaperInterface{

    EditText SecondaryPapersSearch;

    //recyclerview variables
    RecyclerView seconderyPaperRv;
    final String KEY_RECYCLER_STATE = "recycler_state";
    static Bundle recyclerviewState;
    SeconderyPaperAdapter paperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_papers);
        SecondaryPapersSearch = findViewById(R.id.SecondaryPapersSearch);

        //initializing the recyclerview
        seconderyPaperRv = findViewById(R.id.SecondaryBooksRecyclerview);
        seconderyPaperRv.setLayoutManager(new LinearLayoutManager(this));

        //connecting to firebase database
        FirebaseRecyclerOptions<SecondaryPapersModel> options =
                new FirebaseRecyclerOptions.Builder<SecondaryPapersModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("SecondaryPapers"), SecondaryPapersModel.class)
                        .build();
        paperAdapter = new SeconderyPaperAdapter(options, this);
        seconderyPaperRv.setAdapter(paperAdapter);


        //qeury database
        SecondaryPapersSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyWord = SecondaryPapersSearch.getText().toString().trim();
                if(keyWord!=null){
                    //connecting to firebase database
                    FirebaseRecyclerOptions<SecondaryPapersModel> options =
                            new FirebaseRecyclerOptions.Builder<SecondaryPapersModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("SecondaryPapers").orderByChild("title").startAt(keyWord).endAt(keyWord+"\uf8ff"), SecondaryPapersModel.class)
                                    .build();

                    paperAdapter = new SeconderyPaperAdapter(options, SecondaryPapers.this);
                    paperAdapter.startListening();
                    seconderyPaperRv.setAdapter(paperAdapter);
                }else{
                    FirebaseRecyclerOptions<SecondaryPapersModel> options =
                            new FirebaseRecyclerOptions.Builder<SecondaryPapersModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("SecondaryPapers"), SecondaryPapersModel.class)
                                    .build();

                    paperAdapter = new SeconderyPaperAdapter(options, SecondaryPapers.this);
                    seconderyPaperRv.setAdapter(paperAdapter);
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
        Parcelable list_state = seconderyPaperRv.getLayoutManager().onSaveInstanceState();
        recyclerviewState.putParcelable(KEY_RECYCLER_STATE,list_state);
    }
    protected void onPostResume() {
        super.onPostResume();
        // restore RecyclerView state
        if(recyclerviewState != null){
            Parcelable list_state = recyclerviewState.getParcelable(KEY_RECYCLER_STATE);
            seconderyPaperRv.getLayoutManager().onRestoreInstanceState(list_state);
        }
    }


    //stop the music
    @Override
    protected void onStop() {
        super.onStop();
        paperAdapter.stopListening();
    }

    @Override
    public void openPaper(String paperUrl) {
        Intent intent = new Intent(SecondaryPapers.this, OpenPdf.class);
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
        Toast.makeText(SecondaryPapers.this, "Download has started",Toast.LENGTH_LONG);
    }
}