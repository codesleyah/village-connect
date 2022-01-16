package zw.co.icetech.villageconnect3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class News extends AppCompatActivity implements newsInterface{

    //recyclerview variables
    RecyclerView recyclerview_news;
    final String KEY_RECYCLER_STATE = "recycler_state";
    static Bundle recyclerviewState;
    newsAdapter newsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerview_news = findViewById(R.id.recyclerview_news);
        recyclerview_news.setLayoutManager(new LinearLayoutManager(this));


        //connecting to firebase database
        FirebaseRecyclerOptions<newsModel> options =
                new FirebaseRecyclerOptions.Builder<newsModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("news"), newsModel.class)
                        .build();

        newsAdapter = new newsAdapter(options, this);
        recyclerview_news.setAdapter(newsAdapter);
    }


    @Override
    public void readNews(String story) {
        Intent intent = new Intent(News.this, ReadNews.class);
        intent.putExtra("article",story);
        startActivity(intent);
    }
    //start the music
    @Override
    protected void onStart() {
        super.onStart();
        newsAdapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //saving the recyclerview state
        recyclerviewState = new Bundle();
        Parcelable list_state = recyclerview_news.getLayoutManager().onSaveInstanceState();
        recyclerviewState.putParcelable(KEY_RECYCLER_STATE,list_state);
    }
    protected void onPostResume() {
        super.onPostResume();
        // restore RecyclerView state
        if(recyclerviewState != null){
            Parcelable list_state = recyclerviewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerview_news.getLayoutManager().onRestoreInstanceState(list_state);
        }
    }
}