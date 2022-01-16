package zw.co.icetech.villageconnect3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    //main nav buttons
    ImageView podcast, books, education, news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting the imageview variables to actual imageview buttons
        podcast = (ImageView) findViewById(R.id.btn_podcasts);
        books = (ImageView) findViewById(R.id.btn_books);
        education = (ImageView) findViewById(R.id.btn_education);
        news = (ImageView) findViewById(R.id.btn_news);

        //onclick to open the podcast page
        podcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PodcastsActivity.class);
                startActivity(intent);
            }
        });

        //onclick listener to open the books activity
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BooksActivity.class);
                startActivity(intent);
            }
        });
        //onclick listener to open the education activity
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Education.class);
                startActivity(intent);
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, News.class);
                startActivity(intent);
            }
        });
    }




}