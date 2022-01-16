package zw.co.icetech.villageconnect3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PodcastAdapter extends FirebaseRecyclerAdapter<PodcastModel,PodcastAdapter.PodcastsViewHolder> {

    private PodcastInterface recyclerViewInterface;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PodcastAdapter(@NonNull FirebaseRecyclerOptions<PodcastModel> options,PodcastInterface recyclerViewInterface) {
        super(options);
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    protected void onBindViewHolder(@NonNull PodcastsViewHolder holder, int position, @NonNull PodcastModel model) {

        holder.podcast.setText(model.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterface.playPodcast(model.getTitle(),model.getUrl());
            }
        });

    }


    @NonNull
    @Override
    public PodcastsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.podcast_list_item,parent,false);
        return new PodcastsViewHolder(view);
    }

    public class PodcastsViewHolder extends RecyclerView.ViewHolder {

        TextView podcast;
        CardView cardView;
        Context context;
        public PodcastsViewHolder(@NonNull View itemView) {
            super(itemView);

            podcast = itemView.findViewById(R.id.podcast_topic);
            cardView = itemView.findViewById(R.id.pdcast_vcard);
            context = itemView.getContext();

        }
    }
}
