package zw.co.icetech.villageconnect3;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

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

public class SecondaryBookAdapter extends FirebaseRecyclerAdapter<SecondaryBooksModel, SecondaryBookAdapter.SecondaryBookViewHolder> {
    SecondaryBookInterface recyclerViewInterface;

    public SecondaryBookAdapter(@NonNull FirebaseRecyclerOptions<SecondaryBooksModel> options, SecondaryBookInterface recyclerViewInterface){
        super(options);
       this.recyclerViewInterface = recyclerViewInterface;
    }

    protected void onBindViewHolder(@NonNull SecondaryBookAdapter.SecondaryBookViewHolder holder, int position, @NonNull SecondaryBooksModel model){

        // get grade
        holder.grade.setText(model.getGrade());
        holder.title.setText(model.getTitle());

        //open book
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.openBook(model.getUrl());
            }
        });
        //download pdf
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.downlaodPdf(holder.context, model.getTitle(), "pdf",DIRECTORY_DOWNLOADS,model.getUrl());
            }
        });

    }

    public SecondaryBookAdapter.SecondaryBookViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_book_item, parent, false);
        return new SecondaryBookAdapter.SecondaryBookViewHolder(view);
    }



    public class SecondaryBookViewHolder extends RecyclerView.ViewHolder {

        TextView title, grade, download;
        CardView cardView;
        Context context;
        public SecondaryBookViewHolder(@NonNull View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            grade = itemView.findViewById(R.id.grade);
            download = itemView.findViewById(R.id.downloadtBook);
            cardView = itemView.findViewById(R.id.txtBookVcard);
            context = itemView.getContext();
        }

    }
}
