package com.example.josh.top10downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/*
This program will download an XML file from a specified URL. The XML file contains a current list of
the top ten apple apps. It will then present the data in an easy to read List View
 */
public class MainActivity extends AppCompatActivity
{
    private Button btnParse;        //Button variable
    private ListView listApps;      // ListView variable
    private String mFileContents;   // String: XML file contents


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Associates btnParse with Button Widget
        btnParse = (Button) findViewById(R.id.btnParse);
        // Associates listApps with ListView Widget
        listApps = (ListView) findViewById(R.id.xmlListview);

        // Creates new DownloadData Object
        DownloadData d = new DownloadData();
        // Executes DownloadData Object
        d.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");

        // When button is clicked
        btnParse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Creates a new ParseApplications Object and evokes 'process' method
                ParseApplications parseApplications = new ParseApplications(mFileContents);
                parseApplications.process();

                // Creates an ArrayAdapter whose format is determiend by list_item, and whose data
                // is loaded from the parseApplications class
                ArrayAdapter<Application> arrayAdapter = new ArrayAdapter<Application>(
                        MainActivity.this, R.layout.list_item, parseApplications.getApplications());

                // Loads contents of ArrayAdapter into the List View, by default evokes toString
                // method
                listApps.setAdapter(arrayAdapter);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Class to download data from URL
    private class DownloadData extends AsyncTask<String, Void, String>
    {
         @Override
        protected String doInBackground(String... params)
        {
            // evokes downloadXMLFile method and stores result in mFileContents
            mFileContents = downloadXMLFile(params[0]);
            // Log if contents of file are empty
            if(mFileContents == null)
            {
                Log.d("DownloadData", "Error downloading");
            }
            return mFileContents;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            Log.d("DownloadData", "Result was " + result);

        }

        private String downloadXMLFile(String urlPath)
        {
            StringBuilder tempBuffer = new StringBuilder();
            try
            {
                // Define URL to urlPath
                URL url = new URL(urlPath);
                // Opens the URL by type casting url.openConnection to HttpsURLConnection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // Response code (i.e. 404 error)
                int response = connection.getResponseCode();
                // Log response code to console
                Log.d("DownloadData", "The response code was " + response);

                //Defines input stream
                InputStream is        = connection.getInputStream();
                // Processes input stream (accesses data)
                InputStreamReader isr = new InputStreamReader(is);

                int charRead;
                char[] inputBuffer = new char[500];

                while(true)
                {
                    // reads inputBuffer, 500 characters at a time
                    charRead = isr.read(inputBuffer);
                    // if inputBuffer is empty, break
                    if(charRead <= 0)
                    {
                        break;
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer,0,charRead));

                }
                return  tempBuffer.toString();
            }
            catch (IOException e)
            {
                Log.d("DownloadData", "IO Exception reading data: " + e.getMessage());
                //e.printStackTrace();
            }
            catch(SecurityException e)
            {
                Log.d("DownloadData", "Security exception. Needs permissions? " + e.getMessage());
                //e.printStackTrace();
            }

            return null;
        }
    }
}
