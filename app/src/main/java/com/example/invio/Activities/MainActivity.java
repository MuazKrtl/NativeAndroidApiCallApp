package com.example.invio.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.invio.Classes.Cat;
import com.example.invio.Fragments.CatFragment;
import com.example.invio.Fragments.FavoritesFragment;
import com.example.invio.Fragments.HomeFragment;
import com.example.invio.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener, FavoritesFragment.FavoritesFragmentListener, CatFragment.CatFragmentListener {

    private final Fragment fragment1 = new HomeFragment();
    private final Fragment fragment2 = new CatFragment();
    private final Fragment fragment3 = new FavoritesFragment();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active;
    private boolean isFirstOpen = true;
    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");



        if(isFirstOpen){
            isFirstOpen = false;
            View dialogView = getLayoutInflater().inflate(R.layout.custom_splash_dialog,null);
            lottieAnimationView = dialogView.findViewById(R.id.lotti_view);
            Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            dialog.setContentView(dialogView);
            dialog.show();

            lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    dialog.dismiss();
                }
            });

            fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
            fm.beginTransaction().add(R.id.container, fragment2, "2").hide(fragment2).commit();
            active = fragment1;
            fm.beginTransaction().add(R.id.container,fragment1, "1").commit();

        }


    }

    @Override
    public void inputSendHome(String goTo) {
        if(goTo.equals("favorites")){
            fm.beginTransaction().hide(active).show(fragment3).commit();
            active = fragment3;
        }
        if(goTo.equals("catDetail")){
            fm.beginTransaction().hide(active).show(fragment2).commit();
            active = fragment2;
        }
    }

    @Override
    public void inputSendFavorites(String goTo) {
        if(goTo.equals("backToHome")){
            fm.beginTransaction().hide(active).show(fragment1).commit();
            active = fragment1;
        }
        if(goTo.equals("catDetail")){
            fm.beginTransaction().hide(active).show(fragment2).commit();
            active = fragment2;
        }
    }

    @Override
    public void inputSendCat(String goTo) {
        if(goTo.equals("home")){
            fm.beginTransaction().hide(active).show(fragment1).commit();
            active = fragment1;
        }
    }
}