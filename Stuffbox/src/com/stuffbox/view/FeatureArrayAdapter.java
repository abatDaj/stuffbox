/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stuffbox.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.stuffbox.R;
import com.stuffbox.model.Feature;

public class FeatureArrayAdapter extends ArrayAdapter<Feature> {

    final int INVALID_ID = -1;

	private final Context context;
	private boolean selectedList;
    HashMap<Feature, Integer> mIdMap = new HashMap<Feature, Integer>();

    public FeatureArrayAdapter(Context context, int textViewResourceId, List<Feature> objects, boolean selectedList) {
        super(context, textViewResourceId, objects);
		this.context = context;
		this.selectedList = selectedList;
        
		//Erstelle map aus erhaltenen Objekten
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    /**
     * Fügt eine Eigenschaft in die Liste
     * @param feature
     */
    @Override
	public void add(Feature feature){
    	mIdMap.put(feature, mIdMap.size());
    	super.add(feature);
    }
    /**
     * Entfernt eine Eigenschaft aus der Liste
     * @param position
     */
    @Override
	public void remove(Feature feature){
    	mIdMap.remove(feature);
    	super.remove(feature);
    }
    
    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        Feature item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    
    public int getLengthOfArray(){
    	return mIdMap.size();
    }
    
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getCustomView(position, convertView, parent);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_selection_feature, parent, false);
		//Name setzen
		TextView nameText = (TextView) rowView.findViewById(R.id.selected_feature_name);
		nameText.setText(getItem(position).getName());
		//sortnummer setzen
		TextView sortnumberText = (TextView) rowView.findViewById(R.id.selected_feature_sortnumber);
		if(selectedList){
			//zeige Position an
			getItem(position).setSortnumber(position);
			sortnumberText.setText(Integer.toString(position));	
		}else{
			//zeige Position nicht an
			sortnumberText.setText(R.string.empty_string);
		}
		
		//aendere presymbol zu plus
		if(!selectedList){
			TextView preText = (TextView) rowView.findViewById(R.id.presymbol);
			preText.setText(R.string.plus);
		}

		return rowView;
	}
	/**
	 * Returns the items that this adapter holds
	 * @return
	 */
	public ArrayList<Feature> getFeatures(){
		ArrayList<Feature> features = new ArrayList<Feature>();
		for(Feature feature : mIdMap.keySet()){
			features.add(feature);
		}
		return features;
	}
}
