package com.example.invio.Adapters;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invio.CatsRecyclerViewClickListener;
import com.example.invio.Classes.Cat;
import com.example.invio.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleAdapterCats extends RecyclerView.Adapter<RecycleAdapterCats.RecycleHolder> {

    private static CatsRecyclerViewClickListener sClickListener;
    private ArrayList<Cat> arrayListCats;
    private ArrayList<String> arrayListCatsName;

    //type = 0 means came defined at home, 1 means favorites, 2 means cat fragment.
    public RecycleAdapterCats(ArrayList<Cat> arrayListCats,ArrayList<String> arrayListCatsName,CatsRecyclerViewClickListener sClickListener) {
        this.sClickListener = sClickListener;
        this.arrayListCats = arrayListCats;
        this.arrayListCatsName = arrayListCatsName;
    }



    @NonNull
    @Override
    public RecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.home_recycler_row,parent,false);
        return new RecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleHolder holder, int position) {
        holder.textView.setText(arrayListCats.get(position).getName());
        Picasso.get().load(arrayListCats.get(position).getImage()).into(holder.imageView);
        System.out.println("Başta: "+ arrayListCatsName);
        if(arrayListCatsName.contains(arrayListCats.get(position).getName())){
            holder.checkButton.setText("Favorilerden Çıkar");
        }else{
            holder.checkButton.setText("Favorilere Ekle");
        }
    }

    @Override
    public int getItemCount() {
        return arrayListCats.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        Button checkButton;
        CardView cardView;
        public RecycleHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            checkButton = itemView.findViewById(R.id.checkButton);
            imageView = itemView.findViewById(R.id.ImageView);
            textView = itemView.findViewById(R.id.placeRowTextView);
            checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sClickListener.catsRecyclerViewCheckBoxClicked(getLayoutPosition());
                }
            });
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sClickListener.catsRecyclerViewListClicked(getLayoutPosition());
                }
            });
        }
    }

}
