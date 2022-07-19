package com.example.invio.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invio.Adapters.RecycleAdapterCats;
import com.example.invio.Classes.Cat;
import com.example.invio.Classes.MessageFavToHome;
import com.example.invio.Classes.MessageToCat;
import com.example.invio.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class CatFragment extends Fragment{

    private Cat cat;
    private TextView headTextView,typesTextView,descTextView;
    private ImageView contentImageView,imageViewFavorite;
    private ProgressBar requestBar2;
    private CatFragmentListener cfListener;
    private ImageButton btnBackToHome;

    public CatFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cat, container, false);
        headTextView = view.findViewById(R.id.headTextView);
        typesTextView = view.findViewById(R.id.typesTextView);
        descTextView = view.findViewById(R.id.descTextView);
        contentImageView = view.findViewById(R.id.contentImageView);
        requestBar2 = view.findViewById(R.id.requestingBar2);
        btnBackToHome = view.findViewById(R.id.btnBackToHome);
        imageViewFavorite = view.findViewById(R.id.imageViewFavorite);

        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cfListener.inputSendCat("home");
            }
        });
        return view;
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void getUpdateMessage(MessageToCat eventToUpdate){
        cat = eventToUpdate.getCat();
        headTextView.setText("Name: "+cat.getName());
        typesTextView.setText("Intelligence: "+cat.getIntelligence());
        descTextView.setText(cat.getDescription());
        if(eventToUpdate.isFavorite()) {
            imageViewFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        }else {
            imageViewFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
        Picasso.get().load(cat.getImage()).into(contentImageView);
        requestBar2.setVisibility(View.GONE);
        EventBus.getDefault().removeAllStickyEvents();
    }

    @Override
    public void onPause() {
        super.onPause();
        requestBar2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public interface CatFragmentListener{
        void inputSendCat(String goTo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CatFragment.CatFragmentListener) {
            cfListener = (CatFragment.CatFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement me!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cfListener = null;
    }

}