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

public class SeconderyPaperAdapter  extends FirebaseRecyclerAdapter<SecondaryPapersModel, SeconderyPaperAdapter.SeconderyPaperViewHolder> {
    private SecondaryPaperInterface recyclerViewInterface;

    public SeconderyPaperAdapter(FirebaseRecyclerOptions<SecondaryPapersModel> options, SecondaryPaperInterface recyclerViewInterface){
        super(options);
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    protected void onBindViewHolder(@NonNull SeconderyPaperAdapter.SeconderyPaperViewHolder holder, int position, @NonNull SecondaryPapersModel model) {

        holder.paper.setText(model.getTitle());
        holder.grade.setText(model.getGrade());
        //open pdf
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterface.openPaper(model.getUrl());
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

    @NonNull
    @Override
    public SeconderyPaperAdapter.SeconderyPaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_book_item,parent,false);
        return new SeconderyPaperAdapter.SeconderyPaperViewHolder(view);
    }


    public class SeconderyPaperViewHolder extends RecyclerView.ViewHolder {
        TextView paper, grade, download;
        CardView cardView;
        Context context;

        public SeconderyPaperViewHolder(@NonNull View itemView) {
            super(itemView);

            //books and podcasts are using the same listview item
            paper = itemView.findViewById(R.id.book_title);
            grade = itemView.findViewById(R.id.grade);
            download = itemView.findViewById(R.id.downloadtBook);
            cardView = itemView.findViewById(R.id.txtBookVcard);
            context = itemView.getContext();
        }
    }
}
