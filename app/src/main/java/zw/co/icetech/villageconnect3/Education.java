package zw.co.icetech.villageconnect3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Education extends AppCompatActivity {

    Button primaryTxtBooks, primaryPapers, secondaryTxtBooks, seconderyPapers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);

        primaryTxtBooks = findViewById(R.id.btnPrimaryBks);
        primaryPapers = findViewById(R.id.btnPrimaryPprs);
        secondaryTxtBooks = findViewById(R.id.btnSecondaryBks);
        seconderyPapers = findViewById(R.id.btnSecondaryPprs);

        //open primary text books
        primaryTxtBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Education.this, EducationResults.class);
                startActivity(intent);
            }
        });

        //open primary papers
        primaryPapers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Education.this, PrimaryPapers.class);
                startActivity(intent);
            }
        });

        //open seconday Text Books
        secondaryTxtBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Education.this, SecondaryTxtBooks.class);
                startActivity(intent);
            }
        });

        //open seconday Text Books
        seconderyPapers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Education.this, SecondaryPapers.class);
                startActivity(intent);
            }
        });

    }
}