package com.stuffbox.model;

import android.app.AlertDialog;
import android.content.Context;

/**
 * 
 * @author Aaron Joellenbeck
 *Klasse die Hinweise und Nachrichten fuer den Benutzer darstellt.
 */
public class Messenger {
		AlertDialog alertMessage;
	public Messenger(Context context){
		AlertDialog.Builder alertmessage = new AlertDialog.Builder(context);
	}
	
	private void setAlertMessage(String title, String message){
		alertMessage.setTitle(title);
		alertMessage.setMessage(message); 
	}
	
	private void showAlertMessage(String title, String message){
		this.setAlertMessage(title, message);
		alertMessage.isShowing();
	}
}
