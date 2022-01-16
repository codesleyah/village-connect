package zw.co.icetech.villageconnect3;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PodcastsActivity extends AppCompatActivity implements PodcastInterface{

    //recyclerview variables
    RecyclerView recyclerview_pd;
    final String KEY_RECYCLER_STATE = "recycler_state";
    static Bundle bundle_pd_recyclerview_state;
    PodcastAdapter podcast_adapter ,search_podcast_adapter;

    //firebase seaarch variables


    //play podcast variables
    TextView podcast,txt_pd_duration;
    String pd_download_url;
    private boolean is_playing = false;
    MediaPlayer mediaPlayer;
    ImageButton btn_next, btn_prev,btn_play_pause;
    SeekBar songProgressBar;
    Handler handler = new Handler();
    Runnable runnable;
    Button podcast_search_btn;
    EditText podcast_search_bar;

    //variables for downloading the  podcast audio file
    FirebaseStorage firebaseStorage;
    StorageReference storageReference,ref;
    TextView down_load_pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcasts);

        //initializing the recyclerview
        recyclerview_pd = (RecyclerView) findViewById(R.id.recyclerview_pd);
        recyclerview_pd.setLayoutManager(new LinearLayoutManager(this));

        //play podcast variable assignments
        podcast = (TextView) findViewById(R.id.podcast_title);
        btn_next = (ImageButton) findViewById(R.id.btn_next);
        btn_prev = (ImageButton) findViewById(R.id.btn_prev);
        btn_play_pause = (ImageButton) findViewById(R.id.btn_play_pause);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        txt_pd_duration = (TextView) findViewById(R.id.tx_pd_duration);
        down_load_pd = (TextView) findViewById(R.id.down_load_pd);
        podcast_search_bar = (EditText) findViewById(R.id.podcast_search_bar);



        //connecting to firebase database
        FirebaseRecyclerOptions<PodcastModel> options =
                new FirebaseRecyclerOptions.Builder<PodcastModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Podcast"), PodcastModel.class)
                        .build();

        podcast_adapter = new PodcastAdapter(options, this);
        recyclerview_pd.setAdapter(podcast_adapter);

        //serch by edittext
        podcast_search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(podcast_search_bar.getText().toString()!=null){
                    String keyWord = podcast_search_bar.getText().toString().trim();
                    searchDb(keyWord);
                }else{
                    FirebaseRecyclerOptions<PodcastModel> options =
                            new FirebaseRecyclerOptions.Builder<PodcastModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Podcast"), PodcastModel.class)
                                    .build();

                    podcast_adapter = new PodcastAdapter(options, PodcastsActivity.this);
                    recyclerview_pd.setAdapter(podcast_adapter);
                }
            }
        });

        //onclick listener for download button
        down_load_pd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPodcast();
            }
        });

        //onclick listener for the next button
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        //onlick listener for the prev button
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prev();
            }
        });
        //play pause button onclick listenter
        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPause();
            }
        });

        //onclick listener for the progressbar
        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    //search vlues from the database
    private void searchDb(String keyWord){

        //connecting to firebase database
        FirebaseRecyclerOptions<PodcastModel> options =
                new FirebaseRecyclerOptions.Builder<PodcastModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Podcast").orderByChild("title").startAt(keyWord).endAt(keyWord+"\uf8ff"), PodcastModel.class)
                        .build();

        podcast_adapter = new PodcastAdapter(options, this);
        podcast_adapter.startListening();
        recyclerview_pd.setAdapter(podcast_adapter);
    }

    private void downloadPodcast() {
        storageReference = firebaseStorage.getInstance().getReference();
        ref = storageReference.child("mangungundengu_411_chat_show_dj_mbale_speaks_to_freeman_mp3_72739.mp3");

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFiles(PodcastsActivity.this,"mangungundengu_411_chat_show_dj_mbale_speaks_to_freeman_mp3_72739",".mp3",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PodcastsActivity.this, "Download Failed!",Toast.LENGTH_LONG);

            }
        });
    }


    //download the files
    private void downloadFiles(Context context, String fileName, String fileExtension, String destinationDir, String url) {
        DownloadManager downloadManager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDir, fileName+fileExtension);
        downloadManager.enqueue(request);
        Toast.makeText(PodcastsActivity.this, "Download has started",Toast.LENGTH_LONG);

    }

    //play and pause the song
    private void playPause() {
        if(is_playing == true && mediaPlayer!=null){
            mediaPlayer.pause();
            //stoping the handler for progress bar
            handler.removeCallbacks(runnable);
            is_playing = false;
        }else if(is_playing==false && mediaPlayer != null){
            mediaPlayer.start();
            is_playing=true;
        }
    }

    //prev the song five sec back
    private boolean prev() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        int seekBackwardTime = 5000;
        // check if seekBackward time is greater than 0 sec
        if (currentPosition - seekBackwardTime >= 0) {
            // forward song
            mediaPlayer.seekTo(currentPosition - seekBackwardTime);
        } else {
            // backward to starting position
            mediaPlayer.seekTo(0);
        }
        return false;
    }

    //to fast foward 5 seconds
    private boolean next() {
        int seekForwardTime = 5000;
        if(mediaPlayer!=null){
            int currentPosition = mediaPlayer.getCurrentPosition();
            // check if seekForward time is lesser than song duration
            if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {
                // forward song
                mediaPlayer.seekTo(currentPosition + seekForwardTime);
            } else {
                // forward to end position
               mediaPlayer.seekTo(mediaPlayer.getDuration());
            }
        }
        return  false;
    }


    //start the music
    @Override
    protected void onStart() {
        super.onStart();
        podcast_adapter.startListening();
    }

    //stop the music
    @Override
    protected void onStop() {
        super.onStop();
        podcast_adapter.stopListening();
    }

    //function to play the song
    @Override
    public void playPodcast(String podcast,String podcast_url) {

        if(!is_playing){
            is_playing = true;
            this.podcast.setText(podcast);
            String url = podcast_url;
            pd_download_url = url;
            mediaPlayer = new MediaPlayer();

            //attatching song length to the progressbar


            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                initializeProgressBar();
                mediaPlayer.start();
                //set max on seek bar
                songProgressBar.setMax(mediaPlayer.getDuration());
                //Starting the handler
                handler.postDelayed(runnable, 0);
            } catch (IOException e) {
                Toast.makeText(PodcastsActivity.this,url ,Toast.LENGTH_SHORT).show();
            }
        }else{
            mediaPlayer.stop();
            is_playing = true;
            this.podcast.setText(podcast);
            String url = podcast_url;
            pd_download_url = url;
            mediaPlayer = new MediaPlayer();

            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare(); // might take long! (for buffering, etc)
                initializeProgressBar();
                mediaPlayer.start();

            } catch (IOException e) {
                Toast.makeText(PodcastsActivity.this,url ,Toast.LENGTH_SHORT).show();
            }


        }

    }

    //progress bar for the song
    private void initializeProgressBar() {
        runnable = new Runnable() {
            @Override
            public void run() {
                //set progress on seekbar
                songProgressBar.setProgress(mediaPlayer.getCurrentPosition());
                //handler post delay for 0.5 seconds
                handler.postDelayed(this, 500);

            }
        };

        //get duration of mediaplayer
        int pd_duration = mediaPlayer.getDuration();

        // convert milliseconds to minutes and seconds
        String duration = convertFormat(pd_duration);
        //setting duration to textview
        txt_pd_duration.setText(duration);
    }


    //convert the format of the time of the song
    private String convertFormat(int pd_duration) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(pd_duration)
                ,TimeUnit.MILLISECONDS.toSeconds(pd_duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(pd_duration)));
    }

    // search values in firebase

}