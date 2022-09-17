package com.kata.kidspe.Listener;

import com.kata.kidspe.Model.ImageSwipe;

import java.util.List;

public interface FirebaseHelper {
    void onFirebaseLoadSuccess(List<ImageSwipe> imageSwipeList);
    void onFirebaseLoadFailed(String message);

}
