package com.stuffbox.model;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class Icon {
	int id;
	String name;
	String description;
	int drawableId;
	
	public Icon(int id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getResourceId() {
		return description;
	}
	/**
	 * Returns the resource id of the image with the passed name
	 * @param variableName
	 * @return
	 */
	public static int getResId(Context context, String variableName) {
	    try {
	        Field idField = Drawable.class.getDeclaredField(variableName);
	        return idField.getInt(idField);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;
	    } 
	}
	
	/**
	 * Returns the drawable from an inserted string
	 * @param pDrawableName
	 * @return
	 */
	static public Drawable getAndroidDrawable(String pDrawableName){
	    int resourceId=Resources.getSystem().getIdentifier(pDrawableName, "drawable", "android");
	    if(resourceId==0){
	        return null;
	    } else {
	        return Resources.getSystem().getDrawable(resourceId);
	    }
	}
	
	public int getDrawableId() {
		return drawableId;
	}
	public void setDrawableId(int drawableId) {
		this.drawableId = drawableId;
	}	
}
