package com.stuffbox.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.provider.MediaStore; 
import android.provider.MediaStore.MediaColumns;

import com.stuffbox.R;
import com.stuffbox.controller.Controller;


public class MainActivity extends ActionBarActivity {

	private ImageButton btn ;
	
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int REQUEST_CAMERA = 42;
	private static final int SELECT_FILE = 1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        Controller.getInstance(this).init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void openFormularScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), FormularActivity.class.getName());
        startActivity(intent);
    }
    
    public void openFormularNewScreen(View view){    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), NewFormularActivity.class.getName());
        startActivity(intent);
    }		
    
    public void openAbzeichenScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), BadgeActivity.class.getName());
        startActivity(intent);
    }	
    
    public void openArtenScreen(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), NewFeatureActivity.class.getName());
        startActivity(intent);
    }	    

    
    public void openListCategories(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), ListCategoriesActivity.class.getName());
        startActivity(intent);
    }	 

public void chooseCategories(View view) {    	
        Intent intent = new Intent();        
        intent.setClassName(getPackageName(), ChooseCategoriesActivity.class.getName());
        startActivity(intent);
    }
    
//    public void openFotoScreen(View view) {
//        final CharSequence[] items = { "Foto machen", "Galerie",
//                "Abbrechen" };
// 
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("Bild hinzufuegen");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (items[item].equals("Foto machen")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment
//                            .getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent, REQUEST_CAMERA);
//                } else if (items[item].equals("Galerie")) {
//                    Intent intent = new Intent(
//                            Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Select File"),
//                            SELECT_FILE);
//                } else if (items[item].equals("Abbrechen")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }

    
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_CAMERA) {
//            	File path = Environment.getExternalStorageDirectory();
//                File file = new File(path, "temp.jpg");
//
//                try {
//                    Bitmap bm;
//                    btn = (ImageButton)findViewById(R.id.imageButtonFoto);
//                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
// 
//                    bm = BitmapFactory.decodeFile(file.getAbsolutePath(),
//                            btmapOptions);
// 
//                    //bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
//                    btn.setImageBitmap(bm);
//                    try {                        
//                        OutputStream stream = new FileOutputStream(file);
//                        bm.compress(CompressFormat.JPEG, 100, stream);
//                        stream.flush();
//                        stream.close();
//                    } catch (FileNotFoundException e) {
//                    	Log.e(TAG, "Fehler beim Fotografieren file not found: ", e);
//                    } catch (IOException e) {
//                    	Log.e(TAG, "Fehler beim Fotografieren io Ausgabe: ", e);
//                    } catch (Exception e) {
//                    	Log.e(TAG, "Fehler beim Fotografieren allgemeiner fehler: ", e);
//                    }
//                } catch (Exception e) {
//                	Log.e(TAG, "Fehler beim Fotografieren allgemeiner fehler: ", e);
//                }
//            } else if (requestCode == SELECT_FILE) {
//            	try {
//            		final Uri imageUri = data.getData();
//            		btn = (ImageButton)findViewById(R.id.imageButtonFoto);
////            		String tempPath = getPath(selectedImageUri, MainActivity.this);
////            		Bitmap bm;
////            		BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
////            		bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
//            		final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//    				final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//    				btn.setImageBitmap(selectedImage);
//            	} catch (Exception e) {
//            		Log.e(TAG, "Fehler bei der Galerie allgemeiner fehler: ", e);
//				}
//            }
//        }
//    }
    

}
