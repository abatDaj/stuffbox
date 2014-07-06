package com.stuffbox.controller;

import android.app.AlertDialog;
import android.content.Context;

/**
 * 
 * @author Aaron Joellenbeck
 *Klasse die Hinweise und Nachrichten fuer den Benutzer darstellt.
 */
public class Messenger {
	
	private AlertDialog alertMessage;
	
	public Messenger(Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		alertMessage = builder.create();
	}
	
	private void setAlertMessage(String title, String message){
		alertMessage.setTitle(title);
		alertMessage.setMessage(message); 
	}
	
	public void showAlertMessage(String title, String message){
		this.setAlertMessage(title, message);
		alertMessage.show();
	}
}
