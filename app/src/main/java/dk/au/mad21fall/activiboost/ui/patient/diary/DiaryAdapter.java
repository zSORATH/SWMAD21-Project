package dk.au.mad21fall.activiboost.ui.patient.diary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hsalf.smileyrating.SmileyRating;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Diary;

// This is inspired by the Demo in week 3 called "Demo 2: RecyclerView in action".
public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    public interface IDiaryItemClickedListener{
        void onDiaryClicked(int index);
    }

    private IDiaryItemClickedListener listener;

    private List<Diary> diaryList;

    // Constructor
    public DiaryAdapter(IDiaryItemClickedListener listener){
        this.listener = listener;
    }

    public void updateDiaryList(List<Diary> lists){
        diaryList = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.diaryitem, parent, false);
        DiaryViewHolder vh = new DiaryViewHolder(v, listener);
        return vh;
    }

    // This is inspired by https://github.com/sujithkanna/SmileyRating
    // Getting the correct smiley according to the rating
    private int getSmiley(int rating) {
        if (rating == SmileyRating.Type.TERRIBLE.getRating()) {
            return R.drawable.ic_terrible;
        }
        if (rating == SmileyRating.Type.BAD.getRating()) {
            return R.drawable.ic_bad;
        }
        if (rating == SmileyRating.Type.OKAY.getRating()) {
            return R.drawable.ic_okay;
        }
        if (rating == SmileyRating.Type.GOOD.getRating()) {
            return R.drawable.ic_good;
        }
        if (rating == SmileyRating.Type.GREAT.getRating()) {
            return R.drawable.ic_great;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        holder.diary_date.setText(diaryList.get(position).getDate());
        holder.diary_rating.setImageResource(getSmiley(diaryList.get(position).getRating()));
    }


    @Override
    public int getItemCount() {
        // we have to check if the list is null to not crash the app
        if(diaryList != null){
            return diaryList.size();}
        else{
            return 0;
        }
    }

    public class DiaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView diary_rating;
        TextView diary_date;

        IDiaryItemClickedListener listener;

        //constructor
        public DiaryViewHolder(@NonNull View itemView, IDiaryItemClickedListener diaryItemClickedListener) {
            super(itemView);

            diary_rating = itemView.findViewById(R.id.diary_rating);
            diary_date = itemView.findViewById(R.id.diary_date);

            listener = diaryItemClickedListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onDiaryClicked(getAdapterPosition());
        }
    }
}
