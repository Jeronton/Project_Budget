package com.example.project_budget;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Category>> categories;
    private final String TAG = "JG";

    public DataViewModel(MutableLiveData<ArrayList<Category>> categories) {
        this.categories = categories;
    }

    public DataViewModel() {
        this.categories = new MutableLiveData<ArrayList<Category>>();
    }

    public void parseCategories(ArrayList categories){
        ArrayList<Category> tempCategories = new ArrayList<Category>(20);
        for ( Object item : categories) {
            try {
                HashMap<String, Object> hash = (HashMap<String, Object>)item;

                tempCategories.add(Category.parseCategory(hash));

            } catch (Exception e){
                Log.e(TAG, "parseCategories: Error occurred while parsing category: "+ e.toString());
            }
        }
        this.setCategories(tempCategories);
    }

    public MutableLiveData<ArrayList<Category>> getCategories() {
        return categories;
    }

    public void setCategories(MutableLiveData<ArrayList<Category>> categories) {
        this.categories = categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories.setValue(categories);
    }
}
