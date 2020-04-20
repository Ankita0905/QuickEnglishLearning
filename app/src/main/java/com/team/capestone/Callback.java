package com.team.capestone;

public abstract class Callback<T> {
    public abstract void onSuccess(T t);

    public void onError(String error){

    }
}
