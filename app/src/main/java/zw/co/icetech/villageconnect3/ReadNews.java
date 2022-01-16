package zw.co.icetech.villageconnect3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReadNews extends AppCompatActivity {
    TextView story;
    String article;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);

        story = findViewById(R.id.thenews);

        Intent intent = getIntent();
        article = intent.getStringExtra("article");
        story.setText(article);
    }
}