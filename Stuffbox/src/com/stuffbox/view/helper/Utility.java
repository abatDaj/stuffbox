

package com.stuffbox.view.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility {
	
	/*
	 *Quelle:
	 *http://stackoverflow.com/questions/17526418/scroll-linearlayout-with-listview-in-android 
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView, int offset) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + offset;
		listView.setLayoutParams(params);
	}
	
	/*
	 * Quelle:
	 * http://stackoverflow.com/questions/6693069/problem-with-big-images-java-lang-outofmemoryerror-bitmap-size-exceeds-vm-bud
	 */
	public static  Bitmap decodeFile(File f, int sizeToScale){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = sizeToScale;

            //Find the correct scale value. It should be the power of 2.
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }	
	
	public static void replaceImageViewWithPhoto (String pathToFile, ImageView imageView, int sizeToScale) {
		
    	File path = Environment.getExternalStorageDirectory();
    	File file = new File(path, pathToFile);
    	String TAG = Utility.class.getSimpleName() + "#replaceImageViewWithPhoto()";
        
    	try {
        	
        	Bitmap bm = Utility.decodeFile(file, sizeToScale);
        	imageView.setImageBitmap(bm);
            
            try {                        
                //OutputStream stream = new FileOutputStream(file);
                //bm.compress(CompressFormat.JPEG, 100, stream);
                //stream.flush();
                //stream.close();
            /*} catch (FileNotFoundException e) {
            	Log.e(TAG, "Fehler beim Fotografieren file not found: ", e);
            } catch (IOException e) {
            	Log.e(TAG, "Fehler beim Fotografieren io Ausgabe: ", e);*/
            } catch (Exception e) {
            	Log.e(TAG, "Fehler beim Fotografieren allgemeiner fehler: ", e);
            }
        } catch (Exception e) {
        	Log.e(TAG, "Fehler beim Fotografieren allgemeiner fehler: ", e);
        }
	}
	
	/**
	 * Graut den ImageView ein.
	 * 
	 * @param context
	 * @param drawID
	 * @param size
	 * @return
	 */
	public static void makeImageViewGrey(ImageView imageView) {
		ColorFilter filter = new LightingColorFilter( Color.BLACK, Color.GRAY);
		imageView.setColorFilter(filter);
	}
	
	/**
	 * Vergibt 1-5 Sterne.
	 * 
	 * @param context
	 * @param drawID
	 * @param size
	 * @return
	 */
	public static ImageView stuffBoxStarIconCloner (Context context, int drawID, int stars) {
		//ViewGroup.LayoutParams params = imageView.getLayoutParams();
		ImageView imageOfIconInRow = new ImageView(context);
		imageOfIconInRow.setLayoutParams(new LinearLayout.LayoutParams(Controller.BADGE_ICON_SIZE, Controller.BADGE_ICON_SIZE));
		//imageOfIconInRow.setLayoutParams(params);
		Resources ressources = context.getResources();
		Drawable[] layers = new Drawable[2];
		layers[0] = ressources.getDrawable(drawID);
		
		if (stars < 1 || stars > 5)
			throw new RuntimeException("Just 5 Stars are allowed !");
			
		 switch(stars) 
		 {
	        case 1:
	        	layers[1] = layers[1] = ressources.getDrawable(R.drawable.badge_star_1);
	            break;
	        case 2:
	        	layers[1] = layers[1] = ressources.getDrawable(R.drawable.badge_star_2);
	            break;
	        case 3:
	        	layers[1] = layers[1] = ressources.getDrawable(R.drawable.badge_star_3);
	            break;
	        case 4:
	        	layers[1] = layers[1] = ressources.getDrawable(R.drawable.badge_star_4);
	        	break;
	        case 5:
	        	layers[1] = layers[1] = ressources.getDrawable(R.drawable.badge_star_5);
	            break;
	      } 

		LayerDrawable layerDrawable = new LayerDrawable(layers);
		imageOfIconInRow.setImageDrawable(layerDrawable);	
		return imageOfIconInRow;
	}
}