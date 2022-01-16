package zw.co.icetech.villageconnect3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class newsAdapter extends FirebaseRecyclerAdapter<newsModel, newsAdapter.NewsViewHolder> {

    private newsInterface interfaceNews;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public newsAdapter(@NonNull FirebaseRecyclerOptions<newsModel> options, newsInterface interfaceNews) {
        super(options);
        this.interfaceNews = interfaceNews;
    }

    @Override
    protected void onBindViewHolder(@NonNull NewsViewHolder holder, int position, @NonNull newsModel model) {
        holder.headline.setText(model.getHeadline());
        holder.story.setText(model.getStory());
        holder.date.setText(model.getDate());
        holder.auther.setText(model.getAuther());
        Glide.with(holder.context).load(model.getImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceNews.readNews(model.getStory());
            }
        });
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
        return new NewsViewHolder(view);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView headline, auther, date, story;
        CardView cardView;
        Context context;
        ImageView image;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            headline = itemView.findViewById(R.id.main_heading);
            auther = itemView.findViewById(R.id.news_auther);
            date = itemView.findViewById(R.id.news_pub_date);
            story = itemView.findViewById(R.id.news_content);
            cardView = itemView.findViewById(R.id.news_cardview);
            image = itemView.findViewById(R.id.news_image);
            context = itemView.getContext();
        }
    }
}
