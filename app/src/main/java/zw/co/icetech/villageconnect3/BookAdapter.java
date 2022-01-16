package zw.co.icetech.villageconnect3;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class BookAdapter extends FirebaseRecyclerAdapter<BookModel, BookAdapter.BookViewHolder> {

    private BookInterface recyclerViewInterface;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public BookAdapter(FirebaseRecyclerOptions<BookModel> options, BookInterface recyclerViewInterface){
        super(options);
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookViewHolder holder, int position, @NonNull BookModel model) {

        holder.book.setText(model.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterface.openBook(model.getTitle(),model.getUrl());
            }
        });
        //function to download the book
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.downlaodPdf(holder.context, model.getTitle(), "pdf",DIRECTORY_DOWNLOADS, model.getUrl());
            }
        });

    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);
        return new BookViewHolder(view);
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView book, download;
        CardView cardView;
        Context context;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            //books and podcasts are using the same listview item
            book = itemView.findViewById(R.id.podcast_topic);
            cardView = itemView.findViewById(R.id.bkcast_vcard);
            download = itemView.findViewById(R.id.downloadBook);
            context = itemView.getContext();
        }
    }
}
