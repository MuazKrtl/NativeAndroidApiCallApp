package com.example.invio.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invio.Adapters.RecycleAdapterCats;
import com.example.invio.Adapters.RecycleAdapterFavs;
import com.example.invio.CatsRecyclerViewClickListener;
import com.example.invio.Classes.Cat;
import com.example.invio.Classes.MessageFavToHome;
import com.example.invio.Classes.MessageHomeToFav;
import com.example.invio.Classes.MessageToCat;
import com.example.invio.FavsRecyclerViewClickListener;
import com.example.invio.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoritesFragment extends Fragment implements FavsRecyclerViewClickListener {

    private RecyclerView recyclerViewFavs;
    private RecycleAdapterFavs recycleAdapterFavs;
    private ArrayList<Cat> myFavorites;
    private ArrayList<String> arraylistCatNames;
    private ImageButton buttonBack;
    private FavoritesFragmentListener ffListener;
    private View view;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerViewFavs = view.findViewById(R.id.recyclerViewFavs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerViewFavs.setLayoutManager(linearLayoutManager);
        loadData();
        loadCheckList();
        recyclerViewFavs.setAdapter(recycleAdapterFavs);
        buttonBack = view.findViewById(R.id.buttonFavorites);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ffListener.inputSendFavorites("backToHome");
            }
        });
        return view;
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.invio", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favorite", null);
        Type type = new TypeToken<ArrayList<Cat>>() {}.getType();
        myFavorites = gson.fromJson(json, type);
        if (myFavorites == null) {
            myFavorites = new ArrayList<>();
        }
        recycleAdapterFavs = new RecycleAdapterFavs(myFavorites,arraylistCatNames,this);
        recyclerViewFavs.setAdapter(recycleAdapterFavs);
        recycleAdapterFavs.notifyDataSetChanged();
    }

    private void loadCheckList() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.invio", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("checklist", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        arraylistCatNames = gson.fromJson(json, type);
        if (arraylistCatNames == null) {
            arraylistCatNames = new ArrayList<>();
        }
        recycleAdapterFavs = new RecycleAdapterFavs(myFavorites,arraylistCatNames,this);
        recyclerViewFavs.setAdapter(recycleAdapterFavs);
        recycleAdapterFavs.notifyDataSetChanged();
    }

    private void saveCheckList() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.invio", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arraylistCatNames);
        editor.putString("checklist", json);
        editor.apply();
        recycleAdapterFavs = new RecycleAdapterFavs(myFavorites,arraylistCatNames,this);
        recyclerViewFavs.setAdapter(recycleAdapterFavs);
        recycleAdapterFavs.notifyDataSetChanged();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.invio", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myFavorites);
        editor.putString("favorite", json);
        editor.apply();
        recycleAdapterFavs = new RecycleAdapterFavs(myFavorites,arraylistCatNames,this);
        recyclerViewFavs.setAdapter(recycleAdapterFavs);
        recycleAdapterFavs.notifyDataSetChanged();
    }

    @Override
    public void catsRecyclerViewListClicked(int position) {
        System.out.println("Recyle TIKLANDI");
        ffListener.inputSendFavorites("catDetail");
        EventBus.getDefault().postSticky(new MessageToCat(myFavorites.get(position),true));
    }

    @Override
    public void catsRecyclerViewCheckBoxClicked(int position) {

        System.out.println("AAASİLİNDİ");
        System.out.println(myFavorites);
        System.out.println(arraylistCatNames);

        myFavorites.remove(myFavorites.get(position));
        arraylistCatNames.remove(arraylistCatNames.get(position));
        saveCheckList();
        saveData();
        EventBus.getDefault().postSticky(new MessageFavToHome(0));
    }

    public interface FavoritesFragmentListener{
        void inputSendFavorites(String goTo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FavoritesFragment.FavoritesFragmentListener) {
            ffListener = (FavoritesFragment.FavoritesFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement me!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ffListener = null;
    }

    @Subscribe (sticky = true,threadMode = ThreadMode.MAIN)
    public void getUpdateMessage(MessageHomeToFav eventToUpdate){
        loadData();
        loadCheckList();

        Log.e("tag","geldi ki");
        Log.e("tag",myFavorites.toString());
        Log.e("tag",arraylistCatNames.toString());
        saveCheckList();
        saveData();
        EventBus.getDefault().removeAllStickyEvents();
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

}