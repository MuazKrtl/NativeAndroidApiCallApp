package com.example.invio.Fragments;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.invio.Adapters.RecycleAdapterCats;
import com.example.invio.Adapters.RecycleAdapterFavs;
import com.example.invio.CatsRecyclerViewClickListener;
import com.example.invio.Classes.Cat;
import com.example.invio.Classes.MessageFavToHome;
import com.example.invio.Classes.MessageHomeToFav;
import com.example.invio.Classes.MessageToCat;
import com.example.invio.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class HomeFragment extends Fragment implements CatsRecyclerViewClickListener {

    private View view;
    private RecyclerView recyclerViewCats;
    private RecycleAdapterCats recycleAdapterCats;
    private ArrayList<Cat> arrayListCats,myFavorites;
    private ArrayList<String> arraylistCatNames;
    private RequestQueue mQueue;
    private Button buttonLoadMore,searchButton;
    private ImageButton buttonFavorites;
    private EditText searchEditText;
    private ProgressBar requestingBar;
    private int loaded = 0,totalRequest=0;
    private HomeFragmentListener hfListener;
    private String searchText;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayListCats = new ArrayList<>();
        loadData();
        loadCheckList();
        recycleAdapterCats = new RecycleAdapterCats(arrayListCats,arraylistCatNames,this);
        mQueue = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
   /*     if(internetIsConnected()){*/
        view = inflater.inflate(R.layout.fragment_home, container, false);

        requestingBar = view.findViewById(R.id.requestingBar);

        recyclerViewCats = view.findViewById(R.id.recyclerViewFavs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerViewCats.setLayoutManager(linearLayoutManager);
        recyclerViewCats.setAdapter(recycleAdapterCats);
        buttonLoadMore = view.findViewById(R.id.loadMoreButton);
        buttonFavorites = view.findViewById(R.id.buttonFavorites);
        searchButton = view.findViewById(R.id.searchButton);
        searchEditText = view.findViewById(R.id.searchEditText);

        buttonFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hfListener.inputSendHome("favorites");
            }
        });

        buttonLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCall(10);
            };
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText = String.valueOf(searchEditText.getText());
                if(searchText.equals("")){
                    arrayListCats.clear();
                    loaded = 0;
                    buttonLoadMore.setClickable(false);
                    buttonLoadMore.setBackgroundColor(Color.parseColor("#696866"));
                    firstCall(300);
                }else{
                    arrayListCats.clear();
                    manageLoading(false);
                    jsonSearch(new RequestCallBackSearch() {
                        @Override
                        public void onCallBack(ArrayList<Cat> arrayListCats) {
                            manageLoading(true);
                            recycleAdapterCats.notifyDataSetChanged();
                        }
                    },searchText);
                }
            }
        });
        firstCall(10);

        return view;
    }

    private void jsonParse(RequestCallBackSearch requestCallBackSearch,int request) {
        totalRequest += request;
        String url = "https://api.thecatapi.com/v1/breeds?attach_breed=0?api_key=c1e25c19-4f8d-46b6-8b64-168a8666c748";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        while (loaded != totalRequest){
                            try {
                                JSONObject jsonCat = response.getJSONObject(loaded);
                                JSONObject jsonImage = jsonCat.getJSONObject("image");
                                Cat cat = new Cat();
                                cat.setName(jsonCat.getString("id"));
                                cat.setDescription(jsonCat.getString("description"));
                                cat.setImage(jsonImage.getString("url"));
                                cat.setIntelligence(jsonCat.getString("intelligence"));
                                arrayListCats.add(cat);
                                loaded++;
                                System.out.println(cat.getName());
                            }catch (JSONException e) {
                                e.printStackTrace();
                                loaded++;
                            }
                        }requestCallBackSearch.onCallBack(arrayListCats);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        mQueue.add(getRequest);
    }

    private void jsonSearch(RequestCallBackSearch requestCallBackSearch,String searchText) {
        String url = "https://api.thecatapi.com/v1/breeds?attach_breed=0?api_key=c1e25c19-4f8d-46b6-8b64-168a8666c748";

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        for(int i = 0;i<300;i++){
                            try {
                                JSONObject jsonCat = response.getJSONObject(i);
                                if(jsonCat.getString("id").contains(searchText)){
                                    JSONObject jsonImage = jsonCat.getJSONObject("image");
                                    Cat cat = new Cat();
                                    cat.setName(jsonCat.getString("id"));
                                    cat.setDescription(jsonCat.getString("description"));
                                    cat.setImage(jsonImage.getString("url"));
                                    cat.setIntelligence(jsonCat.getString("intelligence"));
                                    arrayListCats.add(cat);
                                    System.out.println(cat.getName());
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }requestCallBackSearch.onCallBack(arrayListCats);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        mQueue.add(getRequest);
    }

    private void firstCall(int request){
        manageLoading(false);
        jsonParse(new RequestCallBackSearch() {
            @Override
            public void onCallBack(ArrayList<Cat> arrayListCats) {
                manageLoading(true);
                recycleAdapterCats.notifyDataSetChanged();
            }
        },request);
    }

    private void manageLoading(boolean isHappened){
        if(isHappened){
            requestingBar.setVisibility(View.GONE);
            buttonLoadMore.setClickable(true);
        }else{
            requestingBar.setVisibility(View.VISIBLE);
            buttonLoadMore.setClickable(false);
        }
    }

    private interface RequestCallBackSearch{
        void onCallBack(ArrayList<Cat> arrayListCats);
    }

    @Override
    public void catsRecyclerViewListClicked(int position) {
        System.out.println("Recyle TIKLANDI");
        hfListener.inputSendHome("catDetail");
        boolean isFavorite;
        if(arraylistCatNames.contains(arrayListCats.get(position).getName()))
            isFavorite = true;
        else
            isFavorite = false;
        EventBus.getDefault().postSticky(new MessageToCat(arrayListCats.get(position),isFavorite));
    }

    @Override
    public void catsRecyclerViewCheckBoxClicked(int position) {
        if (!arraylistCatNames.contains((arrayListCats.get(position).getName()))) {
            myFavorites.add(arrayListCats.get(position));
            arraylistCatNames.add(arrayListCats.get(position).getName());
        } else {
            myFavorites.remove(arraylistCatNames.indexOf(arrayListCats.get(position).getName()));
            arraylistCatNames.remove(arrayListCats.get(position).getName());
        }
        saveData();
        saveCheckList();
        recycleAdapterCats.notifyDataSetChanged();
        EventBus.getDefault().postSticky(new MessageHomeToFav(1));

    }

    @Subscribe (sticky = true,threadMode = ThreadMode.MAIN)
    public void getUpdateMessage(MessageFavToHome eventToUpdate){
        loadData();
        loadCheckList();
        Log.e("AAAAAA",myFavorites.toString());
        recycleAdapterCats = new RecycleAdapterCats(arrayListCats,arraylistCatNames,this);
        recyclerViewCats.setAdapter(recycleAdapterCats);
        recycleAdapterCats.notifyDataSetChanged();
        EventBus.getDefault().removeAllStickyEvents();
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
    }

    private void saveCheckList() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.invio", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arraylistCatNames);
        editor.putString("checklist", json);
        editor.apply();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.invio", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myFavorites);
        editor.putString("favorite", json);
        editor.apply();
    }

    public interface HomeFragmentListener{
        void inputSendHome(String goTo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragmentListener) {
            hfListener = (HomeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement me!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hfListener = null;
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