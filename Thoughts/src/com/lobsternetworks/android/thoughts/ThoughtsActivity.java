package com.lobsternetworks.android.thoughts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;



import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressWarnings("unused")
public class ThoughtsActivity extends Activity {
	String fileName = "my_thoughts.txt";
	String readString = " ";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thoughts);
		
		Spinner spinner = (Spinner) findViewById(R.id.thoughtsTypes);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.thought_types, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		try {
            FileInputStream fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);

            // READ STRING OF UNKNOWN LENGTH
            StringBuilder sb = new StringBuilder();
            char[] inputBuffer = new char[2048];
            int l;
            // FILL BUFFER WITH DATA
            while ((l = isr.read(inputBuffer)) != -1) {
                sb.append(inputBuffer, 0, l);
            }

            // CONVERT BYTES TO STRING
            readString = sb.toString();
            Log.i("LOG_TAG", "Read string: " + readString);

            // CAN ALSO DELETE THE FILE
            //deleteFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
		TextView tv = (TextView)findViewById(R.id.thoughts);
		tv.setText(readString);
		Button b = (Button)findViewById(R.id.buttonNew);
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText et = (EditText)findViewById(R.id.newThought);
				String newThought = et.getText().toString();
				Spinner spinner = (Spinner) findViewById(R.id.thoughtsTypes);
				Integer position = spinner.getSelectedItemPosition();
				System.out.println(position);
				String type = spinner.getItemAtPosition(position).toString();
				
				if(position > 0){
					String output = type + ": " + newThought + "\n" + readString;
					readString = output;
					
				if(newThought.length() >0){
			        writeout(output);
					TextView tv = (TextView)findViewById(R.id.thoughts);
					tv.setText(output);
				}
				}

			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.thoughts, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_clear:
        	writeout("");
        	TextView tv = (TextView)findViewById(R.id.thoughts);
			tv.setText("");
        	return true;
        case R.id.menu_about:
        	about();
        	return true;

   
        }
       return false;
    }
	public void writeout(String write){
		try {
            // CREATE THE FILE AND WRITE
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(write.getBytes());
            fos.close();
            System.out.println("Success");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public boolean about(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Add the buttons
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		           }
		       });

		// Set other dialog properties
		builder.setMessage(R.string.dialog_message)
	       .setTitle(R.string.dialog_title);

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();
		return true;
	}
}
